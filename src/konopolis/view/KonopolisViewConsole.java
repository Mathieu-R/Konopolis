package src.konopolis.view;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Movie;
import src.konopolis.model.Room;
import src.konopolis.model.SeatTakenException;
import src.konopolis.model.SeatUnknownException;
import src.konopolis.model.Show;

public class KonopolisViewConsole extends KonopolisView implements Observer{
	Scanner sc;
	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private String type;
	private LocalDateTime show_start;
	
	public KonopolisViewConsole(KonopolisModel model, KonopolisController control){
		super(model,control);
        sc = new Scanner(System.in);
        init();
	}
	
	public void init() {
        int step1 = 0;

		boolean quit = false;
		show("**************************");
		show("BIENVENUE DANS KONOPOLIS");
		show("**************************");
		do {
		    try {
                show("Faites votre choix:");
                show("1.Liste des films 2.Configuration 3.Fermer");
                step1 = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Come on ! Isn't it easy to choose a number between 1 and 3 ?");
                sc.next();
            }
        } while (step1 < 1 || step1 > 3);

		switch (step1) {
			case 1:
                showMoviesList(); // show the list of movies and the user can choose one
                System.out.println("1.Acheter une place 2.Description 3.Fermer\n");
                int step2 = sc.nextInt();
        			
                switch (step2) {
                    case 1: // Booking
                        control.retrieveMovie(movie_id); // get the movie chosen from db
                        showShowsList(); // show the list of shows for the movie chosen. The user can choose a show.
                        control.retrieveRoom(movie_id, room_id, show_start); // get the room from db for the show chosen
                        showRoomMap(); // show the mapping of the room
                        showTypeOfPeople(); // show all the type of people (to know the reduction to apply)

                        System.out.println("Sélectionner votre place avec x,y\n");
                        String[] chosenSeat = sc.nextLine().split(",");

                        control.addCustomer(
                            Integer.parseInt(chosenSeat[0].trim()),
                            Integer.parseInt(chosenSeat[1].trim()),
                            control.getCustomers_al().size(),
                            room_id,
                            type,
                            movie_id,
                            control.getMovies_al().get(movie_id - 1).getShows().get(show_id).getShow_start()
                            );

                        update(null,null);

                    break;

                    case 2 : // Description
                        control.retrieveMovie(movie_id);
                        show(control.getMovies_al().get(0).getDescription()+"\n");
                        show("---------------------------------------------------------");
                        show(control.getMovies_al().get(0).getDirector()+"\n");
                        for(int i=0;i<control.getMovies_al().get(0).getCasting().size();i++){
                            show(control.getMovies_al().get(0).getCasting().get(i));
                        }
                        show("---------------------------------------------------------");
                        show("Langue: " + control.getMovies_al().get(0).getLanguage());
                        show("Genres:");
                        for(int i = 0 ; i < control.getMovies_al().get(0).getGenres().size() ; i++){
                            show("- " + control.getMovies_al().get(0).getGenres().get(i));
                        }
                        show("---------------------------------------------------------");
                        show(control.getMovies_al().get(0).getTime()+" minutes");
                        show(control.getMovies_al().get(0).getPrice()+"€\n");
                        break;

                    case 3 : // Exit
                        System.exit(0);
                    break;
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

                             System.out.println("Quelle est le r�alisateur ?");
                             director=sc.nextLine();

                             System.out.println("Qui joue dans le film ? (acteur1,acteur2,...)");
                             String repCast=sc.nextLine();
                             casting=repCast.split(",");

                             System.out.println("Combien de temps dure le film ? (en minutes)");
                             String repTime=sc.nextLine();
                             time=Integer.parseInt(repTime);

                             System.out.println("Quelle langue ?");
                             language=sc.nextLine();

                             System.out.println("Quelle est le prix ? (��.cc)");
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
	
    public void showMoviesList() {
        show("Liste des films:");
        for (Map.Entry<Integer, String> movieEntry: control.retrieveAllMoviesTitles().entrySet()) {
            show(movieEntry.getKey() + ") " + movieEntry.getValue());
        }
        showInline("Sélectionnez un film :");
        do {
            try {
                movie_id = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Ce film n'existe pas, choisissez un film de la liste !");
                sc.next();
            }
        } while (movie_id < 1 || movie_id > control.retrieveAllMoviesTitles().entrySet().size());

    }

    public void showShowsList() {
        System.out.println("Sélectionnez la séance: "); // We select the show
        for (Movie movie : control.getMovies_al()) {
            if (movie.getId() == movie_id) {
                for (int i = 0 ; i < movie.getShows().size() ; i++) {
                    System.out.println((i+1) + ") " + movie.getShows().get(i).getShow_start()); // Show the list of shows
                }

                show_id = sc.nextInt(); // Id of the show
                room_id = movie.getShows().get(show_id - 1).getRoom_id(); // We get the room_id
                show_start = movie.getShows().get(show_id - 1).getShow_start(); // We get the show_start
            }
        }
    }

    public void showRoomMap() {
        for (Room room : control.getRooms_al()) {
            if (room.getId() == room_id) {
                System.out.println("Salle choisie:");
                room.displayRoom();
            }
        }
    }

    public void showTypeOfPeople() {
        System.out.println("Quelle type de personne êtes-vous ?\n");
        for (String type : control.retrieveTypes()) { // get all the types
            System.out.println("> " + type); // show it the the console
        }
        type = sc.nextLine(); // get the type entered by the user
        control.checkType(type); // check if the type exist
    }

    /**
     * Show a string in console
     * @param str
     */
    public void show(String str) {
	    System.out.println(str);
    }

    /**
     * Show a inline string in console
     * @param str
     */
    public void showInline(String str) {
        System.out.print(str);
    }


    /**
     * Clear the console
     * H => move to top of the screen
     * 2J => clear entire screen
     */
    public void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

	@Override
	public void update(Observable obs, Object obj) {
		init();
	}
}

