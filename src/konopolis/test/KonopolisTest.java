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
		
		//Cr�ation du model.
		KonopolisModel konMod = new KonopolisModel();
		
		//Cr�ation des controleurs
		KonopolisController control = new KonopolisController(konMod);
		
		//Cr�ation des vues
		KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, control);
		
		//On r�f�rence notre vue au controleur
		control.addView(konViewConsole);
		
	}

}
