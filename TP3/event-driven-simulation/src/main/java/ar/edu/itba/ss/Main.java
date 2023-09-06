package ar.edu.itba.ss;

import ar.edu.itba.ss.models.Collision;
import ar.edu.itba.ss.models.Event;

import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class Main {

    private static final Random r = new Random(123456789L);

    public static void main(String[] args) {
        System.out.println("Hello world");

        Set<Event> set = new TreeSet<>();


        for (int i = 0; i < 100; i++) {
            set.add(new Event(r.nextDouble(), null, Collision.WITH_HORIZONTAL_WALL));
        }

        for (Event cd : set) {
            System.out.println(cd.getTimeToCollision());
        }

    }

}
