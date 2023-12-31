import math

import matplotlib.pyplot as plt
import numpy as np

from utils.utils import mse, analytic_solution, phi_error, mean_vel, individual_density
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

    plt.xlim(0, 5)
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
    plt.grid()
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
        ax.plot(time, phi, label=f'$k = {i + 1}$', linewidth=2)

    ax.legend()
    plt.savefig(OUTPUT_DIRECTORY + '/phi_vs_time')


def plot_mean_vel(data):
    time = [_PRINT_DT * i for i in range(len(data[0]["particles"]))]

    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'$\overline{\omega}$ ($rad/s$)')

    for sim in data:
        n = sim['N']
        mean, std = mean_vel(sim['particles'])  # data[x]['particles']
        plt.plot(time, mean, label=f'N = {n}')

    ax.legend()
    plt.xlim(0, 180)
    plt.savefig(OUTPUT_DIRECTORY + '/mean_vel_vs_time', bbox_inches="tight")


def plot_individual_density(data):
    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'$\omega$ ($rad/s$)')
    ax.set_ylabel(r'$\rho$ ($\frac{1}{cm}$)')

    for sim in data:
        vel, rho = [], []
        for instant in sim['particles']:
            length = len(instant)
            for i, p in enumerate(instant):
                prev_idx, next_idx = (i - 1 + length) % length, (i + 1) % length
                vel.append(p['omega'])
                rho.append(individual_density(p, instant[prev_idx], instant[next_idx]))
        plt.scatter(vel, rho, label=f'N = {sim["N"]}', marker='.')

    ax.legend()
    plt.savefig(OUTPUT_DIRECTORY + '/individual_density', bbox_inches="tight")


def plot_individual_velocity(simulation, filename):
    size = len(simulation['particles'])
    time = [_PRINT_DT * i for i in range(size)]

    print(simulation.keys())
    print(size)

    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'$\omega$ ($rad/s$)')

    for p_id in range(simulation['N']):
        vel = [simulation['particles'][i][p_id]['omega'] for i in range(size)]
        plt.plot(time, vel, linewidth=2, label=f'id = {p_id}')

    ax.legend()
    plt.savefig(OUTPUT_DIRECTORY + '/' + filename, bbox_inches="tight")


_STARTING_TIME = 50


def plot_stationary_vel(data, filename):
    x_val = range(5, 31, 5)
    y_val = []
    y_error = []
    print(x_val)
    start_idx = int(50 / _PRINT_DT)
    print(start_idx)
    for sim in data:
        mean, std = mean_vel(sim['particles'][:start_idx])
        y_val.append(np.mean(mean))
        y_error.append(np.std(mean))

    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'N')
    ax.set_ylabel(r'$\overline{\omega}$ ($rad/s$)')

    plt.errorbar(x_val, y_val, fmt='o-')
    plt.savefig(OUTPUT_DIRECTORY + '/' + filename, bbox_inches='tight')


def _get_idx(bins, val):
    for i in range(len(bins)):
        if val < bins[i]:
            return i - 1
    return -1


def _mid_values(bins):
    ans = []
    for i in range(len(bins) - 1):
        ans.append(np.mean([bins[i], bins[i + 1]]))
    return ans


def plot_pdf(data, filename):
    start_idx = int(_STARTING_TIME / _PRINT_DT)

    min_vel, max_vel, size = None, None, 0
    for sim in data:
        for instant in sim['particles'][:start_idx]:
            for p in instant:
                size += 1
                if max_vel is None or p['omega'] > max_vel:
                    max_vel = p['omega']
                if min_vel is None or p['omega'] < min_vel:
                    min_vel = p['omega']

    bin_count = 1 + round(math.log2(size))
    bin_size = (max_vel - min_vel) / bin_count
    bins = np.arange(min_vel, max_vel, bin_size)
    bin_mid = _mid_values(bins)

    print(min_vel, max_vel)

    plt.cla()
    ax = plt.gca()
    ax.set_xlabel(r'$\omega$ ($rad/s$)')
    ax.set_ylabel(r'Densidad de probabilidad')

    for sim in data:
        n = sim['N']
        if not (n == 10 or n == 20 or n == 30):
            continue
        count = [0 for _ in range(bin_count - 1)]
        for instant in sim['particles'][:start_idx]:
            for p in instant:
                idx = _get_idx(bins, p['omega'])

                if idx < 0 or idx >= bin_count - 1:
                    print(idx, p['omega'])

                count[idx] += 1
        for i in range(len(count)):
            count[i] = count[i] / (bin_size * size)
        plt.plot(bin_mid, count, marker='o', label=f'N = {sim["N"]}')

    min_omega_i, max_omega_i= 9.0 / 21.94, 12.0 / 21.94
    initial_x = np.arange(min_omega_i, max_omega_i, 0.1)
    initial_y = [1/(max_omega_i - min_omega_i) for _ in initial_x]
    plt.plot(initial_x, initial_y, linestyle="dashed", label=f'Inicial')

    ax.legend()
    plt.savefig(OUTPUT_DIRECTORY + '/' + filename, bbox_inches='tight')
