step equ 153
init equ 152
n equ ((12*8)-2)

split   SPL test
        ADD #step, 2
        JMP split
test    MOV -10, n
jump    JMP -1, 1