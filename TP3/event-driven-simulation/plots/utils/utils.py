import math
import numpy as np


def read_dynamic_data(filename):
    data = []
    with open(filename, 'r') as file:
        iteration = None
        header = False
        particles = []
        time = 0.0

        for line in file:
            if line.strip().isdigit():
                if particles:
                    data.append((time, particles))
                particles = []
                iteration = 0 if iteration is None else iteration + 1
                header = True
            elif header:
                header = False
                time = float(line.strip())
            else:
                values = list(map(float, line.strip().split()))
                particle = {
                    'id': int(values[0]),
                    'x': values[1],
                    'y': values[2],
                    'vx': values[3],
                    'vy': values[4],
                    'radius': values[5]
                }
                particles.append(particle)

        if particles:
            data.append((iteration, particles))
    return data


def read_pressure_data(filename):
    data = []
    with open(filename, 'r') as file:
        for line in file:
            values = list(map(float, line.strip().split()))
            pressure = {
                'left': values[1],
                'right': values[2]
            }
            data.append((values[0], pressure))
    return data


def read_static_data(filename):
    data = None
    with open(filename, 'r') as file:
        values = list(map(float, file.readline().strip().split()))
        data = {
            'L': values[0],
            'N': int(values[1])
        }
    return data


def plot_name(static, plot_type) -> str:
    return f"{plot_type}_{static['N']}_{int(static['L'] * 100)}"


def distance(p1, p2):
    return math.sqrt(((p1['x'] - p2['x']) * 100) ** 2 + ((p1['y'] - p2['y']) * 100) ** 2)


def dcm(reference, particles):
    distances = []
    for p, r in zip(particles, reference):
        distances.append(distance(p, r) ** 2)

    return np.mean(distances), np.std(distances)


def mse(x_val, y_val, c):
    error = []
    for x, y in zip(x_val, y_val):
        error.append((y - c*x)**2)
    return np.mean(error)
