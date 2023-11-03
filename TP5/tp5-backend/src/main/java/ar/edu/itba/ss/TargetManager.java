package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;

public class TargetManager {

    private static final double EPS = 1e-5;
    private final double[][] targets = {
            // 9.75, -6.5,
            {-9.75, 6.5},
            {-3.25, -6.5},
            {3.25, -6.5},
            {9.75, 6.5},
    };

    private int currentTarget = 0;

    public void checkState(Particle p) {
        double distanceToTarget = Math.sqrt(Math.pow(p.getX() - targets[currentTarget][0], 2) + Math.pow(p.getY() - targets[currentTarget][1], 2));
        if (distanceToTarget <= 0.3 && currentTarget < targets.length-1) {
            currentTarget++;
        }
    }

    public double[] getTarget() {
        return targets[currentTarget];
    }

}
