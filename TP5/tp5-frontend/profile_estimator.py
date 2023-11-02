def velocity_simulator(initial_vel, final_vel, tau, reps, dt, rep_print):
    ans = []
    vel = initial_vel
    for i in range(reps):
        if i % rep_print == 0:
            ans.append(vel)
        acc = (final_vel - vel) / tau
        vel += acc * dt
    return ans
