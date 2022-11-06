; Name:           Keystone Stone
; Speed:          32.86% of c
; Size:           5
; Durability:     Strong
; Effectiveness:  Good
; Score:

step equ 2517
emerald SPL 0, <-25
        MOV <-step+1, 92
        SUB 2, -1
        DJN -2, <2002
        JMP step, <-step
wait    DJN 0, <-12
paper   DJN 0, <-12
boot    MOV emerald+4, paper-step
        MOV emerald+3, <boot
        MOV emerald+2, <boot
        MOV emerald+1, <boot
        MOV emerald,   <boot
        MOV wait, paper+3053
        JMP @boot
