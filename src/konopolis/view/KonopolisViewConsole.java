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
	
	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private LocalDateTime show_start;
	
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
		System.out.println("1.Films  2.Configuration 3.Fermer\n");
		int etape1 = sc.nextInt();
		switch(etape1){
		/*
		 * On a la liste des films de la db
		 */
		
			case 1: System.out.println("Sélectionnez un film :\n");
	        		
				System.out.println("Liste des films:");
				for (Map.Entry<Integer, String> movieEntry: control.retrieveAllMoviesTitles().entrySet()) {
					System.out.println(movieEntry.getKey() + ") " + movieEntry.getValue());
				}
					
	        		movie_id = sc.nextInt();
	        		
	        		while(!quit) {
	        			
        			System.out.println("1.Acheter place 2.Description 3.Fermer\n");
        			int etape2 = sc.nextInt();
        			
        			switch(etape2) {
        				
        					case 1: control.retrieveMovie(movie_id);
        							
        							System.out.println("Sélectionnez la séance: "); // We select the show
        							
        							for (Movie movie : control.getMovies_al()) {
        								if (movie.getId() == movie_id) {
        									for (int i = 0 ; i < movie.getShows().size() ; i++) {
        										System.out.println((i+1) + ") " + movie.getShows().get(i).getShow_start()); // Show the list of shows
        									}
        									
        									show_id = sc.nextInt(); // Id of the show
    										room_id = movie.getShows().get(show_id - 1).getRoom_id(); // We get the room_id that correspond to the show
    										show_start = movie.getShows().get(show_id - 1).getShow_start(); // We get the show_start 
        								}
        							}
        							
									control.retrieveRoom(movie_id, room_id, show_start);
									
									for (Room room : control.getRooms_al()) {
										if (room.getId() == room_id) {
											System.out.println("Salle choisie:");
											room.displayRoom();
										}
									}
        							
        							System.out.println("Quelle type de personne êtes vous ?\n");
        							System.out.print("> Junior\n" +
        											"> Senior\n" +
        											"> Etudiant\n" +
        											"> VIP\n");
        								
        							String type = sc.nextLine();
        	
        							System.out.println("Sélectionner votre place avec x,y\n");
        							String [] chosenSeat = sc.nextLine().split(",");
        							
        							control.addCustomer(
        									Integer.parseInt(chosenSeat[0]),
        									Integer.parseInt(chosenSeat[1]),
        									control.getCustomers_al().size(),
        									room_id,
        									type,
        									movie_id, 
        									control.getMovies_al().get(movie_id - 1).getShows().get(show_id).getShow_start()
        									);
        							
        							update(null,null);
        								
        					break;
        					
        					case 2 : control.retrieveMovie(movie_id);
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
        							System.out.println(control.getMovies_al().get(0).getTime()+" minutes\n");
        							System.out.println(control.getMovies_al().get(0).getPrice()+" €\n");

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

