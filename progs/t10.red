; Name:           Worm
; Speed:          25% of c (linear)
; Size:           1.75
; Durability:     Very Strong
; Effectiveness:  Poor
; Score:

launch SPL b
       SPL ab
aa     JMP imp
ab     JMP imp+1
b      SPL bb
ba     JMP imp+2
bb     JMP imp+3
imp    MOV imp, imp+1
