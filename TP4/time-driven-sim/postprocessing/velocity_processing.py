from plotter.plotter import plot_mean_vel, plot_individual_velocity, plot_stationary_vel, \
    plot_pdf, plot_individual_density
from utils.utils import read_particles_data

DT = 0.001
if __name__ == "__main__":
    data = []
    for i in range(5, 31, 5):
        data.append(read_particles_data(f'../data/velocities_{i}_dynamic.txt', DT))

    # for iteration in data[0]['particles']:
    #     print([p['omega'] for p in iteration], np.mean([p['omega'] for p in iteration]))

    plot_mean_vel(data)
    plot_individual_velocity(data[0], 'individual_vel_5')
    plot_stationary_vel(data, 'stationary')
    plot_pdf(data, "pdf_10")
    plot_individual_density(data)
