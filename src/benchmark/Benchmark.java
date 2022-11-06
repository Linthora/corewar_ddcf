package benchmark;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import corewar.*;

/**
 * Object used to evaluate given program according to basic metric. Extends {@link corewar.Interpreter}.
 */
public class Benchmark extends Interpreter {

    /**
     * Total number of split done during the evaluation.
     */
    protected int nbSplit;

    /**
     * Number of decesead split during the evaluation.
     */
    protected int nbDeadProcess;

    /**
     * Number of written Cells during the last 50 cycles.
     */
    protected int nbWrittenCell50;

    /**
     * Number of written Cell every 50 cycle.
     */
    protected List<Integer> writtenCell50Cycle;

    /**
     * Creates a Benchmark with the given program
     * @param prog program under its form of List of Cells.
     */
    public Benchmark(List<Cell> prog){
        List<List<Cell>> progs = new ArrayList<>(1);
        progs.add(prog);
        initGame(progs);
    }

    /**
     * Creates a Benchmark with given program's file name.
     * @param progName Name of the file containing the program to evaluate.
     * @throws RuntimeException Throws Exception when illegal instructions or when illegal addresses.
     * @throws IOException Throws when error in the file reading.
     */
    public Benchmark(String progName)  throws RuntimeException, IOException
    {
        List<List<Cell>> progs = new ArrayList<List<Cell>>(1);
        Parser parser = new Parser();
        progs.add(parser.parseFile(progName,0));
        initGame(progs);
    }

    /**
     * Run the evaluation.
     */
    public void eval() {
        this.end = 900;
        this.writtenCell50Cycle = new ArrayList<>();
        this.turnN++; // to facilitate the evaluation of the frequence of bombing.

        do {
            if(this.turnN % 50 == 0) {
                this.writtenCell50Cycle.add(this.nbWrittenCell50);
                this.nbWrittenCell50 = 0;
            }
            playNextTour(); // not a problem if the programs dies at 900 exactly, even with the do while.
        } while(!isOver());
    }

    /**
     * Executable main to evaluate given program.
     * @param args 
     * @throws IOException
     * @throws RuntimeException
     */
    public static void main(String[] args) throws RuntimeException, IOException {
        if (args.length < 1) {
            System.out.println("No programs were given in arguements.");
            System.exit(1);
        }
        Benchmark bench = new Benchmark(args[0]);
        bench.eval();

        System.out.println("Evaluation done");
        bench.displayResults();
    }


    /**
     * Get the arevage bombing freq over 50 cycle.
     * @return The arevage bombing freq over 50 cycle
     */
    public double getGradeBombingFreq() {
        double freq = 0;
        for(Integer i : this.writtenCell50Cycle)
            freq += i;
        freq = freq / (900/50);
        return freq;
    }

    /**
     * Get the number of new written Cell over 1000 cycles.
     * @return The number of new written Cell over 1000 cycles.
     */
    public int getGradeOccupation() {
        int res = 0;

        for(Cell c : this.memory.tab)
            if(c.team > -1)
                res++;
        return res;
    }

    /**
     * Get the number of created process during the game over 1000 cycle.
     * @return The number of created process during the game over 1000 cycle.
     */
    public int getGradeCreatedProcess() {
        return this.nbSplit;
    }

    /**
     * Get the number of alive process at the end of 1000 cycle.
     * @return The number of alive process at the end of 1000 cycle.
     */
    public int getGradeAliveProcess() {
        return this.nbSplit - this.nbDeadProcess +1;
    }

    /**
     * Get the number of survived turn on 900 cycle.
     * @return
     */
    public long getGradeAliveTime900Cycle() {
        return this.turnN;
    }

    /**
     * Get occupation rate of block of n Cells from the memory.
     * @param n The number of block to take in account.
     * @return The occupation rate of block of n Cells from the memory.
     */
    public int getGradeOccupationBlockN(int n) {
        if(n < 1)
            return -1;
        
        int res = 0;

        boolean exploredN = false;
        for(int i=0;i<this.memory.taille;i++) {
            if(i%n == 0) {
                if(exploredN)
                    res++;
                exploredN = false;
            }
            if(this.memory.tab[i].team>-1) {
                exploredN = true;
                if(i%n == 0)
                    i+=n;
                else 
                    i+= (i%n);
            }
        }
        return res;
    }

    /**
     * Get occupation rate of block of 5 Cells from the memory.
     * @return The occupation rate of block of n Cells from the memory.
     */
    public int getGradeOccupationBlockN() {
        return getGradeOccupationBlockN(5);
    }

    /**
     * Final grade formed according to the previous grades.
     * @return The final grade.
     */
    public double getFinalGrade() {
        return 0;
    }

    public void displayResults() {
        System.out.println("Occupation grade: "+ getGradeOccupation());
        System.out.println("Occupation N=5: " + getGradeOccupationBlockN(5));
        System.out.println("Created process grade: " + getGradeCreatedProcess());
        System.out.println("Alive process grade: " + getGradeAliveProcess());
        System.out.println("Bombing frequence grade: " + getGradeBombingFreq());
    }

