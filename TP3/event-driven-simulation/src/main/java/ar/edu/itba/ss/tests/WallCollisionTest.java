package ar.edu.itba.ss.tests;

import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Particle;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class WallCollisionTest {

    private static final double[] ANGLES = {Math.PI/4};
    private static final double RADIUS = 1, V = 1;
    private static List<Particle> particles;

    @Before
    public void init() {
        particles = new ArrayList<>();
        for(double angle : ANGLES) {
            particles.add(new Particle(
                    0, 0, 0, V, angle, RADIUS, null)
            ); // Enclosure, id and position are irrelevant to test collision
        }
    }

    @Test
    public void testCollisionsWithHorizontalWall() {

    }

    @Test
    public void testCollisionsWithVerticalWall() {

    }

}
