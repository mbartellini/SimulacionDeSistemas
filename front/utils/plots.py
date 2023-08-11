from typing import List

import matplotlib.pyplot as plt
import numpy as np

from particle import Particle

DPI_RESOLUTION = 500

plt.style.use('_mpl-gallery')


def plot_particles(l: int, particles: List[Particle]):
    x = [particle.x for particle in particles]
    y = [particle.y for particle in particles]
    sizes = [particle.r for particle in particles]

    fig, ax = plt.subplots(dpi=DPI_RESOLUTION)
    ax.scatter(x, y, s=sizes) # ax.scatter(x, y, s=sizes, c=colors, vmin=0, vmax=100)
    ax.set(xlim=(0, l), ylim=(0, l))
    ax.set_xticks([])
    ax.set_yticks([])

    plt.show()