    /**
     * Custom version of playNextTour in order to check when a new Cell is written.
     * Could have been done more elegantly.
     */
    @Override
    public void playNextTour()
    {
        for (Integer teamNumber: heads.keySet()) {
            LinkedList<Integer> teamHeads = heads.get(teamNumber);
            if (teamHeads.size() == 0) {
                continue;
            }
            Integer currentIndex = teamHeads.removeFirst();
            Cell currentCell = this.memory.tab[currentIndex];
            Integer a = resolveAddr(currentIndex, currentCell.op1);
            Integer b = resolveAddr(currentIndex, currentCell.op2);
            Integer nextIndex = currentIndex+1;
            if (isIllegalInstruction(currentCell.instruction, currentCell.op1, currentCell.op2)) {
                nextIndex = null;
            } else {
                switch (currentCell.instruction) {
                    case SPL:
                        // dealt with after
                        break;
                    case ADD:
                        if (currentCell.op1.type == Type.OCTOTHORPE)
                            memory.tab[b].op2.value += a;
                        else {
                            memory.tab[b].op1.value += memory.tab[a].op1.value;
                            memory.tab[b].op2.value += memory.tab[a].op2.value;
                        }
                        memory.tab[b].team = teamNumber;

                        ///  Here we add the case where we write on a Cell that doesn't belong to us.
                        if(memory.tab[b].team > -1)
                            this.nbWrittenCell50++;
                        break;
                    case SUB:
                        if (currentCell.op1.type == Type.OCTOTHORPE)
                            memory.tab[b].op2.value -= a;
                        else {
                            memory.tab[b].op1.value -= memory.tab[a].op1.value;
                            memory.tab[b].op2.value -= memory.tab[a].op2.value;
                        }
                        memory.tab[b].team = teamNumber;
                        
                        ///  Here we add the case where we write on a Cell that doesn't belong to us.
                        if(memory.tab[b].team > -1)
                            this.nbWrittenCell50++;
                        break;
                    case MOV:
                        if (currentCell.op1.type == Type.OCTOTHORPE)
                            memory.tab[b].op2.value = a;
                        else
                            memory.tab[b] = memory.tab[a].deepcopy();
                        memory.tab[b].team = teamNumber;
                        
                        ///  Here we add the case where we write on a Cell that doesn't belong to us.
                        if(memory.tab[b].team > -1)
                            this.nbWrittenCell50++;
                        break;
                    case CMP:
                        if (currentCell.op1.type == Type.OCTOTHORPE) {
                            if (memory.tab[b].op2.value == a)
                                nextIndex++;
                        } else {
                            if (memory.tab[b].equals(memory.tab[a]))
                                nextIndex++;
                        }
                        break;
                    case SLT:
                        if (currentCell.op1.type == Type.OCTOTHORPE) {
                            if (a < memory.tab[b].op2.value)
                                nextIndex++;
                        } else {
                            if (memory.tab[a].op2.value < memory.tab[b].op2.value)
                                nextIndex++;
                        }
                        break;
                    case JMP:
                        nextIndex = a;
                        break;
                    case JMZ:
                        if (currentCell.op2.type == Type.OCTOTHORPE) {
                            if (currentCell.op2.value != 0)
                                nextIndex = a;
                        } else if (memory.tab[b].op2.value == 0)
                            nextIndex = a;
                        break;
                    case JMN:
                        if (currentCell.op2.type == Type.OCTOTHORPE) {
                            if (currentCell.op2.value != 0)
                                nextIndex = a;
                        } else if (memory.tab[b].op2.value != 0)
                            nextIndex = a;
                        break;
                    case DJN:
                        if (currentCell.op2.type == Type.OCTOTHORPE) {
                            currentCell.op2.value--;
                            if (currentCell.op2.value != 0)
                                nextIndex = a;
                        } else {
                            memory.tab[b].op2.value--;
                            memory.tab[b].team = teamNumber;
                            if (memory.tab[b].op2.value != 0)
                                nextIndex = a;
                        }
                        break;
                    case DAT:
                        nextIndex = null;
                        // Here a process died.
                        this.nbDeadProcess++;
                        break;
                }
            }
            if (nextIndex != null) {
                memory.tab[wrapMemoryAddr(nextIndex)].team = teamNumber;
                heads.get(teamNumber).addLast(wrapMemoryAddr(nextIndex));
            }
            if ( currentCell.instruction == Instruction.SPL) {
                heads.get(teamNumber).addLast(wrapMemoryAddr(a));
                this.nbSplit++;
            }
        }
        // to handle because of the do while.
        if(this.turnN<1000)
            ++this.turnN; 
    }

    /**
     * Custom version of isOver for the evaluation.
     */
    @Override
    public boolean isOver()
    {
        if(this.turnN > this.end)
            return true;
        for(Integer i : this.heads.keySet())
            if(this.heads.get(i).size()>0)
                return false;
        return true;
    }


}
