package ar.edu.itba.ss.models.enclosure;

import ar.edu.itba.ss.models.Particle;

public abstract class Enclosure {

    public abstract double distanceToTopWall(Particle p);
    public abstract double distanceToBottomWall(Particle p);
    public abstract double distanceToLeftWall(Particle p);
    public abstract double distanceToRightWall(Particle p);

}
