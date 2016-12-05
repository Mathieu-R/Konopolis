package src.konopolis.view;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Movie;
import src.konopolis.model.Room;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

public class KonopolisViewConsole extends KonopolisView implements Observer{
	Scanner sc;
	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private String enteredType = "";
	private LocalDateTime show_start;
	
	public KonopolisViewConsole(KonopolisModel model, KonopolisController control){
		super(model,control);
        sc = new Scanner(System.in);
        init();
	}
	
	public void init() {
        int step1 = 0;

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
                moviesList();
                break;
            case 2:
                movieConfig();
                break;
            case 3:
                exit();
                break;
        }
    }

    private void moviesList() {
        int step2 = 0;

        showMoviesList(); // show the list of movies and the user can choose one
        do {
            try {
                show("1.Acheter une place 2.Description 3.Fermer");
                step2 = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Mauvais choix !");
            }
        }  while (step2 < 1 || step2 > 3);

        switch (step2) {
            case 1: // Booking
                bookMovie();
                break;

            case 2: // Description
                descriptionMovie();
                break;

            case 3: // Exit
                exit();
                break;
        }
    }

    private void bookMovie() {
        control.retrieveMovie(movie_id); // get the movie chosen from db
        //update(null, null);
        showShowsList(); // show the list of shows for the movie chosen. The user can choose a show.
        control.retrieveRoom(movie_id, room_id, show_start); // get the room from db for the show chosen
        showRoomMap(); // show the mapping of the room
        showTypeOfPeople(); // show all the type of people (to know the reduction to apply)

        show("Sélectionner votre place avec x,y");
        String[] chosenSeat = sc.nextLine().split(",");

        control.addCustomer(
                Integer.parseInt(chosenSeat[0].trim()),
                Integer.parseInt(chosenSeat[1].trim()),
                control.getCustomers_al().size() + 1,
                room_id,
                enteredType,
                movie_id,
                control.getMovies_al().get(movie_id - 1).getShows().get(show_id - 1).getShow_start()
        );
        // While
        // Ticket de caisse...
        //update(null, null);
    }

    private void descriptionMovie() {
        control.retrieveMovie(movie_id);
        for (Movie movie : control.getMovies_al()) {
            if (movie.getId() == movie_id) {
                showMovieInfos(movie);
            }
        }
    }

    private void movieConfig() {
	    int choiceConfig = 0;

        show("1.Ajouter un film");
        do {
            try {
                choiceConfig = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Mauvais choix !");
                sc.nextInt();
            }
        } while (choiceConfig < 1 || choiceConfig > 1);

        switch (choiceConfig) {
            case 1:
                addMovie();
                break;
        }
    }

    private void addMovie() {
        /* A FINIR !! */

        String title;
        String description;
        ArrayList<String> genres;
        String director;
        ArrayList<String> casting;
        ArrayList<java.util.Date> dates_show; // ArrayList of shows
        int time;
        String language;
        double price;

        sc.nextLine();
        show("Quelle est le titre du film ?");
        title = sc.nextLine();

        show("Donnez une desciption");
        description = sc.nextLine();

        show("Quel est (quels sont) le(s) genre(s) du film ?");
        String repGenres = sc.nextLine();
        genres = new ArrayList<String>(Arrays.asList(repGenres.split(",")));

        dates_show = enterDate(); // function to allow the user to enter a date of show

        show("Dans quel salle se déroule le film ?");
        control.retrieveAllRooms();
        for (int i = 0 ; i < control.getRooms_al().size() ; i++){
            show(control.getRooms_al().get(i).getId() + ".Salle" + control.getRooms_al().get(i).getId());
        }

        int idRoom = sc.nextInt();

        show("Quelle est le réalisateur ?");
        director = sc.nextLine();

        show("Quels sont les acteurs principaux ?");
        String repCast = sc.nextLine();
        casting = new ArrayList<String>(Arrays.asList(repCast.split(",")));

        show("Combien de temps dure le film ? (en minutes)");
        String repTime = sc.nextLine();
        time = Integer.parseInt(repTime);

        show("Quelle est la langue ?");
        language = sc.nextLine();

        show("Quelle est le prix ? (€)");
        String repPrice = sc.nextLine();
        price = Double.parseDouble(repPrice);

        control.addMovie(Movie.getCurrentId() + 1, idRoom, title, description, director, dates_show, casting, time, language, price, genres);

        //update(null, null);
    }

    private ArrayList<java.util.Date> enterDate() {
	    /* user inputs */
        String moreDate = "";

	    /* Boolean to know if we have to do let the user enter a date again */
        boolean enterDate = false;

        ArrayList<java.util.Date> dates_show = new ArrayList<java.util.Date>(); // ArrayList of shows

        show("Quelle sont la ou les séances ?");
        do { // The user can enter a date of show
            int day = enterDay();
            int month = enterMonth();
            int year = enterYear();
            int hour = enterHour();
            int minute = enterMinute();

            java.util.Date date = control.makeDate(day, month, year, hour, minute);
            dates_show.add(date);

            System.out.println("Ajouter une autre séance ?");

            do { // ask if the user want to enter another date of show
                try {
                    show("> oui.");
                    show("> non.");
                    moreDate = sc.next();
                } catch (InputMismatchException e) {
                    show("Veuillez entrez correctement 'oui' ou 'non'.");
                    sc.next();
                }
            } while ((!moreDate.toLowerCase().equals("oui")) || (!moreDate.toLowerCase().equals("non")));

            if (moreDate.toLowerCase().equals("oui")) { // If the user want to enter another date of show
                enterDate = true; // We begin back the loop
            }
        } while (enterDate); // While the user want to enter a date of show

        return dates_show; // return the ArrayList of shows
    }

    private int enterDay() {
        int day = 0;
        do {
            try {
                showInline("Jour (1 - 31):");
                day = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez un jour entre 1 et 31.");
                sc.nextInt();
            }
        } while (day < 1 || day > 31);
        return day;
    }

    private int enterMonth() {
        int month = 0;
        do {
            try {
                showInline("Mois (1 - 12):");
                month = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez un mois entre 1 et 12.");
                sc.nextInt();
            }
        } while (month < 1 || month > 12);
        return month;
    }

    private int enterYear() {
	    int currentYear = Year.now().getValue();
        int year = 0;
        do {
            try {
                showInline("Année (" + currentYear + " - 2018):");
                year = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez une année entre " + currentYear + " et 2.");
                sc.nextInt();
            }
        } while (year < currentYear || year > 2018);
        return year;
    }

    private int enterHour() {
        int hour = 0;
        do {
            try {
                showInline("Heure (13 - 23):");
                hour = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez une heure entre 13 et 23.");
                sc.nextInt();
            }
        } while (hour < 13 || hour > 23);
        return hour;
    }

    private int enterMinute() {
        int minute = 0;
        do {
            try {
                showInline("Minute (1 - 59):");
                minute = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez une minute entre 1 et 59.");
                sc.nextInt();
            }
        } while (minute < 1 || minute > 59);
        return minute;
    }

    private void showMoviesList() {
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
                sc.nextInt();
            }
        } while (movie_id < 1 || movie_id > control.getMoviesTitles().size());

    }

    private void showMovieInfos(Movie movie) {
        show("Description:");
        show(movie.getDescription());
        show("---------------------------------------------------------");
        show("Réalisateur: " + movie.getDirector());
        show("Acteurs principaux: ");
        for (String acteur : movie.getCasting()){
            show("> " + acteur);
        }
        show("---------------------------------------------------------");
        show("Langue: " + movie.getLanguage());
        show("Genres:");
        for(String genre : movie.getGenres()){
            show("> " + genre);
        }
        show("---------------------------------------------------------");
        show(movie.getTime() + " minutes");
        show(movie.getPrice() + "€");
    }

    private void showShowsList() {
        for (Movie movie : control.getMovies_al()) {
            if (movie.getId() == movie_id) {
                for (int i = 0 ; i < movie.getShows().size() ; i++) {
                    show((i+1) + ") " + movie.getShows().get(i).getShow_start()); // Show the list of shows
                }
                do {
                    try {
                        show("Sélectionnez la séance: "); // We select the show
                        show_id = sc.nextInt(); // Id of the show
                    } catch (InputMismatchException e) {
                        show("Mauvais choix");
                        //sc.nextInt();
                    }
                } while (show_id < 1 || show_id > movie.getShows().size());
                room_id = movie.getShows().get(show_id - 1).getRoom_id(); // We get the room_id
                show_start = movie.getShows().get(show_id - 1).getShow_start(); // We get the show_start
            }
        }
    }

    private void showRoomMap() {
        for (Room room : control.getRooms_al()) {
            if (room.getId() == room_id) {
                System.out.println("Salle choisie:");
                room.displayRoom();
            }
        }
    }

    private void showTypeOfPeople() {
        show("Quelle type de personne êtes-vous ?\n");
        for (String type : control.retrieveTypes()) { // get all the types
            show("> " + type); // show it the the console
        }
        do {
            try {
                enteredType = sc.nextLine(); // get the type entered by the user
                show("" + enteredType);
            } catch (InputMismatchException e) {
                show("Ce type n'existe pas.");
                //enteredType = sc.next();
            }
        } while (!control.checkType(enteredType));

    }

    /**
     * Show a string in console
     * @param str
     */
    private void show(String str) {
	    System.out.println(str);
    }

    /**
     * Show a inline string in console
     * @param str
     */
    private void showInline(String str) {
        System.out.print(str);
    }

    /**
     * Clear the console
     * H => move to top of the screen
     * 2J => clear entire screen
     */
    private void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Exit the program
     */
    private void exit() {
        System.exit(0);
    }

	@Override
	public void update(Observable obs, Object obj) {
		//init();
	}
}

