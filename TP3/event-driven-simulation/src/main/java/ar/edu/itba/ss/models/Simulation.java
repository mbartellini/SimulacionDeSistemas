package ar.edu.itba.ss.models;

import ar.edu.itba.ss.models.enclosure.Enclosure;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Simulation {
    private static final Random R = new Random(123456789L);
    private final PriorityQueue<Event> events;
    private final Particle[] particles;
    private final Enclosure enclosure;
    private static final String DYNAMIC = "./data/dynamic.xyz", STATIC = "./data/static.xyz", PRESSURE_FILE = "./data/pressure.xyz";
    private static final int ERROR_STATUS = 1;
    private final long iterations;
    private static final double V = 1.0, MASS = 1.0, RADIUS = 0.15;
    private final double deltaT;
    private double elapsed = 0.0, lastPressureMeasure = 0.0;

    public Simulation(int count, long iter, double length, double deltaT, Enclosure enclosure) {
        particles = Particle.generateRandom(count, V, RADIUS, MASS, length, R);
        events = new PriorityQueue<>();
        this.enclosure = enclosure;
        this.deltaT = deltaT;

        cleanFile(DYNAMIC);
        cleanFile(PRESSURE_FILE);
        writeStatic();
        writeState(0);

        for (Particle p : particles) {
            events.add(p.nextCollision(particles, enclosure));
        }

        this.iterations = iter;
    }

    public void run() {
        double impulseInterval = elapsed; // [impulseInterval, impulseInterval + deltaT)
        for(long i = 0; i < iterations; i++) {
            final Event current = events.poll();
            if(current == null)
                throw new IllegalStateException("There is no event.");

            elapsed += current.getTimeToCollision();
            updateState(this.particles, current);
            writeState(i);

            events.clear();
            for (Particle p : particles) {
                events.add(p.nextCollision(particles, enclosure));
            }

            if (elapsed >= impulseInterval + deltaT) {
                writePressure();
                enclosure.resetImpulse();
                impulseInterval += deltaT;
            }
            enclosure.addImpulse(current);
//
//            final Iterator<Event> it = events.iterator();
//            final List<Particle> involvedParticles =
//                    new ArrayList<>(Arrays.stream(current.getParticlesInvolved()).toList());
//            while(it.hasNext()) {
//                final Event future = it.next();
//                future.updateTime(current.getTimeToCollision());
//                if(areInvolvedInCurrent(current, future)) {
//                    it.remove();
//                    involvedParticles.addAll(Arrays.stream(future.getParticlesInvolved()).toList());
//                }
//            }
//
//            for(Particle p : involvedParticles) {
//                Event next = p.nextCollision(particles, enclosure);
//                if(next == null) {
//                    throw new IllegalStateException(String.format(
//                            "No collision to wall (%d, %d)", p.getId(), i)
//                    );
//                }
//                events.add(next);
//            }
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

    private void cleanFile(String file) {
        try {
            new FileWriter(file, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeStatic() {
        try (FileWriter fw = new FileWriter(STATIC)) {
            fw.write(String.format("%d\n%d\n", iterations, particles.length));
        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s", STATIC, e.getMessage());
            System.exit(ERROR_STATUS);
        }
    }

    private void writeState(long iteration) {
        try (FileWriter fw = new FileWriter(DYNAMIC, true)) {
            fw.append(String.format("%d\n", particles.length));
            fw.append(String.format("%g\n", elapsed));
            for(Particle p : particles) {
                fw.append(String.format("%d %g %g %g %g %g\n", p.getId(), p.getX(), p.getY(), p.getVx(), p.getVy(), p.getRadius()));
            }
        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s", DYNAMIC, e.getMessage());
            System.exit(ERROR_STATUS);
        }
    }

    private void writePressure() {
        try (FileWriter fw = new FileWriter(PRESSURE_FILE, true)) {
            double leftPressure = enclosure.getSidePressure(deltaT, Enclosure.Side.LEFT);
            double rightPressure = enclosure.getSidePressure(deltaT, Enclosure.Side.RIGHT);
            fw.append(String.format("%g %g %g\n", elapsed, leftPressure, rightPressure));
        } catch (IOException e) {
            System.err.printf("Error writing to %s: %s", PRESSURE_FILE, e.getMessage());
            System.exit(ERROR_STATUS);
        }
    }

}
