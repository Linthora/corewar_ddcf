package corewar;

import java.util.Set;

/**
 * Executable class to run a game with the given programs as parameters.
 */
public class Main{

    public static void main(String[] args) {
        
        try {
            Interpreter interpreter = new Interpreter(args);
    
            while(!interpreter.isOver()){
                interpreter.dispMem();
                interpreter.playNextTour();
            }

            Set<Integer> winners = interpreter.getWinners();
            System.out.println("Winners:");
            for (Integer i: winners) {
                System.out.println(interpreter.programNames.get(i)+" ("+i+")");
            }
        } catch (IllegalInstructionException e) {
            System.err.println(e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
