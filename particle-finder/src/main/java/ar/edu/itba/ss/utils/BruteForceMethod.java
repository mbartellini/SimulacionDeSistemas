package ar.edu.itba.ss.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BruteForceMethod {

    private final double gridSize, rc;
    private final boolean enforceBoundary;
    private final List<Particle> particles = new ArrayList<>();

    public BruteForceMethod(double gridSize, double rc, boolean enforceBoundary) {
        this.gridSize = gridSize;
        this.rc = rc;
        this.enforceBoundary = enforceBoundary;
    }

    void insertParticles(List<Particle> particles){
        this.particles.addAll(particles);
    }

    public List<Particle> getNeighbors(Particle target) {
        final List<Particle> ans = new ArrayList<>();

        for(Particle p : particles) {
            if(target.isBorderToBorderNeighbour(rc, p)) {
                ans.add(p);
            }
        }

        return ans;
    }

    public Map<Particle, List<Particle>> getNeighbors(List<Particle> targets) {
        final Map<Particle, List<Particle>> ans = new HashMap<>();

        for(Particle target : targets) {
            ans.put(target,getNeighbors(target));
        }

        return ans;
    }

}
