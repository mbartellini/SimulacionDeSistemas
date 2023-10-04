import matplotlib.pyplot as plt
import numpy as np
from utils.utils import mse, analytic_solution, phi_error
from typing import Dict
import os

_PRINT_DT = 0.1

OUTPUT_DIRECTORY = "figs"

if not os.path.exists(OUTPUT_DIRECTORY):
    os.mkdir(OUTPUT_DIRECTORY)


def plot_oscillator(data: Dict, index, filename):
    plt.cla()

    linestyles = {
        'verlet': 'dashed',
        'beeman': 'dotted',
        'gear': 'dashdot'
    }

    analytic_dt, tf = 0.00001, 5
    analytic = analytic_solution(analytic_dt, tf)
    analytic_time = [i * analytic_dt for i in range(len(analytic))]

    ax = plt.gca()

    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'Position ($m$)')

    plt.xlim(0, 4)
    plt.ylim((-1, 1))

    ax.plot(analytic_time, analytic, color='orange', label='Exact', alpha=0.5)
    for key in data.keys():
        dt, tf, position = data[key][index]
        time = [dt * i for i in range(len(position))]
        ax.plot(time, position, linestyle=linestyles[key], label=key)

    ax.legend()

    plt.savefig(f'figs/{filename}')


def plot_error(data: Dict, filename: str):
    dts, tf = [0.01, 0.001, 0.0001, 0.00001, 0.000001], 5
    errors = {}
    for key in data.keys():
        errors[key] = []

    for i, dt in enumerate(dts):
        analytic = analytic_solution(dt, tf)
        for key in data.keys():
            errors[key].append(mse(analytic, data[key][i + 1][2]))

    ax = plt.gca()
    plt.xscale('log')
    plt.yscale('log')
    ax.set_xlabel(r'$\Delta t$ ($s$)')
    ax.set_ylabel(r'MSE')

    for key in errors:
        ax.plot(dts, errors[key], label=key, marker='o')

    ax.legend()

    plt.savefig(f'figs/{filename}')


def plot_phi(data):
    # assuming all data have same sizes
    time = [i * _PRINT_DT for i in range(len(data[0]['particles']))]

    phis = []
    for k in range(1, 5):
        print(f"k = {k}")
        phis.append([])
        for instant in range(len(data[0]['particles'])):
            my_error = phi_error(data[k - 1]['particles'][instant], data[k]['particles'][instant])
            # my_error = np.log10(my_error)
            phis[-1].append(my_error)

    ax = plt.gca()
    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'$\Phi^k$ (rad)')

    for i, phi in enumerate(phis):
        ax.plot(time[:1500], phi[:1500], label=f'$k = {i+1}$', linewidth=2)

    ax.legend()
    plt.yscale('log')
    plt.savefig(OUTPUT_DIRECTORY + '/phi_vs_time')
