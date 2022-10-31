import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProcessData extends Thread {
    private ArrayList<String> csvLines;
    private Map<String, Integer> moviesInGenre = new HashMap<>();
    private Map<Integer, Integer> moviesInYear = new HashMap<>();
    private Map<String, Integer> wordsInMovies = new HashMap<>();
    private int start;
    private int batchSize;

    public ProcessData(ArrayList<String> csvLines, int start, int batchSize)
    {
        this.csvLines = csvLines;
        this.start = start;
        this.batchSize = batchSize;
    }

    public Map<String, Integer> getMoviesInGenre() {
        return moviesInGenre;
    }

    public Map<Integer, Integer> getMoviesInYear() {
        return moviesInYear;
    }

    public Map<String, Integer> getWordsInMovies() {
        return wordsInMovies;
    }

    public void run() {
        System.out.println(Thread.currentThread().getName() + " started. Επεξεργασία " + batchSize + " γραμμών");
        processLines();
        System.out.println(Thread.currentThread().getName() + " finished");
    }

    private void processLines() {
        for (int i=start; i<(start + batchSize); i++) {
            Movie movie = getMovie(csvLines.get(i));

            calcGenreInMovie(movie);
            calcMoviesInYear(movie.getYear());
            calcWordsInMovies(movie.getTitle());
        }
    }

    private void calcGenreInMovie(Movie movie) {
        for(String genre : movie.getGenres()) {
            if(moviesInGenre.containsKey(genre)) {
                int sum = moviesInGenre.get(genre) + 1;
                moviesInGenre.put(genre, sum);
            } else {
                moviesInGenre.put(genre, 1);
            }
        }
    }

    private void calcMoviesInYear(int year) {
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

    private void calcWordsInMovies(String title) {
        String[] words = title.replaceAll("^[.,\\s]+", "").split("[.,\\s]+");
//        The call to replaceAll() removes leading separators.
//                The split is done on any number of separators.
//
//        The behaviour of split() means that a trailing blank value is ignored, so no need to trim trailing separators before splitting.


        for(String word : words) {
            word = word.toLowerCase();

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

        for (String genre : lineFields[2].split("[|]")) {
            movie.addGenre(genre);
        }

        return movie;
    }
}
