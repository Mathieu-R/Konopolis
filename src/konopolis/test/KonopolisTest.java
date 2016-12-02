/**
 * 
 */
package src.konopolis.test;

import src.konopolis.controller.*;
import src.konopolis.model.*;
import src.konopolis.view.*;


/**
 * @author nathan
 *
 */
public class KonopolisTest {

	/**
	 * Launch the app
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Création du model.
		KonopolisModel konMod = new KonopolisModel();
		
		//Création des controleurs
		KonopolisController control = new KonopolisController(konMod);
		
		//Création des vues
		KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, control);
		
		//On référence notre vue au controleur
		control.addView(konViewConsole);
		
	}

}
