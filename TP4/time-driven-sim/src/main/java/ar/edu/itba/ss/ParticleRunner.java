package ar.edu.itba.ss;

import ar.edu.itba.ss.integrators.GPCIntegrator;

public class ParticleRunner {

    private static final String OUTPUT_FILE = "data/particles.txt";
    private static final double DT = 0.0001, TF = 100, PRINT_DT = 0.01;
    private static final int N = 25;

    public static void main(String[] args) {
        final GPCIntegrator runner = new GPCIntegrator(OUTPUT_FILE, N, DT, TF, (int) Math.floor(PRINT_DT / DT));
        runner.run();
    }
}
