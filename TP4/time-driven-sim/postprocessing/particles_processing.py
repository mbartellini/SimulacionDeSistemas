import math

from utils.utils import read_particles_data

if __name__ == "__main__":
    data = []
    for i in range(1, 6):
        data.append(read_particles_data(f'../data/particles_{i}_dynamic.txt', math.pow(10, -i)))
