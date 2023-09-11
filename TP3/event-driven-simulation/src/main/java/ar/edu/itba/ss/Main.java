package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Simulation;
import ar.edu.itba.ss.models.enclosure.Enclosure;
import com.moandjiezana.toml.Toml;

import java.io.FileInputStream;
import java.io.InputStream;

public class Main {
    private static final String CONFIG = "config.toml";
    private static final double SIDE_LENGTH = 0.09;

    public static void main(String[] args) {
        Long N = 100L, iter = 100L;
        Double L = 0.03, deltaT = 1.0;
        try {
            InputStream inputStream = new FileInputStream(CONFIG);
            Toml toml = new Toml().read(inputStream);
            N = toml.getLong("simulation.N", N);
            L = toml.getDouble("simulation.L", L);
            deltaT = toml.getDouble("simulation.L", deltaT);
            iter = toml.getLong("simulation.iter", iter);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }

        final Enclosure enc = new Enclosure(SIDE_LENGTH, L);
        final Simulation simulation = new Simulation(Math.toIntExact(N), iter, SIDE_LENGTH, deltaT, enc);
        simulation.run();

    }

}
