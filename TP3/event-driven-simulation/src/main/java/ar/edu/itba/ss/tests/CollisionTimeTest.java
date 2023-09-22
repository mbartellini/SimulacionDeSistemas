package ar.edu.itba.ss.tests;

import ar.edu.itba.ss.models.Particle;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollisionTimeTest {

    private static final int ID = 0; // ID is irrelevant for testing
    private static final double L = 1.0, V = 1.0, PRECISION = 0.0001;

    @Test
    public void TestCollisionTimes() {
        double angle = Math.atan2(0.75, 0.5);
        Particle p1 = new Particle(ID, 0.25, 0.0, V*Math.cos(angle), V*Math.sin(angle), 0, 0),
                 p2 = new Particle(ID, 0, 0.25, V*Math.cos(Math.PI/2 - angle), V*Math.sin(Math.PI/2 - angle), 0, 0);

        double t1 = p1.collisionTimeToOther(p2);
        assertEquals(t1, p2.collisionTimeToOther(p1), PRECISION);
        assertEquals(L * Math.sqrt(13)/4, t1, PRECISION);
    }

}
