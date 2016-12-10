
package src.konopolis.controller;

import src.konopolis.model.*;
import src.konopolis.view.KonopolisView;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author nathan
 */
public class KonopolisController {
	 
	private double total = 0;

	private KonopolisModel model;
	private ArrayList<String> types = new ArrayList<String>();
	private HashMap<Integer, String> moviesTitles = new HashMap<Integer, String>();
	private HashMap<String, Double> booking = new HashMap<String, Double>();

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
	public void addCustomer(int x, int y, int customer_id, int room_id, String type, int movie_id, LocalDateTime show_start) throws RuntimeException {

		double reduction = 0;

		// We create the new customer
		for (Room room : model.getRooms_al()) {
            if (room.getId() == room_id) {
                final Customer newCust;
                try { // try to create the customer
                    newCust = new Customer(x, y, room, type, customer_id);
                    reduction = newCust.getReduction(); // get the reduction

                    // Price
                    for (Movie movie : model.getMovies_al()) {
                        if (movie.getId() == movie_id) {
                            final double reducedPrice = movie.getPrice() - movie.getPrice() * reduction; // apply the reduction
                            booking.put(type, reducedPrice); // add the type and reducedPrice to the HashMap
                            total += reducedPrice; // add the price to the total;
                        }
                    }

                    // Finally, add the customer to the db if everything else is ok
                    model.addCustomer(x, y, customer_id, room_id, type, movie_id, show_start);

                } catch (SeatUnknownException e) {
                    throw new RuntimeException(e);
                } catch (SeatTakenException e) {
                    throw new RuntimeException(e);
                }

            }
        }


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
     * @return LocalDateTime, a LocalDateTime constructed from the parameters passed in the function
     */
	public LocalDateTime makeDate(int day, int month, int year, int hours, int minutes) {
        return LocalDateTime.of(year, month, day, hours, minutes);
    }

    public String dateInFrench(LocalDateTime date) {
	    String[] days = {"Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche"};
        String[] months = {"Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"};
	    final int dayName = date.getDayOfWeek().getValue(); // day of week in number (1 -> 7)
        final int day = date.getDayOfMonth();
        final int month = date.getMonthValue(); // month of the year (1 -> 12)
        final int year = date.getYear();
        final int hour = date.getHour();
        final String minutes = date.getMinute() < 10 ? "0" + Integer.toString(date.getMinute()) : Integer.toString(date.getMinute());

        return ("Le " + days[dayName - 1] + " " + day + " " + months[month - 1] + " " + year + " à " + hour + "h" + minutes);
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
	public void addMovie(int movie_id, int room_id, String title, String description, String director, ArrayList<LocalDateTime> shows_start, ArrayList<String> casting, int time, String language, double price, ArrayList<String> genres) {
		ArrayList<Show> shows = new ArrayList<Show>();
		
		// For every start of a show 
		// We create an instance of Show Class 
		// That we put in the ArrayList shows
		// This ArrayList will be added to the Movie instance
		for (LocalDateTime show_start: shows_start) {
			// show_start, show_end, movie_id, room_id
			shows.add(new Show(show_start, show_start.plus(time, ChronoUnit.MINUTES), movie_id, room_id));
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
     * No need anymore ?
     * Convert a java.util.Date (type) into a LocalDateTime
     * @param date, the date in a Date type
     * @return, the date in LocalDateTime type
     */
    private LocalDateTime dateToLocalDateTime(java.util.Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
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

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public HashMap<String, Double> getBooking() {
        return booking;
    }

    public void setBooking(HashMap<String, Double> booking) {
        this.booking = booking;
    }

}
