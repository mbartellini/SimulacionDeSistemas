def parse_static(filename: str):
    with open(filename) as f:
        lines = f.readlines()
        particle_count = int(lines[0])
        grid_size = int(lines[1])
        properties_lines = lines[2:]
        properties = [_split_line_into_floats(line) for line in properties_lines]
    return particle_count, grid_size, properties


def _split_line_into_floats(line: str):
    return [float(prop) for prop in line.split()] # Should check if we always take 2 props


def parse_dynamic(filename: str, particle_count: int): # First iteration, only for
    with open(filename) as f:
        lines = f.readlines()
        time = int(lines[0])
        data = []
        for p in range(particle_count):
            data.append(_split_line_into_floats(lines[p+1]))
    return time, data
