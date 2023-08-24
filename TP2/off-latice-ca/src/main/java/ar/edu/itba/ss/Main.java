package ar.edu.itba.ss;


import java.io.*;
import java.util.*;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.utils.CellIndexMethod;
import com.moandjiezana.toml.Toml;

/**
 * Main Simulation.
 *
 */
public class Main {
    static final String OUTPUT_FILE = "DynamicCA.txt";
    public static void main(String[] args) {
        Double v = 1.0, noise = 0.1, r = 1.0;
        Long N = 100L, L = 10L, iter = 100L, deltaT = 1L;
        // Set the seed for the random number generator
        long seed = 123456789L; // Change this seed value to any long value preferred

        try {
            InputStream inputStream = new FileInputStream("config.toml");
            Toml toml = new Toml().read(inputStream);
            N = toml.getLong("simulation.N", N);
            L = toml.getLong("simulation.L", L);
            iter = toml.getLong("simulation.iter", iter);
            seed = toml.getLong("simulation.seed", seed);
            deltaT = toml.getLong("simulation.deltaT", deltaT);
            v = toml.getDouble("simulation.v", v);
            r = toml.getDouble("simulation.r", r);
            noise = toml.getDouble("simulation.noise", noise);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        Random random = new Random(seed);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(OUTPUT_FILE));

            // Initial random particle list.
            List<Particle> particles = Particle.randomList(N, v, r, L, random);

            Long gridCount = Math.floorDiv(L, r.longValue());
            double gridSize = (double) L /gridCount;

            for (long i = 0; i < iter; i++) {

                CellIndexMethod cim = new CellIndexMethod(gridSize, gridCount, gridCount, r, true);
                cim.insertParticles(particles);

                Map<Particle, List<Particle>> neighbors = cim.getNeighbors(particles);

                // Logic of change & Write generation to output file. (Dynamically)
                writer.write(String.format("%d\n", i));

                List <Particle> newGen = new ArrayList<>();

                for (Map.Entry<Particle, List<Particle>> particle : neighbors.entrySet()) {
                    double px = 0.0, py = 0.0, pv = particle.getKey().v, pr = particle.getKey().r, ptita = particle.getKey().tita;

                    px = particle.getKey().x + deltaT*Math.cos(particle.getKey().tita)*particle.getKey().v;
                    py = particle.getKey().y + deltaT*Math.sin(particle.getKey().tita)*particle.getKey().v;

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
                        ptita += particleNeighbor.tita;
                    }

                    ptita /= (Integer.valueOf(particle.getValue().size() + 1).doubleValue());

                    ptita += (random.nextDouble() * noise) - noise/2;

                    newGen.add(new Particle(particle.getKey().id, px, py, pv, ptita, pr, 0));
                    writer.write(String.format("%d %g %g %g %g %g\n", particle.getKey().id, px, py, pv, pr, ptita));
                }

                particles = newGen;
            }


            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to " + OUTPUT_FILE);
        }

    }
}