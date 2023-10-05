package ar.edu.itba.ss;

import ar.edu.itba.ss.integrators.GPCIntegrator;

public class VelocityRunner {
    private static final String OUTPUT_FILE_FMT = "data/velocities_%d_%s.txt";
    private static final int[] N_VALUES = new int[] {5, 10, 15, 20, 25, 30};
    private static final double TF = 180, DT = 0.0001, PRINT_DT = 0.1; // PRINT_DT = 0.1

    public static void main(String[] args) {
        for(int N : N_VALUES) {
            final String dynamicFilename = String.format(OUTPUT_FILE_FMT, N, "dynamic");
            final String staticFilename = String.format(OUTPUT_FILE_FMT, N, "static");

            long start = System.currentTimeMillis();
            final GPCIntegrator runner = new GPCIntegrator(staticFilename, dynamicFilename, N, DT, TF, (int) Math.round(PRINT_DT / DT));
            runner.run();
            long end = System.currentTimeMillis();

            System.out.printf("%s done in %d ms\n", dynamicFilename, end - start);
        }
    }
}
