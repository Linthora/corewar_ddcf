; Name:           Winter Werewolf
; Author:         W. Mintardjo
; Speed:          25% of c
; Size:           7
; Durability:     Weak
; Effectiveness:  Excellent
; Score:

step equ 153
init equ 152
n equ ((12*8)-2)
        JMP boot
data    DAT <-4-n, #0
split   SPL 0, <-3-step-n
main    MOV jump, @3
        MOV split, <2
        ADD #step, 1
        JMP main, init
        MOV @-4, <n
jump    JMP -1, 1
boot    MOV main+5, -500+5
        MOV main+4, -500+3
        MOV main+3, -500+1
        MOV main+2, -500-1
        MOV main+1, -500-3
        MOV main,   -500-5
        MOV main-1, -500-7
        MOV data, -500-9
        JMP -500-9
