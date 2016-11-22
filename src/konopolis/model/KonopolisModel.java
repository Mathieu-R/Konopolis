package src.konopolis.model;

import java.util.Observable;

/**
 * Model
 * Abstract Class
 * Implements Observer
 * It informs the modifications to the view
 */
public class KonopolisModel extends Observable {

    /**
     * 1) Connection to the DB
     * 2) Retrieve all the informations (rooms, movies, movie shows, customers,...)
     * 3) Creation of the rooms, movies, movie shows, filling the rooms,...)
     */
    public KonopolisModel() {
        final DB db = new DB();
        db.createConnection();

    }
}
