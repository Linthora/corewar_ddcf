package genetic;

import java.util.HashSet;
import java.util.Set;

/**
 * Object used as a for elitisme method of selection. Extending {@link Selector}. 
 */
public class Elitism extends Selector {

    /**
     * Creates a Elitism Selector.
     * @param wantedNumber
     */
    public Elitism(int wantedNumber) {
        super(wantedNumber);
    }

    @Override
    public Set<Integer> select(int[] scores) {
        assert scores.length >= wantedNumber;
        HashSet<Integer> selected = new HashSet<>();
        for (int i=0;i<scores.length;++i) {
            if (selected.size() == wantedNumber) { // full
                Integer min = null;
                Integer t = null;
                for (Integer j: selected) {
                    if (min == null) {
                        min = scores[j];
                        t = j;
                    } else {
                        Integer score = scores[j];
                        if (score < min) {
                            min = score;
                            t = j;
                        }
                    }
                }
                if (scores[i] > min) {
                    selected.remove(t);
                    selected.add(i);
                }
            } else {
                selected.add(i);
            }
        }
        return selected;
    }
}
