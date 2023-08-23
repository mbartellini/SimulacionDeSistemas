from numbers import Number
from typing import List

import matplotlib.pyplot as plt
import numpy as np

from particle import Particle

DPI_RESOLUTION = 500

plt.style.use('_mpl-gallery')


def plot_particles(l: int, particles: List[Particle], fig=None, ax=None):
    x = [particle.x for particle in particles]
    y = [particle.y for particle in particles]
    sizes = [particle.r for particle in particles]

    if fig is None or ax is None:
        fig, ax = plt.subplots(dpi=DPI_RESOLUTION)
    ax.scatter(x, y, s=sizes) # ax.scatter(x, y, s=sizes, c=colors, vmin=0, vmax=100)
    ax.set(xlim=(0, l), ylim=(0, l))
    ax.set_xticks([])
    ax.set_yticks([])

    plt.show()


def plot_neighbors(l: int, particles: List[Particle], chosen: int, neighbor_list: List[List[int]]):
    fig, ax = plt.subplots(dpi=DPI_RESOLUTION)
    neighbors = neighbor_list[chosen]
    neighbors_particles = [particles[n] for n in neighbors]
    ax.scatter(
        [n.x for n in neighbors_particles],
        [n.y for n in neighbors_particles],
        s=[n.r for n in neighbors_particles],
        c='#ffef00',
        zorder=2
    )
    ax.scatter(
        [particles[chosen].x],
        [particles[chosen].y],
        s=[particles[chosen].r],
        c='#ff0000',
        zorder=2
    )
    plot_particles(l, particles, fig, ax)

    plt.show()


def plot_line(x: List[Number], y: List[Number], dy: List[Number], fig=None, ax=None):
    if fig is None or ax is None:
        fig, ax = plt.subplots(dpi=DPI_RESOLUTION)
    ax.plot(x, y, '-')
    # ax.errorbar(x, y, yerr=dy)

    plt.show()


def plot_multiline(n: List[List[Number]], y: List[List[Number]], dy: List[List[Number]]):
    fig, ax = plt.subplots(dpi=DPI_RESOLUTION)
    for i in range(len(y)):
        ax.plot(n[i], y[i], dy[i], '-')
    plt.show()


