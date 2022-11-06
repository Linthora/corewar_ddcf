package test.genetic;

import java.util.Set;
import java.util.stream.IntStream;

import genetic.Elitism;
import test.Testable;

public class TestElitism implements Testable {

    short errorCode = 0;

    @Override
    public boolean test() {
        final int wantedNumber = 5;
        Elitism elitism = new Elitism(wantedNumber);
        int[] scores = {0, 2, 1, 4, 5};
        Set<Integer> selected = elitism.select(scores);
        if (selected.size() != wantedNumber ||
            !selected.contains(0) ||
            !selected.contains(1) ||
            !selected.contains(2) ||
            !selected.contains(3) ||
            !selected.contains(4)) {
            errorCode = 1;
            return false;
        }
        scores = IntStream.of(234, 999, 102, 2, 49, 95, 1000, 5, 74, 63, 41).toArray();
        selected = elitism.select(scores);
        if (selected.size() != wantedNumber ||
            !selected.contains(6) ||
            !selected.contains(1) ||
            !selected.contains(0) ||
            !selected.contains(2) ||
            !selected.contains(5)) {
            errorCode = 2;
            return false;
        }
        return true;
    }

    @Override
    public String getError() {
        return "TestElitism: "+errorCode;
    }
}
