package src.konopolis.model;

import java.util.HashMap;
import java.util.Observable;

/**
 * @author Mathieu R. - Groupe 3
 */

/**
 * Model
 * Abstract Class
 * Implements Observer
 * It informs the modifications to the view
 */
public class KonopolisModel extends Observable {

    private DB db = null;
    
    public KonopolisModel() {
    	db = new DB();
    	db.createConnection();
    }
    
    public HashMap<Integer, String> retrievesAllMoviesTitles() {
    	return db.retrieveAllMoviesTitles();
    }

}
