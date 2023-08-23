package ar.edu.itba.ss.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Particle {
    public final long id;
    public double x, y, v, tita;

    public final double r, property;

    public Particle(long id, double x, double y, double v, double tita, double r, double property) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.v = v;
        this.tita = tita;
        this.r = r;
        this.property = property;
    }

    public static List<Particle> randomList(long n, Double v, Double r, Long l, Random random) {
        ArrayList<Particle> particles = new ArrayList<>();

        while (n != 0) {
            particles.add(new Particle(n, random.nextDouble()*l, random.nextDouble()*l, v, Math.toRadians(random.nextDouble() * 360), r, 0));
            n--;
        }

        return particles;
    }

    public double getDistanceTo(Particle p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
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