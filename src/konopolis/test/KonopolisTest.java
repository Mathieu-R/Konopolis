package konopolis.test;

import konopolis.controller.KonopolisController;
import konopolis.model.KonopolisModel;
import konopolis.view.KonopolisView;
import konopolis.view.KonopolisViewConsole;
import konopolis.view.KonopolisViewGUI;

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
		KonopolisViewGUI konView = new KonopolisViewGUI(konMod, control);
		
		new Thread(konView).start();
		// We add the view to the controller
		control.addView(konView);
		
	}

}
