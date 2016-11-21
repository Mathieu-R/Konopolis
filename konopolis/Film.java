	package konopolis;
	
	import java.util.Date;
/**
 * 
 * @author Sebastien.H - Groupe 3
 *
 */
public class Film  {
	private String Titre;
	private String description;
	private String []  genre;
	private Date dateSeanceDebut;
	private Date dateSeanceFin;
	private String real;
	private String [] casting;
	private int temps;
	private String langue;
	
	/* constructor */
	public Film(String titre, String description, String[] genre, Date dateSeanceDebut, Date dateSeanceFin, String real, String[] casting,
			int temps, String langue) {
		this.Titre = titre;
		this.description = description;
		this.genre = genre;
		this.dateSeanceDebut = dateSeanceDebut;
		this.dateSeanceFin = dateSeanceFin;
		this.real = real;
		this.casting = casting;
		this.temps = temps;
		this.langue = langue;
	}

	
	/* All Getters and Setters */
	
	public String getTitre() {
		return Titre;
	}

	public void setTitre(String titre) {
		Titre = titre;
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

	public Date getDateSeanceDebut() {
		return dateSeanceDebut;
	}

	public void setDateSeanceDebut(Date dateSeanceDebut) {
		this.dateSeanceDebut = dateSeanceDebut;
	}

	public Date getDateSeanceFin() {
		return dateSeanceFin;
	}

	public void setDateSeanceFin(Date dateSeanceFin) {
		this.dateSeanceFin = dateSeanceFin;
	}

	public String getReal() {
		return real;
	}

	public void setReal(String real) {
		this.real = real;
	}

	public String[] getCasting() {
		return casting;
	}

	public void setCasting(String[] casting) {
		this.casting = casting;
	}

	public int getTemps() {
		return temps;
	}

	public void setTemps(int temps) {
		this.temps = temps;
	}

	public String getLangue() {
		return langue;
	}

	public void setLangue(String langue) {
		this.langue = langue;
	}
	

}
