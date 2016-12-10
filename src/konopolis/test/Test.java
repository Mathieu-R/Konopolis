/**
 * 
 */
package src.konopolis.test;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.view.KonopolisView;
import src.konopolis.view.KonopolisViewConsole;

/**
 * @author natha
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		KonopolisModel model=new KonopolisModel();
		
		KonopolisController control=new KonopolisController(model);
		
		KonopolisView view=new KonopolisViewConsole(model,control);

	}

}
