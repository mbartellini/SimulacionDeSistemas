package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Particle;
import ar.edu.itba.ss.models.PredeterminedParticle;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Simulation {

    public static final String STATIC_OUTPUT = "../tp5-data/Simulation_STATIC.txt",
            DYNAMIC_OUTPUT = "../tp5-data/Simulation_DYNAMIC.txt";
    public static final double VMAX = 1.75, TAU = 1.64, DISTANCE = 3.1;
    private static final double HEURISTIC_DISTANCE = 0.9;
    private static final double A_P = 2000, B_P = 0.08;

    private final Particle agent;
    private final List<PredeterminedParticle> predeterminedParticles = new ArrayList<>();
    private final double dt, tf;
    private final int printEach;

    public Simulation(Particle agent, Map<Integer, List<Runner.InputData>> inputDataByID, double dt, double tf, int printEach) {
        this.agent = agent;
        for (Map.Entry<Integer, List<Runner.InputData>> entry : inputDataByID.entrySet()) {
            entry.getValue().sort(Comparator.comparing(Runner.InputData::getSecond));
            this.predeterminedParticles.add(new PredeterminedParticle(entry.getKey(),
                    entry.getValue().get(0).getX(),
                    entry.getValue().get(0).getY(),
                    0.3,
                    70,
                    entry.getValue()));
        }
        this.dt = dt;
        this.tf = tf;
        this.printEach = printEach;

        this.writeStaticState();
    }

    public void run() {
        cleanFile(DYNAMIC_OUTPUT);
        TargetManager targetManager = new TargetManager();
        VelocityManager velocityManager = new VelocityManager();

        for (long i = 0; i * dt < tf; i++) {
            if(i % printEach == 0)
                writeState(i * dt);
            for (PredeterminedParticle p : predeterminedParticles) {
                p.setPosition(i * dt);
            }
            targetManager.checkState(agent);

            double[] target = targetManager.getTarget();
            System.out.printf("%g %g\n", agent.getX(), agent.getY());
            double desiredVelocity = velocityManager.getDesiredVelocity(agent, target, DISTANCE, VMAX);
            double[] force = new double[2];
            double[] vectorToTarget = {target[0] - agent.getX(), target[1] - agent.getY()};
            double length = Math.sqrt(Math.pow(vectorToTarget[0], 2) + Math.pow(vectorToTarget[1], 2));
            double[] versorToTarget = {vectorToTarget[0] / length, vectorToTarget[1]/length};
            Double minDistance = null;
            Particle neigh = null;
            for (Particle other : predeterminedParticles) {
                double distance = Math.sqrt(Math.pow(other.getX() - agent.getX(), 2) + Math.pow(other.getY() - agent.getY(), 2));
                if (minDistance == null || distance < minDistance) {
                    minDistance = distance;
                    neigh = other;
                }
                double[] vectorToNeighbor = {other.getX() - agent.getX(), other.getY() - agent.getY()};
                length = Math.sqrt(Math.pow(vectorToNeighbor[0], 2) + Math.pow(vectorToNeighbor[1], 2));
                double[] versorToNeighbor = {vectorToNeighbor[0] / length, vectorToNeighbor[1]/length};
                if (distance < 2 * 0.3) {
                    force[0] += (distance - 2 * 0.3) * 12000 * versorToNeighbor[0];
                    force[1] += (distance - 2 * 0.3) * 12000 * versorToNeighbor[1];
                } else {
                    double angle = Math.atan2(versorToNeighbor[1], versorToNeighbor[0]);
                    double magnitude = A_P * Math.exp(- (distance - 2 * 0.3)/B_P) * Math.cos(angle);
                    double[] nersor = {versorToNeighbor[0] * magnitude,
                        versorToNeighbor[1] * magnitude};
                    versorToTarget[0] -= nersor[0];
                    versorToTarget[1] -= nersor[1];
                }
            }
            length = Math.sqrt(Math.pow(versorToTarget[0], 2) + Math.pow(versorToTarget[1], 2));
            versorToTarget[0] /= length;
            versorToTarget[1] /= length;
            double[] drivingForce = {agent.getMass() / TAU * (VMAX * versorToTarget[0] - agent.getVx()),
                agent.getMass() / TAU * (VMAX * versorToTarget[1] - agent.getVy())};
            force[0] += drivingForce[0];
            force[1] += drivingForce[1];
            double[] acc = {force[0] / agent.getMass(), force[1] / agent.getMass()};

            agent.setVx(agent.getVx() + acc[0] * dt);
            agent.setVy(agent.getVy() + acc[1] * dt);
            agent.setX(agent.getX() + agent.getVx() * dt);
            agent.setY(agent.getY() + agent.getVy() * dt);
        }
    }

    private void writeStaticState() {
        cleanFile(STATIC_OUTPUT);
        try (final FileWriter fw = new FileWriter(STATIC_OUTPUT, true)) {
            fw.write(String.format("%d\n", predeterminedParticles.size() + 1));
            fw.write(String.format("%s\n", Particle.OVITO_FORMAT_STATIC));
            for (Particle p : predeterminedParticles) {
                fw.write(p.toStaticFile() + "\n");
            }
            fw.write(agent.toStaticFile() + "\n");
        } catch (IOException ex) {
            System.err.printf("Error writing to file %s: %s", STATIC_OUTPUT, ex.getMessage());
            System.exit(1);
        }
    }

    private void writeState(double t) {
        try (final FileWriter fw = new FileWriter(DYNAMIC_OUTPUT, true)) {
            // fw.write(String.format("%d\n", predeterminedParticles.size() + 1));
            // fw.write(String.format("%s\n", Particle.OVITO_FORMAT));
            for (Particle p : predeterminedParticles) {
                fw.write(String.format("%g\t%g\t%g\t%d\n", t, p.getY(), p.getX(), p.getId()));
            }
            fw.write(String.format("%g\t%g\t%g\t%d\n", t, agent.getY(), agent.getX(), agent.getId()));
        } catch (IOException ex) {
            System.err.printf("Error writing to file %s: %s", DYNAMIC_OUTPUT, ex.getMessage());
            System.exit(1);
        }
    }

    private void cleanFile(String file) {
        try {
            new FileWriter(file, false).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
