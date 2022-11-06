package corewar;
/**
 * Object representing one Operand from a Cell
 */
public class Operand
{
    /**
     * Type of the current Operand
     */
    public Type type;

    /**
     * int value held by the current Operand
     */
    public int value;

    /**
     * Creates an Operand
     * @param type
     * @param value
     */
    public Operand(Type type, int value)
    {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return type.toString()+"("+value+")";
    }

    /**
     * Return a full copy of the current Operand.
     * @return A full copy of the current Operand.
     */
    public Operand deepcopy() {
        return new Operand(type, value);
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Operand) {
            Operand op = (Operand) object;
            return op.value == value && op.type == type;
        }
        return false;
    }
}
