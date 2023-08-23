package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.BruteForceMethod;
import ar.edu.itba.ss.utils.CellIndexMethod;
import ar.edu.itba.ss.utils.Particle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExperimentTimeVsN {
    private static final String TIME_VS_N_FILE = "../data/ExperimentTimeVsN_CIM.txt";
    private static final int MAX_PARTICLE_COUNT = 5000;
    private static final int TEST_COUNT = 5;
    private static final double INTERACTION_RADIUS = 1;
    private static final double RADIUS = 0.25;
    private static final double AREA_SIZE = 20;
    private static final int PROPERTY = 1;

    public static void main(String[] args) {
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(TIME_VS_N_FILE));
            for (int particleCount = 1; particleCount <= MAX_PARTICLE_COUNT; particleCount+=100) {
                writer.write(String.format("%d", particleCount));
                for (int test = 0; test < TEST_COUNT; test++) {
                    List<Particle> particles = new ArrayList<>();
                    for (int i = 0; i < particleCount; i++) {
                        particles.add(new Particle(i, random(0, AREA_SIZE), random(0, AREA_SIZE), RADIUS, PROPERTY));
                    }

                    long startTime = System.currentTimeMillis();
                    long m = (int)(AREA_SIZE/(INTERACTION_RADIUS + 2 * RADIUS));
                    CellIndexMethod cim = new CellIndexMethod(AREA_SIZE/m, m, m, INTERACTION_RADIUS, true);
                    cim.insertParticles(particles);
                    Map<Particle, List<Particle>> neighbors = cim.getNeighbors(particles);
/*                    BruteForceMethod bfm = new BruteForceMethod(INTERACTION_RADIUS, true);
                    bfm.insertParticles(particles);
                    Map<Particle, List<Particle>> neighbors = bfm.getNeighbors(particles);*/
                    long endTime = System.currentTimeMillis();

                    writer.write(String.format(" %d", endTime - startTime));
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to output file");
        }
    }
    private static double random(double lower, double upper) {
        return Math.random() * (upper - lower) + lower;
    }
}
