;name Looping Paper
step   equ 5620

paper   mov    #5,       0
copy    mov    <paper,   @dest
        jmn    copy,     paper
        spl    >paper,   @-1277
dest    jmz    step,     0
