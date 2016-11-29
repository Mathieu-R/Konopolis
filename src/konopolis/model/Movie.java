package src.konopolis.model;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Sebastien.H - Groupe 3
 */

public class Movie {
	private int id = 0;
	private String title;
	private String description;
	private String [] genres;
    private ArrayList<src.konopolis.model.Show> shows = new ArrayList<Show>();
	private String director;
	private String [] casting;
	private int time;
	private String language;
    private double price;
	
	/* constructor */
	public Movie(int id, String title, String description, String[] genres, ArrayList<Show> shows, String director, String[] casting,
				 int time, String language, double price) {
        this.id = id;
		this.title = title;
		this.description = description;
		this.genres = genres;
		this.shows = shows;
		this.director = director;
		this.casting = casting;
		this.time = time;
		this.language = language;
		this.price = price;
	}

	
	/* All Getters and Setters */

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String[] getGenres() {
		return genres;
	}

	public void setGenres(String[] genres) {
		this.genres = genres;
	}

	public ArrayList<Show> getShows() {
		return shows;
	}

	public void setShows(ArrayList<Show> shows) {
		this.shows = shows;
	}

	public String getDirector() {
		return director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String[] getCasting() {
		return casting;
	}

	public void setCasting(String[] casting) {
		this.casting = casting;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (!title.equals(movie.title)) return false;
        return description.equals(movie.description);

    }

    @Override
    public int hashCode() {
        int result = title.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", genres=" + Arrays.toString(genres) +
                ", shows=" + shows +
                ", director='" + director + '\'' +
                ", casting=" + Arrays.toString(casting) +
                ", time=" + time +
                ", language='" + language + '\'' +
                ", price=" + price +
                '}';
    }
}
