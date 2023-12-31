package ar.edu.itba.ss.utils;

import ar.edu.itba.ss.models.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Receives N particles, N, L, M, r_c
public class CellIndexMethod {
    private final double gridSize; // Size of the grid cells
    private final Long numCellsX, numCellsY;
    private final Map<Long, List<Particle>> grid;
    private final double r_c;
    private final boolean enforceBoundary;

    public CellIndexMethod(double gridSize, Long numCellsX, Long numCellsY, double r_c, boolean enforceBoundary) {
        this.gridSize = gridSize;
        this.numCellsX = numCellsX;
        this.numCellsY = numCellsY;
        this.grid = new HashMap<>();
        this.r_c = r_c;
        this.enforceBoundary = enforceBoundary;
    }

    public void insertParticles(List<Particle> particles) {
        for (Particle particle : particles) {
            long cellIndex = calculateCellIndex(particle);
            grid.computeIfAbsent(cellIndex, k -> new ArrayList<>()).add(particle);
        }
    }

    public Map<Particle, List<Particle>> getNeighbors(List<Particle> particles) {
        Map<Particle, List<Particle>> resultMap = new HashMap<>();
        for (Particle particle : particles) {
            resultMap.putIfAbsent(particle, getNeighbors(particle));
        }
        return resultMap;
    }

    public List<Particle> getNeighbors(Particle particle) {
        long cellIndex = calculateCellIndex(particle);
        List<Particle> neighbors = new ArrayList<>();

        for (long dx = -1L; dx <= 1; dx++) {
            for (long dy = -1L; dy <= 1; dy++) {
                long neighborCellX = (cellIndex % numCellsX) + dx;
                long neighborCellY = (cellIndex / numCellsX) + dy;

                if ((neighborCellX >= 0 && neighborCellX < numCellsX &&
                    neighborCellY >= 0 && neighborCellY < numCellsY) || (! this.enforceBoundary)) {
                    neighborCellX = getMod(neighborCellX, numCellsX);
                    neighborCellY = getMod(neighborCellY, numCellsY);

                    long neighborCellIndex = neighborCellX + neighborCellY * numCellsX;
                    List<Particle> cellParticles = grid.getOrDefault(neighborCellIndex, new ArrayList<>());
                    for (Particle p : cellParticles) {
                        if (particle.isBorderToBorderNeighbour(r_c, p)) {
                            neighbors.add(p);
                        }
                    }
                }
            }
        }

        return neighbors;
    }

    private long calculateCellIndex(Particle particle) {
        int cellX = (int) (particle.x / gridSize);
        int cellY = (int) (particle.y / gridSize);
        return cellX + cellY * numCellsX;
    }

    private long getMod(long x, long mod) {
        return ((x % mod) + mod) % mod;
    }
}