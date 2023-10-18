package ar.edu.itba.ss;

import ar.edu.itba.ss.interfaces.Rule;
import ar.edu.itba.ss.interfaces.Simulation;
import ar.edu.itba.ss.interfaces.State;

import java.io.FileWriter;
import java.io.IOException;

public class Runner {

    public static void main(String[] args) {
        Simulation s = new Simulation() {
            @Override
            public void run(State initialState, Rule rule, double dt, long iterations, long printInterval) {
                Simulation.super.run(initialState, rule, dt, iterations, printInterval);
            }
        };

        // s.run();

    }
}
