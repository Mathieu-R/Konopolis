/**
 * 
 */
package src.konopolis.controller;


import src.konopolis.view.KonopolisView;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import src.konopolis.model.*;

/**
 * @author nathan
 *
 */
public class KonopolisController {
	 
	private KonopolisModel model;
	private int inputs;
	private ArrayList<String> types = new ArrayList<String>();

	private KonopolisView view = null;

	public KonopolisController(KonopolisModel m) {
	       this.model = m;
	}
	
	public void setInputs(int inputs) {
		this.inputs = inputs;
	}

	public HashMap<Integer, String> retrieveAllMoviesTitles() {
		return model.retrieveAllMoviesTitles();
	}
	
	public void retrieveMovie(int movie_id){
		model.retrieveMovie(movie_id);
	}
	
	public void retrieveRoom(int movie_id, int room_id, LocalDateTime show_start) {
		model.retrieveRoom(movie_id, room_id, show_start);
	}
	
	public void addCustomer(int x, int y, int customer_id, int room_id, String type, int movie_id, LocalDateTime show_start) {
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
     * @param genre
     */
	public void checkType(String type) {
	    if (!types.contains(type.trim())) { // If the type does not exist
            System.out.println("Ce type de personne n'existe pas");
        }
	}
	 
    public int retrieveOrCreateGenreId(String genre) {
    	return model.retrieveOrCreateGenreId(genre);
    }
	  
	public void addMovie(int movie_id,int room_id, String title, String description, String director, ArrayList<Date> shows_start, ArrayList<String> casting, int time, String language, double price, ArrayList<String> genres) {
		model.addMovie(movie_id, room_id, title, description, director, shows_start, casting, time, language, price, genres);
	}
	 
	public void retrieveAllRooms() {
	    model.retrieveAllRooms();
	}
	
	public int getCustomerId(){
		return model.getCustomers_al().size();
	}
	 
	public void retrieveCustomers(int room_id, int movie_id, Date show_start) {
		model.retrieveCustomers(room_id, movie_id, show_start);
	}

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

    /**
     * add a view to the controller
     * @param view, a view (console, gui,...)
     */
	public void addView(KonopolisView view) {
		this.view = view;
	}

	/* Getters and Setters */

    public KonopolisModel getModel() {
        return model;
    }

    public void setModel(KonopolisModel model) {
        this.model = model;
    }

    public int getInputs() {
        return inputs;
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
}
