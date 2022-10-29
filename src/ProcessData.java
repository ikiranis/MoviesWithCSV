import java.util.ArrayList;
import java.util.Comparator;
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

            getGenreInMovie(movie);
            getMoviesInYear(movie.getYear());
            getWordsInMovies(movie.getTitle());
        }

        System.out.println("\nMovies in Genre");
        for(Map.Entry<String, Integer> genre : moviesInGenre.entrySet()) {
            System.out.println(genre.getKey() + ": " + genre.getValue());
        }

        System.out.println("\nMovies in Year");
        for(Map.Entry<Integer, Integer> year : moviesInYear.entrySet()) {
            System.out.println(year.getKey() + ": " + year.getValue());
        }


        Map<String, Integer> sortedWords = new HashMap<>();
        wordsInMovies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEachOrdered(x -> sortedWords.put(x.getKey(), x.getValue()));


        System.out.println("\nWords in Movies");
        int counter = 0;
        for(Map.Entry<String, Integer> word : sortedWords.entrySet()) {
            System.out.println(word.getKey() + ": " + word.getValue());
            counter += word.getValue();
        }

        System.out.println("\nΣύνολο εμφανίσεων των πιο συχνών λέξεων: " + counter);
    }

    private void getGenreInMovie(Movie movie) {
        for(String genre : movie.getGenres()) {
            if(moviesInGenre.containsKey(genre)) {
                int sum = moviesInGenre.get(genre) + 1;
                moviesInGenre.put(genre, sum);
            } else {
                moviesInGenre.put(genre, 1);
            }
        }
    }

    private void getMoviesInYear(int year) {
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

    private void getWordsInMovies(String title) {
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
