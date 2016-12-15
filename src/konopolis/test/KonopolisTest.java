package src.konopolis.test;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.view.KonopolisViewGUI;

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
		//KonopolisController controlCLI = new KonopolisController(konMod);
		KonopolisController controlGUI = new KonopolisController(konMod);
		
		// View instance
		//KonopolisViewConsole konViewConsole = new KonopolisViewConsole(konMod, controlCLI);
		KonopolisViewGUI konopolisViewGUI = new KonopolisViewGUI(konMod, controlGUI);

		// We add the view to the controller
		//controlCLI.addView(konViewConsole);
		controlGUI.addView(konopolisViewGUI);

	}


}
