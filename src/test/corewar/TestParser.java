package test.corewar;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import corewar.Cell;
import corewar.Instruction;
import corewar.Operand;
import corewar.Parser;
import corewar.Type;
import test.Testable;

public class TestParser extends Parser implements Testable {

    short errorCode = 0;

    @Override
    public boolean test() {
        if (Parser.parseOperandType("59+step") != Type.DIRECT || Parser.parseOperandType("#2*5") != Type.OCTOTHORPE ||
            Parser.parseOperandType("@-9/3") != Type.INDIRECT || Parser.parseOperandType("<l") != Type.PREDECREMENT ||
            Parser.parseOperandType(">3-a") != Type.PREINCREMENT) {
            errorCode = 1;
            return false;
        }
        HashMap<String, String> constants = new HashMap<>(1);
        constants.put("c1", "9");
        HashMap<String, Integer> labels = new HashMap<>(1);
        labels.put("l1", 3);
        Operand op = parseOperand("@9", 0, constants, labels);
        if (op.type != Type.INDIRECT || op.value != 9) {
            errorCode = 3;
            return false;
        }
        op = parseOperand("#c1-4", 1, constants, labels);
        if (op.type != Type.OCTOTHORPE || op.value != 5) {
            errorCode = 4;
            return false;
        }
        op = parseOperand("-2*l1-1", 0, constants, labels);
        if (op.type != Type.DIRECT || op.value != -7) {
            errorCode = 5;
            return false;
        }
        if (parseInstruction("mov") != Instruction.MOV) {
            errorCode = 6;
            return false;
        }
        try {
            List<Cell> cells = parseFile("src/test/corewar/test_parser.red", 5);
            if (cells.get(0).instruction != Instruction.MOV ||
                !cells.get(0).op1.equals(new Operand(Type.DIRECT, 5)) ||
                !cells.get(0).op2.equals(new Operand(Type.PREDECREMENT, -1))) {
                errorCode = 8;
                return false;
            }
            if (cells.get(1).instruction != Instruction.ADD ||
                !cells.get(1).op1.equals(new Operand(Type.INDIRECT, 2)) ||
                !cells.get(1).op2.equals(new Operand(Type.DIRECT, 3))) {
                errorCode = 9;
                return false;
            }
            if (cells.get(2).instruction != Instruction.SUB ||
                !cells.get(2).op1.equals(new Operand(Type.PREINCREMENT, 10)) ||
                !cells.get(2).op2.equals(new Operand(Type.DIRECT, 10))) {
                errorCode = 10;
                return false;
            }
            if (cells.get(3).instruction != Instruction.JMP ||
                !cells.get(3).op1.equals(new Operand(Type.DIRECT, 1)) ||
                !cells.get(3).op2.equals(new Operand(Type.OCTOTHORPE, 9))) {
                errorCode = 11;
                return false;
            }
            if (cells.get(4).instruction != Instruction.JMZ ||
                !cells.get(4).op1.equals(new Operand(Type.DIRECT, 18)) ||
                !cells.get(4).op2.equals(new Operand(Type.OCTOTHORPE, 3))) {
                errorCode = 12;
                return false;
            }
            if (cells.get(5).instruction != Instruction.JMN ||
                !cells.get(5).op1.equals(new Operand(Type.DIRECT, 4)) ||
                !cells.get(5).op2.equals(new Operand(Type.INDIRECT, 2))) {
                errorCode = 13;
                return false;
            }
            if (cells.get(6).instruction != Instruction.CMP ||
                !cells.get(6).op1.equals(new Operand(Type.INDIRECT, -9)) ||
                !cells.get(6).op2.equals(new Operand(Type.PREDECREMENT, 8))) {
                errorCode = 14;
                return false;
            }
            if (cells.get(7).instruction != Instruction.SLT ||
                !cells.get(7).op1.equals(new Operand(Type.DIRECT, 1)) ||
                !cells.get(7).op2.equals(new Operand(Type.PREINCREMENT, 2))) {
                errorCode = 15;
                return false;
            }
            if (cells.get(8).instruction != Instruction.DJN ||
                !cells.get(8).op1.equals(new Operand(Type.DIRECT, 325644521)) ||
                !cells.get(8).op2.equals(new Operand(Type.DIRECT, -8))) {
                errorCode = 16;
                return false;
            }
            if (cells.get(9).instruction != Instruction.SPL ||
                !cells.get(9).op1.equals(new Operand(Type.INDIRECT, 1054)) ||
                !cells.get(9).op2.equals(new Operand(Type.INDIRECT, -480))) {
                errorCode = 17;
                return false;
            }
        } catch (IllegalArgumentException | IOException e) {
            e.printStackTrace();
            errorCode = 18;
            return false;
        }
        return true;
    }

    @Override
    public String getError() {
        return "TestParser: "+errorCode;
    }
}
