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

    public static Particle[] generateRandom(int n, Double v, Double r, Double mass, Double l, Random random) {
        Particle[] particles = new Particle[n];

        for (int i = 0; i < particles.length; i++) {
            final double angle = random.nextDouble() * 2 * Math.PI;
            particles[i] = new Particle(i,
                    random.nextDouble()*l,
                    (random.nextDouble() - 0.5) * l,
                    v * Math.cos(angle),
                    v * Math.sin(angle), r, mass);
        }

        return particles;
    }

    /*
        Method to figure out what the next event will be for particle p
     */
    public Event nextCollision(Particle[] particles, Enclosure enclosure) {
        Event next = enclosure.nextCollisionToWall(this);

        if(next == null) return null;

        double minTime = next.getTimeToCollision();
        Integer minIdx = null;
        for (int i = 0; i < particles.length; i++) {
            if(this.equals(particles[i])) continue;
            final double ct = collisionTimeToOther(particles[i]);

            if(ct < 0.0) continue;

            if(ct < minTime) {
                minTime = ct;
                minIdx = i;
            }
        }

        if(minIdx != null) {
            next = new Event(minTime, new Particle[] {this, particles[minIdx]}, Collision.WITH_OTHER);
        }

        return next;
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

    /*
        Method updates particle position after a given time.
        Method assumes particle DOES NOT collide with anything during that time.
     */
    public void updatePosition(double time) {
        x += vx * time;
        y += vy * time;
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

    public long getId() {
        return id;
    }
}