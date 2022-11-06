package corewar;

/**
 * Custom Exception to handle illegal instructions.
 */
public class IllegalInstructionException extends IllegalArgumentException {
    public IllegalInstructionException(Cell cell) {
        super(cell.toString());
    }
}
