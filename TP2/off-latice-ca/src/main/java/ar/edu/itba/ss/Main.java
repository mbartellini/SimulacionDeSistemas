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
    static final String OUTPUT_FILE = "data/tp2/DynamicCA.txt";
    public static void main(String[] args) {
        Double v = 1.0, noise = 0.1, r = 1.0, L = 10.;
        Long N = 100L, iter = 100L, deltaT = 1L;
        // Set the seed for the random number generator
        long seed = 123456789L; // Change this seed value to any long value preferred

        try {
            InputStream inputStream = new FileInputStream("config.toml");
            Toml toml = new Toml().read(inputStream);
            N = toml.getLong("simulation.N", N);
            L = toml.getDouble("simulation.L", L);
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
            writer.write("0\n");
            for (Particle particle : particles) {
                writer.write(String.format("%d %g %g %g %g %g\n", particle.id, particle.x, particle.y, particle.v, particle.r, particle.theta));
            }

            Long gridCount = (long) Math.floor(L / r);
            double gridSize = (double) L /gridCount;

            for (long i = 1; i <= iter; i++) {

                CellIndexMethod cim = new CellIndexMethod(gridSize, gridCount, gridCount, r, false);
                cim.insertParticles(particles);

                Map<Particle, List<Particle>> neighbors = cim.getNeighbors(particles);

                // Logic of change & Write generation to output file. (Dynamically)
                writer.write(String.format("%d\n", i));

                List <Particle> newGen = new ArrayList<>();

                for (Map.Entry<Particle, List<Particle>> particle : neighbors.entrySet()) {
                    double px, py, pv = particle.getKey().v, pr = particle.getKey().r, ptheta;
                    double psin = Math.sin(particle.getKey().theta), pcos = Math.cos(particle.getKey().theta);

                    px = particle.getKey().x + deltaT*Math.cos(particle.getKey().theta)*particle.getKey().v;
                    py = particle.getKey().y + deltaT*Math.sin(particle.getKey().theta)*particle.getKey().v;

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
        } catch (IOException e) {
            System.err.println("Error while writing to " + OUTPUT_FILE);
        }

    }
}