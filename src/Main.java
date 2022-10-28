public class Main {
    public static void main(String[] args) {
        ReadCSV readCSV = new ReadCSV("movies.csv");

        readCSV.readFile();

        readCSV.readCSVLines();

        for (Movie movie : readCSV.getMovies()) {
            System.out.println(movie);
        }
    }
}