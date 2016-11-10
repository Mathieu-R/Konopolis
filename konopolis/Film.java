package konopolis;

public class Film  {
	private String Titre;
	private String description;
	private String []  genre;
	private String dateSeance;
	private String real;
	private String [] casting;
	private int temps;
	private String langue;
	private double prix;	
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
	public String getDateSeance() {
		return dateSeance;
	}
	public void setDateSeance(String dateSeance) {
		this.dateSeance = dateSeance;
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
	public double getPrix() {
		return prix;
	}
	public void setPrix(double prix) {
		this.prix = prix;
	}	
		public Film(String titre, String description, String[] genre, String dateSeance, String real, String[] casting,
			int temps, String langue,double prix) {
		super();
		Titre = titre;
		this.description = description;
		this.genre = genre;
		this.dateSeance = dateSeance;
		this.real = real;
		this.casting = casting;
		this.temps = temps;
		this.langue = langue;
		this.prix = prix;
	}
}
