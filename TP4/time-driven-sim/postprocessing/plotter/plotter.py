import matplotlib.pyplot as plt
from utils.utils import mse, analytic_solution, phi_error, mean_vel, individual_density
from typing import Dict
import os

_PRINT_DT = 0.01

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
            phis[-1].append(my_error)

    ax = plt.gca()
    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'$\Phi^k$ (rad)')

    for i, phi in enumerate(phis):
        if i == 0:
            continue
        ax.plot(time, phi, label=f'$k = {i+1}$', linewidth=2)

    ax.legend()
    plt.savefig(OUTPUT_DIRECTORY + '/phi_vs_time')


def plot_mean_vel(data):
    time = [_PRINT_DT * i for i in range(len(data[0]["particles"]))]

    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'$\overline{\omega}$ ($\frac{rad}{s}$)')

    for sim in data:
        n = sim['N']
        mean, std = mean_vel(sim['particles']) # data[x]['particles']
        plt.errorbar(time, mean, label=f'N = {n}')

    ax.legend()
    plt.xlim(0, 180)
    plt.savefig(OUTPUT_DIRECTORY + '/mean_vel_vs_time')


def plot_individual_density(data):
    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'$\omega$ ($rad$)')
    ax.set_ylabel(r'$\rho$ ($\frac{1}{cm}$)')

    for sim in data:
        vel, rho = [], []
        for instant in sim['particles']:
            length = len(instant)
            for i, p in enumerate(instant):
                prev_idx, next_idx = (i - 1 + length) % length, (i + 1) % length
                vel.append(p['omega'])
                rho.append(individual_density(p, instant[prev_idx], instant[next_idx]))
        plt.scatter(vel, rho, label=f'N = {sim["N"]}')

    ax.legend()
    plt.savefig(OUTPUT_DIRECTORY + '/individual_density')
