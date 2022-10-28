import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadCSV {
    private String filename;
    private ArrayList<String> csvLines = new ArrayList<>();

    public ReadCSV(String filename) {
        this.filename = filename;
    }

    public ArrayList<String> readFile() {
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
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
