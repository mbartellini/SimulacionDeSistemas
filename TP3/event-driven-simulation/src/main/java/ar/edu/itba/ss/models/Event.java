package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.enclosure.Enclosure;

public class Event implements Comparable<Event> {
    private double timeToCollision;

    public Particle[] getParticlesInvolved() {
        return particlesInvolved;
    }

    private final Particle[] particlesInvolved;
    private Collision type;

    private Enclosure.Side side;

    public Event(double timeToCollision, Particle[] particlesInvolved, Collision type) {
        if(timeToCollision < 0.0) {
            throw new IllegalArgumentException("Time to collision must be positive");
        }

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

    public Collision getType() {
        return type;
    }

    public void updateTime(double tc) {
        if(tc > timeToCollision) throw new IllegalStateException("Updating time is greater than collision time");
        timeToCollision -= tc;
    }

    public Enclosure.Side getSide() {
        return side;
    }

    public void setSide(Enclosure.Side side) {
        this.side = side;
    }
}