package corewar;
/**
 * Object representing one Cell from the MARS.
 */
public class Cell
{
    /**
     * Type of instruction stored in the cell.
     */
    public Instruction instruction;
    
    /**
     * The Operands A and B of the Cell.
     */
    public Operand op1, op2;
    
    /**
     * Hold the team number of the last team who wrote in this Cell.
     */
    public int team;

    /**
     * Creates a Cell.
     * 
     * @param instruction Type of intruction which is to be written in the Cell.
     * @param op1 {@link Operand} A which is to be written in the Cell.
     * @param op2 {@link Operand} B which is to be written in the Cell.
     */
    public Cell(Instruction instruction,Operand op1,Operand op2)
    {
        this.instruction = instruction;
        this.op1 = op1;
        this.op2 = op2;
        this.team = -1;
    }

    /**
     * Creates a Cell and associate it directly with a team number.
     * 
     * @param instruction Type of intruction which is to be written in the Cell.
     * @param op1 {@link Operand} A which is to be written in the Cell.
     * @param op2 {@link Operand} B which is to be written in the Cell.
     * @param team int that represent the team number of the team who wrote this Cell.
     */
    public Cell(Instruction instruction,Operand op1,Operand op2, int team)
    {
        this(instruction, op1, op2);
        this.team = team;
    }

    @Override
    public String toString() {
        return instruction.toString()+" "+op1.toString()+" "+op2.toString();
    }

    /**
     * Return a full copy of the current Cell.
     * @return A full copy of the current Cell.
     */
    public Cell deepcopy() {
        return new Cell(instruction, op1.deepcopy(), op2.deepcopy(), team);
    }

    /**
     * Set the team attribute to the given value.
     * @param team
     */
    public void setTeam(int team) {
        this.team = team;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Cell) {
            Cell cell = (Cell) object;
            return cell.instruction == instruction && cell.op1.equals(op1) && cell.op2.equals(cell.op2);
        }
        return false;
    }
}