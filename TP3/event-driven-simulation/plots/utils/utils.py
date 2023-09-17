def read_particle_data(filename):
    data = []
    with open(filename, 'r') as file:
        iteration = None
        skip = False
        particles = []

        for line in file:
            if line.strip().isdigit():
                if particles:
                    data.append((iteration, particles))
                particles = []
                iteration = 0 if iteration is None else iteration + 1
                skip = True
            elif skip:
                skip = False
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