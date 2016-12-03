
package src.konopolis.controller;
import src.konopolis.model.*;
import src.konopolis.view.KonopolisView;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * @author nathan
 *
 */
public class KonopolisController {
	 
	private KonopolisModel model;
	private ArrayList<String> types = new ArrayList<String>();
	private HashMap<Integer, String> moviesTitles = new HashMap<Integer, String>();

	private KonopolisView view = null;

	public KonopolisController(KonopolisModel m) {
	       this.model = m;
	}

	/* Methods */

    /**
     * Retrieve all the movies titles
     * @return
     */
	public HashMap<Integer, String> retrieveAllMoviesTitles() {
	    return this.moviesTitles = model.retrieveAllMoviesTitles();
	}

    /**
     * Retrieve a movie according it id
     * @param movie_id
     */
	public void retrieveMovie(int movie_id){
		model.retrieveMovie(movie_id);
	}

    /**
     * Retrieve a room where a movie is broadcasted (for a show given)
     * @param movie_id
     * @param room_id
     * @param show_start
     */
	public void retrieveRoom(int movie_id, int room_id, LocalDateTime show_start) {
		model.retrieveRoom(movie_id, room_id, show_start);
	}

    /**
     * Create a new customer object
     * If it is created, we add the customer to the db
     * @param x
     * @param y
     * @param customer_id
     * @param room_id
     * @param type
     * @param movie_id
     * @param show_start
     */
	public void addCustomer(int x, int y, int customer_id, int room_id, String type, int movie_id, LocalDateTime show_start) {

		// We create the new customer
		for (Room room : model.getRooms_al()) {
            if (room.getId() == room_id) {
                new Customer(x, y, room, type, customer_id);
            }
        }

		model.addCustomer(x, y, customer_id, room_id, type, movie_id, show_start);
	}

    /**
     * Retrieve all the type of people available
     */
	public ArrayList<String> retrieveTypes() {
	    return types = model.retrieveTypes();
    }

    /**
     * Check if the type of people entered by the user exist
     * @param type
     */
	public boolean checkType(String type) {
	    if (!types.contains(type.trim())) { // If the type does not exist
            System.out.println("Ce type de personne n'existe pas");
            return false;
        }
        return true;
	}

    /**
     * Create a Date object from a day, month, year, hours and minutes
     * month - 1 because month is "0 based" so it begins from 0 but the user begin by 1.
     * @return Date, a Date object constructed from the parameters passed in the function
     */
	public Date makeDate(int day, int month, int year, int hours, int minutes) {
        Calendar c = Calendar.getInstance(); // new instance of Calendar
        c.set(day, month - 1, year, hours, minutes); // set a date
        System.out.println("Date created: " + c.getTime());
        return new java.sql.Date(c.getTimeInMillis()); // return the Date SQL format
    }

    /**
     * A a movie to the db
     * Create a new Movie object
     * @param movie_id
     * @param room_id
     * @param title
     * @param description
     * @param director
     * @param shows_start
     * @param casting
     * @param time
     * @param language
     * @param price
     * @param genres
     */
	public void addMovie(int movie_id, int room_id, String title, String description, String director, ArrayList<Date> shows_start, ArrayList<String> casting, int time, String language, double price, ArrayList<String> genres) {
		ArrayList<Show> shows = new ArrayList<Show>();
		
		// For every start of a show 
		// We create an instance of Show Class 
		// That we put in the ArrayList shows
		// This ArrayList will be added to the Movie instance
		for (Date show_start: shows_start) {
			// show_start, show_end, movie_id, room_id
			shows.add(new Show(dateToLocalDateTime(show_start), dateToLocalDateTime(show_start).plus(time, ChronoUnit.MINUTES), movie_id, room_id));
		}
		
		model.getMovies_al().add(new Movie(movie_id, title, description, genres, shows, director, casting, time, language, price));

		model.addMovie(movie_id, room_id, title, description, director, shows_start, casting, time, language, price, genres);
	}

    /**
     * Retrieve all the rooms available
     */
	public void retrieveAllRooms() {
	    model.retrieveAllRooms();
	}

    /**
     * add a view to the controller
     * @param view, a view (console, gui,...)
     */
	public void addView(KonopolisView view) {
		this.view = view;
	}

    /**
     * Convert a Date (type) into a LocalDateTime
     * @param show, the date in a Date type
     * @return, the date in LocalDateTime type
     */
    private LocalDateTime dateToLocalDateTime(Date show) {
        Instant instant = Instant.ofEpochMilli(show.getTime());
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

	/* Getters and Setters */

    public ArrayList<Movie> getMovies_al() {
        return model.getMovies_al();
    }

    public ArrayList<Room> getRooms_al() {
        return model.getRooms_al();
    }

    public ArrayList<Show> getShows_al() {
        return model.getShows_al();
    }

    public ArrayList<Customer> getCustomers_al() {
        return model.getCustomers_al();
    }

    public KonopolisModel getModel() {
        return model;
    }

    public void setModel(KonopolisModel model) {
        this.model = model;
    }

    public ArrayList<String> getTypes() {
        return types;
    }

    public void setTypes(ArrayList<String> types) {
        this.types = types;
    }

    public KonopolisView getView() {
        return view;
    }

    public void setView(KonopolisView view) {
        this.view = view;
    }

    public HashMap<Integer, String> getMoviesTitles() {
        return moviesTitles;
    }

    public void setMoviesTitles(HashMap<Integer, String> moviesTitles) {
        this.moviesTitles = moviesTitles;
    }
}
