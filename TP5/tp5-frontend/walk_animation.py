import pandas as pd
import matplotlib.pyplot as plt
import numpy as np
from matplotlib.animation import FuncAnimation, PillowWriter
from tqdm import tqdm

# Leer el archivo de datos
data_path = "../tp5-data/Trayectorias_Unificadas.txt"  # Asegúrate de que este sea el camino correcto al archivo
data = pd.read_csv(data_path, sep='\t', header=None, names=['seg', 'y', 'x', 'id'])

data['y'] = -data['y']

# Función para trazar un solo fotograma
def plot_frame(seg_value, data, trail_length=11):
    frame_data = data[data['seg'] == seg_value]
    ax.clear()
    ax.set_xlim(data['x'].min() - 1, data['x'].max() + 1)
    ax.set_ylim(data['y'].min() - 1, data['y'].max() + 1)
    ax.set_title(f"Segmento: {seg_value}")
    colors = plt.cm.jet(np.linspace(0, 1, len(frame_data)))
    for i, row in frame_data.iterrows():
        particle_id = row['id']
        particle_data = data[data['id'] == particle_id].sort_values(by='seg')
        trail_data = particle_data[particle_data['seg'] <= seg_value].tail(trail_length)
        ax.plot(trail_data['x'], trail_data['y'], '-o', color=colors[int(particle_id)], markersize=4, alpha=0.7)
        ax.plot(row['x'], row['y'], 'o', color=colors[int(particle_id)], markersize=7)


# Crear la figura y el eje
fig, ax = plt.subplots(figsize=(10, 6))


# Función de inicialización para la animación
def init():
    ax.clear()
    ax.set_xlim(data['x'].min() - 1, data['x'].max() + 1)
    ax.set_ylim(data['y'].min() - 1, data['y'].max() + 1)


# Crear la animación
num_frames = len(data['seg'].unique())
ani = FuncAnimation(fig, lambda i: plot_frame(data['seg'].unique()[i], data), frames=tqdm(range(num_frames)),
                    init_func=init, repeat=True)

# Guardar la animación como un archivo .gif
ani.save("animation.gif", writer=PillowWriter(fps=5))