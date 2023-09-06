package ar.edu.itba.ss.tests;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.enclosure.Enclosure;
import ar.edu.itba.ss.models.enclosure.SquareEnclosure;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParticleDynamicsTest {

    private static final double SIDE_LENGTH = 1.0, RADIUS = 0.1, MASS = 0, PRECISION = 0.0001;
    private static final Enclosure ENCLOSURE = new SquareEnclosure(SIDE_LENGTH);

    @Test
    public void testParticleDynamics() {
        final Particle p = new Particle(0, 0, 0, 1, Math.PI/4, RADIUS, MASS);

        assertEquals(ENCLOSURE.distanceToTopWall(p), 0.9, PRECISION);
        assertEquals(ENCLOSURE.distanceToRightWall(p), 0.9, PRECISION);

        p.updatePosition(Math.sqrt(2)/2);

        assertEquals(ENCLOSURE.distanceToTopWall(p), 0.4, PRECISION);
        assertEquals(ENCLOSURE.distanceToBottomWall(p), 0.4, PRECISION);
        assertEquals(ENCLOSURE.distanceToRightWall(p), 0.4, PRECISION);
        assertEquals(ENCLOSURE.distanceToLeftWall(p), 0.4, PRECISION);
    }

}
