/**
 * 
 */
package konopolis;

/**
 * @author natha
 *
 */
public class Movie {
	private String title;
	private String description;
	private String []  genre;
	private String dateShow;
	private String director;
	private String [] casting;
	private int time;
	private String lang;
	private double price;	
	
	public Movie(String titre, String description, String[] genre, String dateShow, String director, String[] casting,int time, String lang,double price) {
		title = titre;
		this.description = description;
		this.genre = genre;
		this.dateShow = dateShow;
		this.director = director;
		this.casting = casting;
		this.time = time;
		this.lang = lang;
		this.price = price;
	}
	
	public String getTitre() {
		return title;
	}
	
	public void setTitre(String titre) {
		title = titre;
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
	
	public String getDateSeance() {
		return dateShow;
	}
	
	public void setDateSeance(String dateShow) {
		this.dateShow = dateShow;
	}
	
	public String getReal() {
		return director;
	}
	
	public void setReal(String director) {
		this.director = director;
	}
	
	public String[] getCasting() {
		return casting;
	}
	
	public void setCasting(String[] casting) {
		this.casting = casting;
	}
	public int getTemps() {
		return time;
	}
	
	public void setTemps(int time) {
		this.time = time;
	}
	
	public String getLangue() {
		return lang;
	}
	
	public void setLangue(String lang) {
		this.lang = lang;
	}
	
	public double getPrix() {
		return price;
	}
	
	public void setPrix(double price) {
		this.price = price;
	}	

}
