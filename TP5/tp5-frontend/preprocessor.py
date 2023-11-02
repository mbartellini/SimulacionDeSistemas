with open("../tp5-data/Trayectorias_Unificadas_NZE.txt", "r") as f:
    lines = f.readlines()

new_lines = []
for i, line in enumerate(lines):
    line_split = line.split()
    time = float(line_split[0]) - 4/30
    if abs(time) < 4/30 / 2:
        time = 0
    line_split[0] = round(time, 6)
    new_lines.append(f"{line_split[0]}\t {line_split[1]}\t{line_split[2]}\t{line_split[3]}\n")

with open("../tp5-data/Trayectorias_Unificadas.txt", "w") as f:
    f.writelines(new_lines)