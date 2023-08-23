package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.CellIndexMethod;
import ar.edu.itba.ss.utils.Particle;

import java.io.*;
import java.util.*;

public class Experiment {

    private static final long PARTICLE_COUNT = 100;
    private static final double AREA_SIZE = 100;
    private static final long CELL_COUNT = 5;
    private static final double INTERACTION_RADIUS = 1;
    private static final double RADII = 0.25;
    private static final String STATIC_FILE = "../data/ExperimentStatic.txt";
    private static final String DYNAMIC_FILE = "../data/ExperimentDynamic.txt";
    private static final String NEIGHBOR_FILE = "../data/ExperimentNeighbor.txt";

    public static void main(String[] args) {
        // TODO: Set seed
        List<Particle> particles = new ArrayList<>();
        for (int i = 0; i < PARTICLE_COUNT; i++) {
            particles.add(new Particle(i, random(0, AREA_SIZE), random(0, AREA_SIZE), RADII, 1));
        }

        long startTime = System.currentTimeMillis();
        CellIndexMethod cim = new CellIndexMethod(AREA_SIZE/CELL_COUNT, CELL_COUNT, CELL_COUNT, INTERACTION_RADIUS, true);
        cim.insertParticles(particles);

        Map<Particle, List<Particle>> neighbors = cim.getNeighbors(particles);
        long endTime = System.currentTimeMillis();
        System.out.printf("Runtime: %dms\n", endTime - startTime);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(STATIC_FILE));
            writer.write(String.format("%d\n", PARTICLE_COUNT));
            writer.write(String.format("%d\n", (int)AREA_SIZE));
            for (Particle p : particles) {
                writer.write(String.format("%f %f\n", p.r, p.property));
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to neighbor file");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(DYNAMIC_FILE));
            writer.write(String.format("0\n"));
            for (Particle p : particles) {
                writer.write(String.format("%f %f\n", p.x, p.y));
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to neighbor file");
        }

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(NEIGHBOR_FILE));
            for (Map.Entry<Particle, List<Particle>> neighborList : neighbors.entrySet()) {
                writer.write(String.valueOf(neighborList.getKey().id));
                for (Particle particle : neighborList.getValue()) {
                    writer.write(String.format(" %d", particle.id));
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to neighbor file");
        }

    }

    // Experiment: time vs. N and MxM


    private static double random(double lower, double upper) {
        return Math.random() * (upper - lower) + lower;
    }

}
