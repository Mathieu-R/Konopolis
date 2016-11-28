<<<<<<< HEAD
	package src.konopolis;
	
	import java.time.LocalDate;
=======
package src.konopolis.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1

/**
 * @author Sebastien.H - Groupe 3
 */
<<<<<<< HEAD
public class Movie {
	private String title;
	private String description;
	private String [] genre;
	private LocalDate dateSeanceDebut;
	private LocalDate dateSeanceFin;
=======

public class Movie {
	private int id = 0;
	private String title;
	private String description;
	private String [] genres;
    private ArrayList<Show> shows = new ArrayList<Show>();
>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1
	private String director;
	private String [] casting;
	private int time;
	private String language;
<<<<<<< HEAD
	
	/* constructor */
	public Movie(String title, String description, String[] genre, LocalDate dateSeanceDebut, LocalDate dateSeanceFin, String director, String[] casting,
                 int time, String language) {
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.dateSeanceDebut = dateSeanceDebut;
		this.dateSeanceFin = dateSeanceFin;
=======
    private double price;
	
	/* constructor */
	public Movie(int id, String title, String description, String[] genres, ArrayList<Show> shows, String director, String[] casting,
				 int time, String language) {
        this.id = id;
		this.title = title;
		this.description = description;
		this.genres = genres;
		this.shows = shows;
>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1
		this.director = director;
		this.casting = casting;
		this.time = time;
		this.language = language;
	}

	
	/* All Getters and Setters */
<<<<<<< HEAD
	
=======

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1
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

<<<<<<< HEAD
	public String[] getGenre() {
		return genre;
	}

	public void setGenre(String[] genre) {
		this.genre = genre;
	}

	public LocalDate getDateSeanceDebut() {
		return dateSeanceDebut;
	}

	public void setDateSeanceDebut(LocalDate dateSeanceDebut) {
		this.dateSeanceDebut = dateSeanceDebut;
	}

	public LocalDate getDateSeanceFin() {
		return dateSeanceFin;
	}

	public void setDateSeanceFin(LocalDate dateSeanceFin) {
		this.dateSeanceFin = dateSeanceFin;
=======
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
>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1
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
<<<<<<< HEAD
	

=======

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
>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1
}
