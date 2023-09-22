from utils.utils import read_dynamic_data, read_pressure_data, read_static_data, plot_name
from plotter.plotter import plot_pressures, plot_pressure_versus_area, plot_dcm

if __name__ == "__main__":
    # dynamic = read_dynamic_data("../data/dynamic_1.xyz")
    static = read_static_data("../data/static.xyz")
    pressures = read_pressure_data("../data/pressure.xyz")

    plot_pressures(pressures, static)
