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

    public void setTimeToCollision(double timeToCollision) {
        this.timeToCollision = timeToCollision;
    }

    public void setType(Collision type) {
        this.type = type;
    }

    public Event(double timeToCollision, Particle[] particlesInvolved, Collision type) {
        this.timeToCollision = timeToCollision;
        this.particlesInvolved = particlesInvolved;
        this.type = type;
    }

    public Event(Particle[] particlesInvolved) {
        this.particlesInvolved = particlesInvolved;
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