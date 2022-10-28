import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ReadCSV {
    private String filename;

    private ArrayList<String> csvLines = new ArrayList<>();
    private ArrayList<Movie> movies = new ArrayList<>();

    public ReadCSV(String filename) {
        this.filename = filename;
    }

    public ArrayList<String> getCsvLines() {
        return csvLines;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void readFile() {
        try {
            File file = new File(filename);
            Scanner myReader = new Scanner(file);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if (!data.contains("movieId,title,genres")) {
                    csvLines.add(data);
                }
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Problem with file");
            e.printStackTrace();
        }
    }

    public void readCSVLines() {
        for(String line : csvLines) {
            String[] lineFields = line.split(",(?=([^\\\"]*\\\"[^\\\"]*\\\")*[^\\\"]*$)");
            Movie movie = new Movie(Long.parseLong(lineFields[0]), lineFields[1]);

            if (!lineFields[2].contains("(no genres listed)")) {
                String[] genres = lineFields[2].split("[|)]");

                for(String genre : genres) {
                    movie.addGenre(new Genre(genre));
                }
            }

            movies.add(movie);
        }
    }


}
