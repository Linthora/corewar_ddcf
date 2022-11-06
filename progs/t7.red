; Name:           Dwarf
; Author:         A.K.Dewdney
; Speed:          33.33% of c
; Size:           4
; Durability:     Weak
; Effectiveness:  Average
; Score:

bomb   DAT #0
dwarf  ADD #4, bomb
       MOV bomb, @bomb
       JMP dwarf
