package ar.edu.itba.ss;

public class Util {
    private static final double[] ALPHA = {3.0 / 16.0, 251.0 / 360.0, 1.0, 11.0 / 18.0, 1.0 / 6.0, 1.0 / 60.0},
            FACTORIAL = {1.0, 1.0, 2.0, 6.0, 24.0, 120.0};

    public static double taylorExpansion(double[] values, double dt, int startFrom) {
        double sum = values[startFrom];
        for (int i = startFrom + 1; i < values.length; i++) {
            sum += values[i] * Math.pow(dt, i - startFrom) / FACTORIAL[i - startFrom];
        }
        return sum;
    }

    public static double correct(double predicted, double dR2, double dt, int index) {
        return predicted + ALPHA[index] * dR2 * FACTORIAL[index] / Math.pow(dt, index);
    }

}
