package test.corewar;

import corewar.Cell;
import corewar.Instruction;
import corewar.Operand;
import corewar.Type;
import test.Testable;

public class TestCell implements Testable {

    short errorCode = 0;

    @Override
    public boolean test() {
        Cell c1 = new Cell(Instruction.ADD, new Operand(Type.DIRECT, 4), new Operand(Type.OCTOTHORPE, 29), 1);
        Cell c2 = new Cell(Instruction.ADD, new Operand(Type.DIRECT, 4), new Operand(Type.OCTOTHORPE, 29), 90);
        if (c1.instruction != c2.instruction || c1.team != 1 || c2.team != 90 || !c1.equals(c2)) {
            errorCode = 1;
            return false;
        }
        Cell c3 = c2.deepcopy();
        if (c3 == c2 || !c3.equals(c2) || c3.team != c2.team) {
            errorCode = 2;
            return false;
        }
        return true;
    }

    @Override
    public String getError() {
        return "TestCell: "+errorCode;
    }
}
