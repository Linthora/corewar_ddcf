; Name:           Armadillo
; Author:         Stefan Strack
; Speed:          32.86% of c
; Size:           5
; Durability:     Strong
; Effectiveness:  Average
; Score:

bomb    SPL 0
loop    ADD #3039, ptr
ptr     MOV bomb, 81
        JMP loop
        MOV 1, <-1
