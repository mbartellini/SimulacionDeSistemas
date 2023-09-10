package ar.edu.itba.ss.models;

public class Event implements Comparable<Event> {
    private double timeToCollision;

    public Particle[] getParticlesInvolved() {
        return particlesInvolved;
    }

    private Particle[] particlesInvolved;
    private Collision type;

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
}