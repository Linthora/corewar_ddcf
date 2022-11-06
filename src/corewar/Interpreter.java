package corewar;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Object holding the core with it's memory and the interpreter to play each cycle.
 */
public class Interpreter 
{
    /**
     * Max number of cycle authorized before the game is declared over if no programs wins before.
     */
    protected long end;

    /**
     * Number of played cycle.
     */
    protected long turnN;

    /**
     * Total number of programs in the game at the beginning.
     */
    public int nbWarrior;

    /**
     * The Core memory of the game.
     */
    public Memory memory;

    /**
     * Map matching a program's number to its name.
     */
    public Map<Integer, String> programNames;

    /**
     * Map matching a program's number to its FIFO of its process location.
     */
    public Map<Integer, LinkedList<Integer>> heads; 
    
    /**
     * Map matching a program's number to the cycle number of its death, or -1 if not dead yet, used for the GUI.
     */
    public Map<Integer,Long> scores;

    /**
     * First constructor, only used in tests.
     */
    public Interpreter() {
        // only used in tests
    }

    /**
     * Second constructor, used to create an Interpreter with given programs.
     * @param programs List of programs represented as List of Cells.
     */
    public Interpreter(List<List<Cell>> programs)
    {
        initGame(programs);
    }

    /**
     * Second constructor, used to create an Interpreter with given programs.
     * @param warriorsName Array of strings representing the programs file path.
     * @throws RuntimeException Throws Exception when illegal instructions or when illegal addresses.
     * @throws IOException Throws when error in the file reading.
     */
    public Interpreter(String[] warriorsName) throws RuntimeException, IOException
    {
        List<List<Cell>> warriors = new ArrayList<List<Cell>>(warriorsName.length);
        Parser parser = new Parser();
        programNames = new HashMap<>(warriorsName.length);
        for(int i=0; i<warriorsName.length; i++)
        {
            programNames.put(i, warriorsName[i]);
            warriors.add(parser.parseFile(warriorsName[i],i));
            System.out.println(warriorsName[i]+" => teamn n°"+i);
        }
        initGame(warriors);
    }

    /**
     * State if game is Over or not. Weither it is by reaching the max cycle number or if there is 1 or 0 programs alive.
     * @return False if the game isn't over, True otherwise.
     */
    public boolean isOver()
    {
        if(turnN>end)
            return true;

        int alive = 0;
        for (Integer teamNumber: heads.keySet()) {
            if (heads.get(teamNumber).size() > 0)
                if(++alive>=2)
                    return false;
        }
        return true;
    }

    /**
     * Give set of alive programs at this moment.
     * @return set of programs' numbers currently alive.
     */
    public Set<Integer> getWinners() {
        HashSet<Integer> winners = new HashSet<>();
        for (Integer teamNumber: heads.keySet()) {
            if (heads.get(teamNumber).size() > 0)
                winners.add(teamNumber);
        }
        return winners;
    }

    /**
     * Initialise all the attributes of the Interpreter according to the given programs.
     * @param warriors List of programs represented as List of Cells.
     */
    protected void initGame(List<List<Cell>> warriors)
    {
        this.nbWarrior = warriors.size();
        this.heads = new HashMap<Integer, LinkedList<Integer>>(nbWarrior);
        this.memory = new Memory();
        this.memory.fillWithDat();
        this.memory.writeProg(warriors);
        for(Integer i=0; i < this.nbWarrior; i++)
        {
            LinkedList<Integer> tmp = new LinkedList<Integer>();
            tmp.add(this.memory.indicesStart.get(i));
            this.heads.put(warriors.get(i).get(0).team, tmp);
        }
        this.end = 10_000L + (long) (Math.random() * (50_000L - 10_000L)) ;// random GRAND (1Million -10 M ?)
        this.turnN=0;
        this.scores = new HashMap<>();
        for(Integer i : this.heads.keySet())
            this.scores.put(i,(long)-1); // safer to init directly to -1
    }
    
    /**
     * Getter of the current cycle number.
     * @return Current cycle number.
     */
    public long getTurnN() 
    {
        return this.turnN;
    }

    /**
     * Basic display of the Core.
     */
    public void dispMem()
    {
        String disp="";
        int [] stats = new int[this.nbWarrior];
        for(int i=0;i<this.nbWarrior;i++)
        {
            stats[i]=0;
        } 
        for(int i=0;i<this.memory.tab.length;i++)
        {
            if(this.memory.tab[i].team == -1)
            {
                disp+='x';
            }
            else
            {
                for (Integer teamNumber : heads.keySet()) {
                    if (heads.get(teamNumber).size()>0 && heads.get(teamNumber).getFirst() == i) {
                        disp+="\u001B[31m";
                        break;
                    }
                }
                disp+=this.memory.tab[i].team;
                stats[this.memory.tab[i].team]++;
                disp+="\u001B[0m";
            } 
        }

        /*for(int i=0;i<this.nbWarrior;i++)
        {
            disp+="\nJoueur " + (i+1) +": " + stats[i] +" cellules contrôlés; " + this.heads.get(i).size()+" tâches";
        }*/
        System.out.println(disp);
        
    }

