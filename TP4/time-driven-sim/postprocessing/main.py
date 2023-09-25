from utils.utils import read_oscillator_data, analytic_solution
from plotter.plotter import plot_oscillator

if __name__ == '__main__':
    data = read_oscillator_data("../dynamic.txt")
    analytic = analytic_solution(data[0], data[1])
    plot_oscillator(data, analytic, "solutions")
