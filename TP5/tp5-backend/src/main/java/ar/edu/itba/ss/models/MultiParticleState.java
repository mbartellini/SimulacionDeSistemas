package ar.edu.itba.ss.models;

import ar.edu.itba.ss.interfaces.State;

public class MultiParticleState implements State {

    private Particle[] particles;

    public MultiParticleState(int size) {
        assert size >= 0;
        particles = new Particle[size];
    }

    @Override
    public String getStaticState() {
        StringBuilder sb = new StringBuilder();
        for (Particle p : particles) {
            sb.append(p.toStaticFile());
        }
        return sb.toString();
    }

    @Override
    public String getDynamicState() {
        StringBuilder sb = new StringBuilder();
        for (Particle p : particles) {
            sb.append(p.toFile());
        }
        return sb.toString();
    }

}
