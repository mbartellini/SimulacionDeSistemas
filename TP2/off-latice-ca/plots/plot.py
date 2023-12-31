import matplotlib.pyplot as plt
import numpy as np
from PIL import Image
from tqdm import tqdm
import toml
from utils.utils import angle_to_color


config = toml.load("./config.toml")['simulation']
FRAME_DURATION = config['frame_duration']
X_AX = config['L']
Y_AX = config['L']
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


# Main function
def main():
    data = read_particle_data('DynamicCA.txt')

    fig = plt.figure()
    ax = plt.gca()
    ax.set_xlim(0, X_AX)
    ax.set_ylim(0, Y_AX)
    ax.set_xticks([])
    ax.set_yticks([])

    frames = []

    for iteration, particles in tqdm(data, desc="Iterations"):
        ax.clear()  # Clear the previous plot
        ax.set_xlim(0, X_AX)
        ax.set_ylim(0, Y_AX)
        ax.set_xticks([])
        ax.set_yticks([])

        # Add x and y axes
        ax.set_xlabel('X')
        ax.set_ylabel('Y')
        ax.grid(which='both', color='gray', linestyle='--', linewidth=0.5)

        process_particles(ax, particles)

        fig.canvas.draw()
        frame = np.array(fig.canvas.renderer.buffer_rgba())
        frames.append(Image.fromarray(frame))

    plt.close(fig)

    # Save the frames as a GIF
    frames[0].save('plots/simulation.gif', format='GIF', append_images=frames[1:], save_all=True,
                   duration=FRAME_DURATION,
                   loop=0)


if __name__ == '__main__':
    main()
