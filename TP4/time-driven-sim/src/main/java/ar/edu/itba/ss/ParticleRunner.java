package ar.edu.itba.ss;

import ar.edu.itba.ss.integrators.GPCIntegrator;

import java.time.LocalTime;

public class ParticleRunner {

    private static final String OUTPUT_FILE_FMT = "data/particles_%d_%s.txt";
    private static final double TF = 180, PRINT_DT = 0.01;
    private static final double[] DTs = new double[] {0.1, 0.01, 0.001, 0.0001, 0.00001} ;
    private static final int N = 25;

    public static void main(String[] args) {
        for(double dt : DTs) {
            final String dynamicFilename = String.format(OUTPUT_FILE_FMT, (int) -Math.log10(dt), "dynamic");
            final String staticFilename = String.format(OUTPUT_FILE_FMT, (int) -Math.log10(dt), "static");

            long start = System.currentTimeMillis();
            final GPCIntegrator runner = new GPCIntegrator(staticFilename, dynamicFilename, N, dt, TF, (int) Math.round(PRINT_DT / dt));
            runner.run();
            long end = System.currentTimeMillis();

            System.out.printf("%s done in %d ms\n", dynamicFilename, end - start);
        }
    }
}
