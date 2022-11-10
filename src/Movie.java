import java.util.ArrayList;

/**
 * Κλάση με τα δεδομένα της κάθε ταινίας
 */
public class Movie {
    private long movieId;
    private String title;
    private String year;
    private ArrayList<String> genres = new ArrayList<>();

    public Movie(long movieId, String title, String year) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public String getYear() {
        return year;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", year='" + year + '\'' +
                ", genres=" + genres +
                '}';
    }
}
