; Name:           Nimbus Spiral
; Speed:          50% of c (somewhat linear)
; Size:           1.992
; Durability:     Very Strong
; Effectiveness:  Fair
; Score:

step equ 127
imp    MOV 0, step
launch SPL 1     ;1 process
       SPL 1     ;2 processes
       SPL 1     ;4 processes
       SPL 1     ;8 processes
       SPL 1     ;16 processes
       MOV -1, 0  ;32 processes
       SPL 1     ;63 processes
       SPL 2     ;126 processes
spread JMP @spread, imp
       ADD #step, spread
