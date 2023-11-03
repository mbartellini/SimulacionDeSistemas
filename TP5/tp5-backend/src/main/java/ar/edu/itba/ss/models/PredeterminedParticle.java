package ar.edu.itba.ss.models;

import ar.edu.itba.ss.Runner;
import ar.edu.itba.ss.Util;

import java.util.Comparator;
import java.util.List;

public class PredeterminedParticle extends Particle {

    private final List<Runner.InputData> history;

    public PredeterminedParticle(long id, double x, double y, double radius, double mass, List<Runner.InputData> history) {
        super(id, x, y, radius, mass, 0, 0);
        this.history = history;
        this.history.sort(Comparator.comparing(Runner.InputData::getSecond));
    }

    public void setPosition(double time) {
        int frame = (int) (time / Runner.DT2);
        if (frame >= history.size())
            frame = history.size() - 1;
        this.x = history.get(frame).getX();
        this.y = history.get(frame).getY();
    }

}
