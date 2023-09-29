from utils.utils import read_oscillator_data
from plotter.plotter import plot_oscillator, plot_error

if __name__ == '__main__':
    algs = ['verlet', 'beeman', 'gear']
    data = {}
    for alg in algs:
        data[alg] = []
        for i in range(1, 6):
            data[alg].append(read_oscillator_data(f'../data/{alg}{i}.txt'))

    plot_error(data, 'error_vs_dt')
    plot_oscillator(data, 2, 'solution')
