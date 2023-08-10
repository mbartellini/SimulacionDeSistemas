from particle import Particle
from utils.parser import *
from utils.plots import plot_particles


def main():
    particle_count, grid_size, static_properties = parse_static("../data/Static100.txt")
    time, dynamic_data = parse_dynamic("../data/Dynamic100.txt", particle_count)
    particles = []
    for p in range(particle_count):
        particles.append(Particle(
            dynamic_data[p][0],
            dynamic_data[p][1],
            static_properties[p][0],
            static_properties[p][1]
        ))

    plot_particles(grid_size, particles)


if __name__ == '__main__':
    main()
