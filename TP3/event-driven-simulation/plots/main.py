from utils import utils

if __name__ == "__main__":
    data = utils.read_particle_data("../data/dynamic.xyz")
    print(len(data))
    it, d = data[0]
    print(len(d))
    print(data[0][1][0])
