;name Transposition Stone

step   equ 1185           ; mod 5

inc     spl -step, <step
stone   mov >step, 1-step
        sub inc, stone
        djn stone, <5555
