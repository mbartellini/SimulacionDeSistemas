from particle import Particle
from utils.parser import *


def main():
    particle_count, grid_size, static_properties = parse_static("Static100.txt")
    time, dynamic_data = parse_dynamic("Dynamic100.txt", particle_count)
    particles = []
    for p in range(particle_count):
        particles.append(Particle(
            dynamic_data[p][0],
            dynamic_data[p][1],
            static_properties[p][0],
            static_properties[p][1]
        ))


if __name__ == '__main__':
    main()
