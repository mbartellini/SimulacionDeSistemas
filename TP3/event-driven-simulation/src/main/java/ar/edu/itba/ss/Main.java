package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Event;
import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.enclosure.Enclosure;
import ar.edu.itba.ss.models.enclosure.SquareEnclosure;

import java.util.*;

public class Main {

    private static final Random r = new Random(123456789L);

    public static void main(String[] args) {;
        Enclosure enc = new SquareEnclosure(1);
        Particle p = new Particle(0, 0.5, 0.5, 0.2, 0.3, 0, 0);
        Event e = enc.nextCollisionToWall(p);

        System.out.println(e.getTimeToCollision());
        System.out.println(Arrays.toString(e.getParticlesInvolved()));
        System.out.println(e.getType());

    }

}
