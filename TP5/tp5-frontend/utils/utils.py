from typing import List, Dict
import numpy as np
import math

SYSTEM_RADIUS = 21.49

def _analytic(A, gamma, mass, k, t):
    return A * math.exp(-(gamma * t) / (2 * mass)) * math.cos(
        math.sqrt((k / mass) - (gamma ** 2) / (4 * mass ** 2)) * t)


def analytic_solution(dt, tf):
    size = math.floor(tf / dt)
    return [_analytic(1, 100, 70, 10000, i * dt) for i in range(size)]


def read_oscillator_data(filename) -> (float, float, List[float]):
    dt, tf, positions = None, None, []
    with open(filename, 'r') as file:
        header = file.readline()
        values = list(map(float, header.strip().split()))
        tf, dt = values[0], values[1]

        for line in file:
            positions.append(float(line.strip()))
    return dt, tf, positions


def read_particles_data(filename, dt) -> Dict:
    ans = {
        'dt': dt,
        'particles': []
    }
    with open(filename, 'r') as file:
        ans['N'] = int(file.readline().strip())
        particles = []
        for line in file:
            stripped = line.strip()
            if stripped.isdigit() or stripped.startswith("Properties"):
                if particles:
                    ans['particles'].append(particles)
                    particles = []
            else:
                values = list(map(float, stripped.split()))
                particle = {
                    'x': values[1],
                    'y': values[2],
                    'vx': values[3],
                    'vy': values[4],
                    'angle': values[5],
                    'omega': values[6]
                }
                particles.append(particle)
        if particles:
            ans['particles'].append(particles)
    return ans


def phi_error(population_a, population_b):
    assert len(population_a) == len(population_b)
    phi_t = 0
    for a, b in tuple(zip(population_a, population_b)):
        phi_t += abs(a['angle'] - b['angle'])
    return phi_t


def mse(expected, actual):
    error = []
    for e, a in zip(expected, actual):
        error.append((e - a) ** 2)
    return np.mean(error)


def mean_vel(simulation):
    mean, std = [], []
    for particles in simulation:
        vel = [math.fabs(p['omega']) for p in particles]
        mean.append(np.mean(vel))
        std.append(np.std(vel))
    return mean, std


def arc_distance(particle, other):
    return SYSTEM_RADIUS * math.fabs(particle['angle'] - other['angle'])


def individual_density(particle, prev_part, next_part):
    return 1 / (arc_distance(particle, prev_part) + arc_distance(particle, next_part))
