package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.CellIndexMethod;
import ar.edu.itba.ss.utils.Particle;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.moandjiezana.toml.Toml;

/**
 * Main Cell Index Method Algorithm.
 *
 */
public class App {
    public static void main(String[] args) {
        String staticParticleFilePath = null;
        String dynamicParticleFilePath = null;
        Double neighR = 1.0;
        Long gridCount = 2L;
        Long numCellsX = 5L;
        Long numCellsY = 5L;

        try {
            InputStream inputStream = new FileInputStream("config.toml");
            Toml toml = new Toml().read(inputStream);
            dynamicParticleFilePath = toml.getString("files.dynamicParticleFile", "");
            staticParticleFilePath = toml.getString("files.staticParticleFile", "");
            neighR = toml.getDouble("constants.neighR", neighR);
            gridCount = toml.getLong("constants.gridSize", gridCount);
            numCellsX = toml.getLong("constants.numCellsX", numCellsX);
            numCellsY = toml.getLong("constants.numCellsY", numCellsY);

            if (staticParticleFilePath.equals("") || dynamicParticleFilePath.equals(""))
                throw new Exception("Missing particleFile");

            if (gridCount == 0) {
                throw new Exception("Invalid gridCount of 0");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        List<Particle> particles = new ArrayList<>();
        long N = 0L;
        double L = 0.0, t;

        // Read the file
        try {
            File staticParticleFile = new File(staticParticleFilePath);
            Scanner staticScanner = new Scanner(staticParticleFile).useLocale(Locale.US);

            File dynamicParticleFile = new File(dynamicParticleFilePath);
            Scanner dynamicScanner = new Scanner(dynamicParticleFile).useLocale(Locale.US);

            // Read the first two values
            N = staticScanner.nextInt();
            L = staticScanner.nextDouble();
            t = dynamicScanner.nextDouble();


            // Read the rest of the lines and create Particle objects
            for (long i = 0; i < N; i++) {
                    double radius = staticScanner.nextDouble();
                    double property = staticScanner.nextDouble();
                    double x = dynamicScanner.nextDouble();
                    double y = dynamicScanner.nextDouble();
                    if (x < 0 || x > L || y < 0 || y > L) {
                        throw new Exception(String.format("Invalid Particle Location at: %g, %g", x, y));
                    }
                    particles.add(new Particle(x, y, radius, i, property));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        CellIndexMethod cim = new CellIndexMethod(L/gridCount, numCellsX, numCellsY, neighR);
        cim.insertParticles(particles);

        Particle queryParticle = new Particle(1, 7, 0, 0, 0);
        List<Particle> neighbors = cim.getNeighbors(queryParticle);

        System.out.println("Neighbors of the query particle:");
        for (Particle neighbor : neighbors) {
            System.out.println("Particle at (" + neighbor.x + ", " + neighbor.y + ")");
        }
    }
}
