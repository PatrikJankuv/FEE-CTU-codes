\ LP format - for model browsing. Use MPS format to capture full model detail.
Minimize
  1020 e0_1 + 1530 e0_2 + 765 e1_0 + 1530 e1_2 + 1275 e2_0 + 1020 e2_1
Subject To
 R0: e0_0 + e1_0 + e2_0 + e3_0 = 1
 R1: e0_1 + e1_1 + e2_1 + e3_1 = 1
 R2: e0_2 + e1_2 + e2_2 + e3_2 = 1
 R3: e0_3 + e1_3 + e2_3 + e3_3 = 1
 R4: e0_0 + e0_1 + e0_2 + e0_3 = 1
 R5: e1_0 + e1_1 + e1_2 + e1_3 = 1
 R6: e2_0 + e2_1 + e2_2 + e2_3 = 1
 R7: e3_0 + e3_1 + e3_2 + e3_3 = 1
Bounds
 e0_0 = 0
 e1_1 = 0
 e2_2 = 0
 e3_3 = 0
Binaries
 e0_0 e0_1 e0_2 e1_0 e1_1 e1_2 e2_0 e2_1 e2_2 e3_0 e3_1 e3_2 e0_3 e1_3 e2_3
 e3_3
End
