package genetic;

import corewar.*;
import java.util.List;
import java.io.File;
import java.io.PrintWriter;

/**
 * Class to turn given program under the form of a List of Cells to a File with given name.
 */
public class CellToFile {

    /**
     * Write in redcode format in a file the given program with the given name.
     * @param prog Program represented as List of Cells.
     * @param name Name of the file in which we need to write the program.
     * @return True if the program was abble to be correclty written, False otherwise.
     */
    public static boolean progToFile(List<Cell> prog, String name) {
        String futureFile = ";name "+name+"\n";
        PrintWriter file;

        try {
            file = new PrintWriter(new File(name));
        } catch (Exception e) {
            System.err.println("An error occured while creating file");
            return false;
        }
        
        for(Cell c : prog) {
            futureFile+="        ";
            switch (c.instruction) {
                case ADD:
                    futureFile+="add";
                    break;
                case SUB:
                    futureFile+="sub";
                    break;
                case MOV:
                    futureFile+="mov";
                    break;
                case CMP:
                    futureFile+="cmp";
                    break;
                case SLT:
                    futureFile+="slt";
                    break;
                case JMP:
                    futureFile+="jmp";
                    break;
                case JMZ:
                    futureFile+="jmz";
                    break;
                case JMN:
                    futureFile+="jmn";
                    break;
                case DJN:
                    futureFile+="djn";
                    break;
                case SPL:
                    futureFile+="spl";
                    break;
                case DAT:
                    futureFile+="dat";
                    break;
            }
            futureFile+=evalOP(c.op1)+","+evalOP(c.op2)+"\n";
        }

        file.write(futureFile);
        file.close();
        return true;
    }

    /**
     * Turn given Operand into a String to write in file.
     * @param op Operand to transform.
     * @return Operand under the form of a String.
     */
    private static String evalOP(Operand op) {
        String res=" ";

        switch (op.type) {
            case OCTOTHORPE:
                res+="#";
                break;
            case INDIRECT:
                res+="@";
                break;
            case PREDECREMENT:
                res+="<";
                break;
            case PREINCREMENT:
                res+=">";
                break;
            case DIRECT:
                break;
        }
        res+=op.value;
        return res;
    }
    
}
