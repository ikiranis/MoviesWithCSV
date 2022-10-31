import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int maxThreads = 2;    // Μέγιστος αριθμός threads, σε δυνάμεις του 2
    private static ProcessData[] processes;

    private static Map<String, Integer> moviesInGenre;
    private static Map<Integer, Integer> moviesInYear;
    private static Map<String, Integer> wordsInMovies;

    /**
     * Εκκίνηση όλων των threads, περνώντας τις αντίστοιχες παραμέτρους δεδομένων σε κάθε ένα
     *
     * @param csvLines
     * @param batchSize
     */
    private static void startThreads(ArrayList<String> csvLines, int batchSize) {
        for(int i=0; i<processes.length; i++) {
            processes[i] = new ProcessData(csvLines, i * batchSize, batchSize);
            processes[i].start();
        }
    }

    /**
     * Αναμονή από το thread της main, για να τερματίσουν όλα τα threads
     */
    private static void waitThreads() {
        for (ProcessData process: processes) {
            try {
                process.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void addInGenres(Map<String, Integer> genres) {
        genres.entrySet()
                .forEach(x -> {
                    if(moviesInGenre.containsKey(x.getKey())) {
                        int newValue = moviesInGenre.get(x.getKey()) + x.getValue();

                        moviesInGenre.put(x.getKey(), newValue);
                    } else {
                        moviesInGenre.put(x.getKey(), x.getValue());
                    }
                });
    }

    private static void addInYears(Map<Integer, Integer> years) {
        years.entrySet()
                .forEach(x -> {
                    if(moviesInYear.containsKey(x.getKey())) {
                        int newValue = moviesInYear.get(x.getKey()) + x.getValue();

                        moviesInYear.put(x.getKey(), newValue);
                    } else {
                        moviesInYear.put(x.getKey(), x.getValue());
                    }
                });
    }

    private static void addInWords(Map<String, Integer> words) {
        words.entrySet()
                .forEach(x -> {
                    if(wordsInMovies.containsKey(x.getKey())) {
                        int newValue = wordsInMovies.get(x.getKey()) + x.getValue();

                        wordsInMovies.put(x.getKey(), newValue);
                    } else {
                        wordsInMovies.put(x.getKey(), x.getValue());
                    }
                });
    }

    public static void main(String[] args) {
        ReadCSV readCSV = new ReadCSV("movies.csv");

        ArrayList<String> csvLines = readCSV.readFile();

        // Δοκιμή επεξεργασίας με διαφορετικό πλήθος threads
        for (int i=0; i<=maxThreads; i++) {
            int threadsNumber = (int) Math.pow(2, i);  // Πλήθος threads σε δυνάμεις του 2

            // Αρχικοποίηση hashmaps
            moviesInGenre = new HashMap<>();
            moviesInYear = new HashMap<>();
            wordsInMovies = new HashMap<>();

            // Αρχικοποίηση του array των threads με την κλάση HammingCalculator
            processes = new ProcessData[threadsNumber];

            // Αρχικοποίηση του χρόνου που αρχίζει η επεξεργασία
            long start = System.currentTimeMillis();

            startThreads(csvLines, (csvLines.size() / threadsNumber));

            waitThreads();

            // Τερματισμός του χρόνου επεξεργασίας
            long end = System.currentTimeMillis();

            for (ProcessData process: processes) {
                addInGenres(process.getMoviesInGenre());
                addInYears(process.getMoviesInYear());
                addInWords(process.getWordsInMovies());
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

            System.out.println("\nΧρονική διάρκεια επεξεργασίας: " + (end - start) + "msec");
        }

    }
}