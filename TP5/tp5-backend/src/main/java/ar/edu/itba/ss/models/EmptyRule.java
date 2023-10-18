package ar.edu.itba.ss.models;

import ar.edu.itba.ss.interfaces.Rule;
import ar.edu.itba.ss.interfaces.State;

public class EmptyRule implements Rule {


    @Override
    public void apply(State state) {
        return;
    }
}
