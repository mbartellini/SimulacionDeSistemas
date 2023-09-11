package ar.edu.itba.ss.tests;

import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Particle;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParticleCollisionTest {
    private static final int ID = 0;
    private static final double V = 1, MASS = 1, RADIUS = 0.1, PRECISION = 0.001;

    @Test
    public void testCollisionBetweenParticles() {
        Particle[] ps = new Particle[]{
                new Particle(ID, 0.4, 0.5, V, 0, RADIUS, 2*MASS),
                new Particle(ID, 0.6, 0.5, -V, 0, RADIUS, MASS)
        };
        Collision.WITH_OTHER.collide(ps);
        assertEquals(-V/3, ps[0].getVx(),  PRECISION);
        assertEquals(5*V/3, ps[1].getVx(), PRECISION);
    }

}
