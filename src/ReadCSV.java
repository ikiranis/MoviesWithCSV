import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Κλάση που διαβάζει ένα αρχείο CSV
 */
public class ReadCSV {
    private String filename;        // Το όνομα του αρχείου CSV
    private ArrayList<String> csvLines = new ArrayList<>();     // Array με τις γραμμές του CSV

    public ReadCSV(String filename) {
        this.filename = filename;
    }

    /**
     * Διάβασμα του αρχείου και προσθήκη στο array με τις γραμμές
     *
     * @return
     */
    public ArrayList<String> readFile() {
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                // Αγνοεί την πρώτη γραμμή που έχει το όνομα των πεδίων
                if (!data.contains("movieId,title,genres")) {
                    csvLines.add(data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }

        return csvLines;
    }
}
