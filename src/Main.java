import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        ReadCSV readCSV = new ReadCSV("movies.csv");

        ArrayList<String> csvLines = readCSV.readFile();

        ProcessData process = new ProcessData(csvLines);
        process.start();

        try {
            process.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
}