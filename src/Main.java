import java.util.*;

public class Main {
    private static final int maxThreads = 3;    // Μέγιστος αριθμός threads, σε δυνάμεις του 2
    private static ProcessData[] processes;

    private static Map<String, Integer> moviesInGenre;
    private static Map<String, Integer> moviesInYear;
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

    private static void addNewProcessData(Map<String, Integer> newData, Map<String, Integer> data) {
        newData.entrySet()
            .forEach(x -> {
                if(data.containsKey(x.getKey())) {
                    int newValue = data.get(x.getKey()) + x.getValue();

                    data.put(x.getKey(), newValue);
                } else {
                    data.put(x.getKey(), x.getValue());
                }
            });
    }

    private static void addNewDataFromProcesses() {
        // Αρχικοποίηση hashmaps
        moviesInGenre = new HashMap<>();
        moviesInYear = new HashMap<>();
        wordsInMovies = new HashMap<>();

        for (ProcessData process: processes) {
            addNewProcessData(process.getMoviesInGenre(), moviesInGenre);
            addNewProcessData(process.getMoviesInYear(), moviesInYear);
            addNewProcessData(process.getWordsInMovies(), wordsInMovies);
        }
    }

    private static void printMoviesInGenre() {
        System.out.println("\nΤαινίες που βρέθηκαν σε κάθε κατηγορία");
        System.out.println("--------------------------------------");
        for(Map.Entry<String, Integer> genre : moviesInGenre.entrySet()) {
            if(!genre.getKey().contains("(no genres listed)")) {
                System.out.printf("Στην κατηγορία %s, βρέθηκαν %s ταινίες\n", genre.getKey(), genre.getValue());
            }
        }

        System.out.printf("\nΧωρίς κατηγορία βρέθηκαν %s ταινίες\n", moviesInGenre.get("(no genres listed)"));
    }

    private static void printMoviesInYear() {
        System.out.println("\nΤαινίες που βρέθηκαν σε κάθε έτος");
        System.out.println("---------------------------------");
        for(Map.Entry<String, Integer> year : moviesInYear.entrySet()) {
            System.out.printf("Το έτος %s, βρέθηκαν %s ταινίες\n", year.getKey(), year.getValue());
        }
    }

    private static void printWordsInMovies() {
        Map<String, Integer> sortedWords = new LinkedHashMap<>();
        wordsInMovies.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .forEachOrdered(x -> sortedWords.put(x.getKey(), x.getValue()));


        System.out.println("\n10 πιο συχνές λέξεις σε τίτλους ταινιών");
        System.out.println("---------------------------------------");
        int counter = 0;
        for(Map.Entry<String, Integer> word : sortedWords.entrySet()) {
            System.out.printf("Η λέξη \"%s\", βρέθηκε %s φορές\n", word.getKey(), word.getValue());
            counter += word.getValue();
        }

        System.out.println("\nΣύνολο εμφανίσεων των πιο συχνών λέξεων: " + counter);
    }

    public static void main(String[] args) {
        ReadCSV readCSV = new ReadCSV("movies.csv");

        ArrayList<String> csvLines = readCSV.readFile();

        // Δοκιμή επεξεργασίας με διαφορετικό πλήθος threads
        for (int i=0; i<=maxThreads; i++) {
            int threadsNumber = (int) Math.pow(2, i);  // Πλήθος threads σε δυνάμεις του 2

            // Αρχικοποίηση του array των threads με την κλάση HammingCalculator
            processes = new ProcessData[threadsNumber];

            System.out.println("\n==================================================================");
            System.out.println("Επεξεργασία " + csvLines.size() + " γραμμών, με "
                    + threadsNumber
                    + ((threadsNumber>1) ? " threads" : " thread")
                    + "\n");

            // Αρχικοποίηση του χρόνου που αρχίζει η επεξεργασία
            long start = System.currentTimeMillis();

            startThreads(csvLines, (csvLines.size() / threadsNumber));

            waitThreads();

            // Τερματισμός του χρόνου επεξεργασίας
            long end = System.currentTimeMillis();

            addNewDataFromProcesses();

            printMoviesInGenre();

            printMoviesInYear();

            printWordsInMovies();

            System.out.println("\nΧρονική διάρκεια επεξεργασίας: " + (end - start) + "msec");
        }

    }
}