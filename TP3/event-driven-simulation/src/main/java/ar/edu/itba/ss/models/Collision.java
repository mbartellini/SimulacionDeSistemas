package ar.edu.itba.ss.models;

public enum Collision {
    WITH_OTHER {
        @Override
        void collide(Particle[] involved) {

        }
    },
    WITH_OBSTACLE {
        @Override
        void collide(Particle[] involved) {

        }
    },
    WITH_HORIZONTAL_WALL {
        @Override
        void collide(Particle[] involved) {
            if(involved.length != 1) throw new IllegalArgumentException("Only one particle may collide with a wall");
            involved[0].setTheta(-involved[0].getTheta());
        }
    },
    WITH_VERTICAL_WALL {
        @Override
        void collide(Particle[] involved) {
            if(involved.length != 1) throw new IllegalArgumentException("Only one particle may collide with a wall");
            involved[0].setTheta(Math.PI - involved[0].getTheta());
        }
    };

    abstract void collide(Particle[] involved);
}
