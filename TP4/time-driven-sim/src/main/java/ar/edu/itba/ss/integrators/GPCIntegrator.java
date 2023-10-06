package ar.edu.itba.ss.integrators;

import ar.edu.itba.ss.Util;
import ar.edu.itba.ss.models.Particle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.BiFunction;

public class GPCIntegrator implements Integrator {
    private static final double SYSTEM_RADIUS = 21.49, MASS = 25.0, RADIUS = 2.25, MIN_UI = 9.0, MAX_UI = 12.0;
    private double dt, tf;
    private int printEach;
    private Particle[] particles;
    private String output, staticOutput;

    public GPCIntegrator() {
    }

    public GPCIntegrator(String staticOutput, String output, int N, double dt, double tf, int printEach, boolean order) {
        this.staticOutput = staticOutput;
        this.output = output;
        this.dt = dt;
        this.printEach = Math.max(printEach, 1);
        this.tf = tf;
        final Random r = new Random(1234567890L);
        particles = new Particle[N];
        for (int i = 0; i < particles.length; i++) {
            final double ui = r.nextDouble() * (MAX_UI - MIN_UI) + MIN_UI;
            particles[i] = new Particle(i, i * 2 * Math.PI / N, ui / SYSTEM_RADIUS, RADIUS, MASS, ui);
        }

        if(order) {
            Arrays.sort(particles, Comparator.comparingDouble(Particle::getLimit));
            for (int i = 0; i < particles.length; i++) {
                particles[i].setAngle(i * 2 * Math.PI / N);
            }
        }

        this.writeStaticState();
    }

    @Override
    public double[] solve(double r0, double v0, double dt, double tf, double mass, BiFunction<Double, Double, Double> force) {
        int size = (int) Math.floor(tf / dt);
        final double[] position = new double[size];

        final double[] r = new double[6], rpred = new double[6]; // Order 5
        position[0] = r[0] = r0;
        r[1] = v0;
        r[2] = force.apply(r0, v0) / mass;

        for (int i = 1; i < size; i++) {
            // Predict
            for (int j = 0; j < rpred.length; j++) {
                rpred[j] = Util.taylorExpansion(r, dt, j);
            }

            // Evaluate
            final double da = (force.apply(rpred[0], rpred[1]) / mass) - rpred[2];
            final double dR2 = da * dt * dt * 0.5;

            // Correct
            for (int j = 0; j < r.length; j++) {
                r[j] = Util.correct(rpred[j], dR2, dt, j);
            }

            position[i] = r[0];
        }

        return position;
    }

    public void run() {
        cleanFile(output);

        final long iterations = (long) Math.floor(tf / dt);
        System.out.printf("Iterations: %d, print each %d\n", iterations, printEach);
        for (long i = 0; i < iterations; i++) {
            if(i % printEach == 0)
                writeState();
            for (Particle p : particles) {
                p.predict(dt);
            }

            for (int j = 0; j < particles.length; j++) {
                int prev = (j - 1 + particles.length) % particles.length, next = (j + 1) % particles.length;
                particles[j].correct(dt, particles[prev], particles[next]);
            }
        }
    }

    private void writeStaticState() {
        cleanFile(staticOutput);
        try (final FileWriter fw = new FileWriter(this.staticOutput, true)) {
            fw.write(String.format("%d\n", particles.length));
            fw.write(String.format("%s\n", Particle.OVITO_FORMAT_STATIC));
            for (Particle p : particles) {
                fw.write(p.toStaticFile() + "\n");
            }
        } catch (IOException ex) {
            System.err.printf("Error writing to file %s: %s", staticOutput, ex.getMessage());
            System.exit(1);
        }
    }

    private void writeState() {
        try (final FileWriter fw = new FileWriter(output, true)) {
            fw.write(String.format("%d\n", particles.length));
            fw.write(String.format("%s\n", Particle.OVITO_FORMAT));
            for (Particle p : particles) {
                fw.write(p.toFile() + "\n");
            }
        } catch (IOException ex) {
            System.err.printf("Error writing to file %s: %s", output, ex.getMessage());
            System.exit(1);
        }
    }

    private void cleanFile(String file) {
        try {
            new FileWriter(file, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
