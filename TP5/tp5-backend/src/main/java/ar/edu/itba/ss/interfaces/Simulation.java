package ar.edu.itba.ss.interfaces;

public interface Simulation {

    public default void run(State initialState, Rule rule, double dt, long iterations, long printInterval) {
        State s = initialState;
        s.getStaticState();
        for(long i = 0; i < iterations; i++) {
            double time = i * dt;
            if (0 == i % printInterval)
                s.getDynamicState();
            rule.apply(s);
        }
    }

}
