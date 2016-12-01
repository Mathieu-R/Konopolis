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
		
		//Cr�ation du model.
		KonopolisModel konMod = new KonopolisModel();
		
		//Cr�ation des controleurs
		KonopolisController control=new KonopolisController(konMod);
		
		//Cr�ation des vues
		KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, control);
		
		//On r�f�rence notre vue au controleur
		control.addView(konViewConsole);
		
	}

}
