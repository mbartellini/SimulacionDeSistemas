package ar.edu.itba.ss.models.enclosure;


import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;

public class Wall {

    private final Point start, finish;
    private static final double EPS = 0.0000001;

    public Wall(double x0, double y0, double x1, double y1) {
        start = new Point(x0, y0);
        finish = new Point(x1, y1);
    }

    public Event collisionWith(Particle p) {
        Point intercept = getIntercept(p);

        if(intercept == null || !containsPoint(intercept))
            return null;

        final double tx = (intercept.x - p.getX() - p.getRadius()) / p.getVx(),
                     ty = (intercept.y - p.getY() - p.getRadius()) / p.getVy();

        if(Math.abs(tx - ty) > EPS || tx < 0) {
            return null;
        }

        Event e;
        if(Math.abs(start.x - finish.x) < EPS)
            e = new Event(tx, new Particle[]{p}, Collision.WITH_HORIZONTAL_WALL);
        else if(Math.abs(start.y - finish.y) < EPS)
            e = new Event(tx, new Particle[]{p}, Collision.WITH_VERTICAL_WALL);
        else
            throw new IllegalStateException("Wall is neither horizontal nor vertical");
        return e;
    }

    private boolean containsPoint(Point p) {
        if(p.x < Math.min(start.x, finish.x) ||
           p.x > Math.max(start.x, finish.x) ||
           p.y < Math.min(start.y, finish.y) ||
           p.y > Math.max(start.y, finish.y)) return false;

        return Math.abs((start.x - finish.x)*(p.y - start.y) - (p.x - start.x)*(start.y - finish.y)) < EPS;
    }

    private Point getIntercept(Particle p) {
        // Line equation of the wall in the form a0*x + b0*y = c0
        final double a0 = start.y - finish.y, b0 = finish.x - start.x, c0 = finish.x * start.y - finish.y * start.x;

        final double x0 = p.getX(), y0 = p.getY();
        final double x1 = x0 + p.getVx(), y1 = y0 + p.getVy();
        // Line equation described by p in the form a1*x + b1*y = c1
        final double a1 = y0 - y1, b1 = x1 - x0, c1 = x1*y0 - y1*x0;

        final double det = a0 * b1 - a1 * b0;

        if(Math.abs(det) < EPS)
            return null;

        return new Point((b1 * c0 - b0 * c1) / det, (-a1 * c0 + a0 * c1) / det);
    }

    public static void main(String[] args) {
    }

    private record Point(double x, double y) {
    }
}
