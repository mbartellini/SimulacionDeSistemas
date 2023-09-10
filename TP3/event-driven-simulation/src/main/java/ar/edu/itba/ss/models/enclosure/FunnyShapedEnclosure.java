package ar.edu.itba.ss.models.enclosure;

public class FunnyShapedEnclosure extends Enclosure {

    public FunnyShapedEnclosure(double L, double l) {
        walls.add(new Wall(0, -L/2, 0, L/2));
        walls.add(new Wall(0, L/2, L, L/2));
        walls.add(new Wall(L, L/2, L, l/2));
        walls.add(new Wall(L, l/2, 2*L, l/2));
        walls.add(new Wall(2*L, l/2, 2*L, -l/2));
        walls.add(new Wall(2*L, -l/2, L, -l/2));
        walls.add(new Wall(L, -l/2, L, -L/2));
        walls.add(new Wall(L, -L/2, 0, -L/2));
    }
}
