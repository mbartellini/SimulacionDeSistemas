package ar.edu.itba.ss.models.enclosure;

import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;
import java.util.List;

public abstract class Enclosure {

    protected final List<Wall> walls = new ArrayList<>();

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

}
