package test;

import test.corewar.TestCell;
import test.corewar.TestInterpreter;
import test.corewar.TestMemory;
import test.corewar.TestOperand;
import test.corewar.TestParser;
import test.genetic.TestElitism;

public class Main {
    public static void main(String[] args) {
        Testable[] tests = {
            new TestCell(),
            new TestOperand(),
            new TestMemory(),
            new TestElitism(),
            new TestParser(),
            new TestInterpreter(),
        };
        for (Testable testable: tests) {
            if (!testable.test()) {
                System.out.println("FAILED: "+testable.getError());
                return;
            }
        }
        System.out.println("All test OK");
    }
}
