/**
 * 
 */
package src.konopolis.controller;


import src.konopolis.view.KonopolisView;

import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

import src.konopolis.model.*;
import src.konopolis.view.*;

/**
 * @author nathan
 *
 */
public class KonopolisController {
	 
	private KonopolisModel model;
	private int inputs;
	
	private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/konopolis";
    private final String USER = "root";
    private final String PWD = "H1perGl0bulle";
	
	 //Je crée une vue  
	private KonopolisView view = null;
	 	
	 //Création du constructeur
	public KonopolisController(KonopolisModel m) {
	       this.model = m;
	       registerDriver();
	}
	
	public void registerDriver() {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Driver OK");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
	
	public void createConnection() {
        model.createConnection();
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
	
	 public void addCustomer(int x, int y, int customer_id, int room_id, String type, int seat_id, int movie_id, LocalDateTime show_start) {
	    	
		 model.addCustomer(x, y, customer_id, room_id, type, seat_id, movie_id, show_start);
	    }
	 
	    public int retrieveOrCreateGenreId(String genre) {
	    	return model.retrieveOrCreateGenreId(genre);
	    }
	
	 public ArrayList<Movie> getMovies_al() {
			return model.getMovies_al();
		}
	 
	/* public void addShows(int movie_id, int room_id, LocalDateTime show_start) {
		 model.addShows(movie_id, room_id, show_start);
	  }
	 */  
	 public void addMovie(int movie_id,int room_id, String title, String description, String director, ArrayList<Date> shows_start, ArrayList<String> casting, int time, String language, double price, ArrayList<String> genres) {
		 model.addMovie(movie_id, room_id, title, description, director, shows_start, casting, time, language, price, genres);
	 }
	 
	 public void retrieveAllRooms() {
		 	model.getRooms_al().clear();
	        model.retrieveAllRooms();
	    }
	 public int getCustomerId(){
		 return model.getCustomers_al().size();
	 }
	 
	 public void retrieveCustomers(int room_id, int movie_id, LocalDateTime show_start) {
		 	model.retrieveCustomers(room_id, movie_id, show_start);
	    }
	
		public void setMovies_al(ArrayList<Movie> movies_al) {
			model.setMovies_al(movies_al);
		}
	
		public ArrayList<Show> getShows_al() {
			return model.getShows_al();
		}
	
		public void setShows_al(ArrayList<Show> shows_al) {
			model.setShows_al(shows_al);
		}
	
		public ArrayList<Customer> getCustomers_al() {
			return model.getCustomers_al();
		}
		
		 public LocalDateTime stringToLocalDateTime(String show) {
		    	return model.stringToLocalDateTime(show);
		    }
	
		public void setCustomers_al(ArrayList<Customer> customers_al) {
			model.setCustomers_al(customers_al);
		}
	
		public ArrayList<Room> getRooms_al() {
			return model.getRooms_al();
		}
	
		public void setRooms_al(ArrayList<Room> rooms_al) {
			model.setRooms_al(rooms_al);
		}
	
	public void addView(KonopolisView view) {
		this.view = view;
	}
}
