import math

from utils.utils import read_particles_data, phi_error
from plotter.plotter import plot_phi

if __name__ == "__main__":
    data = []
    for i in range(1, 6):
        data.append(read_particles_data(f'../data/particles_{i}_dynamic.txt', math.pow(10, -i)))

    for i in range(0, 5):
        print(len(data[i]['particles']))

    print(phi_error(data[3]['particles'][-1], data[4]['particles'][-1]))
    plot_phi(data)
