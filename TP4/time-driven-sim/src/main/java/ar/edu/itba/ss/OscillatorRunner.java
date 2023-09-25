package ar.edu.itba.ss;

import ar.edu.itba.ss.integrators.BeemanIntegrator;
import ar.edu.itba.ss.integrators.Integrator;
import ar.edu.itba.ss.integrators.VerletIntegrator;

import java.io.FileWriter;
import java.io.IOException;

public class OscillatorRunner {

    private static final double MASS = 70, K = 10000, GAMMA = 100, TF = 5, DT = 0.001, A = 1;

    public static void main(String[] args) {
        Integrator integrator = new BeemanIntegrator();
        double[] solution = integrator.solve(1, -A * GAMMA / (2 * MASS), DT, TF, MASS,
                (r, v) -> -K * r - GAMMA * v);

        System.out.println(solution.length);

        try (FileWriter fw = new FileWriter("dynamic.txt")) {
            fw.write(String.format("%g %g", TF, DT));
            for(double r : solution) {
                fw.write(String.format("\n%g", r));
            }
        } catch (IOException ex) {
            System.err.printf("Could not open file %s for writing: %s\n", "dynamic.txt", ex.getMessage());
            System.exit(1);
        }

    }
}
