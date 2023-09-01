import statistics
from typing import List, Tuple

import matplotlib.pyplot as plt
import numpy as np
from PIL import Image
from tqdm import tqdm
import toml
import math
from ..utils.utils import angle_to_color

config = toml.load("./config.toml")['simulation']
FRAME_DURATION = config['frame_duration']
X_AX = 20.0
Y_AX = 20.0
width = config['width']
length = config['length']


# Read particle data from file
def read_particle_data(filename):
    data = []
    with open(filename, 'r') as file:
        iteration = None
        particles = []

        for line in file:
            if line.strip().isdigit():
                if particles:
                    data.append((iteration, particles))
                iteration = int(line)
                particles = []
            else:
                values = list(map(float, line.strip().split()))
                particle = {
                    'id': int(values[0]),
                    'x': values[1],
                    'y': values[2],
                    'v': values[3],
                    'theta': values[5]
                }
                particles.append(particle)

        if particles:
            data.append((iteration, particles))
    return data


# Particle processing function using numpy and matplotlib
def process_particles(ax, particles):
    positions = np.array([[particle['x'], particle['y']] for particle in particles])
    velocities = np.array([particle['v'] for particle in particles])
    thetas = np.array([particle['theta'] for particle in particles])

    dx = velocities * np.cos(thetas) * length
    dy = velocities * np.sin(thetas) * length

    for x, y, dx_i, dy_i, theta in zip(positions[:, 0], positions[:, 1], dx, dy, thetas):
        ax.arrow(x, y, dx_i, dy_i, width=width, length_includes_head=True,
                 fc=angle_to_color(theta), ec=angle_to_color(theta))


def calculate_order(velocities_and_angles: List[Tuple[float, float]]) -> float:
    sum_of_velocities = (0, 0)
    total_velocity = 0
    for velocity, angle in velocities_and_angles:
        total_velocity += velocity
        sum_of_velocities = (sum_of_velocities[0] + math.cos(angle) * velocity,
                             sum_of_velocities[1] + math.sin(angle) * velocity)
    return math.sqrt(math.pow(sum_of_velocities[0], 2) + math.pow(sum_of_velocities[1], 2)) / total_velocity


# Main function
def main():
    # data = read_particle_data('data/tp2/experiments/density/DynamicCA_500_0.txt')
    #
    # fig = plt.figure()
    # ax = plt.gca()
    # ax.set_xlim(0, X_AX)
    # ax.set_ylim(0, Y_AX)
    # ax.set_xticks([])
    # ax.set_yticks([])
    #
    # frames = []
    #
    # for iteration, particles in data:
    #     ax.clear()  # Clear the previous plot
    #     ax.set_xlim(0, X_AX)
    #     ax.set_ylim(0, Y_AX)
    #     ax.set_xticks([])
    #     ax.set_yticks([])
    #
    #     # Add x and y axes
    #     ax.set_xlabel('X')
    #     ax.set_ylabel('Y')
    #     ax.grid(which='both', color='gray', linestyle='--', linewidth=0.5)
    #
    #     process_particles(ax, particles)
    #
    #     fig.canvas.draw()
    #     frame = np.array(fig.canvas.renderer.buffer_rgba())
    #     frames.append(Image.fromarray(frame))
    #
    # plt.close(fig)
    #
    # # Save the frames as a GIF
    # frames[0].save('figs/tp2/experiments/density/animation_500_0.gif', format='GIF',
    #                append_images=frames[1:],
    #                save_all=True,
    #                duration=FRAME_DURATION,
    #                loop=0)
    #
    # # Va vs iteration for 3 density values
    # EXPERIMENT_SET = [
    #     (500, 'x:b'),
    #     (2500, 'x:r'),
    #     (5000, 'x:g'),
    # ]
    # fig = plt.figure()
    # ax = plt.gca()
    # ax.grid()
    # ax.set_xlabel('Iteración')
    # ax.set_ylabel(r'$v_a$')
    # for N, fmt in EXPERIMENT_SET:
    #     data = read_particle_data('data/tp2/experiments/density/DynamicCA_' + str(N) + '_1.txt')
    #     x_axis = []
    #     order = []
    #     for iteration, particles in data:
    #         x_axis.append(iteration)
    #         current_order = calculate_order([(particle['v'], particle['theta']) for particle in particles])
    #         order.append(current_order)
    #     ax.plot(x_axis, order, fmt, label=r'$\rho = {{{}}}$'.format(round(N/(X_AX * Y_AX), 2)))
    # plt.legend()
    # plt.savefig('figs/tp2/experiments/density/time_graph.png')

    # Va vs density
    fig = plt.figure()
    ax = plt.gca()
    ax.grid()
    ax.set_xlabel(r'$\rho$')
    ax.set_ylabel(r'$v_a$')
    EXPERIMENT_SET = [
        (0, 150, 'x:b'),
        (1, 150, 'x:r'),
        (2, 150, 'x:g'),
    ]
    for noise_factor, permanent_iteration, fmt in EXPERIMENT_SET:
        orders = []
        densities = []
        for experiment_n in range(500, 5000, 500):
            data = read_particle_data(f"data/tp2/experiments/density/DynamicCA_{experiment_n}_{noise_factor}.txt")
            densities.append(experiment_n / (X_AX * Y_AX))

            order_history = []
            for iteration, particles in data:
                if iteration < permanent_iteration:
                    continue
                current_order = calculate_order([(particle['v'], particle['theta']) for particle in particles])
                order_history.append(current_order)
            orders.append((statistics.mean(order_history), statistics.stdev(order_history)))

        ax.errorbar(
            densities,
            [order[0] for order in orders],
            fmt=fmt,
            yerr=[order[1] for order in orders],
            label=r'$\eta = {{{}}}\pi$'.format(noise_factor)
        )
    plt.legend()
    plt.savefig('figs/tp2/experiments/density/order_vs_density.png')


if __name__ == '__main__':
    main()
