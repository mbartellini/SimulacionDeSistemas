package ar.edu.itba.ss;

import ar.edu.itba.ss.utils.CellIndexMethod;
import ar.edu.itba.ss.utils.Particle;

import java.io.*;
import java.util.*;

import com.moandjiezana.toml.Toml;

/**
 * Main Cell Index Method Algorithm.
 *
 */
public class App {
    public static void main(String[] args) {
        String staticParticleFilePath = null;
        String dynamicParticleFilePath = null;
        String neighborFilePath = null;
        Double neighR = 1.0;
        Long gridCount = 5L;

        try {
            InputStream inputStream = new FileInputStream("config.toml");
            Toml toml = new Toml().read(inputStream);
            dynamicParticleFilePath = toml.getString("files.dynamicParticleFile", "");
            staticParticleFilePath = toml.getString("files.staticParticleFile", "");
            neighborFilePath = toml.getString("files.neighborFile", "");
            neighR = toml.getDouble("constants.neighR", neighR);
            gridCount = toml.getLong("constants.gridCount", gridCount);

            if (staticParticleFilePath.equals("") || dynamicParticleFilePath.equals(""))
                throw new Exception("Missing particleFile");
            if (neighborFilePath.equals(""))
                throw new Exception("Missing neighborFilepath");
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
                    particles.add(new Particle(i, x, y, radius, property));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        gridCount = (long) (L / (neighR + 2 * 1));

        long startTime = System.currentTimeMillis();
        CellIndexMethod cim = new CellIndexMethod(L/gridCount, gridCount, gridCount, neighR, true);
        cim.insertParticles(particles);

        Map<Particle, List<Particle>> neighbors = cim.getNeighbors(particles);
        long endTime = System.currentTimeMillis();
        System.out.printf("Runtime: %dms\n", endTime - startTime);

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(neighborFilePath));
            for (Map.Entry<Particle, List<Particle>> neighborList : neighbors.entrySet()) {
                writer.write(String.valueOf(neighborList.getKey().id));
                for (Particle particle : neighborList.getValue()) {
                    writer.write(String.format(" %d", particle.id));
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.err.println("Error while writing to " + neighborFilePath);
        }

    }
}
