
package src.konopolis.controller;

import src.konopolis.model.*;
import src.konopolis.view.KonopolisView;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author nathan
 */
public class KonopolisController {

  private double total = 0;

  private KonopolisModel model;
  private ArrayList<String> types = new ArrayList<String>();
  private LinkedHashMap<Integer, String> moviesTitles = new LinkedHashMap<Integer, String>();
  private HashMap<String, Double> booking = new HashMap<String, Double>();

  private KonopolisView view = null;

  public KonopolisController(KonopolisModel m) {
    this.model = m;
  }

  /* Methods */

  /*
   * authentify a user
   */
  public synchronized boolean authUser(String username, String password) throws InvalidUserException {
    return model.authUser(username, password);
  }

  /**
   * Retrieve all the movies titles
   *
   * @return HashMap of all the movies titles
   */
  public synchronized LinkedHashMap<Integer, String> retrieveAllMoviesTitles() {
    return this.moviesTitles = model.retrieveAllMoviesTitles();
  }

  /**
   * Retrieve a movie according it id
   *
   * @param movie_id
   * @return movie, a Movie instance
   */
  public synchronized Movie retrieveMovie(int movie_id) {
    return model.retrieveMovie(movie_id);
  }

  public synchronized int retrieveMovieId(String title) {
    return model.retrieveMovieId(title);
  }

  /**
   * Retrieve a room where a movie is broadcasted (for a show given)
   *
   * @param movie_id
   * @param room_id
   * @param show_start
   * @return room, a Room instance
   */
  public synchronized Room retrieveRoom(int movie_id, int room_id, LocalDateTime show_start) {
    return model.retrieveRoom(movie_id, room_id, show_start);
  }

  /**
   * Create a new customer object
   * If it is created, we add the customer to the db
   *
   * @param x,           seat of the row
   * @param y,           row
   * @param customer_id, id of customer
   * @param room_id,     id of room
   * @param type,        type of customer
   * @param movie_id,    id of the movie
   * @param show_start,  beginning date of the show
   */
  public synchronized void addCustomer(int x, int y, int customer_id, int room_id, String type, int movie_id,
      LocalDateTime show_start) {
    double reduction = 0.0;

    // We create the new customer
    for (Room room : model.getRooms_al()) {
      if (room.getId() == room_id) {
        final Customer newCust;
        try { // try to create the customer
          newCust = new Customer(x, y, room, type);
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
          model.addCustomer(x, y, type, movie_id, room_id, show_start);

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
  public synchronized ArrayList<String> retrieveTypes() {
    return types = model.retrieveTypes();
  }

  /**
   * Check if the type of people entered by the user exist
   *
   * @param type
   */
  public synchronized boolean checkType(String type) {
    if (!types.contains(type.trim())) { // If the type does not exist
      System.out.println("Ce type de personne n'existe pas");
      return false;
    }
    return true;
  }

  /**
   * Create a Date object from a day, month, year, hours and minutes
   * month - 1 because month is "0 based" so it begins from 0 but the user begin
   * by 1.
   *
   * @return LocalDateTime, a LocalDateTime constructed from the parameters passed
   *         in the function
   */
  public synchronized LocalDateTime makeDate(int day, int month, int year, int hours, int minutes) {
    return LocalDateTime.of(year, month, day, hours, minutes);
  }

  public synchronized String dateInFrench(LocalDateTime date) {
    String[] days = { "Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi", "Samedi", "Dimanche" };
    String[] months = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin", "Juillet", "Août", "Septembre", "Octobre",
        "Novembre", "Décembre" };
    final int dayName = date.getDayOfWeek().getValue(); // day of week in number (1 -> 7)
    final int day = date.getDayOfMonth();
    final int month = date.getMonthValue(); // month of the year (1 -> 12)
    final int year = date.getYear();
    final int hour = date.getHour();
    final String minutes = date.getMinute() < 10 ? "0" + Integer.toString(date.getMinute())
        : Integer.toString(date.getMinute());

    return ("Le " + days[dayName - 1] + " " + day + " " + months[month - 1] + " " + year + " à " + hour + "h"
        + minutes);
  }

  /**
   * A a movie to the db
   * Create a new Movie object
   *
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
  public synchronized void addMovie(/* int movie_id, */ int room_id, String title, String description, String director,
      ArrayList<LocalDateTime> shows_start, ArrayList<String> casting, int time, String language, double price,
      ArrayList<String> genres) {
    // ArrayList<Show> shows = new ArrayList<Show>();

    try {
      model.addMovie(room_id, title, description, director, shows_start, casting, time, language, price, genres);
      // For every start of a show
      // We create an instance of Show Class
      // That we put in the ArrayList shows
      // This ArrayList will be added to the Movie instance
      /*
       * for (LocalDateTime show_start: shows_start) {
       * // show_start, show_end, movie_id, room_id
       * shows.add(new Show(show_start, show_start.plus(time, ChronoUnit.MINUTES),
       * movie_id, room_id));
       * }
       */
      // model.getMovies_al().add(new Movie(movie_id, title, description, genres,
      // shows, director, casting, time, language, price));
    } catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieve all the rooms available
   */
  public synchronized void retrieveAllRooms() {
    model.retrieveAllRooms();
  }

  /**
   * Retrieve all languages available
   * @return, languages are put in an ArrayList of String
   */
  public synchronized ArrayList<String> retrieveAllLanguages() {
    return model.retrieveAllLanguages();
  }

  /**
   * Create a date (LocalDateTime) from a String
   */
  public synchronized LocalDateTime makeDateFromString(String date) throws DateTimeParseException {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); // pattern
    return LocalDateTime.parse(date, formatter); // String => LocalDateTime (according to the pattern)
  }

  /**
   * add a view to the controller
   *
   * @param view, a view (console, gui,...)
   */
  public synchronized void addView(KonopolisView view) {
    this.view = view;
  }

  /**
   * No need anymore ?
   * Convert a java.util.Date (type) into a LocalDateTime
   *
   * @param date, the date in a Date type
   *              @return, the date in LocalDateTime type
   */
  private synchronized LocalDateTime dateToLocalDateTime(java.util.Date date) {
    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
  }

  /* Getters and Setters */

  public synchronized ArrayList<Movie> getMovies_al() {
    return model.getMovies_al();
  }

  public synchronized ArrayList<Room> getRooms_al() {
    return model.getRooms_al();
  }

  public synchronized ArrayList<Show> getShows_al() {
    return model.getShows_al();
  }

  public synchronized ArrayList<Customer> getCustomers_al() {
    return model.getCustomers_al();
  }

  public synchronized KonopolisModel getModel() {
    return model;
  }

  public synchronized void setModel(KonopolisModel model) {
    this.model = model;
  }

  public synchronized ArrayList<String> getTypes() {
    return types;
  }

  public synchronized void setTypes(ArrayList<String> types) {
    this.types = types;
  }

  public synchronized KonopolisView getView() {
    return view;
  }

  public synchronized void setView(KonopolisView view) {
    this.view = view;
  }

  public synchronized LinkedHashMap<Integer, String> getMoviesTitles() {
    return moviesTitles;
  }

  public synchronized void setMoviesTitles(LinkedHashMap<Integer, String> moviesTitles) {
    this.moviesTitles = moviesTitles;
  }

  public synchronized double getTotal() {
    return total;
  }

  public synchronized void setTotal(double total) {
    this.total = total;
  }

  public synchronized HashMap<String, Double> getBooking() {
    return booking;
  }

  public synchronized void setBooking(HashMap<String, Double> booking) {
    this.booking = booking;
  }

}
