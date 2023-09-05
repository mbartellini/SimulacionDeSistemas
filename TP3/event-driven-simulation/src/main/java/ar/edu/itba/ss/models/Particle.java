package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.enclosure.Enclosure;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Particle {
    private final long id;
    private double x, y, v, theta;

    private final double radius;

    private final Enclosure enclosure;

    public Particle(long id, double x, double y, double v, double theta,
                    double r, Enclosure enclosure) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.v = v;
        this.theta = theta;
        this.radius = r;
        this.enclosure = enclosure;
    }

    public static List<Particle> randomList(long n, Double v, Double r, Double l, Enclosure enclosure, Random random) {
        ArrayList<Particle> particles = new ArrayList<>();

        while (n != 0) {
            particles.add(new Particle(n,
                    random.nextDouble()*l,
                    random.nextDouble()*l, v,
                    random.nextDouble() * 2 * Math.PI, r,
                    enclosure));
            n--;
        }

        return particles;
    }

    /*
        Method to figure out what the next event will be.
     */
    public static CollisionDetails nextCollision(List<Particle> particles) {
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
        return theta;
    }

    public void setTheta(double theta) {
        this.theta = theta;
    }

    public static void updateState(List<Particle> particles, CollisionDetails details) {
        particles.forEach(p -> p.updatePosition(details.timeToCollision));
        updateCollidingAngles(details);
    }

    /*
        Method updates particle position after a given time.
        Method assumes particle DOES NOT collide with anything during that time.
     */
    public void updatePosition(double time) {
        x += xVelocity() * time;
        y += yVelocity() * time;
    }

    private static void updateCollidingAngles(CollisionDetails details) {
        // TODO: update angles accordingly to collision type
    }

    public double collisionTimeToWall() {
        final double xVel = xVelocity(), yVel = yVelocity();

        final double xDist = xVel > 0? enclosure.distanceToRightWall(this) : enclosure.distanceToLeftWall(this);
        final double yDist = yVel > 0? enclosure.distanceToTopWall(this) : enclosure.distanceToBottomWall(this);

        return Math.min(xDist/xVel, yDist/yVel);
    }

    private double xVelocity() {
        return v * Math.cos(theta);
    }

    private double yVelocity() {
        return v * Math.sin(theta);
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

    public static class CollisionDetails {
        private final double timeToCollision;
        private final Particle[] particlesInvolved;
        private final Collision type;


        public CollisionDetails(double timeToCollision, Particle[] particlesInvolved, Collision type) {
            this.timeToCollision = timeToCollision;
            this.particlesInvolved = particlesInvolved;
            this.type = type;
        }

        public void applyCollision() {
            type.collide(particlesInvolved);
        }
    }
}