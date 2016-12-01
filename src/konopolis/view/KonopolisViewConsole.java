package src.konopolis.view;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Scanner;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Movie;
import src.konopolis.model.Room;
import src.konopolis.model.SeatTakenException;
import src.konopolis.model.SeatUnknownException;
import src.konopolis.model.Show;

public class KonopolisViewConsole extends KonopolisView implements Observer{
	
	public KonopolisViewConsole(KonopolisModel model,KonopolisController control){
		super(model,control);
	}
	
	
	public void init(){
		boolean quit=false;
		Scanner sc = new Scanner(System.in);
		System.out.println("**************************");
		System.out.println("BIENVENUE DANS KONOPOLIS");
		System.out.println("**************************");
		System.out.println("Faites votre choix:");
		System.out.println("1.Films  2.Configuration 3.Fermer");
		int etape1 = sc.nextInt();
		switch(etape1){
		/*
		 * On a la liste des films de la db
		 */
		
			case 1: System.out.println("Sélectionnez un film :");
	        		
				System.out.println("Liste des films:");
				for (Map.Entry<Integer, String> movieEntry: control.retrieveAllMoviesTitles().entrySet()) {
					System.out.println(movieEntry.getKey() + ") " + movieEntry.getValue());
				}
					
	        		int idFilm =sc.nextInt();
	        		while(!quit){
	        			
	        		
        			System.out.println("1.Acheter place 2.Description 3.Fermer");
        			int etape2=sc.nextInt();
        			
        			switch(etape2){
        					case 1 : System.out.println("Quelle âge ?");
        					
        							int age=sc.nextInt();
        							String type;
        							if(age>=65){
        								type="Senior";
        							}else{
        								type="Simple";
        							}
        							System.out.println("Quelle séance ?");
        							control.retrieveMovie(idFilm);
        							for(int i=0;i<control.getMovies_al().get(idFilm).getShows().size();i++){
        							 System.out.println(i+" "+control.getMovies_al().get(idFilm).getShows().get(i).getShow_start());
        							}
        							int idShow=sc.nextInt();
        							System.out.println("Sélectionner votre place avec x,y");
        							String [] chosenSeat = sc.nextLine().split(",");
        							
        							control.retrieveCustomers(control.getMovies_al().get(idFilm).getShows().get(idShow).getRoom_id(),control.getMovies_al().get(idFilm).getShows().get(idShow).getMovie_id(),control.getMovies_al().get(idFilm).getShows().get(idShow).getShow_start());
        							
        							control.addCustomer(Integer.parseInt(chosenSeat[0]),Integer.parseInt(chosenSeat[1]), control.getCustomers_al().size(),control.getMovies_al().get(idFilm).getShows().get(idShow).getRoom_id(), type, control.getCustomers_al().size(), idFilm, control.getMovies_al().get(idFilm).getShows().get(idShow).getShow_start());
        							update(null,null );
        								
        					break;
        					
        					case 2 : control.retrieveMovie(idFilm);
        							System.out.println(control.getMovies_al().get(0).getDescription()+"\n");
        							System.out.println("---------------------------------------------------------");
        							System.out.println(control.getMovies_al().get(0).getDirector()+"\n");
        							for(int i=0;i<control.getMovies_al().get(0).getCasting().size();i++){
        								System.out.println(control.getMovies_al().get(0).getCasting().get(i));
        							}
        							System.out.println("---------------------------------------------------------");
        							System.out.println(control.getMovies_al().get(0).getLanguage()+"\n");
        							for(int i=0;i<control.getMovies_al().get(0).getGenres().size();i++){
        								System.out.println(control.getMovies_al().get(0).getGenres().get(i));
        							}
        							System.out.println("---------------------------------------------------------");
        							System.out.println(control.getMovies_al().get(0).getTime()+"\n");
        							System.out.println(control.getMovies_al().get(0).getPrice()+"\n");

        					break;
        					
        					case 3 : System.exit(0);
        					break;
        				}
        			}
        			
        			break;
					
					/*case 2 : System.out.println("1.Ajouter film");
							 int choix =sc.nextInt();
			
					switch(choix){
							
							 case 1:  
								 	String title;
									String description;
									String [] genres;
									String director;
									String [] casting;
									int time;
									String language;
								    double price;
								    ArrayList<Date> dates_show=new ArrayList<Date>();
								     
								     sc.nextLine();
									 System.out.println("Quelle est le titre ?");
								     title=sc.nextLine();
									 
									 
									 System.out.println("Donnez une desciption");
									 description=sc.nextLine();
									 
									 System.out.println("De quelles genres est le film (genre1,genre2,..)");
									 String repGenres=sc.nextLine();
									 genres=repGenres.split(",");
									 
									 boolean encore=true;
									 while(encore){
										 System.out.println("Quand (yyyy-MM-dd HH:mm:ss.SSSSSS)?");
										 String  show_start = sc.nextLine();
										 LocalDateTime temp = control.stringToLocalDateTime(show_start);
										 dates_show.add(temp);
										 System.out.println("Une autre ?");
										 System.out.println("1.oui 2.non");
										 int moreDate=sc.nextInt();
										 switch(moreDate){
									 	
										 case 1: break;
									 
									 
										 case 2: encore=false;
									 		break;
									 		
									 		default: System.out.println("Je n'ai pas compris");
									    		break;
									 	}
									 }
									 System.out.println("Quelle salle ?");
									 control.retrieveAllRooms();
									 for (int i=0;i<control.getRooms_al().size();i++){
										 System.out.println(control.getRooms_al().get(i).getId()+".Salle"+control.getRooms_al().get(i).getId());
									 }
									 int idRoom=sc.nextInt();
									 
									 System.out.println("Quelle est le réalisateur ?");
									 director=sc.nextLine();
									 
									 System.out.println("Qui joue dans le film ? (acteur1,acteur2,...)");
									 String repCast=sc.nextLine();
									 casting=repCast.split(",");
									 
									 System.out.println("Combien de temps dure le film ? (en minutes)");
									 String repTime=sc.nextLine();
									 time=Integer.parseInt(repTime);
									 
									 System.out.println("Quelle langue ?");
									 language=sc.nextLine();
									 
									 System.out.println("Quelle est le prix ? (€€.cc)");
									 String repPrice=sc.nextLine();
									 price=Double.parseDouble(repPrice);
									 
									 control.addMovie(control.retrieveAllMoviesTitles().size(), idRoom, title, description, director, dates_show, casting, time, language, price, genres);
									 
									 update(null, null);
								 break;
							*/ default:
								 System.exit(0);
								 break;
			
					case 3: System.exit(0);
					
					break;

			
        	
		}
		}
	


	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		init();
	}
}

