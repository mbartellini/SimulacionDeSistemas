package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Util;

public class Particle {
    public static String OVITO_FORMAT = "Properties=id:I:1:pos:R:2",
    OVITO_FORMAT_STATIC = "Properties=id:I:1:radius:R:1:mass:R:1";

    private final long id;
    private final double radius, mass;
    double x, y, vx, vy;

    public Particle(long id, double x, double y, double radius, double mass, double vx, double vy) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
    }

    public double getVx() {
        return vx;
    }

    public double getVy() {
        return vy;
    }

    public long getId() {
        return id;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public String toStaticFile() {
        return String.format("%d %g %g", this.id, getRadius(), getMass());
    }

    public String toFile() {
        // id x y
        return String.format("%d %g %g", id, getX(), getY());
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }
}
