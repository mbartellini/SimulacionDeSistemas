package ar.edu.itba.ss.experiments;


import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.CellIndexMethod;
import com.moandjiezana.toml.Toml;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Main Simulation.
 */
public class DensityExperiment {
    static final String OUTPUT_FILE_FORMAT = "data/tp2/experiments/density/DynamicCA_%d_%d.txt";
    static final int MAX_POPULATION_SIZE = 100, POPULATION_STEP = 10;

    public static void main(String[] args) {
        Double v = 1.0, r = 1.0, L = 3.1;
        Long iter = 100L, deltaT = 1L;
        // Set the seed for the random number generator
        long seed = 123456789L; // Change this seed value to any long value preferred

        try {
            InputStream inputStream = new FileInputStream("config.toml");
            Toml toml = new Toml().read(inputStream);
            iter = toml.getLong("simulation.iter", iter);
            seed = toml.getLong("simulation.seed", seed);
            deltaT = toml.getLong("simulation.deltaT", deltaT);
            v = toml.getDouble("simulation.v", v);
            r = toml.getDouble("simulation.r", r);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Random random = new Random(seed);

        try {
            for (int noiseFactor = 0; noiseFactor <= 2; noiseFactor++) {
                double noise = noiseFactor * Math.PI;
                for (long N = 0; N <= MAX_POPULATION_SIZE; N += POPULATION_STEP) {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(String.format(OUTPUT_FILE_FORMAT, noiseFactor, N)));

                    // Initial random particle list.
                    List<Particle> particles = Particle.randomList(N, v, r, L, random);
                    writer.write("0\n");
                    for (Particle particle : particles) {
                        writer.write(String.format("%d %g %g %g %g %g\n", particle.id, particle.x, particle.y, particle.v, particle.r, particle.theta));
                    }

                    Long gridCount = (long) Math.floor(L / r);
                    double gridSize = (double) L / gridCount;

                    for (int i = 1; i <= iter; i++) {

                        CellIndexMethod cim = new CellIndexMethod(gridSize, gridCount, gridCount, r, true);
                        cim.insertParticles(particles);

                        Map<Particle, List<Particle>> neighbors = cim.getNeighbors(particles);

                        // Logic of change & Write generation to output file. (Dynamically)
                        writer.write(String.format("%d\n", i));

                        List<Particle> newGen = new ArrayList<>();

                        for (Map.Entry<Particle, List<Particle>> particle : neighbors.entrySet()) {
                            double px, py, pv = particle.getKey().v, pr = particle.getKey().r, ptheta;
                            double psin = Math.sin(particle.getKey().theta), pcos = Math.cos(particle.getKey().theta);

                            px = particle.getKey().x + deltaT * Math.cos(particle.getKey().theta) * particle.getKey().v;
                            py = particle.getKey().y + deltaT * Math.sin(particle.getKey().theta) * particle.getKey().v;

                            while (px < 0.0) {
                                px = (L + px);
                            }
                            while (py < 0.0) {
                                py = (L + py);
                            }

                            while (px > L) {
                                px = px - L;
                            }
                            while (py > L) {
                                py = py - L;
                            }

                            for (Particle particleNeighbor : particle.getValue()) {
                                psin += Math.sin(particleNeighbor.theta);
                                pcos += Math.cos(particleNeighbor.theta);
                            }

                            ptheta = Math.atan2(psin, pcos) + noise * (random.nextDouble() - 0.5);

                            newGen.add(new Particle(particle.getKey().id, px, py, pv, ptheta, pr, 0));
                            writer.write(String.format("%d %g %g %g %g %g\n", particle.getKey().id, px, py, pv, pr, ptheta));
                        }

                        particles = newGen;
                    }

                    writer.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Error while writing to " + OUTPUT_FILE_FORMAT);
        }

    }
}