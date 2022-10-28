import java.util.ArrayList;

public class Movie {
    private long movieId;
    private String title;
    private ArrayList<Genre> genres = new ArrayList<>();

    public Movie(long movieId, String title) {
        this.movieId = movieId;
        this.title = title;
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

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    public void addGenre(Genre genre) {
        genres.add(genre);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "movieId=" + movieId +
                ", title='" + title + '\'' +
                ", genres=" + genres +
                '}';
    }
}