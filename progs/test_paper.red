step    equ 50

start   spl    attac,       #2
        add    #step,       attac
        jmp    start,       #0
attac   mov    3,       step
data    jmp    -step,      #0
