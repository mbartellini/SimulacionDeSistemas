package ar.edu.itba.ss.models.enclosure;

import ar.edu.itba.ss.models.Particle;

public class Corner extends Particle {
    public Corner(double x, double y) {
        super(-1, x, y, 0, 0, 0, Double.POSITIVE_INFINITY);
    }

}
