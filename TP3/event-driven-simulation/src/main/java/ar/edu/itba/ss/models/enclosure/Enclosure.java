package ar.edu.itba.ss.models.enclosure;

import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;
import java.util.List;

public class Enclosure {

    protected final List<Wall> walls = new ArrayList<>();
    private double leftImpulse = 0.0, rightImpulse = 0.0;
    private final double leftPerimeter, rightPerimeter;

    public Enclosure(double sideLength, double openingLength) {
        walls.add(new Wall(0, -sideLength/2, 0, sideLength/2, Side.LEFT));
        walls.add(new Wall(0, sideLength/2, sideLength, sideLength/2, Side.LEFT));
        walls.add(new Wall(sideLength, sideLength/2, sideLength, openingLength/2, Side.LEFT));
        walls.add(new Wall(sideLength, openingLength/2, 2*sideLength, openingLength/2, Side.RIGHT));
        walls.add(new Wall(2*sideLength, openingLength/2, 2*sideLength, -openingLength/2, Side.RIGHT));
        walls.add(new Wall(2*sideLength, -openingLength/2, sideLength, -openingLength/2, Side.RIGHT));
        walls.add(new Wall(sideLength, -openingLength/2, sideLength, -sideLength/2, Side.LEFT));
        walls.add(new Wall(sideLength, -sideLength/2, 0, -sideLength/2, Side.LEFT));

        double lp = 0.0, rp = 0.0;

        for(Wall w : walls) {
            if(w.getSide() == Side.LEFT) {
                lp = w.getLength();
            } else {
                rp = w.getLength();
            }
        }

        leftPerimeter = lp;
        rightPerimeter = rp;
    }

    public Event nextCollisionToWall(Particle p) {
        Event min = null;

        for(Wall w: walls) {
            Event e = w.collisionWith(p);
            if(e == null) continue;
            if(min == null || min.compareTo(e) > 0)
                min = e;
        }

        return min;
    }

    public void resetImpulse() {
        leftImpulse = rightImpulse = 0.0;
    }

    public double getSidePressure(double timeStep, Side side) {
        if(side == Side.LEFT) {
            return leftImpulse / (timeStep * leftPerimeter);
        }
        return rightImpulse / (timeStep * rightPerimeter);
    }

    public void addImpulse(Event e) {
        final Enclosure.Side side = e.getSide();
        if(side == null) return;

        final Collision type = e.getType();
        final Particle p = e.getParticlesInvolved()[0];
        double v = 0;
        if (type == Collision.WITH_HORIZONTAL_WALL) {
            v = p.getVy();
        } else if (type == Collision.WITH_VERTICAL_WALL) {
            v = p.getVx();
        } else {
            throw new IllegalStateException("Impulse can only be calculated in wall collisions");
        }

        final double impulse = 2 * v * p.getMass();

        switch (side) {
            case LEFT -> leftImpulse += impulse;
            case RIGHT -> rightImpulse += impulse;
            default -> throw new IllegalStateException("Invalid side");
        }
    }

    public enum Side {
        LEFT,
        RIGHT
    }

}
