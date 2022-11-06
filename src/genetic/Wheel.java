package genetic;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * Object used as a for wheel method of selection. Extending {@link Selector}.
 */
public class Wheel extends Selector {

    /**
     * Creates a Wheel Selector.
     * @param wantedNumber
     */
    public Wheel(int wantedNumber) {
        super(wantedNumber);
    }

    @Override
    public Set<Integer> select(int[] scores) {
        Set<Integer> selected = new HashSet<>();
        int max = scores[0];
        for (int i=1;i<scores.length;++i) {
            int score = scores[i];
            if (score > max) {
                max = score;
            }
        }
        System.out.println("max : "+max);
        // we need to handle the case where there are no winner at all because it is rare enough to get a 0 with Math.random().
        if(max == 0) { // this one is easy
            Random rand = new Random();
            while (selected.size() < wantedNumber) {
                int tmp = rand.nextInt(scores.length);
                if(!selected.contains(tmp))
                    selected.add(tmp);
            }
        } else { // and we need to handle the case where there are less than the wanted number case of programs with more than 0 victories, causing almost infinite loop.
            while (selected.size() < wantedNumber) {
                for (int i=0;i<scores.length;++i) {
                    //first condition to avoid (or try at least) infinite loop
                    if ( (scores[i] == 0 && Math.random() < (double)(0.5/max) ) || (Math.random() < (double)scores[i]/max)) { // fixed here ? 
                        selected.add(i);
                        if (selected.size() == wantedNumber)
                            break;
                    }
                }
            }
        }
        return selected;
    }
}
