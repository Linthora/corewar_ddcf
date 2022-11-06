package genetic;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;

import benchmark.Benchmark;

import java.util.List;
import java.io.IOException;

//`java -cp build genetic.AlgoGen <generations> <selector> [liste des programmes]`
import corewar.*;

public class AlgoGen {
    static final int INITIAL_PROGRAM_LENGTH_MIN = 2;
    static final int INITIAL_PROGRAM_LENGTH_MAX = 10;

    Selector selector;
    int numerOfGenerations;
    Random random;

    public AlgoGen(Selector selector, int numerOfGenerations, Random random) {
        this.selector = selector;
        this.numerOfGenerations = numerOfGenerations;
        this.random = random;
    }

    public static void printProgram(List<Cell> programCells) {
        System.out.println("\nprogram size: "+programCells.size());
        for (Cell c: programCells) {
            System.out.println(c);
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        int nbSelected;
        int numberOfPrograms;
        int selectorIndex;
        int genIndex;
        List<List<Cell>> programs;
        if (args.length == 3) {
            nbSelected = Integer.parseInt(args[0]);
            numberOfPrograms = nbSelected+(nbSelected*(nbSelected-1))/2+nbSelected;
            // nbSelected (without modifications) + nbMixed + nbMutated (same as nbSelected)
            genIndex = 1;
            selectorIndex = 2;
            programs = initPop(numberOfPrograms, random);
        } else {
            numberOfPrograms = args.length - 2;
            nbSelected = (int)(-1.5+Math.sqrt(2*numberOfPrograms+2.25));
            genIndex = 0;
            selectorIndex = 1;
            Parser parser = new Parser();
            programs = new ArrayList<>(numberOfPrograms);
            for (int i=2; i<numberOfPrograms+2; ++i) {
                try {
                    programs.add(parser.parseFile(args[i], -1)); // team will be set in the main AG loop
                } catch (IllegalArgumentException | IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
	    }
        System.out.println("numberOfPrograms: "+numberOfPrograms+ " nbSelected: "+nbSelected);

        Selector selector = null;
        switch (args[selectorIndex]) {
            case "e":
                selector = new Elitism(nbSelected);
                break;
            case "w":
                selector = new Wheel(nbSelected);
                break;
            default:
                System.err.println("Invalid selector: "+args[selectorIndex]);
                System.exit(1);
        }

        AlgoGen ag = new AlgoGen(selector, Integer.parseInt(args[genIndex]), random);
        
        boolean tmpDataCollection = true;
        List<Cell> bestProg = ag.evolve(programs, tmpDataCollection);

        printProgram(bestProg);
        CellToFile.progToFile(bestProg, "progs/best.red");
    }

    static List<List<Cell>> initPop(int nbIndividuals, Random random) {
        List<List<Cell>> programs = new ArrayList<>(nbIndividuals);
        for (int i=0; i<nbIndividuals; ++i) {
            programs.add(randomProgram(random));
        }
        return programs;
    }

    static List<Cell> randomProgram(Random random) {
        int programLength = random.nextInt(INITIAL_PROGRAM_LENGTH_MAX-INITIAL_PROGRAM_LENGTH_MIN+1) + INITIAL_PROGRAM_LENGTH_MIN;
        List<Cell> programCells = new ArrayList<>(programLength);
        for (int j=0; j<programLength; ++j) {
            programCells.add(Util.randomCell(random));
        }
        return programCells;
    }

    static boolean checkTeams(List<List<Cell>> programs) {
        boolean ok = true;
        for (int j=0; j<programs.size(); ++j) {
            for (Cell c: programs.get(j)) {
                if (c.team != j) {
                    ok = false;
                    break;
                }
            }
        }
        return ok;
    }

    List<Cell> evolve(List<List<Cell>> programs, boolean dataCollection) {
        int[] scores = {1}; // need to be init because of search of max afterward

        Pool pool = new Pool(4);

        Data dataCollector = null;
        
        if(dataCollection)
            dataCollector = new Data("AlgoGenData");

        for (int i=0; i<numerOfGenerations; ++i) {
            for (int j=0; j<programs.size(); ++j) {
                // write program teams in their cells
                for (Cell c: programs.get(j)) {
                    c.team = j;
                }
            }
            assert checkTeams(programs);

            pool.setter(programs);
            scores = pool.play(programs.size());

            programs = nextGen(programs, scores);
            
            System.out.println("\nGen : "+i);

            if(dataCollection) {
                int max = scores[0];
                int t = 0;
                for (int j=1;j<scores.length;++j) {
                    int score = scores[j];
                    if (score > max) {
                        max = score;
                        t = j;
                    }
                }
                Benchmark bench = new Benchmark(programs.get(t));
                bench.eval();
                dataCollector.add(i, bench.getGradeAliveTime900Cycle(), 
                                    bench.getGradeOccupation(), bench.getGradeCreatedProcess(), 
                                    bench.getGradeAliveProcess(), bench.getGradeBombingFreq(),
                                    bench.getGradeOccupationBlockN());
            }
        }
        pool.shutThread();
        if(dataCollection)
            dataCollector.createFile();

        int max = scores[0];
        int t = 0;
        for (int i=1;i<scores.length;++i) {
            int score = scores[i];
            if (score > max) {
                max = score;
                t = i;
            }
        }
        return programs.get(t);
    }

    static List<Cell> programDeepCopy(List<Cell> prog) {
        List<Cell> newProg = new ArrayList<>(prog.size());
        for (Cell c: prog) {
            newProg.add(c.deepcopy());
        }
        return newProg;
    }

    List<List<Cell>> nextGen(List<List<Cell>> progs, int[] scores) {
        Set<Integer> selectedTeams = selector.select(scores);
        Mixer mixer = new Mixer(selector.wantedNumber);
        Mutator mutator = new Mutator(random);
        List<List<Cell>> newPrograms = new ArrayList<>(progs.size());
        for (Integer team: selectedTeams) {
            mixer.addProgram(progs.get(team)); // mixer takes care of deepcopying cells
            newPrograms.add(programDeepCopy(progs.get(team))); // adding selected programs
            newPrograms.add(programDeepCopy(mutator.mutate(progs.get(team)))); // adding mutated programs
        }
        List<List<Cell>> mixedPrograms = mixer.mix(random);
        newPrograms.addAll(mixedPrograms);
        return newPrograms;
    }
}
