from typing import List

import matplotlib.pyplot as plt
import numpy as np

from particle import Particle

plt.style.use('_mpl-gallery')
plt.figure(dpi=1200)
plt.gcf().set_dpi(300)


def plot_particles(l: int, particles: List[Particle]):
    x = [particle.x for particle in particles]
    y = [particle.y for particle in particles]
    sizes = [particle.r for particle in particles]

    fig, ax = plt.subplots()
    ax.scatter(x, y, s=sizes) # ax.scatter(x, y, s=sizes, c=colors, vmin=0, vmax=100)
    ax.set(xlim=(0, l), ylim=(0, l))

    plt.show()
