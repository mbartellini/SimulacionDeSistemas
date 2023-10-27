import pandas as pd
import matplotlib.pyplot as plt
import numpy as np

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

            velocities.append({'seg': t2, 'particle_id': particle, 'velocity': velocity})

# Crear un DataFrame con los datos de velocidad
df_velocity = pd.DataFrame(velocities)

# Graficar la velocidad de cada partícula en gráficos separados
for particle in df_velocity['particle_id'].unique():
    particle_velocity_data = df_velocity[df_velocity['particle_id'] == particle]
    plt.figure()
    plt.plot(particle_velocity_data['seg'], particle_velocity_data['velocity'])
    plt.xlabel('Tiempo (seconds)')
    plt.ylabel('Velocidad (m/s)')

    # Guardar el gráfico en la carpeta 'data/'
    plt.savefig(f'../tp5-data/velocities/Velocity_of_Particle_{particle}.png')
    plt.close()

