package genetic;

import java.io.File;
import java.io.PrintWriter;
import java.lang.StringBuilder;

/**
 * Object managing the data collection. /!\ Should be ameliorated to directly write into file at each addition.
 */
public class Data {

    /**
     * Content of the future <file>.csv
     */
    protected StringBuilder futureCSV;

    /**
     * The file.
     */
    protected PrintWriter myfile;

    public Data(String name) {
        this.futureCSV = new StringBuilder();
        futureCSV.append("Generation,Lifetime Over 900 Cycle,Total Occumation Grade,Occupation Block of 5 Grade,Created Process Grade,Alive Process Grade,Bombing Freq Grade");
        try {
            this.myfile=new PrintWriter(new File(name+".csv"));
        } catch (Exception e) {
            System.err.println("Erreur dans l'écriture du csv.");
            System.exit(1);
        }
    }

    /**
     * Add a new line to the csv file with givene data.
     * @param generation
     * @param lifetime
     * @param totOccupation
     * @param createdProcess
     * @param aliveProcess
     * @param bombingFreqGrade
     */
    public void add(int generation, long lifetime, 
                    int totOccupation, int createdProcess, 
                    int aliveProcess, double bombingFreqGrade,
                    int occupation5block) {
        this.futureCSV.append("\n"+generation+","+lifetime+","+totOccupation+","+ occupation5block + ","+createdProcess+","+aliveProcess+","+bombingFreqGrade);
    }
    
    /**
     * Creates the csv file with the content of the StringBuilder.
     */
    public void createFile()
    {
        myfile.write(this.futureCSV.toString());
		myfile.close();
		System.out.println("\nFichier csv créé");
    }
}