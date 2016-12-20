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
		
		KonopolisView konGUI = new KonopolisViewGUI(konMod,control);
		KonopolisView konConsole = new KonopolisViewConsole(konMod, control);
		
		
		// View instance
		new Thread(konGUI).start();
		Thread threadConsole = new Thread(konConsole);
		
		threadConsole.start();
		
		// We add the view to the controller
		control.addView(konGUI);
		control.addView(konConsole);
	
		
	}

}
