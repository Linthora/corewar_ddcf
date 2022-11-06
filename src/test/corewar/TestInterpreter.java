package test.corewar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import corewar.Cell;
import corewar.Instruction;
import corewar.Interpreter;
import corewar.Operand;
import corewar.Parser;
import corewar.Type;
import test.Testable;

public class TestInterpreter extends Interpreter implements Testable {

    float errorCode = 0;

    @Override
    public boolean test() {
        if (!isIllegalInstruction(Instruction.JMP, new Operand(Type.OCTOTHORPE, 2), null)) {
            errorCode = 0.5f;
            return false;
        }
        Parser parser = new Parser();
        List<List<Cell>> progs = new ArrayList<>(2);
        try {
            List<Cell> prog1 = parser.parseFile("src/test/corewar/test_interpreter_1.red", 0);
            progs.add(prog1);
            List<Cell> prog2 = parser.parseFile("src/test/corewar/test_interpreter_2.red", 1);
            progs.add(prog2);
        } catch (IllegalArgumentException|IOException e) {
            e.printStackTrace();
            return false;
        }
        initGame(progs);
        if (nbWarrior != 2 || memory.indicesStart.size() != 2 || heads.size() != 2) {
            errorCode = 1;
            return false;
        }
        if (!heads.keySet().contains(0) || !heads.keySet().contains(1)) {
            errorCode = 2;
            return false;
        }
        if (heads.get(0).size() != 1 || heads.get(1).size() != 1) {
            errorCode = 3;
            return false;
        }
        if (isOver() || scores.get(0) != -1 || scores.get(0) != -1) {
            errorCode = 4;
            return false;
        }
        if (wrapMemoryAddr(memory.taille+1) != 1 || wrapMemoryAddr(memory.taille*2) != 0 || wrapMemoryAddr(memory.taille*29+memory.taille/2) != memory.taille/2) {
            errorCode = 5;
            return false;
        }
        for (int i=0; i<progs.size(); ++i) {
            for (int j=0; j<progs.get(i).size(); ++j) {
                if (!memory.tab[wrapMemoryAddr(heads.get(i).get(0)+j)].equals(progs.get(i).get(j))) {
                    errorCode = 6;
                    return false;
                }
            }
        }
        if (resolveAddr(152, new Operand(Type.OCTOTHORPE, 5)) != 5 || resolveAddr(memory.taille-10, new Operand(Type.DIRECT, 50)) != 40) {
            errorCode = 7;
            return false;
        }
        int prog1Start = heads.get(0).get(0);
        if (resolveAddr(prog1Start, new Operand(Type.INDIRECT, 2)) != prog1Start-3) {
            errorCode = 8;
            return false;
        }
        int prog2Start = heads.get(1).get(0);
        int v = resolveAddr(prog2Start, new Operand(Type.PREINCREMENT, -1));
        if (v != prog2Start+1 || !memory.tab[prog2Start-1].equals(new Cell(Instruction.DAT, new Operand(Type.OCTOTHORPE, 0), new Operand(Type.OCTOTHORPE, 1)))) {
            errorCode = 9;
            return false;
        }
        v = resolveAddr(prog2Start+1, new Operand(Type.PREDECREMENT, -2));
        if (v != prog2Start+1 || !memory.tab[prog2Start-1].equals(new Cell(Instruction.DAT, new Operand(Type.OCTOTHORPE, 0), new Operand(Type.OCTOTHORPE, 0)))) {
            errorCode = 10;
            return false;
        }

        for (int i=0; i<4; ++i) {
            playNextTour();
            if (isOver()) {
                errorCode = 11;
                return false;
            }
        }
        if (turnN != 4 || heads.get(0).get(0) != prog1Start+5 || heads.get(1).size() != 2 || heads.get(1).get(0) != prog2Start+4 || heads.get(1).get(1) != wrapMemoryAddr(prog2Start+3+654584)) {
            errorCode = 12;
            return false;
        }
        for (int i=0; i<6; ++i) {
            if (isOver()) {
                errorCode = 13;
                return false;
            }
            playNextTour();
        }
        if (!isOver() || turnN != 10 || scores.get(0) != -1 || scores.get(1) != turnN) {
            errorCode = 14;
            return false;
        }
        return true;
    }

    @Override
    public String getError() {
        return "TestInterpreter: "+errorCode;
    }
    
}
