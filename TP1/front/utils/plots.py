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
    print(neighbors)
    neighbors_particles = [particles[n] for n in neighbors]
    print([n.x for n in neighbors_particles])
    ax.scatter(
        [n.x for n in neighbors_particles],
        [n.y for n in neighbors_particles],
        s=[n.r for n in neighbors_particles],
        c='#888888',
        zorder=2
    )
    ax.scatter(
        [particles[chosen].x],
        [particles[chosen].y],
        s=[particles[chosen].r],
        c='#000000',
        zorder=2
    )
    plot_particles(l, particles, fig, ax)

    plt.show()