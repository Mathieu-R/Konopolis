/**
 * 
 */
package controller;

import java.lang.reflect.Array;
import java.sql.*;
import java.util.HashMap;

import model.DB;
import model.KonopolisModel;
import view.KonopolisView;

/**
 * @author natha
 *
 */
public abstract class KonopolisController {
	 
	private KonopolisModel model;
	
	
	 //Je crée une vue  
	private KonopolisView view = null;
	 	
	 //Création du constructeur
	public KonopolisController(KonopolisModel m) {
	       this.model = m;
	}
	
}
