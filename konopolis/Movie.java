	package konopolis;
	
	import java.time.LocalDate;

/**
 * @author Sebastien.H - Groupe 3
 */
public class Movie {
	private String title;
	private String description;
	private String [] kind;
	private LocalDate dateShowStart;
	private LocalDate dateShowEnd;
	private String director;
	private String [] casting;
	private int time;	
	private String language;
	
	/* constructor */
	public Movie(String title, String description, String[] kind, LocalDate dateShowStart, LocalDate dateShowEnd, String director, String[] casting,
                 int time, String language) {
		this.title = title;
		this.description = description;
		this.kind = kind;
		this.dateShowStart = dateShowStart;
		this.dateShowEnd = dateShowEnd;
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

	public String[] getkind() {
		return kind;
	}

	public void setGenre(String[] kind) {
		this.kind = kind;
	}

	public LocalDate getDateShowStart() {
		return dateShowStart;
	}

	public void setDateSeanceDebut(LocalDate dateShowStart) {
		this.dateShowStart = dateShowStart;
	}

	public LocalDate getDateShowEnd() {
		return dateShowEnd;
	}

	public void setDateShowEnd(LocalDate dateShowEnd) {
		this.dateShowEnd = dateShowEnd;
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
