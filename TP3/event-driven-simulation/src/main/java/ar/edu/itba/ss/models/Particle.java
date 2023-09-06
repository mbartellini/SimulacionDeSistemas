package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.enclosure.Enclosure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import static ar.edu.itba.ss.models.Util.dotProduct;

public class Particle {
    private final long id;

    // TODO: change everything from angle to vx, vy
    private double x, y, vx, vy;

    private final double radius, mass;

    public Particle(long id, double x, double y, double vx, double vy,
                    double r, double mass) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.radius = r;
        this.mass = mass;
    }

    public static List<Particle> randomList(long n, Double v, Double r, Double mass, Double l, Random random) {
        ArrayList<Particle> particles = new ArrayList<>();

        while (n != 0) {
            particles.add(new Particle(n,
                    random.nextDouble()*l,
                    random.nextDouble()*l, v,
                    random.nextDouble() * 2 * Math.PI, r, mass));
            n--;
        }

        return particles;
    }

    /*
        Method to figure out what the next event will be.
     */
    public static Event nextCollision(List<Particle> particles) {
        return null;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getRadius() {
        return radius;
    }

    public double getTheta() {
        return Math.atan2(vy, vx);
    }

    public static void updateState(List<Particle> particles, Event details) {
        particles.forEach(p -> p.updatePosition(details.getTimeToCollision()));
        details.applyCollision();
    }

    /*
        Method updates particle position after a given time.
        Method assumes particle DOES NOT collide with anything during that time.
     */
    public void updatePosition(double time) {
        x += vx * time;
        y += vy * time;
    }

    public double collisionTimeToWall(Enclosure enclosure) {
        final double xDist = vx > 0? enclosure.distanceToRightWall(this) : enclosure.distanceToLeftWall(this);
        final double yDist = vy > 0? enclosure.distanceToTopWall(this) : enclosure.distanceToBottomWall(this);

        return Math.min(xDist/vx, yDist/vy);
    }

    public double collisionTimeToOther(Particle o) {
        final double sigma = radius + o.radius;
        final double[] dr = new double[] {o.x - x, o.y - y},
                       dv = new double[] {o.vx - vx, o.vy - vy};

        final double dvdr = dotProduct(dv, dr); // dvdr = dot(dv, dr)

        if(dvdr >= 0) return Double.POSITIVE_INFINITY;

        final double drdr = dotProduct(dr, dr); // drdr = dot(dr, dr)
        final double dvdv = dotProduct(dv, dv); // dvdv = dot(dv, dv)
        final double d = dvdr * dvdr - (dvdv) * (drdr - sigma * sigma);

        if(d < 0) return Double.POSITIVE_INFINITY;

        return - (dvdr + Math.sqrt(d)) / dvdv;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Particle))
            return false;
        Particle other = (Particle) obj;
        return this.id == other.id;
    }

    public double getMass() {
        return mass;
    }
}