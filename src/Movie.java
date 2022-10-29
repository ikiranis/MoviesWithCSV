import java.util.ArrayList;

public class Movie {
    private long movieId;
    private String title;
    private int year;
    private ArrayList<String> genres = new ArrayList<>();

    public Movie(long movieId, String title, int year) {
        this.movieId = movieId;
        this.title = title;
        this.year = year;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getGenres() {
        return genres;
    }

    public void addGenre(String genre) {
        genres.add(genre);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
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
