package ar.edu.itba.ss.models;

public enum Collision {
    WITH_OTHER {
        @Override
        public void collide(Particle[] involved) {
            if(involved.length != 2) throw new IllegalArgumentException("Only two particles may collide");
            final double[]
                    r = new double[] {involved[1].getX() - involved[0].getX(), involved[1].getY() - involved[0].getY()},
                    v = new double[] {involved[1].getVx() - involved[0].getVx(),
                                      involved[1].getVy() - involved[0].getVy()};
            final double sigma = involved[0].getRadius() + involved[1].getRadius();
            final double J = (2 * involved[0].getMass() * involved[1].getMass() * Util.dotProduct(v, r)) /
                    (sigma * (involved[1].getMass() + involved[0].getMass()));
            final double Jx = (J * r[0]) / sigma, Jy = (J * r[1]) / sigma;

            involved[0].setVx(involved[0].getVx() + Jx / involved[0].getMass());
            involved[0].setVy(involved[0].getVy() + Jy / involved[0].getMass());

            involved[1].setVx(involved[1].getVx() - Jx / involved[1].getMass());
            involved[1].setVy(involved[1].getVy() - Jy / involved[1].getMass());

        }
    },
    WITH_OBSTACLE {
        @Override
        public void collide(Particle[] involved) {

        }
    },
    WITH_HORIZONTAL_WALL {
        @Override
        public void collide(Particle[] involved) {
            if(involved.length != 1) throw new IllegalArgumentException("Only one particle may collide with a wall");
            involved[0].setVy(-involved[0].getVy());
        }
    },
    WITH_VERTICAL_WALL {
        @Override
        public void collide(Particle[] involved) {
            if(involved.length != 1) throw new IllegalArgumentException("Only one particle may collide with a wall");
            involved[0].setVx(-involved[0].getVx());
        }
    },
    WITH_CORNER {
        @Override
        public void collide(Particle[] involved) {
            if(involved.length != 1) throw new IllegalArgumentException("Only one particle may collide with a corner");
            involved[0].setVx(-involved[0].getVx());
            involved[0].setVy(-involved[0].getVy());
            // TODO: Check if this makes sense as a particle with infinite mass
        }
    };

    public abstract void collide(Particle[] involved);
}
