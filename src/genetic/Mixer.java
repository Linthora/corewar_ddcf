package genetic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import corewar.Cell;
import corewar.Parser;

/**
 * Object used to mix programs together.
 */
public class Mixer {
    
    /**
     * Number of programs to mix. Used to allocate space for programs to mix.
     */
    int numberOfPrograms;

    /**
     * Programs to mix.
     */
    List<List<Cell>> programs;

    /**
     * Create a Mixer.
     * @param numberOfPrograms Constant number of programs that are going to be mixed at each generations.
     */
    public Mixer(int numberOfPrograms) {
        this.numberOfPrograms = numberOfPrograms;
        programs = new ArrayList<>(numberOfPrograms);
    }

    /**
     * Add program to the list of programs to be mixed.
     * @param program Program to be added to the list of programs to be mixed.
     */
    public void addProgram(List<Cell> program) {
        programs.add(program);
    }

    /**
     * Creates new programs based on the fusion of the given programs.
     * @param random Give {@link java.util.Random} instance.
     * @return List of programs born from the mix of the originals two by two.
     */
    public List<List<Cell>> mix(Random random) {
        List<List<Cell>> mixedPrograms = new ArrayList<>(numberOfPrograms*(numberOfPrograms-1)/2);
        for (int i=0; i<programs.size(); ++i) {
            for (int j=i+1; j<programs.size(); ++j) {
                if (j == i) continue;
                List<Cell> otherMixedProgramCells = programs.get(j);
                List<Cell> programCells = programs.get(i);
                int newProgramSize = Math.round((float)(otherMixedProgramCells.size()+programCells.size())/2f);
                List<Cell> newCells = new ArrayList<>(newProgramSize);
                for (int k=0; k<newProgramSize; ++k) {
                    if (k < programCells.size() && k < otherMixedProgramCells.size()) {
                        if (random.nextBoolean()) {
                            newCells.add(programCells.get(k).deepcopy());
                        } else {
                            newCells.add(otherMixedProgramCells.get(k).deepcopy());
                        }
                    } else if (k >= programCells.size()) {
                        newCells.add(otherMixedProgramCells.get(k).deepcopy());
                    } else {
                        newCells.add(programCells.get(k).deepcopy());
                    }
                }
                mixedPrograms.add(newCells);
            }
        }
        return mixedPrograms;
    }

    /**
     * Executable main to test manually.
     * @param args
     */
    public static void main(String[] args) {
        Mixer m = new Mixer(2);
        Parser p = new Parser();
        try {
            for (int i=0; i<args.length; ++i)
                m.addProgram(p.parseFile(args[i], i));
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (List<Cell> programCells: m.mix(new Random(System.currentTimeMillis()))) {
            System.out.println("\nprogram size: "+programCells.size());
            for (Cell c: programCells) {
                System.out.println(c);
            }
        }
    }
}
