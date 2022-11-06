#!/usr/bin/env python3

import numpy as np
import matplotlib.pyplot as plt 

data = np.loadtxt("AlgoGenData.csv",delimiter=",",skiprows=1, dtype=float)
nameData = ["Lifetime_900_Cycle", "Total_Occupation", "Occupation_Block_5", "Created_Process", "Alive_Process", "Bombing_Freq"]

for i in range(1,7):
    plt.plot(data[0::100,0], data[0::100,i], '-')
    plt.xlabel("Nombre de générations")
    plt.ylabel(nameData[i-1])
    plt.savefig(nameData[i-1]+".png")
    plt.close()


# for i in range(1,7):
#     plt.plot(data[:21,0], data[:21,i], '-')
#     plt.savefig(nameData[i-1]+"_20Cycle.png")
#     plt.close()
