package ar.edu.itba.ss.models;

public class Particle {

    public static double SYSTEM_RADIUS = 1.;

    private final long id;
    private double angle, velocity;
    private final double radius, mass;

    public Particle(long id, double angle, double velocity, double radius, double mass) {
        this.id = id;
        this.angle = angle;
        this.velocity = velocity;
        this.radius = radius;
        this.mass = mass;
    }

    public long getId() {
        return id;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public double getVelocity() {
        return velocity;
    }

    public void setVelocity(double velocity) {
        this.velocity = velocity;
    }

    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getX() {
        return SYSTEM_RADIUS * Math.cos(this.angle);
    }

    public double getY() {
        return SYSTEM_RADIUS * Math.sin(this.angle);
    }

    public boolean overlapsWith(Particle p) {
        return Math.pow(this.getX() - p.getX(), 2) + Math.pow(this.getY() - p.getY(), 2) <= Math.pow(this.radius + p.radius, 2);
    }

    public void update() {
        this.angle += this.velocity;
    }

}
