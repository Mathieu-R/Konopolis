	package src.konopolis;
	
	import java.time.LocalDate;

/**
 * @author Sebastien.H - Groupe 3
 */
public class Movie {
	private String title;
	private String description;
	private String [] genre;
	private LocalDate dateSeanceDebut;
	private LocalDate dateSeanceFin;
	private String director;
	private String [] casting;
	private int time;
	private String language;
	
	/* constructor */
	public Movie(String title, String description, String[] genre, LocalDate dateSeanceDebut, LocalDate dateSeanceFin, String director, String[] casting,
                 int time, String language) {
		this.title = title;
		this.description = description;
		this.genre = genre;
		this.dateSeanceDebut = dateSeanceDebut;
		this.dateSeanceFin = dateSeanceFin;
		this.director = director;
		this.casting = casting;
		this.time = time;
		this.language = language;
	}

	
	/* All Getters and Setters */
	
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
	

}
