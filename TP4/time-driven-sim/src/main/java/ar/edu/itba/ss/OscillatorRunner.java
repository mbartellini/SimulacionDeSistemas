package ar.edu.itba.ss;

import ar.edu.itba.ss.integrators.BeemanIntegrator;
import ar.edu.itba.ss.integrators.GPCIntegrator;
import ar.edu.itba.ss.integrators.Integrator;
import ar.edu.itba.ss.integrators.VerletIntegrator;

import java.io.FileWriter;
import java.io.IOException;

public class OscillatorRunner {

    private static final double MASS = 70, K = 10000, GAMMA = 100, TF = 5, A = 1.0;
    private static final Integrator[] INTEGRATORS = {
            new VerletIntegrator(), new BeemanIntegrator(), new GPCIntegrator()
    };
    private static final String[] INTEGRATOR_NAMES = {"verlet", "beeman", "gear"};
    private static final String PREFIX = "data/", SUFFIX = ".txt";
    private static final double[] DT = {0.1, 0.01, 0.001, 0.0001, 0.00001};


    public static void main(String[] args) {
        for(int i = 0; i < INTEGRATORS.length; i++) {
            final Integrator integrator = INTEGRATORS[i];
            for(double dt : DT) {
                final double[] solution = integrator.solve(
                        A, -A*GAMMA/(2*MASS), dt, TF, MASS, (r, v) -> -K*r - GAMMA*v
                );
                try (FileWriter fw = new FileWriter(String.format("%s%s%d%s",
                        PREFIX, INTEGRATOR_NAMES[i], (int)-Math.log10(dt), SUFFIX))) {
                    fw.write(String.format("%g %g", TF, dt));
                    for(double r : solution) {
                        fw.write(String.format("\n%g", r));
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }
    }
}
