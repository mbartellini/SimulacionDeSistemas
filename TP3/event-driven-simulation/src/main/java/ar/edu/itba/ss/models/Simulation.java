package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.enclosure.Enclosure;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOError;
import java.io.IOException;
import java.util.*;

public class Simulation {
    private static final Random R = new Random(123456789L);
    private final PriorityQueue<Event> events;
    private final Particle[] particles;
    private final Enclosure enclosure;
    private static final String DYNAMIC = "dynamic.txt", STATIC = "static.txt";
    private static final int ERROR_STATUS = 1;
    private final long iterations;
    private static final double V = 0.01, MASS = 1, RADIUS = 0.0015;
    private final double deltaT;
    private double ellapsed = 0.0, lastPressureMeasure = 0.0;

    public Simulation(int count, long iter, double length, double deltaT, Enclosure enclosure) {
        particles = Particle.generateRandom(count, V, RADIUS, MASS, length, R);
        events = new PriorityQueue<>();
        this.enclosure = enclosure;
        this.deltaT = deltaT;

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
            enclosure.addImpulse(current);
            writeState(i);

            while(ellapsed - lastPressureMeasure > deltaT) {
                lastPressureMeasure += deltaT;
                System.out.printf("Left side pressure: %g\n", enclosure.getSidePressure(deltaT, Enclosure.Side.LEFT));
                System.out.printf("Right side pressure: %g\n", enclosure.getSidePressure(deltaT, Enclosure.Side.RIGHT));
                enclosure.resetImpulse();
            }

            final Iterator<Event> it = events.iterator();
            final List<Particle> involvedParticles =
                    new ArrayList<>(Arrays.stream(current.getParticlesInvolved()).toList());
            while(it.hasNext()) {
                final Event future = it.next();
                future.updateTime(current.getTimeToCollision());
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
        try (FileWriter fw = new FileWriter(STATIC)) {
            fw.write(String.format("%d\n%d\n", iterations, particles.length));
        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s", STATIC, e.getMessage());
            System.exit(ERROR_STATUS);
        }
    }

    private void writeState(long iteration) {
        try (FileWriter fw = new FileWriter(DYNAMIC)) {
            fw.write(String.format("%d\n", iteration));
            for(Particle p : particles) {
                fw.write(String.format("%d %g %g\n", p.getId(), p.getX(), p.getY()));
            }
        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s", DYNAMIC, e.getMessage());
            System.exit(ERROR_STATUS);
        }
    }

}
