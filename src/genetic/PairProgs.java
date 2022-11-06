package genetic;

import java.util.LinkedList;
import java.util.List;
import corewar.*;

/**
 * Object representing a task for the thread and holding the results of its evaluation.
 */
public class PairProgs {

    /**
     * List of the 2 programs to evaluate.
     */
    protected List<List<Cell>> progs;

    /**
     * Id of the first program.
     */
    public int t1;

    /**
     * Id of the second program.
     */
    public int t2;

    /**
     * Array holding the results of the duels between the 2 programs.
     */
    public int results[];

    /**
     * Create a PairProgs.
     * @param p1 First programs.
     * @param p2 Second programs.
     * @param t1 Id of the first programs.
     * @param t2 Id of the second programs.
     */
    public PairProgs(List<Cell>p1, List<Cell> p2, int t1, int t2) {
        this.progs = new LinkedList<>();
        this.progs.add(p1);
        this.progs.add(p2);
        this.t1 = t1;
        this.t2 = t2;
        this.results = new int[2];
        // this.results[0] = 0;
        // this.results[1] = 0;
    }

    /**
     * Get the score the the program of the given Id.
     * @param t Id of the program we want to get the score of.
     * @return The score of the wanted program.
     */
    public int getScore(int t) {
        if(t==this.t1)
            return results[0];
        return results[1];
    }
}
