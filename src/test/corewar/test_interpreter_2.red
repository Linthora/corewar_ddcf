here    equ 654584;

start   jmz 1, 0;
        jmn 1, -1;
        djn 2569, #1;
        spl @0, here;
        jmp start, <5;