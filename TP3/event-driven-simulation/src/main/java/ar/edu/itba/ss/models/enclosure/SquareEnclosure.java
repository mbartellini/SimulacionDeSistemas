package ar.edu.itba.ss.models.enclosure;

import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;

public class SquareEnclosure extends Enclosure {

    public SquareEnclosure(double sideLength) {
        walls.add(new Wall(0.0, 0.0, 0.0, sideLength));
        walls.add(new Wall(0.0, sideLength, sideLength, sideLength));
        walls.add(new Wall(sideLength, sideLength, sideLength, 0.0));
        walls.add(new Wall(sideLength, 0.0, 0.0, 0.0));
    }
}
