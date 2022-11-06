package test.corewar;

import java.util.ArrayList;
import java.util.Random;

import corewar.Cell;
import corewar.Instruction;
import corewar.Memory;
import test.Testable;

public class TestMemory extends Memory implements Testable {

    short errorCode = 0;

    @Override
    public boolean test() {
        fillWithDat();
        for (Cell cell: tab) {
            if (cell.instruction != Instruction.DAT || cell.team != -1) {
                errorCode = 1;
                return false;
            }
        }
        indicesStart = new ArrayList<>();
        indicesEnd = new ArrayList<>();
        if (!isAvailable(0, 10)) {
            errorCode = 2;
            return false;
        }
        indicesStart.add(0);
        indicesEnd.add(10);
        if (isAvailable(0, 10) || isAvailable(2, 3) || isAvailable(5, 20)) {
            errorCode = 3;
            return false;
        }
        if (!isAvailable(11, 4) || isAvailable(taille, 10) || !isAvailable(50, 50)) {
            errorCode = 4;
            return false;
        }
        indicesStart.add(50);
        indicesEnd.add(53);
        if (!isAvailable(44, 5) || isAvailable(45, 6) || isAvailable(52, 6) || !isAvailable(54, 20)) {
            errorCode = 5;
            return false;
        }
        Random random = new Random(System.currentTimeMillis());
        int start = getRandLocation(random, 10);
        if (start <= 10 || (start > 40 && start < 53)) {
            errorCode = 6;
            return false;
        }
        return true;
    }

    @Override
    public String getError() {
        return "TestMemory: "+errorCode;
    }
}
