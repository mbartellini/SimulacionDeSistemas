from utils import utils

if __name__ == "__main__":
    data = utils.read_particle_data("../data/dynamic.xyz")
    reference = data[0][1]
    for iteration in data:
        print(iteration[0], utils.dcm(reference, iteration[1]))
