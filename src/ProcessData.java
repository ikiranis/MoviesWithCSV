import java.util.ArrayList;
import java.util.HashMap;

public class ProcessData extends Thread {
    private ArrayList<String> csvLines = new ArrayList<>();
    private HashMap<String, Integer> moviesInGenres = new HashMap<String, Integer>();

    public ProcessData(ArrayList<String> csvLines) {
        this.csvLines = csvLines;
    }

    public void run() {
        processLines();
    }

    private void processLines() {
        for(String line : csvLines) {
            Movie movie = getMovie(line);


        }
    }

    /**
     *
     * @param line
     * @return movie
     */
    private Movie getMovie(String line) {
        String[] lineFields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        String title = lineFields[1].replace("\"", "");
        int year = 0;

        try {
            year = Integer.parseInt(title.substring(title.length() - 5, title.length() - 1));
        } catch (Exception e) {
            System.out.println(e);
        }

        title = title.replace(" (" + String.valueOf(year) + ")", "");

        Movie movie = new Movie(Long.parseLong(lineFields[0]), title, year);

        if (!lineFields[2].contains("(no genres listed)")) {
            for (String genre : lineFields[2].split("[|)]")) {
                movie.addGenre(genre);
            }
        }

        System.out.println(movie);

        return movie;
    }
}
