; Name:           Gate Crashing Spiral
; Speed:          12.5% of c (mostly linear)
; Size:           5.875
; Durability:     Very Strong
; Effectiveness:  Good
; Score:

step1 equ 2667
step2 equ 2668
start    SPL lnch1
         SPL lnch3

lnch2    SPL 8
         SPL 4
         SPL 2
         JMP imp2+(step2*0)
         JMP imp2+(step2*1)
         SPL 2
         JMP imp2+(step2*2)
         JMP imp2+(step2*3)
         SPL 4
         SPL 2
         JMP imp2+(step2*4)
         JMP imp2+(step2*5)
         SPL 2
         JMP imp2+(step2*6)
         JMP imp2+(step2*7)

lnch3    SPL 8
         SPL 4
         SPL 2
         JMP imp3+(step2*0)
         JMP imp3+(step2*1)
         SPL 2
         JMP imp3+(step2*2)
         JMP imp3+(step2*3)
         SPL 4
         SPL 2
         JMP imp3+(step2*4)
         JMP imp3+(step2*5)
         SPL 2
         JMP imp3+(step2*6)
         JMP imp3+(step2*7)

lnch1    SPL 8
         SPL 4
         SPL 2
         JMP imp1+(step1*0)
         JMP imp1+(step1*1)
         SPL 2
         JMP imp1+(step1*2)
         JMP imp1+(step1*3)
         SPL 4
         SPL 2
         JMP imp1+(step1*4)
         JMP imp1+(step1*5)
         SPL 2
         JMP imp1+(step1*6)
         JMP imp1+(step1*7)

imp1     MOV 0, step1
         DAT #0
         DAT #0
         DAT #0
imp2     MOV 0, step2
         MOV 0, step2
imp3     MOV 0, step2
         MOV 0, step2
