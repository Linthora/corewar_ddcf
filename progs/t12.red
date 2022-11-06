; Name:           Spiral
; Speed:          37.5% of c (mostly linear)
; Size:           1.875
; Durability:     Very Strong
; Effectiveness:  Fair
; Score:

step equ 2667
launch  SPL 8
        SPL 4
        SPL 2
        JMP imp
        JMP imp+step
        SPL 2
        JMP imp+(step*2)
        JMP imp+(step*3)
        SPL 4
        SPL 2
        JMP imp+(step*4)
        JMP imp+(step*5)
        SPL 2
        JMP imp+(step*6)
        JMP imp+(step*7)
imp     MOV 0, step
