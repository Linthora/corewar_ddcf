package genetic;

import java.util.Random;

import corewar.Cell;
import corewar.Instruction;
import corewar.Interpreter;
import corewar.Operand;
import corewar.Type;

/**
 * Class containing static usefull methods for the {@link AlgoGen}. 
 */
public class Util {

    /**
     * This methods takes an Enum class and return a random element of this Enum class.
     * @param <T> 
     * @param clazz an enum class.
     * @param random
     * @return A random element of the given enum.
     */
    public static <T extends Enum<?>> T randomEnumVariant(Class<T> clazz, Random random) {
        T[] variant = clazz.getEnumConstants();
        int x = random.nextInt(variant.length);
        return variant[x];
    }

    /**
     * This method return a random Enum Instruction 
     * @param random 
     * @return A random Instruction.
     */
    public static Instruction randomInstruction(Random random) {
        Instruction instruction;
        do {
            instruction = randomEnumVariant(Instruction.class, random);
        } while (instruction == Instruction.DAT);
        return instruction;
    }

    /**
     * This method generates a random new Cell.
     * @param random
     * @return A new randomly generated Cell.
     */
    public static Cell randomCell(Random random) {
        Instruction instruction;
        Operand op1;
        Operand op2;
        do {
            instruction = Util.randomInstruction(random);
            op1 = new Operand(Util.randomEnumVariant(Type.class, random), random.nextInt(1000));
            op2 = new Operand(Util.randomEnumVariant(Type.class, random), random.nextInt(1000));
        } while (Interpreter.isIllegalInstruction(instruction, op1, op2));
        return new Cell(instruction, op1, op2);
    }
}
