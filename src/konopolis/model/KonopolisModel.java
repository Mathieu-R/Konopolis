package src.konopolis.model;

import java.util.HashMap;
import java.util.Observable;

/**
 * Model
 * Abstract Class
 * Implements Observer
 * It informs the modifications to the view
 */
public class KonopolisModel extends Observable {

    private DB db = null; // will contain an instance of DB Class

    /**
     * 1) Connection to the DB
     * 2) Retrieve all the informations (rooms, movies, movie shows, customers,...)
     * 3) Creation of the rooms, movies, movie shows, filling the rooms,...)
     */
    public KonopolisModel() {
        db = new DB();
        db.createConnection();
    }

    public HashMap<Integer, String> retrieveAllMoviesTitles() {
        return db.retrieveAllMoviesTitles();
    }
}
