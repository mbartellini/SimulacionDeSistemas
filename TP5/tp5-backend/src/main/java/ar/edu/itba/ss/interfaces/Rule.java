package ar.edu.itba.ss.interfaces;

@FunctionalInterface
public interface Rule {

    public void apply(State state);

}
