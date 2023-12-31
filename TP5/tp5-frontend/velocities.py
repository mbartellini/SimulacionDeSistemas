import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from velocity_analysis import stationary, departure, arrival
from profile_estimator import velocity_simulator
from utils.utils import mse

# Cargar los datos del archivo en un DataFrame
df = pd.read_csv('../tp5-data/Trayectorias_Unificadas.txt', delim_whitespace=True, header=None,
                 names=['seg', 'y', 'x', 'particle_id'])

# Ordenar los datos por particle_id y tiempo
df.sort_values(by=['particle_id', 'seg'], inplace=True)

# Inicializar una lista para almacenar datos de velocidad
velocities = []

# Iterar a través de cada partícula única
for particle in df['particle_id'].unique():
    particle_data = df[df['particle_id'] == particle]

    # Asegurarse de que hay más de un punto para calcular la velocidad
    if len(particle_data) > 1:
        for i in range(1, len(particle_data)):
            x1, y1, t1 = particle_data.iloc[i - 1][['x', 'y', 'seg']]
            x2, y2, t2 = particle_data.iloc[i][['x', 'y', 'seg']]

            # Calcular la velocidad
            dx = x2 - x1
            dy = y2 - y1
            dt = t2 - t1
            velocity = np.sqrt((dx / dt) ** 2 + (dy / dt) ** 2)

            velocities.append({'seg': t1, 'particle_id': particle, 'velocity': velocity})

# Crear un DataFrame con los datos de velocidad
df_velocity = pd.DataFrame(velocities)

# Graficar la velocidad de cada partícula en gráficos separados
for particle in df_velocity['particle_id'].unique():
    particle_velocity_data = df_velocity[df_velocity['particle_id'] == particle]
    plt.figure()
    plt.plot(particle_velocity_data['seg'], particle_velocity_data['velocity'])
    plt.xlabel('Tiempo (seconds)')
    plt.ylabel('Velocidad (m/s)')
#    plt.xlim(5, 21)

    # Guardar el gráfico en la carpeta 'data/'
    plt.savefig(f'../tp5-data/velocities/Velocity_of_Particle_{particle}.png')
    plt.close()

vmax = {}
for data in stationary:
    particle_id = data['particle_id']
    interval_start, interval_finish = data['interval']
    particle_velocity_data = df_velocity[df_velocity['particle_id'] == particle_id]
    stationary_data = particle_velocity_data[particle_velocity_data['seg'].between(interval_start, interval_finish)]
    plt.figure()
    plt.plot(particle_velocity_data['seg'], particle_velocity_data['velocity'])
    average_stationary_velocity = np.average(stationary_data['velocity'])
    vmax[particle_id] = average_stationary_velocity
    plt.axhline(y=average_stationary_velocity, color='r')
    plt.xlabel('Tiempo (seconds)')
    plt.ylabel('Velocidad (m/s)')
    # plt.xlim(interval_start, interval_finish)

    # Guardar el gráfico en la carpeta 'data/'
    plt.savefig(f'../tp5-data/velocities/Velocity_of_Particle_{particle_id}_With_Stationary_Average.png')
    plt.close()

taus = {}
for data in departure:
    particle_id = data['particle_id']
    taus[particle_id] = []
    for i, (interval_start, interval_finish) in enumerate(data['intervals']):
        particle_velocity_data = df_velocity[df_velocity['particle_id'] == particle_id]
        departure_data = particle_velocity_data[particle_velocity_data['seg'].between(interval_start, interval_finish)]
        initial_velocity = departure_data['velocity'].iloc[0]

        best_error = None
        best_tau = None
        errors = []
        for tau in np.arange(0.1, 2, 0.01):
            estimation = velocity_simulator(initial_velocity, vmax[particle_id], tau, len(departure_data) * (100 - 1),
                                            4 / 30 / 100, 100)
            curr_error = mse(departure_data['velocity'], estimation)
            if best_error is None or curr_error < best_error:
                best_tau = tau
                best_error = curr_error
            errors.append(curr_error)
        plt.figure()
        plt.plot(np.arange(0.1, 2, 0.01), errors)
        plt.xlabel('Tau')
        plt.ylabel('MSE')
        plt.savefig(f'../tp5-data/velocities/MSE_Of_Tau_{particle_id}_{i}.png')
        plt.close()
        print(f"Particle: {particle_id}, departure tau: {best_tau}")

        plt.figure()
        plt.plot(departure_data['seg'], departure_data['velocity'], 'o-')
        estimation = velocity_simulator(initial_velocity, vmax[particle_id], best_tau, len(departure_data) * (100-1), 4/30/100, 100)
        plt.plot(departure_data['seg'], estimation)
        plt.axhline(y=initial_velocity, color='r')
        plt.axhline(y=vmax[particle_id], color='r')
        plt.xlabel('Tiempo (seconds)')
        plt.ylabel('Velocidad (m/s)')
        # plt.xlim(interval_start, interval_finish)

        # Guardar el gráfico en la carpeta 'data/'
        plt.savefig(f'../tp5-data/velocities/Velocity_of_Particle_{particle_id}_With_Initial_Velocity_{i}.png')
        plt.close()

for data in arrival:
    particle_id = data['particle_id']
    for i, (interval_start, interval_finish) in enumerate(data['intervals']):
        particle_velocity_data = df_velocity[df_velocity['particle_id'] == particle_id]
        arrival = particle_velocity_data[particle_velocity_data['seg'].between(interval_start, interval_finish)]
        final_velocity = arrival['velocity'].iloc[-1]

        best_error = None
        best_tau = None
        errors = []
        for tau in np.arange(0.1, 10, 0.01):
            estimation = velocity_simulator(vmax[particle_id], final_velocity, tau, len(arrival) * (100 - 1),
                                            4 / 30 / 100, 100)
            curr_error = mse(arrival['velocity'], estimation)
            if best_error is None or curr_error < best_error:
                best_tau = tau
                best_error = curr_error
            errors.append(curr_error)
        print(f"Particle: {particle_id}, arrival tau: {best_tau}")

        plt.figure()
        plt.plot(arrival['seg'], arrival['velocity'])
        plt.xlabel('Tiempo (seconds)')
        plt.ylabel('Velocidad (m/s)')

        # Guardar el gráfico en la carpeta 'data/'
        plt.savefig(f'../tp5-data/velocities/Arrival_of_Particle_{particle_id}_{i}.png')
        plt.close()