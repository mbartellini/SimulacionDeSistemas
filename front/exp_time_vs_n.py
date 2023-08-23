from particle import Particle
from utils.parser import *
from utils.plots import *
import numpy as np


CIM_TIMES_FILE = "../data/ExperimentTimeVsN_CIM.txt"
BRUTE_TIMES_FILE = "../data/ExperimentTimeVsN_Brute.txt"

def main():
    brute_n, times = parse_run_time(BRUTE_TIMES_FILE)
    brute_avg_time = [np.mean(time_list) for time_list in times]
    brute_std_time = [np.std(time_list) for time_list in times]
    cim_n, times = parse_run_time(CIM_TIMES_FILE)
    cim_avg_time = [np.mean(time_list) for time_list in times]
    cim_std_time = [np.std(time_list) for time_list in times]
    print(cim_n)
    print(brute_n)

    plot_multiline([brute_n, cim_n], [brute_avg_time, cim_avg_time], [brute_std_time, cim_std_time])


if __name__ == '__main__':
    main()
