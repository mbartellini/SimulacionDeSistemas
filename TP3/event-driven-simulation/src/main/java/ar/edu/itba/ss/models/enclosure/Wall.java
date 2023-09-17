package ar.edu.itba.ss.models.enclosure;


import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.Util;

public class Wall {

    private final Point start, finish;
    private static final double EPS = 0.00000001;
    private final double length;
    private final Enclosure.Side side;

    public Enclosure.Side getSide() {
        return side;
    }

    public Wall(double x0, double y0, double x1, double y1, Enclosure.Side side) {
        start = new Point(x0, y0);
        finish = new Point(x1, y1);
        this.side = side;
        length = start.distanceTo(finish);
    }

    public Event collisionWith(Particle p) {
//        Point intercept = getIntercept(p);
//
//        if(intercept == null || !containsPoint(intercept))
//            return null;

        double t;
        Collision type;
        if(Math.abs(start.x - finish.x) < EPS) {
            type = Collision.WITH_VERTICAL_WALL;
            t = (p.getVx() > 0) ?
                    (this.start.x - p.getRadius() - p.getX()) / p.getVx() :
                    (this.start.x + p.getRadius() - p.getX()) / p.getVx();
            if (! Util.inRangeDouble(p.getY() + t * p.getVy(),
                    Math.min(start.y, finish.y),
                    Math.max(start.y, finish.y),
                    EPS)) {
                return null;
            }
        } else if(Math.abs(start.y - finish.y) < EPS) {
            type = Collision.WITH_HORIZONTAL_WALL;
            t = (p.getVy() > 0) ?
                    (this.start.y - p.getRadius() - p.getY()) / p.getVy() :
                    (this.start.y + p.getRadius() - p.getY()) / p.getVy();
            if (! Util.inRangeDouble(p.getX() + t * p.getVx(),
                    Math.min(start.x, finish.x),
                    Math.max(start.x, finish.x),
                    EPS)) {
                return null;
            }
        } else {
            throw new IllegalStateException("Wall is neither horizontal nor vertical");
        }

        if(t < EPS) // t = 0 or negative
            return null;

        Event e = new Event(t, new Particle[] {p}, type);
        e.setSide(side);
        return e;
    }

    private boolean containsPoint(Point p) {
        // EPS correction: rounding error makes 0.0000000000002 technically larger than 0.0
        if(p.x < Math.min(start.x, finish.x) - EPS ||
           p.x > Math.max(start.x, finish.x) + EPS ||
           p.y < Math.min(start.y, finish.y) - EPS ||
           p.y > Math.max(start.y, finish.y) + EPS) return false;

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

    public double getLength() {
        return length;
    }

    public Point getStart() {
        return start;
    }

    public Point getFinish() {
        return finish;
    }

    private record Point(double x, double y) {

        private double distanceTo(Point o) {
            double dx = x - o.x, dy = y - o.y;
            return Math.sqrt(dx * dx + dy * dy);
        }

        @Override
        public String toString() {
            return String.format("(%.2f, %.2f)", x, y);
        }
    }
}
