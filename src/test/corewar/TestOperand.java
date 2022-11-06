package test.corewar;

import corewar.Operand;
import corewar.Type;
import test.Testable;

public class TestOperand implements Testable {

    short errorCode = 0;

    @Override
    public boolean test() {
        Operand op1 = new Operand(Type.PREDECREMENT, 29);
        Operand op2 = new Operand(Type.PREDECREMENT, 29);
        if (op1.type != Type.PREDECREMENT || op1.value != 29 || op1 == op2 || !op1.equals(op2)) {
            errorCode = 1;
            return false;
        }
        Operand op3 = new Operand(Type.INDIRECT, 29);
        if (op3.equals(op1)) {
            errorCode = 2;
            return false;
        }
        Operand op4 = new Operand(Type.PREDECREMENT, 30);
        if (op4.equals(op2)) {
            errorCode = 3;
            return false;
        }
        Operand op5 = op1.deepcopy();
        if (op5 == op1 || !op5.equals(op1)) {
            errorCode = 4;
            return false;
        }
        return true;
    }

    @Override
    public String getError() {
        return "TestOperand: "+errorCode;
    }
}
