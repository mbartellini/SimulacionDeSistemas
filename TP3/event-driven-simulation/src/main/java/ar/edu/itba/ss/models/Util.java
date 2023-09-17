package ar.edu.itba.ss.models;

public class Util {

    public static double dotProduct(double[] v1, double[] v2) {
        if(v1.length != v2.length) throw new IllegalArgumentException("Vector sizes do not match");

        double prod = 0.0;
        for (int i = 0; i < v1.length; i++) {
            prod += v1[i]*v2[i];
        }

        return prod;
    }

    public static boolean inRangeDouble(double x, double left, double right, double epsilon) {
        return left - epsilon <= x && x <= right + epsilon;
    }

}
