import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

/**
 * Κλάση για τον υπολογισμό των δεδομένων από τις ταινίες, που τρέχει σαν thread
 */
public class ProcessData extends Thread {
    private ArrayList<String> csvLines;     // Το array με τις γραμμές του CSV

    // Τα hashmaps για τα δεδομένα που υπολογίζονται
    private Map<String, Integer> moviesInGenre = new HashMap<>();
    private Map<String, Integer> moviesInYear = new HashMap<>();
    private Map<String, Integer> wordsInMovies = new HashMap<>();

    private int start;      // Αρχικό σημείο διαβάσματος του array
    private int batchSize;  // Μέγεθος κομματιού που θα διαβαστεί το array

    public ProcessData(ArrayList<String> csvLines, int start, int batchSize)
    {
        this.csvLines = csvLines;
        this.start = start;
        this.batchSize = batchSize;
    }

    public Map<String, Integer> getMoviesInGenre() {
        return moviesInGenre;
    }

    public Map<String, Integer> getMoviesInYear() {
        return moviesInYear;
    }

    public Map<String, Integer> getWordsInMovies() {
        return wordsInMovies;
    }

    /**
     * Εκκίνηση του thread
     */
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " started. Επεξεργασία " + batchSize + " γραμμών");
        processLines();
        System.out.println(Thread.currentThread().getName() + " finished");
    }

    /**
     * Διάβασμα των γραμμών και υπολογισμός δεδομένων
     */
    private void processLines() {
        // Διάβασμα της κάθε γραμμής
        for (int i=start; i<(start + batchSize); i++) {
            // Διαβάζει την γραμμή του CSV και παίρνει τα δεδομένα
            // της ταινίας σε αντίστοιχο Movie αντικείμενο
            Movie movie = getMovie(csvLines.get(i));

            // Υπολογισμός δεδομένων
            calcGenreInMovie(movie);
            calcMoviesInYear(movie.getYear());
            calcWordsInMovies(movie.getTitle());
        }
    }

    /**
     * Βρίσκει ποιες κατηγορίες υπάρχουν στην ταινία και προσθέτει την
     * κάθε εμφάνιση κατηγορίας, στο αντίστοιχο hashmap που κρατάει
     * πόσες φορές εμφανίζεται η κάθε μία
     *
     * @param movie
     */
    private void calcGenreInMovie(Movie movie) {
        // Για κάθε κατηγορία
        for(String genre : movie.getGenres()) {
            // Αν η κατηγορία υπάρχει ήδη σαν key στο hashmap
            if(moviesInGenre.containsKey(genre)) {
                // Αυξάνει τον αριθμό εμφάνισης
                int sum = moviesInGenre.get(genre) + 1;
                moviesInGenre.put(genre, sum);
            } else { // Αν η κατηγορία δεν υπάρχει ήδη, την προσθέτει
                moviesInGenre.put(genre, 1);
            }
        }
    }

    /**
     * Προσθέτει την ύπαρξη της ταινίας στο αντίστοιχο έτος της
     *
     * @param year
     */
    private void calcMoviesInYear(String year) {
        // Αν στην ταινία δεν υπάρχει η πληροφορία του έτους
        if(year.equals("0")) {
            return;
        }

        // Αν το έτος υπάρχει σαν key στο hashmap
        if(moviesInYear.containsKey(year)) {
            // Αυξάνει τον αριθμό εμφάνισης
            int sum = moviesInYear.get(year) + 1;
            moviesInYear.put(year, sum);
        } else { // Αν το έτος δεν υπάρχει ήδη, το προσθέτει
            moviesInYear.put(year, 1);
        }
    }

    /**
     * Βρίσκει τον αριθμό εμφάνισης λέξεων στους τίτλους των ταινιών
     *
     * @param title
     */
    private void calcWordsInMovies(String title) {
        // Παίρνει array με λέξεις που υπάρχουν στον τίτλο,
        // χρησιμοποιώντας regex
        String[] words = Pattern.compile("\\b(?:\\w|-)+\\b")
                .matcher(title)
                .results()
                .map(MatchResult::group)
                .toArray(String[]::new);

        // Ελέγχει κάθε λέξη
        for(String word : words) {
            // Μετατρέπει όλες τις λέξεις σε πεζά, για να μην εμφανίζονται
            // σε διαφορετικές εκδόσεις
            word = word.toLowerCase();

            if(word.length()>1) {
                // Αν η λέξει υπάρχει σαν key στο hashmap
                if(wordsInMovies.containsKey(word)) {
                    // Αυξάνει τον αριθμό εμφάνισης
                    int sum = wordsInMovies.get(word) + 1;
                    wordsInMovies.put(word, sum);
                } else { // Αν η λέξη δεν υπάρχει ήδη, την προσθέτει
                    wordsInMovies.put(word, 1);
                }
            }
        }
    }

    /**
     * Βρίσκει την λέξη στον τίτλο
     *
     * @param title
     * @return
     */
    private int getYearFromTitle(String title) {
        // Αν η λέξη που βρίσκει δεν είναι αριθμός,
        // σημαίνει ότι στον τίτλο δεν υπάρχει έτος
        try {
            // Παίρνει το substring που βρίσκεται στο τέλος του τίτλου (όπου βρίσκεται το έτος)
            return Integer.parseInt(title.substring(title.length() - 5, title.length() - 1));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Διάβασμα μιας γραμμής του CSV και σπάσιμο των δεδομένων που υπάρχουν σ' αυτήν
     * Τα δεδομένα περνάνε σε αντικείμενο της κλάσης Movie
     * ,
     * @param line
     * @return movie
     */
    private Movie getMovie(String line) {
        // Παίρνει σε array τα κομμάτια της γραμμής που χωρίζονται με ","
        String[] lineFields = line.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

        // Διάβασμα του τίτλου και αφαίρεση των εισαγωγικών που υπάρχουν σε κάποιους
        // από αυτούς
        String title = lineFields[1].replace("\"", "");

        // Διαβάζει το έτος
        int year = getYearFromTitle(title);

        // Αφαιρεί το έτος από τον τίτλο, για να μείνει σκέτος αυτός
        title = title.replace(" (" + String.valueOf(year) + ")", "");

        // Δημιουργία του αντικειμένου movie
        Movie movie = new Movie(Long.parseLong(lineFields[0]), title, String.valueOf(year));

        // Διάβασμα των κατηγοριών της ταινίας και προσθήκη αυτών
        // στο αντικείμενο movie
        for (String genre : lineFields[2].split("[|]")) {
            movie.addGenre(genre);
        }

        return movie;
    }
}
