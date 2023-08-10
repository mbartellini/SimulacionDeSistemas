package ar.edu.itba.ss.utils;

public class Particle {
  public double x, y, property;
  public final long id;

  public final double r;

  public Particle(double x, double y, double r, long id, double property) {
      this.x = x;
      this.y = y;
      this.r = r;
      this.id = id;
      this.property = property;
  }

  public double getDistanceTo(Particle p) {
      return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
  }

  public boolean isBorderToBorderNeighbour(double radius, Particle p) {
      return this.getDistanceTo(p) < radius + this.r + p.r;
  }
}
