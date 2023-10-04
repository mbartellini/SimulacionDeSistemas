package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Util;

public class Particle {

    public static double SYSTEM_RADIUS = 21.49, TAU = 1., KAPPA = 2500.0;
    public static String OVITO_FORMAT = "Properties=id:I:1:pos:R:2:velo:R:2:angle:R:1:omega:R:1",
            OVITO_FORMAT_STATIC = "Properties=id:I:1:radius:R:1:mass:R:1";

    private final long id;
    private final double radius, mass, limit;
    final double[] velocity, angle, acceleration; // 0 index for time i-1, 1 index for time i-2
    double predAngle, predVel, predAcc;

    public Particle(long id, double angle, double velocity, double radius, double mass, double limit, double dt) {
        this.id = id;
        this.radius = radius;
        this.mass = mass;
        this.limit = limit;

        this.angle = new double[]{angle, angle - dt * velocity};
        this.velocity = new double[]{velocity, velocity};
        this.acceleration = new double[2];
    }

    public long getId() {
        return id;
    }

    public double getAngle() {
        return angle[0];
    }

    public double getAngularVelocity() {
        return velocity[0];
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
        return SYSTEM_RADIUS * Math.cos(this.predAngle);
    }

    private double getPredY() {
        return SYSTEM_RADIUS * Math.sin(this.predAngle);
    }

    public boolean predictedOverlap(Particle p) {
        return Math.pow(this.getPredX() - p.getPredX(), 2) + Math.pow(this.getPredY() - p.getPredY(), 2) <=
                Math.pow(this.radius + p.radius, 2);
    }

    public double predictedDrivingForce() {
        return (limit / SYSTEM_RADIUS - predVel) / TAU;
    }

    // Returns the ABSOLUTE value of the contact force. Caller should know if it is positive or negative
    public double predictedContactForce(Particle o) {
        if (!predictedOverlap(o))
            return 0;
        double dtheta = o.predAngle - this.predAngle;
        dtheta = Math.min(Math.abs(dtheta), Math.abs(Math.abs(dtheta) - 2*Math.PI));
        return KAPPA * (Math.abs(dtheta) - radius / SYSTEM_RADIUS);
    }

    public String toStaticFile() {
        return String.format("%d %g %g", this.id, getRadius(), getMass());
    }

    public String toFile() {
        return String.format("%d %g %g %g %g %g %g", id, getX(), getY(), getVx(), getVy(), getAngle(), getAngularVelocity());
    }

    public void predict(double dt) {
        predAngle = angle[0] + velocity[0] * dt + 2 * acceleration[0] * Math.pow(dt, 2) / 3 - acceleration[1] * Math.pow(dt, 2) / 6;
        predVel = velocity[0] + 3 * acceleration[0] * dt / 2 - acceleration[1] * dt / 2;
    }

    public void correct(double dt, Particle prev, Particle next) {
        final double force = predictedDrivingForce() + predictedContactForce(prev) - predictedContactForce(next);
        predAcc = force / mass;

        double correctedVel = velocity[0] + predAcc * dt / 3 + 5 * acceleration[0] * dt / 6 - acceleration[1] * dt / 6;

        // BOOOOOCA
        angle[1] = angle[0];
        velocity[1] = velocity[0];
        acceleration[1] = acceleration[0];

        angle[0] = predAngle;
        velocity[0] = correctedVel;
        acceleration[0] = predAcc;
    }



}
