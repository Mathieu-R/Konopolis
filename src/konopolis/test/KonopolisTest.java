/**
 * 
 */
package src.konopolis.test;


import src.konopolis.model.KonopolisModel;
import src.konopolis.view.KonopolisView;

import src.konopolis.controller.KonopolisController;
import src.konopolis.view.*;


/**
 * @author natha
 *
 */
public class KonopolisTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Création du model.
		KonopolisModel konMod = new KonopolisModel();
		
		//Création des controleurs
		KonopolisController control=new KonopolisController(konMod);
		
		//Création des vues
		KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, control);
		
		//On référence notre vue au controleur
		control.addView(konViewConsole);
		
	}

}
