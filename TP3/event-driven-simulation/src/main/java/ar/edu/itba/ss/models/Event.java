package ar.edu.itba.ss.models;

public class Event implements Comparable<Event> {
    private final double timeToCollision;
    private final Particle[] particlesInvolved;
    private final Collision type;


    public Event(double timeToCollision, Particle[] particlesInvolved, Collision type) {
        this.timeToCollision = timeToCollision;
        this.particlesInvolved = particlesInvolved;
        this.type = type;
    }

    public void applyCollision() {
        type.collide(particlesInvolved);
    }

    @Override
    public int compareTo(Event o) {
        return Double.compare(timeToCollision, o.timeToCollision);
    }

    public double getTimeToCollision() {
        return timeToCollision;
    }
}