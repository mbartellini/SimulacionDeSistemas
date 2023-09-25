package ar.edu.itba.ss.integrators;

import java.util.function.BiFunction;

public class BeemanIntegrator implements Integrator{
    @Override
    public double[] solve(double r0, double v0, double dt, double tf, double mass, BiFunction<Double, Double, Double> force) {
        int size = (int) Math.floor(tf / dt);
        double[] r = new double[size];
        double[] v = new double[] {v0, v0}; // v[0] -> v in time i-1, v[1] -> v in time i-2
        r[0] = r0;
        double rprev = r[0] - dt * v[0];
        r[1] = r[0] + dt * v[0] + 2 * dt*dt * force.apply(r[0], v[0]) / (3 * mass) -
                dt*dt * force.apply(rprev, v[1]) / (6 * mass);

        // TODO: Could this be done within the loop? Definitely. Do I want to think how to do it? No
        double predicted = v0 + 3 * dt * force.apply(r[0], v0) / (2 * mass) -
                dt * force.apply(rprev, v0) / (2 * mass);
        double corrected = v0 + dt * force.apply(r[1], predicted) / (3 * mass) +
                5 * dt * force.apply(r0, v0) / (6 * mass) - dt * force.apply(rprev, v0) / (6 * mass);
        v[0] = corrected;

        for(int i = 2; i < size; i++) {
            r[i] = r[i-1] + dt * v[0] + 2 * dt*dt * force.apply(r[i-1], v[0]) / (3 * mass)-
                    dt*dt * force.apply(r[i-2], v[1]) / (6 * mass);
            predicted = v[0] + 3 * dt * force.apply(r[i-1], v[0]) / (2 * mass) -
                    dt * force.apply(r[i-2], v[1]) / (2 * mass);
            corrected = v[0] + dt * force.apply(r[i], predicted) / (3 * mass) +
                    5 * dt * force.apply(r[i-1], v[0]) / (6 * mass) - dt * force.apply(r[i-2], v[1]) / (6 * mass);
            v[1] = v[0];
            v[0] = corrected;
        }

        return r;
    }
}
