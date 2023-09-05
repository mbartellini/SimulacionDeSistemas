package ar.edu.itba.ss.models.enclosure;

import ar.edu.itba.ss.models.Particle;

public class SquareEnclosure extends Enclosure {

    private final double sideLength;

    public SquareEnclosure(double sideLength) {
        this.sideLength = sideLength;
    }

    @Override
    public double distanceToTopWall(Particle p) {
        return sideLength - p.getY() - p.getRadius();
    }

    @Override
    public double distanceToBottomWall(Particle p) {
        return p.getY() - p.getRadius();
    }

    @Override
    public double distanceToLeftWall(Particle p) {
        return p.getX() - p.getRadius();
    }

    @Override
    public double distanceToRightWall(Particle p) {
        return sideLength - p.getX() - p.getRadius();
    }
}
