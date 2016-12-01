/**
 * 
 */
package src.konopolis.test;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.view.KonopolisView;

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
		KonopolisController konControl = new KonopolisController(konMod);
		KonopolisController konControl = new KonopolisController(konMod);
		
		//Création des vues
		KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, konControl);
		KonopolisViewModel konViewGui = new KonopolisViewGui(konMod, konControl);
		
		//On référence notre vue au controleur
		konControl.addView(konViewConsole);
		konControl.addView(konViewGui);
		
	}

}
