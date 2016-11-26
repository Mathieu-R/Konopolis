	package konopolis;
	
	import java.time.LocalDate;
	import java.util.ArrayList;
/**
 * @author Sebastien.H - Groupe 3
 */
public class Movie {
	private String title;
	private String description;
	private String [] kind;
	private ArrayList<ArrayList<DateShow>> DateShow	= new ArrayList<ArrayList<DateShow>>();

	private String director;
	private String [] casting;
	private int time;	
	private String language;
	private double price;
	/* constructor */
	public Movie(String title, String description, String[] kind, LocalDate dateShow, String director, String[] casting,
                 int time, String language, double price, ArrayList<ArrayList<konopolis.DateShow>> DateShow) {
		this.title = title;
		this.description = description;
		this.kind = kind;
		this.DateShow = DateShow;
		this.director = director;
		this.casting = casting;
		this.time = time;
		this.language = language;
		this.price = price;
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

    public ArrayList<ArrayList<DateShow>> getDateShow() {
        return DateShow;
    }

    public void setDateShow(ArrayList<ArrayList<DateShow>> DateShow) {
        this.DateShow = DateShow;
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


	public String[] getKind() {
		return kind;
	}


	public void setKind(String[] kind) {
		this.kind = kind;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}
	

}
