package ar.edu.itba.ss.utils;

import java.util.Objects;

public class Particle {
    public final long id;
    public double x, y;

    public final double r, property;

    public Particle(long id, double x, double y, double r, double property) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.id = id;
        this.property = property;
    }

    public double getDistanceTo(Particle p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    public boolean isBorderToBorderNeighbour(double radius, double x, double y) {
        return Math.sqrt(Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2)) < radius;
    }

    public boolean isBorderToBorderNeighbour(double radius, Particle p) {
        return ! this.equals(p) && this.getDistanceTo(p) < radius + this.r + p.r;
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
}
