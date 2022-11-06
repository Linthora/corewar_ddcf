package genetic;

import java.util.Set;

/**
 * Abstract Object containing method to select programs for the {@link AlgoGen}.
 */
public abstract class Selector {

    /**
     * Number of programs that needs to be selected.
     */
    public int wantedNumber;

    /**
     * Creates a Selector.
     */
    public Selector(int wantedNumber) {
        this.wantedNumber = wantedNumber;
    }

    /**
     * Method to select programs for the {@link AlgoGen}.
     * @param scores Array of the scores of each programs in the previously made evalutaiton.
     * @return A set of the selected programs' Id.
     */
    public abstract Set<Integer> select(int[] scores);
}
