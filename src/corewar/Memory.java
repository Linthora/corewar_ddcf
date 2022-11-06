package corewar;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * Object representing the memory of the game.
 */
public class Memory
{
    /**
     * Size of the Memory: number of Cells contained in the memory.
     */
    public final int taille = 1000;

    /**
     * Array of Cells representing the memory.
     */
    public Cell[] tab;

    /**
     * List containing all the indices where programs written in the memory begin.
     */
    public List<Integer> indicesStart;

    /**
     * List containing all the indices where programs written in the memory end.
     */
    protected List<Integer> indicesEnd;

    /**
     * Creates a Memory.
     */
    public Memory()
    {
        this.tab = new Cell[taille];
    }

    /**
     * Initialize the memory array with the neutral Cells (Dat #0, #0).
     */
    public void fillWithDat() {
        for(int i=0;i<taille;++i)
            tab[i] = new Cell(Instruction.DAT, new Operand(Type.OCTOTHORPE, 0), new Operand(Type.OCTOTHORPE, 0), -1);
    }

    /**
     * Write given programs in the memory.
     * @param progList List of List of Cells representing a List of programs.
     */
    public void writeProg(List<List<Cell>> progList)
    {
        indicesStart=new ArrayList<>();
        indicesEnd=new ArrayList<>();
        Random rand=new Random(System.currentTimeMillis());

        for(int i=0;i<progList.size();i++)
        {
            List<Cell> currentProg=progList.get(i);
            Integer tmp=getRandLocation(rand,currentProg.size());
            indicesStart.add(tmp);
            indicesEnd.add(tmp+currentProg.size());
            for(int j=tmp, k=0; j<tmp+currentProg.size(); j++, k++)
            {
                int cell = Math.floorMod(j, taille);
                this.tab[cell]=currentProg.get(k).deepcopy();
            }
        }
    }

    

    /**
     * Search for a random index in the memory array where a program could be written without overriding on another.
     * @param rand Random object to avoid creating a new one each time.
     * @param size size of the program we want to write.
     * @return index where a programe of given size could be written.
     */
    protected int getRandLocation(Random rand, int size)
    {
        int i;
        do{
            i=rand.nextInt(this.taille);
        }while(!isAvailable(i, size));
        
        return i;
    }

    /**
     * Check if given index would be okay to start writing a program of given size without overriding on another written program.
     * @param debut
     * @param size
     * @return A boolean value stating if the given index to start writing new program is OK to use.
     */
    protected boolean isAvailable(int debut, int size)
    {
        int fin = Math.floorMod(debut+size, taille);
        for(int i=0; i<indicesStart.size(); i++)
        {
            /*
             * indicesEnd n'est pas wrappé donc pas besoins de gérer les cas où les programmes
             * commencent à la fin de la mémoire et termine au début
            */
            if (indicesStart.get(i) <= fin && indicesEnd.get(i) >= debut)
                return false;
            if (fin < debut && indicesStart.get(i) <= fin) {
                return false;
            }
        }
        return true;
    }

}