    /**
     * Transform given address from Operand to real adress in the memory.
     * @param addr Adress given by Operand.
     * @return Associated address in the Memory.
     */
    protected int wrapMemoryAddr(int addr) {
        return Math.floorMod(addr, memory.taille);
    }

    /**
     * Transform address and process operation given by the Operand and return real corresponding address from the memory.
     * @param currentIndex Current address of where the Operand needs to be processed.
     * @param op Operand to precess.
     * @return Corresponding address in the memory.
     */
    protected int resolveAddr(int currentIndex, Operand op) {
        if (op.type == Type.OCTOTHORPE)
            return op.value;
        int absoluteAddr = wrapMemoryAddr(op.value+currentIndex);
        if (op.type == Type.DIRECT)
            return absoluteAddr;
        int value = memory.tab[absoluteAddr].op2.value;
        if (op.type == Type.PREDECREMENT)
            value--;
        else if (op.type == Type.PREINCREMENT)
            value++;
        memory.tab[absoluteAddr].op2.value = value;
        return wrapMemoryAddr(value+currentIndex);
    }

    /**
     * Verify if given Intruction and Operand form an illegal Instruction.
     * @param instruction Instruction to evaluate with the followng Operands.
     * @param op1 Operand A of the given Instruction.
     * @param op2 Operand B of the given Instruction.
     * @return True if it's illegal, False otherwise.
     */
	public static boolean isIllegalInstruction(Instruction instruction, Operand op1, Operand op2) {
		switch (instruction) {
			case ADD:
				return op2.type == Type.OCTOTHORPE;
			case CMP:
				return op2.type == Type.OCTOTHORPE;
			case DAT:
				return false;
			case DJN:
				return op1.type == Type.OCTOTHORPE;
			case JMN:
				return op1.type == Type.OCTOTHORPE;
			case JMP:
				return op1.type == Type.OCTOTHORPE;
			case JMZ:
				return op1.type == Type.OCTOTHORPE;
			case MOV:
				return op2.type == Type.OCTOTHORPE;
			case SLT:
				return op2.type == Type.OCTOTHORPE;
			case SPL:
				return op1.type == Type.OCTOTHORPE;
			case SUB:
				return op2.type == Type.OCTOTHORPE;
		}
		return true;
	}

    /**
     * Move current State to the next.
     */
    public void playNextTour()
    {
        for (Integer teamNumber: heads.keySet()) {
            LinkedList<Integer> teamHeads = heads.get(teamNumber);
            if (teamHeads.size() == 0) {
                continue;
            }
            Integer currentIndex = teamHeads.removeFirst();
            Cell currentCell = this.memory.tab[currentIndex];
            // System.out.println("Team n°"+teamNumber+": "+currentIndex+" => "+currentCell);
            Integer a = resolveAddr(currentIndex, currentCell.op1);
            Integer b = resolveAddr(currentIndex, currentCell.op2);
            Integer nextIndex = currentIndex+1;
            // System.out.println("Executing "+currentCell.instruction+" "+a+" "+b);
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
                        break;
                    case SUB:
                        if (currentCell.op1.type == Type.OCTOTHORPE)
                            memory.tab[b].op2.value -= a;
                        else {
                            memory.tab[b].op1.value -= memory.tab[a].op1.value;
                            memory.tab[b].op2.value -= memory.tab[a].op2.value;
                        }
                        memory.tab[b].team = teamNumber;
                        break;
                    case MOV:
                        if (currentCell.op1.type == Type.OCTOTHORPE)
                            memory.tab[b].op2.value = a;
                        else
                            memory.tab[b] = memory.tab[a].deepcopy();
                        memory.tab[b].team = teamNumber;
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
                        break;
                }
            }
            if (nextIndex != null) {
                memory.tab[wrapMemoryAddr(nextIndex)].team = teamNumber;
                heads.get(teamNumber).addLast(wrapMemoryAddr(nextIndex));
            }
            if ( currentCell.instruction == Instruction.SPL)
                heads.get(teamNumber).addLast(wrapMemoryAddr(a));
        }
        ++this.turnN;
        // for scores
        for(Integer teamNumber : heads.keySet())
            if (heads.get(teamNumber).size() == 0 && scores.get(teamNumber)<0)
                scores.put(teamNumber,this.turnN); 
    }
}
