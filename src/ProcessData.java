import java.util.ArrayList;
import java.util.HashMap;

public class ProcessData extends Thread {
    private ArrayList<String> csvLines = new ArrayList<>();
    private HashMap<String, Integer> moviesInGenres = new HashMap<String, Integer>();

    public ProcessData(ArrayList<String> csvLines) {
        this.csvLines = csvLines;
    }

    public void run() {
        for(String line : csvLines) {
            String[] lineFields = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");

            Movie movie = new Movie(Long.parseLong(lineFields[0]), lineFields[1]);

            if (!lineFields[2].contains("(no genres listed)")) {
                for(String genre : lineFields[2].split("[|)]")) {
                    movie.addGenre(genre);
                }
            }
        }
    }
}
