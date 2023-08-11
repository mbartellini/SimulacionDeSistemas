def parse_static(filename: str):
    with open(filename) as f:
        lines = f.readlines()
        particle_count = int(lines[0])
        grid_size = int(lines[1])
        properties_lines = lines[2:]
        properties = [_split_line_into_floats(line) for line in properties_lines]
    return particle_count, grid_size, properties


def _split_line_into_floats(line: str):
    return [float(prop) for prop in line.split()]  # Should check if we always take 2 props


def _split_line_into_ints(line: str):
    return [int(prop) for prop in line.split()]  # Should check if we always take 2 props


def parse_dynamic(filename: str, particle_count: int): # First iteration, only for
    with open(filename) as f:
        lines = f.readlines()
        time = int(lines[0])
        data = []
        for p in range(particle_count):
            data.append(_split_line_into_floats(lines[p+1]))
    return time, data


def parse_neighbors(filename: str, n: int):
    neighbors_list = []
    for i in range(n):
        neighbors_list.append([])
    with open(filename) as f:
        lines = f.readlines()
        for line in lines:
            ids = _split_line_into_ints(line)
            if len(ids) <= 0:
                continue
            for neigh in ids[1:]:
                neighbors_list[ids[0]-1].append(neigh-1)
    return neighbors_list
