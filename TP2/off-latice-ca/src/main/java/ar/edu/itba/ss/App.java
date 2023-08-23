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
public class App {
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
                    double px = 0.0, py = 0.0, pv = 0.0, pr = 0.0, ptita = particle.getKey().tita;

                    px = particle.getKey().x + deltaT*Math.cos(particle.getKey().tita)*particle.getKey().v;
                    py = particle.getKey().y + deltaT*Math.sin(particle.getKey().tita)*particle.getKey().v;

                    for (Particle particleNeighbor : particle.getValue()) {
                        ptita += particleNeighbor.tita;
                    }

                    ptita /= (particle.getValue().size() + 1);

                    ptita = Math.atan( / );

                    newGen.add(new Particle(particle.getKey().id, px, py, pv, ptita, pr, 0));
                    writer.write(String.format("%d %g %g %g %g %g\n", particle.getKey().id, px, py, pv, pr, ptita));
                }

            }


            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to " + OUTPUT_FILE);
        }

    }
}