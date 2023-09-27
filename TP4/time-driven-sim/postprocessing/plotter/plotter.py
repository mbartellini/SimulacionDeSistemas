import matplotlib.pyplot as plt
from utils.utils import mse, analytic_solution
from typing import Dict


def plot_oscillator(data, filename):
    plt.cla()
    dt, tf, position = data
    time = [dt * i for i in range(len(position))]
    analytic = analytic_solution(data[0], data[1])

    ax = plt.gca()

    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'Position ($m$)')

    plt.xlim(0, 5)
    plt.ylim(-1, 1)

    ax.plot(time, position, color='blue', linestyle='dashdot', label='Approximation')
    ax.plot(time, analytic, color='orange', label='Exact', alpha=0.5)

    ax.legend()

    plt.savefig(f'figs/{filename}')


def plot_error(data: Dict, filename: str):
    dts, tf = [0.1, 0.01, 0.001, 0.0001, 0.00001], 5
    errors = {}
    for key in data.keys():
        errors[key] = []

    for i, dt in enumerate(dts):
        analytic = analytic_solution(dt, tf)
        for key in data.keys():
            errors[key].append(mse(analytic, data[key][i][2]))

    ax = plt.gca()
    plt.xscale('log')
    plt.yscale('log')
    ax.set_xlabel(r'$\Delta t$ ($s$)')
    ax.set_ylabel(r'MSE')

    for key in errors:
        ax.plot(dts, errors[key], label=key, marker='o')

    ax.legend()

    plt.savefig(f'figs/{filename}')
