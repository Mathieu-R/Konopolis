/**
 * 
 */
package src.konopolis.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
/**
 * @author nathan
 *
 */
public class KonopolisView implements Observer {
	Scanner sc;
	
	private KonopolisController control;
	private KonopolisModel model;
	
	public KonopolisView(KonopolisModel model,KonopolisController control){
			this.control=control;
			this.model=model;
			update(null,null);
			sc=new Scanner(System.in);
			model.addObserver(this);
			init();
	}
	public KonopolisView(){
		init();
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		init();
	}
	
	private void init(){
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
	        		/*for (Entry<Integer, String> movieEntry: model.getDB().retrieveAllMoviesTitles().entrySet()) {
	        			System.out.println(movieEntry.getKey() + ") " + movieEntry.getValue());
	        		}
	        		*/
					for(int i =0;i< listMovies.size();i++){
						System.out.println(i+"."+listMovies.get(i).getTitle());
					}
					
	        		int idFilm =sc.nextInt();
	        		while(!quit){
        			System.out.println("1.Acheter place 2.Description 3.Fermer");
        			int etape2=sc.nextInt();
        			
        			
        			switch(etape2){
        					case 1 : System.out.println("Quelle séance ?");
        							for(int i=0;i<listShows.size();i++){
        							 if(listShows.get(i).contains(this.listShows,listMovies.get(idFilm))){
        								 System.out.println(i+"."+listShows.get(i).getShow_start()+" salle"+listShows.get(i).getRoom().getId());
        							 }
        							}
        							int idShow=sc.nextInt();
        							System.out.println("Sélectionner votre place avec x,y");
        							String [] chosenSeat = sc.nextLine().split(",");
						try {
							listShows.get(idShow).getRoom().giveSeat(Integer.parseInt(chosenSeat[0]),Integer.parseInt(chosenSeat[1]));
							quit=true;
						} catch (NumberFormatException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SeatUnknownException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (SeatTakenException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}	
        					break;
        					
        					case 2 : System.out.println(listMovies.get(idFilm).getDescription());
        							
        					break;
        					
        					case 3 : System.exit(0);
        					break;
        				}
        			}
        			
        			break;
					
					case 2 : System.out.println("1.Créer séance 2.Ajouter film 3.Ajouter salle");
							 int choix =sc.nextInt();
							 switch(choix){
							 case 1:	LocalDate day_start;
							 			LocalDateTime show_start;
							    		Movie movie;
							    		Room room;
							    		sc.nextLine();
							    		System.out.println("Quelle jour ? (yyyy,mm,dd)");
							    		String [] date =sc.nextLine().split(",");
							    		System.out.println("A quelle heure ? (hh:mm)");
							    		String [] hour = sc.nextLine().split(":");
							    		day_start=LocalDate.of(Integer.parseInt(date[0]),Integer.parseInt(date[1]),Integer.parseInt(date[2]));
							    		show_start=day_start.atTime(Integer.parseInt(hour[0]),Integer.parseInt(hour[1]));
							    		System.out.println("Séance de quel film ?");
							    		for(int i=0;i<listMovies.size();i++){
							    			System.out.println(i+"."+listMovies.get(i).getTitle());
							    		}
							    		int idMovie=sc.nextInt();
							    		
							    			movie=listMovies.get(idMovie);
							    		
							    		
							    		System.out.println("Dans quel salle ?");
							    		for(int i=0;i<listRooms.size();i++){
							    			System.out.println(i+".Salle "+listRooms.get(i).getId());
							    		}
							    		int idRoom=sc.nextInt();
							    		
							    		room=listRooms.get(idRoom);
							    		
							    		listShows.add(new Show(show_start,movie,room));
							    		update(null, null);
								 break;
							 case 2:  
								 	String title;
									String description;
									String [] genres;
									String director;
									String [] casting;
									int time;
									String language;
								    double price;
								     
								     sc.nextLine();
									 System.out.println("Quelle est le titre ?");
								     title=sc.nextLine();
									 
									 
									 System.out.println("Donnez une desciption");
									 description=sc.nextLine();
									 
									 System.out.println("De quelles genres est le film (genre1,genre2,..)");
									 String repGenres=sc.nextLine();
									 genres=repGenres.split(",");
									 
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
									 
									 listMovies.add(new Movie(title,description,genres,director,casting,time,language,price));	
									 update(null, null);
								 break;
							 case 3: int rows;
							 		 int sitsByRow;
							 		 System.out.println("Combien de rangées ?");
							 		 rows=sc.nextInt();
							 		 System.out.println("Combien de sièges par rangées ?");
							 		 sitsByRow=sc.nextInt();
							 		 listRooms.add(new Room(rows,sitsByRow));
							 		 System.out.println("Salle crée");
							 		update(null, null);
							 default:
								 System.exit(0);
								 break;
							 }
							 break;
							 default:
								 System.exit(0);
								 break;
			
					case 3: System.exit(0);
					
					break;
			/*case 3 : System.out.println("Donnez un titre");
					 String title=sc.nextLine();
					 System.out.println("Donnez une description");
					 String description=sc.nextLine();
					 System.out.println("Donnez les heures des séances");
					 //Créer un objet qui pouura contenir l'heure
					 System.out.println("Quelle est la durée du film (en minutes)");
					 int time=sc.nextInt();
					 System.out.println("Quelle est la langue ?");
					 //Affiche les langues déja existantes ou ajoute une nouvelle
					 System.out.println("Combien coute le film ?");
					 double price=sc.nextDouble();
					 break;
			*/
			
        	
		}
	}	
}
