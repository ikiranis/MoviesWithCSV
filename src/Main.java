import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final int maxThreads = 0;    // Μέγιστος αριθμός threads, σε δυνάμεις του 2
    private static ProcessData[] processes;

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

    public static void main(String[] args) {
        ReadCSV readCSV = new ReadCSV("movies.csv");

        ArrayList<String> csvLines = readCSV.readFile();

        // Δοκιμή επεξεργασίας με διαφορετικό πλήθος threads
        for (int i=0; i<=maxThreads; i++) {
            int threadsNumber = (int) Math.pow(2, i);  // Πλήθος threads σε δυνάμεις του 2

            // Αρχικοποίηση του array των threads με την κλάση HammingCalculator
            processes = new ProcessData[threadsNumber];

            // Αρχικοποίηση του χρόνου που αρχίζει η επεξεργασία
            long start = System.currentTimeMillis();

            startThreads(csvLines, (csvLines.size() / threadsNumber));

            waitThreads();

            // Τερματισμός του χρόνου επεξεργασίας
            long end = System.currentTimeMillis();

            System.out.println("\nMovies in Genre");
            for(Map.Entry<String, Integer> genre : processes[0].getMoviesInGenre().entrySet()) {
                System.out.println(genre.getKey() + ": " + genre.getValue());
            }

            System.out.println("\nMovies in Year");
            for(Map.Entry<Integer, Integer> year : processes[0].getMoviesInYear().entrySet()) {
                System.out.println(year.getKey() + ": " + year.getValue());
            }


            Map<String, Integer> sortedWords = new HashMap<>();
            processes[0].getWordsInMovies().entrySet()
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