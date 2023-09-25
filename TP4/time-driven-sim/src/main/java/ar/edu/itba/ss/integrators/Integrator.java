package ar.edu.itba.ss.integrators;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface Integrator {

    // TODO: check if methods should return or directly output to file for efficiency
    double[] solve(double r0, double v0, double dt, double tf, double mass, BiFunction<Double, Double, Double> force);

}
