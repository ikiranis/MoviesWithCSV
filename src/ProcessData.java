import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessData extends Thread {
    private ArrayList<String> csvLines = new ArrayList<>();
    private Map<String, Integer> moviesInGenre = new HashMap<>();
    private Map<Integer, Integer> moviesInYear = new HashMap<>();
    private Map<String, Integer> wordsInMovies = new HashMap<>();

    public ProcessData(ArrayList<String> csvLines) {
        this.csvLines = csvLines;
    }

    public void run() {
        processLines();
    }

    private void processLines() {
        for(String line : csvLines) {
            Movie movie = getMovie(line);

            countGenreInMovie(movie);
            countMoviesInYear(movie.getYear());
            countWordsInMovies(movie.getTitle());
        }

        System.out.println("\nMovies in Genre");
        for(Map.Entry<String, Integer> genre : moviesInGenre.entrySet()) {
            System.out.println(genre.getKey() + ": " + genre.getValue());
        }

        System.out.println("\nMovies in Year");
        for(Map.Entry<Integer, Integer> year : moviesInYear.entrySet()) {
            System.out.println(year.getKey() + ": " + year.getValue());
        }

        System.out.println("\nWords in Movies");
        for(Map.Entry<String, Integer> year : wordsInMovies.entrySet()) {
            System.out.println(year.getKey() + ": " + year.getValue());
        }
    }

    private void countGenreInMovie(Movie movie) {
        for(String genre : movie.getGenres()) {
            if(moviesInGenre.containsKey(genre)) {
                int sum = moviesInGenre.get(genre) + 1;
                moviesInGenre.put(genre, sum);
            } else {
                moviesInGenre.put(genre, 1);
            }
        }
    }

    private void countMoviesInYear(int year) {
        if(year == 0) {
            return;
        }

        if(moviesInYear.containsKey(year)) {
            int sum = moviesInYear.get(year) + 1;
            moviesInYear.put(year, sum);
        } else {
            moviesInYear.put(year, 1);
        }
    }

    private void countWordsInMovies(String title) {
        String[] words = title.split(" ");

        for(String word : words) {
            word = word.replaceAll("[^a-zA-Z0-9_-]", "");

            if(wordsInMovies.containsKey(word)) {
                int sum = wordsInMovies.get(word) + 1;
                wordsInMovies.put(word, sum);
            } else {
                wordsInMovies.put(word, 1);
            }
        }
    }

    /**
     *
     * @param title
     * @return
     */
    private int getYearFromTitle(String title) {
        try {
            return Integer.parseInt(title.substring(title.length() - 5, title.length() - 1));
        } catch (Exception e) {
            return 0;
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

        int year = getYearFromTitle(title);

        title = title.replace(" (" + String.valueOf(year) + ")", "");

        Movie movie = new Movie(Long.parseLong(lineFields[0]), title, year);

        if (!lineFields[2].contains("(no genres listed)")) {
            for (String genre : lineFields[2].split("[|)]")) {
                movie.addGenre(genre);
            }
        }

        return movie;
    }
}
