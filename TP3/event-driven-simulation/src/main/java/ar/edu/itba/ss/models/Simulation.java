package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.enclosure.Enclosure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.*;

public class Simulation {

    private final PriorityQueue<Event> events;
    private final Particle[] particles;
    private final Enclosure enclosure;
    private static final String DYNAMIC = "dynamic.txt", STATIC = "static.txt";
    private static final int ERROR_STATUS = 1;
    private final long iterations;
    private long length, count;

    public Simulation(int count, long iter, Enclosure enclosure) {
        particles = Particle.generateRandom(count, 0.0, 0.0, 0.0, 0.0, null);
        events = new PriorityQueue<>();
        this.enclosure = enclosure;

        for (Particle particle : particles) {
            events.add(particle.nextCollision(particles, enclosure));
        }

        this.iterations = iter;
    }

    public void run() {
        for(long i = 0; i < iterations; i++) {
            final Event current = events.poll();
            if(current == null) return;
            updateState(this.particles, current);
            writeState(i);

            final Iterator<Event> it = events.iterator();
            final List<Particle> involvedParticles =
                    new ArrayList<>(Arrays.stream(current.getParticlesInvolved()).toList());
            while(it.hasNext()) {
                final Event future = it.next();
                if(areInvolvedInCurrent(current, future)) {
                    it.remove();
                    involvedParticles.addAll(Arrays.stream(future.getParticlesInvolved()).toList());
                }
            }

            for(Particle p : involvedParticles) {
                events.add(p.nextCollision(particles, enclosure));
            }
        }
    }

    private static boolean areInvolvedInCurrent(Event current, Event future) {
        for(Particle p : current.getParticlesInvolved()) {
            for(Particle q : future.getParticlesInvolved()) {
                if(q.equals(p)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void updateState(Particle[] particles, Event details) {
        for(Particle p : particles) {
            p.updatePosition(details.getTimeToCollision());
        }
        details.applyCollision();
    }

    private void writeStatic() throws IOException {
        FileWriter fw = null;
        try {
            fw = new FileWriter(STATIC);
            fw.write((int) length);
            fw.write((int) count);
        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s", STATIC, e.getMessage());
            System.exit(ERROR_STATUS);
        } finally {
            if(fw != null) fw.close();
        }
    }

    private void writeState(long iteration) {

    }

}
