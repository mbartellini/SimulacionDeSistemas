from particle import Particle
from utils.parser import *
from utils.plots import *

# STATIC_FILE = "../data/Static100.txt"
# DYNAMIC_FILE = "../data/Dynamic100.txt"
# NEIGHBOR_FILE = "../data/Neighbor100.txt"

STATIC_FILE = "../data/ExperimentStatic.txt"
DYNAMIC_FILE = "../data/ExperimentDynamic.txt"
NEIGHBOR_FILE = "../data/ExperimentNeighbor.txt"

def main():
    particle_count, grid_size, static_properties = parse_static(STATIC_FILE)
    time, dynamic_data = parse_dynamic(DYNAMIC_FILE, particle_count)
    n_list = parse_neighbors(NEIGHBOR_FILE, particle_count)

    particles = []
    for p in range(particle_count):
        particles.append(Particle(
            dynamic_data[p][0],
            dynamic_data[p][1],
            static_properties[p][0],
            static_properties[p][1]
        ))

    plot_neighbors(grid_size, particles, 80, n_list)


if __name__ == '__main__':
    main()
