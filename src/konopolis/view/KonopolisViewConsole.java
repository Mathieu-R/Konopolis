package src.konopolis.view;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.*;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.*;

/**
 * Console view
 * @author Nathan - @fix Mathieu
 */
public class KonopolisViewConsole extends KonopolisView implements Observer{
	Scanner sc;
	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private String enteredType = "";
	private LocalDateTime show_start;

    /**
     * Constructor, send the model and controller to the view parent class
     * Launch the init function that start the programm in console
     * @param model
     * @param control
     */
	public KonopolisViewConsole(KonopolisModel model, KonopolisController control){
		super(model,control);
        sc = new Scanner(System.in);
        init();
	}

    /**
     * Launch the application
     * 3 choices : Movie list, configuration and close the programm
     */
	public void init() {
        int step1 = 0;

        show("**************************");
        show("BIENVENUE DANS KONOPOLIS");
        show("**************************");
        do {
            try {
                show("Faites votre choix: ");
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

    /**
     * Launch the function that show the list of movies
     * 3 choices : Buy a seat, show the description or close the program
     */
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

    /**
     * method that book a seat for a movie show
     */
    private void bookMovie() {
        boolean book = false; // true if the user wanna buy another place
        String moreBooking = ""; // to know if the user wanna buy another place
        String[] chosenSeat = new String[2]; // Array that contains the chosen seat (x, y)
        boolean successCustomer = false; // true if the chosen seat is correct (so it exists and is not taken)

        control.retrieveMovie(movie_id); // get the movie chosen from db
        showShowsList(); // show the list of shows for the movie chosen. The user can choose a show.
        control.retrieveRoom(movie_id, room_id, show_start); // get the room from db for the show chosen
        showRoomMap(); // show the mapping of the room

        do {
            showTypeOfPeople(); // show all the type of people (to know the reduction to apply)

            do {
                try {
                    show("Sélectionner votre place en x,y (siège, rangée)");
                    chosenSeat = sc.nextLine().split(","); // split the coordinates
                    control.addCustomer( // try to add the customer
                            Integer.parseInt(chosenSeat[0].trim()),
                            Integer.parseInt(chosenSeat[1].trim()),
                            control.getCustomers_al().size() + 1, // next customer_id
                            room_id,
                            enteredType,
                            movie_id,
                            control.getMovies_al().get(movie_id - 1).getShows().get(show_id - 1).getShow_start()
                    );
                    successCustomer = true; // if the seat exist
                } catch (RuntimeException e) { // if the seat is unknown
                    //show("" + e.getMessage()); // "" => Hack to use the show function
                }

            } while (!successCustomer); // while the chosen seat is incorrect



            show("Commander une autre place ?");

            do { // ask if the user want to book another place
                try {
                    show("> oui.");
                    show("> non.");
                    moreBooking = sc.next();
                } catch (InputMismatchException e) { // if the user type something else than 'oui' or 'non'
                    show("Veuillez entrez correctement 'oui' ou 'non'.");
                }
            } while ((!moreBooking.toLowerCase().equals("oui")) && (!moreBooking.toLowerCase().equals("non"))); // while the user type something else than 'oui' or 'non'

            if (moreBooking.toLowerCase().equals("oui")) { // If the user want to enter another date of show
                book = true; // We begin back the loop
            } else {
                book = false;
            }


        } while (book); // while the user book a place
        
        // Show the receipt
        for (Map.Entry<String, Double> bookingEntry: control.getBooking().entrySet()) {
            show("> " + bookingEntry.getKey() + " - " + Math.round(bookingEntry.getValue() * 100.0) / 100.0 + "€");
        }
        
        // Total to pay
        show("TOTAL A PAYER : " + Math.round(control.getTotal() * 100.0) / 100.0 + "€");
        control.setTotal(0); // Put back the total at 0;

        //update(null, null);
    }

    /**
     * Show the full description of a movie
     */
    private void descriptionMovie() {
        control.retrieveMovie(movie_id); // we get the movie from db
        for (Movie movie : control.getMovies_al()) {
            if (movie.getId() == movie_id) { // look for it
                showMovieInfos(movie); // show the full description
            }
        }
    }

    /**
     * function the manage the configuration part of the app (add a movie,...)
     * The user can chose the different available configurations (only 1 for now)
     */
    private void movieConfig() {
	    int choiceConfig = 0;

        show("1.Ajouter un film"); // only one choice, could add other one later
        do {
            try {
                choiceConfig = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Mauvais choix !");
            }
        } while (choiceConfig != 1); // while the user does not type 1

        switch (choiceConfig) { // according to the choice of the user
            case 1: // if 1
                addMovie(); // launch add movie function
                break;
        }
    }

    /**
     * function that allow to add movie to the db
     */
    private void addMovie() {

        String title = ""; // title of the movie
        String description = ""; // description of the movie
        String repGenres = ""; // genres of the movie in a string (user input)
        ArrayList<String> genres = new ArrayList<String>();
        int idRoom = 0; // id of the room
        String director = "";
        String repCast = "";
        ArrayList<String> casting = new ArrayList<String>();
        ArrayList<LocalDateTime> dates_show = new ArrayList<LocalDateTime>(); // ArrayList of shows
        int time = 0; // duration of the movie
        String language = "";
        double price = 0.0;

        do { // the user choose a title
            try {
                show("Quelle est le titre du film ?");
                title = sc.nextLine();
            } catch (InputMismatchException e) {
                show("Titre manquant !");
            }
        } while (title.equals(""));

        do { // the user choose a description
            try {
                show("Donnez une description");
                description = sc.nextLine();
            } catch (InputMismatchException e) {
                show("Description manquante !");
            }
        } while (description.equals(""));

        do { // the user choose one or more genre(s)
            try {
                show("Quel est (quels sont) le(s) genre(s) du film ? (séparés par une virgule)");
                repGenres = sc.nextLine();
                genres = new ArrayList<String>(Arrays.asList(repGenres.split(","))); // split the genres in an ArrayList
            } catch (InputMismatchException e) {
                show("Genre(s) manquant(s) ou pattern incorrecte.");
            }
        } while (repGenres.equals(""));

        dates_show = enterDate(); // function to allow the user to enter a date of show

        show("Dans quel salle se déroule le film ?");

        control.retrieveAllRooms(); // Get all the rooms
        for (Room room : control.getRooms_al()) { // Show every room
            show(room.getId() + ") " + room.getRows() + " rangées - " + room.getSeatsByRow() + " sièges par rangée - " + room.getTotSeats() + " places");
        }

        do { // the user can now choose a room
            try {
                idRoom = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Salle inexistante");
            }
        } while (idRoom < 1 || idRoom > control.getRooms_al().size());

        do { // the user can choose the director
            try {
                show("Quelle est le réalisateur ?");
                director = sc.nextLine();
            } catch (InputMismatchException e) {
                show("Réalisateur manquant !");
            }
        } while (director.equals(""));

        do { // the user can choose the actors
            try {
                show("Quels sont les acteurs principaux ? (séparés par une virgule)");
                repCast = sc.nextLine();
                casting = new ArrayList<String>(Arrays.asList(repCast.split(",")));
            } catch (InputMismatchException e) {
                show("Acteur(s) manquant(s) ou pattern non-respecté");
            }
        } while (repCast.equals(""));

        do { // the user can choose a duration
            try {
                show("Combien de temps dure le film ? (en minutes)");
                String repTime = sc.nextLine();
                time = Integer.parseInt(repTime); // parse the integer into a int
            } catch (InputMismatchException e) {
                show("Durée manquante !");
            }
        } while (time == 0);

        do { // the user can chose a language
            try {
                show("Quelle est la langue ?");
                language = sc.nextLine();
            } catch (InputMismatchException e) {
                show("Langue manquante !");
            }
        } while (language.equals(""));

        do { // the user can chose a price
            try {
                show("Quelle est le prix ? (€)");
                String repPrice = sc.nextLine();
                price = Double.parseDouble(repPrice); // parse into a double
            } catch (InputMismatchException e) {
                show("Prix manquant !");
            }
        } while (price == 0.0);

        try {
            // Eventually, we try to add the movie to the db
            // Movie.getCurrentId() + 1 => next id of the movie
            control.addMovie(Movie.getCurrentId() + 1, idRoom, title, description, director, dates_show, casting, time, language, price, genres);
        } catch (RuntimeException e) {
            //update(null, e);
        }

        //update(null, null);
    }

    /**
     * function that allows the user to enter one or more date(s) of show
     * @return, an ArrayList of LocalDateTime that contains all the shows entered by the user
     */
    private ArrayList<LocalDateTime> enterDate() {
        String moreDate = ""; // to know if the user want to enter another date of show
        boolean enterDate = false;  // Boolean to know if we have to let the user enter a date again
        ArrayList<LocalDateTime> dates_show = new ArrayList<LocalDateTime>(); // ArrayList of shows

        show("Quelle sont la ou les séances ?");
        do { // The user can enter a date of show
            int day = enterDay();
            int month = enterMonth();
            int year = enterYear();
            int hour = enterHour();
            int minute = enterMinute();

            LocalDateTime date = control.makeDate(day, month, year, hour, minute); // we create the date
            dates_show.add(date); // add the date to the ArrayList

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
            } while ((!moreDate.toLowerCase().equals("oui")) && (!moreDate.toLowerCase().equals("non"))); // if the user doesn't write 'oui' or 'non'

            if (moreDate.toLowerCase().equals("oui")) { // If the user want to enter another date of show
                enterDate = true; // We begin back the loop
            } else {
                enterDate = false;
            }
        } while (enterDate); // While the user want to enter a date of show

        return dates_show; // return the ArrayList of shows
    }

    /**
     * Allows the user to enter a day
     * @return, int, the day chosen
     */
    private int enterDay() {
        int day = 0;
        do {
            try {
                showInline("Jour (1 - 31): ");
                day = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez un jour entre 1 et 31.");
                sc.nextInt();
            }
        } while (day < 1 || day > 31);
        return day;
    }

    /**
     * Allows the user to enter a month
     * @return, int, the month chosen
     */
    private int enterMonth() {
        int month = 0;
        do {
            try {
                showInline("Mois (1 - 12): ");
                month = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez un mois entre 1 et 12.");
                sc.nextInt();
            }
        } while (month < 1 || month > 12);
        return month;
    }

    /**
     * Allows the user to enter a year
     * The year must be equals or greater than the current year
     * @return, int, the year chosen
     */
    private int enterYear() {
	    int currentYear = Year.now().getValue();
        int year = 0;
        do {
            try {
                showInline("Année (" + currentYear + " - 2018): ");
                year = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez une année entre " + currentYear + " et 2.");
                sc.nextInt();
            }
        } while (year < currentYear || year > 2018);
        return year;
    }

    /**
     * Allows the user to enter an hour
     * @return, int, the hour chosen
     */
    private int enterHour() {
        int hour = 0;
        do {
            try {
                showInline("Heure (13 - 23): ");
                hour = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez une heure entre 13 et 23.");
                sc.nextInt();
            }
        } while (hour < 13 || hour > 23);
        return hour;
    }

    /**
     * Allows the user to enter a minute
     * @return, int, the minute chosen
     */
    private int enterMinute() {
        int minute = 0;
        do {
            try {
                showInline("Minute (00 - 59): ");
                minute = sc.nextInt();
            } catch (InputMismatchException e) {
                show("Entrez une minute entre 00 et 59.");
                sc.nextInt();
            }
        } while (minute < 0 || minute > 59);
        return minute;
    }

    /**
     * Show the list of movies available
     */
    private void showMoviesList() {
        // The number in the list (1,2,3) is not equals the movie_id but the position of the movie_id and its title in the LinkedHashMap (+ 1)
        int movieListNumber = 0;
        show("Liste des films:");
        int index = 1;
        for (Map.Entry<Integer, String> movieEntry: control.retrieveAllMoviesTitles().entrySet()) { // for every movie in the HashMap
            show(index++ + ") " + movieEntry.getValue());
        }
        showInline("Sélectionnez un film : ");
        do { // the user can choose one
            try {
                movieListNumber = sc.nextInt(); // get the number of the list (which is equals the position of the movie in the LinkedHashMap + 1)
                movie_id = (new ArrayList<Integer>(control.getMoviesTitles().keySet())).get(movieListNumber - 1); // get the right id according its position in the LinkedHashMap
                //show(Integer.toString(movie_id));
            } catch (InputMismatchException e) {
                show("Ce film n'existe pas, choisissez un film de la liste !");
                sc.nextInt();
            }
        } while (movieListNumber < 1 || movieListNumber > control.getMoviesTitles().size()); // while the int typed is not in the list of movie

    }

    /**
     * Show the full description of a movie
     * @param movie, a movie chosen
     */
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

    /**
     * show a list of shows for a movie given
     * then, the user can choose the show he wanna go
     */
    private void showShowsList() {
        for (Movie movie : control.getMovies_al()) { // for every movie
            if (movie.getId() == movie_id) { // look for the right one
                for (int i = 0 ; i < movie.getShows().size() ; i++) { // for every shows of this movie
                    show((i+1) + ") " + control.dateInFrench(movie.getShows().get(i).getShow_start())); // Show the list of shows
                }

                do { // the user can chose the show he wanna go
                    try {
                        show("Sélectionnez la séance: "); // We select the show
                        show_id = sc.nextInt(); // Id of the show
                    } catch (InputMismatchException e) {
                        show("Mauvais choix");
                    }
                } while (show_id < 1 || show_id > movie.getShows().size()); // if the show is not in the list
                room_id = movie.getShows().get(show_id - 1).getRoom_id(); // We get the room_id
                show_start = movie.getShows().get(show_id - 1).getShow_start(); // We get the show_start
            }
        }
    }

    /**
     * Show the mapping of a room
     */
    private void showRoomMap() {
        int rowIndex = 1;
        int seatIndex = 1;
        for (Room room : control.getRooms_al()) {
            if (room.getId() == room_id) {
                show("Salle choisie:");
                for (ArrayList<Seat> seatsRow : room.getSeats()) { // For Each row
                    //seatIndex = 1;
                    if (rowIndex == 1) {
                        showInline("   "); // three spaces
                        for (Seat seat : seatsRow) {
                            if (seatIndex < 10) showInline(seatIndex + "  "); // Show the column above the map, two spaces for number < 10
                            else showInline(seatIndex + " "); // one space for number > 10
                            seatIndex++;
                        }
                        showInline("\n"); // Turn back to the line
                    }
                    if (rowIndex < 10) showInline(rowIndex + "  "); // Show the row at the right of the map, two spaces for number < 10
                    else showInline(rowIndex + " "); // one space for number > 10
                    for (Seat seat : seatsRow) { // For Each seat
                        if (seat.isTaken()) showInline("[X]"); // If the seat is taken
                        else showInline("[O]"); // Otherwise, if the seat is available
                    }
                    showInline("\n"); // Turn back to the line for every row
                    rowIndex++;
                }
            }
        }
    }

    /**
     * Show every type of people available (VIP, Senior, Student,..) to apply a reduction
     * The user can choose the corresponding one
     */
    private void showTypeOfPeople() {
        show("Quel type de personne êtes-vous ?\n");
        for (String type : control.retrieveTypes()) { // get all the types
            show("> " + type); // show it the the console
        }
        sc.nextLine(); // flush the buffer of the scanner
        do {
            try {
                enteredType = sc.nextLine(); // get the type entered by the user
                //show("" + enteredType);
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
        //show((String) obj);
		//init();
	}
}

