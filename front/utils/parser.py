def parse_static(filename: str):
    with open(filename) as f:
        lines = f.readlines()
        particle_count = int(lines[0])
        grid_size = int(lines[1])
        properties_lines = lines[2:]
        properties = [_parse_static_line(line) for line in properties_lines]
    return particle_count, grid_size, properties


def _parse_static_line(line: str):
    return [float(prop) for prop in line.split()] # Should check if we always take 2 props
