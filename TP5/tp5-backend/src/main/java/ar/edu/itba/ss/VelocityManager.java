package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;

public class VelocityManager {

    private static final double EPS = 1e-5;

    private int currentTarget = 0;

    public double getDesiredVelocity(Particle p, double[] target, double distanceForArrival, double maxVelocity) {
        double distanceToTarget = Math.sqrt(Math.pow(p.getX() - target[0], 2) + Math.pow(p.getY() - target[1], 2));
        if (distanceToTarget <= distanceForArrival) {
            return 0.25;
        }
        return maxVelocity;
    }

}
