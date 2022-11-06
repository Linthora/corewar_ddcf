package genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import corewar.Cell;
import corewar.Instruction;
import corewar.Interpreter;
import corewar.Operand;
import corewar.Parser;
import corewar.Type;

/**
 * Object used to mutate given programs.
 */
public class Mutator {
    /**
     * Probability percentage that mutation occurs.
     */
    final int threshold = 10;
    
    /**
     * Max distance between mutated address.
     */
    final int newAddressRange = 5;

    Random random;

    /**
     * Creates a Mutator.
     * @param random
     */
    public Mutator(Random random) {
        this.random = random;
    }

    /**
     * Mutates given {@link Operand}.
     * @param op Operand to mutate.
     */
    public void mutateOperand(Operand op) {
        if (random.nextInt(100) < threshold) {
            op.type = Util.randomEnumVariant(Type.class, random);
        }
        if (random.nextInt(100) < threshold) {
            op.value += random.nextInt(newAddressRange*2)-newAddressRange;
        }
    }

    /**
     * Creates a mutated version of the given program.
     * @param original Program to mutate.
     * @return Mutation of the given program.
     */
    public List<Cell> mutate(List<Cell> original) {
        int originalLength = original.size();
        int newLength = getNewLength(originalLength);
        List<Cell> newPrograms = new ArrayList<>(newLength);
        for (int i=0; i<Math.min(originalLength, newLength); ++i) {
            Instruction instruction;
            Operand op1;
            Operand op2;
            do {
                if (random.nextInt(100) < threshold) {
                    instruction = Util.randomInstruction(random);
                } else {
                    instruction = original.get(i).instruction;
                }
                op1 = original.get(i).op1;
                mutateOperand(op1);
                op2 = original.get(i).op2;
                mutateOperand(op2);
            } while (Interpreter.isIllegalInstruction(instruction, op1, op2));
            newPrograms.add(new Cell(instruction, op1, op2));
        }
        for (int i=originalLength; i<newLength; ++i) {
            newPrograms.add(Util.randomCell(random));
        }
        return newPrograms;
    }

    /**
     * Return the new length of the mutated programs.
     * @param originalLength length of the origninal programs.
     * @return The new length.
     */
    private int getNewLength(int originalLength) {
        int newLength;
        do {
            newLength = random.nextInt(3)+originalLength-1;
        } while(newLength <= 0 || newLength > 100);
        return newLength; 
    }

    /**
     * Executable main to test manually.
     * @param args
     */
    public static void main(String[] args) {
        Mutator m = new Mutator(new Random());
        Parser p = new Parser();
        try {
            List<Cell> prog = p.parseFile(args[0], 0);
            for (int i=0; i<Integer.parseInt(args[1]); ++i) {
                prog = m.mutate(prog);
                System.out.println("program size: "+prog.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
