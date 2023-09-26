package ar.edu.itba.ss.integrators;

import java.util.function.BiFunction;

public class GPCIntegrator implements Integrator {

    private final double[] ALPHA = {3.0/16.0, 251.0/360.0, 1, 11.0/18.0, 1.0/6.0, 1.0/60.0};

    @Override
    public double[] solve(double r0, double v0, double dt, double tf, double mass, BiFunction<Double, Double, Double> force) {
        int size = (int) Math.floor(tf / dt);
        final double[] position = new double[size];

        final double[] r = new double[6], rpred = new double[6]; // Order 5
        r[0] = r0; r[1] = v0; r[2] = force.apply(r0, v0) / mass;

        for(int i = 1; i < size; i++) {
            // Predict
            for(int j = 0; j < rpred.length; j++) {
                rpred[j] = taylorExpansion(r, dt, j);
            }

            final double da = (force.apply(rpred[0], rpred[1]) / mass) - rpred[2];
            final double dR2 = da * dt * dt * 0.5;

            // Correct
            double factorial = 1.0;
            for(int j = 0; j < r.length; j++) {
                if(j != 0)
                    factorial *= j;
                r[j] = rpred[j] + ALPHA[j] * dR2 * factorial / Math.pow(dt, j);
            }

            position[i] = r[0];
        }

        return position;
    }

    private double taylorExpansion(double[] values, double dt, int startFrom) {
        double sum = values[startFrom], factorial = 1.0;
        for(int i = startFrom + 1; i < values.length; i++) {
            factorial *= i - startFrom;
            sum += values[i] * Math.pow(dt, i) / factorial;
        }
        return sum;
    }

}
