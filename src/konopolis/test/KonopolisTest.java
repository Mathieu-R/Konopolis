package src.konopolis.test;

import src.konopolis.model.KonopolisModel;
import src.konopolis.controller.KonopolisController;
import src.konopolis.view.KonopolisViewConsole;

/**
 * @author nathan
 */
public class KonopolisTest {

	/**
	 * Launch the app
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Model instance
		KonopolisModel konMod = new KonopolisModel();
		
		// Controller instance
		KonopolisController control = new KonopolisController(konMod);
		
		// View instance
		KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, control);
		
		// We add the view to the controller
		control.addView(konViewConsole);
		
	}

}
