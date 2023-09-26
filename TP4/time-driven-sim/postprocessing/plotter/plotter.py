import matplotlib.pyplot as plt
from utils.utils import mse


def plot_oscillator(data, analytic, filename):
    plt.cla()
    dt, tf, position = data
    time = [dt * i for i in range(len(position))]

    ax = plt.gca()

    ax.set_xlabel(r'Time ($s$)')
    ax.set_ylabel(r'Position ($m$)')

    plt.xlim(0, 5)
    plt.ylim(-1, 1)

    ax.plot(time, position, color='blue', linestyle='dashdot', label='Approximation')
    ax.plot(time, analytic, color='orange', label='Exact', alpha=0.5)

    ax.legend()

    plt.savefig(f'figs/{filename}')



