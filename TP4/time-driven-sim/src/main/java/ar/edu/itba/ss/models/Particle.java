package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Util;

public class Particle {

    public static double SYSTEM_RADIUS = 21.49, TAU = 1., KAPPA = 2500.0;
    public static String OVITO_FORMAT = "Properties=id:I:1:pos:R:2:velo:R:2",
    OVITO_FORMAT_STATIC = "Properties=id:I:1:radius:R:1:mass:R:1";

    private final long id;
    private final double radius, mass, limit;
    // Store theta and theta predictions
    private final double[] theta = new double[6], thetaPred = new double[6];

    public Particle(long id, double angle, double velocity, double radius, double mass, double limit) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.limit = limit;

        theta[0] = angle;
        theta[1] = velocity;
    }

    public long getId() {
        return id;
    }

    public double getAngle() {
        return theta[0];
    }

    public double getAngularVelocity() {
        return theta[1];
    }


    public double getRadius() {
        return radius;
    }

    public double getMass() {
        return mass;
    }

    public double getX() {
        return SYSTEM_RADIUS * Math.cos(this.getAngle());
    }

    public double getY() {
        return SYSTEM_RADIUS * Math.sin(this.getAngle());
    }

    public double getVx() {
        return SYSTEM_RADIUS * getAngularVelocity() * Math.cos(getAngle() + Math.PI * 0.5);
    }

    public double getVy() {
        return SYSTEM_RADIUS * getAngularVelocity() * Math.sin(getAngle() + Math.PI * 0.5);
    }

    private double getPredX() {
        return SYSTEM_RADIUS * Math.cos(this.thetaPred[0]);
    }

    private double getPredY() {
        return SYSTEM_RADIUS * Math.sin(this.thetaPred[0]);
    }

    public boolean predictedOverlap(Particle p) {
        return Math.pow(this.getPredX() - p.getPredX(), 2) + Math.pow(this.getPredY() - p.getPredY(), 2) <=
                Math.pow(this.radius + p.radius, 2);
    }

    public double predictedDrivingForce() {
        return (limit / SYSTEM_RADIUS - thetaPred[1]) / TAU;
    }

    // Returns the ABSOLUTE value of the contact force. Caller should know if it is positive or negative
    public double predictedContactForce(Particle o) {
        if (!predictedOverlap(o))
            return 0;
        double dtheta = o.thetaPred[0] - this.thetaPred[0];
        dtheta = Math.min(Math.abs(dtheta), Math.min(Math.abs(dtheta + 2 * Math.PI), Math.abs(dtheta - 2 * Math.PI)));
        return KAPPA * (Math.abs(dtheta) - radius / SYSTEM_RADIUS);
    }

    public String toStaticFile() {
        return String.format("%d %g %g", this.id, getRadius(), getMass());
    }

    public String toFile() {
        // id x y vx vy
        return String.format("%d %g %g %g %g", id, getX(), getY(), getVx(), getVy());
    }

    public void predict(double dt) {
        for (int i = 0; i < thetaPred.length; i++) {
            thetaPred[i] = Util.taylorExpansion(theta, dt, i);
        }
    }

    public void correct(double dt, Particle prev, Particle next) {
        final double force = predictedDrivingForce() + predictedContactForce(prev) - predictedContactForce(next);
        final double da = (force / mass) - thetaPred[2];
        final double dR2 = da * dt * dt * 0.5;
        for (int i = 0; i < theta.length; i++) {
            theta[i] = Util.correct(thetaPred[i], dR2, dt, i);
        }
    }

}
