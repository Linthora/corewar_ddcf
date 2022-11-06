package corewar;

import java.util.List;
/**
 * Executable class used to check if <file>.red doesn't contain illegal instructions. 
 */
public class Checker {
    public static void main(String[] args) {
        if(args.length<1)
            System.out.println("Erreur usage: Checker [nom du programme à évaluler]");

        Parser parser = new Parser();
        for(String s : args)
        {
            System.out.println("Evaluation de "+s);
            try {
                List<Cell> test=parser.parseFile(s,1);
                System.out.println("Le fichier "+s+" est correct");
            } catch (Exception e) {
                System.out.println("Le programme "+s+" contient des erreurs (ou nom fourni incorrect).");
            }
        }
    }    
}
