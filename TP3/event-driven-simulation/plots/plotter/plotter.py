import matplotlib.pyplot as plt
import numpy as np

import utils.utils
from utils.utils import plot_name


def plot_lineplot(ax, x, y, label):
    ax.plot(x, y, label=label)


def plot_pressures(pressures, static):
    times, left, right, mix = [], [], [], []
    for p in pressures:
        if p[0] > 50:
            mix.append(0.5 * p[1]['left'] + 0.5 * p[1]['right'])
        times.append(p[0])
        left.append(p[1]['left'])
        right.append(p[1]['right'])

    with open("pressures.txt", 'a') as file:
        area = 0.09 ** 2 + 0.09 * static['L']
        print(area)
        file.write(f"{1.0 / area} {np.mean(mix)} {np.std(mix)}\n")

    ax = plt.gca()

    ax.set_xlabel(r'Time (s)')
    ax.set_ylabel(r'Pressure ($\frac{kg}{s^2}$)')

    plot_lineplot(ax, times, left, r'Recinto izquierdo')
    plot_lineplot(ax, times, right, r'Recinto derecho')

    plt.legend()

    plt.savefig(f'./figs/{plot_name(static, "pressures")}')


def param_evaluation(x_val, y_val) -> float:
    min_c, min_e = None, None
    c_val = np.arange(0.01, 0.035, 0.0002)
    error = []
    for c in c_val:
        e = utils.utils.mse(x_val, y_val, c)
        if min_e is None or e < min_e:
            min_e = e
            min_c = c
        error.append(e)

    ax = plt.gca()

    plt.scatter(c_val, error, s=4)
    plt.plot([min_c for _ in range(11)], np.arange(0, 1.1, 0.1), label=f'c* = {round(min_c, 4)}', color='orange')
    plt.ylim(0, 1)

    ax.set_xlabel(r'c ($\frac{1}{m^2}$)')
    ax.set_ylabel(r'ECM')

    plt.legend()
    plt.savefig(f'./figs/regression_1')

    return min_c


def plot_pressure_versus_area():
    data = []
    with open("pressures.txt", 'r') as file:
        for line in file:
            data.append(list(map(float, line.strip().split())))
    area = list(list(zip(*data))[0])
    mean = list(list(zip(*data))[1])
    std = list(list(zip(*data))[2])

    for a, p in zip(area, mean):
        print(p / a)

    c = param_evaluation(area, mean)

    plt.cla()
    ax = plt.gca()
    plt.errorbar(area, mean, yerr=std, fmt='o')

    x_adj = range(60, 100)
    y_adj = [x * c for x in x_adj]
    plt.plot(x_adj, y_adj, color='orange', label='Regresión')

    ax.set_xlabel(r'$A^{-1}$ ($\frac{1}{m^2}$)')
    ax.set_ylabel(r'Pressure ($\frac{kg}{s^2}$)')
    plt.legend(loc='upper left')
    plt.savefig(f'./figs/pressure_versus_area')


def evaluate_d(t_val, dcm_val):
    min_d, min_e = None, None
    d_val = np.arange(0.1, 0.275, 0.001)
    error = []
    for d in d_val:
        e = utils.utils.mse(t_val, dcm_val, 4 * d)
        if min_e is None or e < min_e:
            min_e = e
            min_d = d
        error.append(e)

    ax = plt.gca()

    plt.scatter(d_val, error, s=4)
    plt.plot([min_d for _ in range(-2, 70)], range(-2, 70), label=f'D* = {round(min_d, 4)}', color='orange')
    plt.ylim(-2, 70)

    ax.set_xlabel(r'D ($\frac{cm^2}{s}$)')
    ax.set_ylabel(r'ECM')

    plt.legend(loc='lower left')
    plt.savefig(f'./figs/regression_2')

    return min_d


def plot_dcm(static, dynamic):
    t0 = 0.0
    times = []
    dcm = []
    std = []
    reference = None
    for iteration in dynamic:
        if iteration[0] < 100:
            continue
        elif iteration[0] > 300:
            break
        else:
            if reference is None:
                reference = iteration[1]
                t0 = iteration[0]
            times.append(iteration[0] - t0)
            val = utils.utils.dcm(reference, iteration[1])
            dcm.append(val[0])
            std.append(val[1]/2)

    d = evaluate_d(times[:25], dcm[:25])
    plt.cla()

    ax = plt.gca()
    plt.errorbar(times, dcm, xerr=0.01, yerr=std, fmt='o', markersize=3)
    x_adj = range(50)
    y_adj = [4 * d * x for x in x_adj]
    plt.plot(x_adj, y_adj, label='Regresión', linewidth=5)

    ax.set_xlabel(r'$t$ ($s$)')
    ax.set_ylabel(r'DCM ($cm^2$)')

    plt.legend(loc="upper left")
    plt.savefig(f'figs/{plot_name(static, "dcm")}')
