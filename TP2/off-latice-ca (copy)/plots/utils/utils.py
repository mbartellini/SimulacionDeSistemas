import math
import matplotlib.pyplot as plt

plt.cm.get_cmap('twilight', 360)  # Use the 'hsv' colormap

def normalize_angle(angle: float) -> float:
    norm = (math.atan2(math.sin(angle), math.cos(angle)) + math.pi) / (2 * math.pi)
    if norm < 0 or norm > 1:
        print("AH")
    return (math.atan2(math.sin(angle), math.cos(angle)) + math.pi) / (2 * math.pi)


# Define a color mapping function using the Cyclic HSV colormap
def angle_to_color(angle):
    colormap = plt.cm.get_cmap('twilight', 360)  # Use the 'hsv' colormap
    norm = normalize_angle(angle)  # Normalize angles to 0-360 range
    color = colormap(norm)
    return color
