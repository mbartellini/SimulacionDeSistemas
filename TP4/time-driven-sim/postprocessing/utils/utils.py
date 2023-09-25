from typing import List
import numpy as np
import math


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


def mse(expected, actual):
    error = []
    for e, a in zip(expected, actual):
        error.append((e - a) ** 2)
    return np.mean(error)
