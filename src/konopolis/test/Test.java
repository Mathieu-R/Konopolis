/**
 * 
 */
package konopolis.test;

import konopolis.controller.KonopolisController;
import konopolis.model.KonopolisModel;
import konopolis.view.KonopolisView;
import konopolis.view.KonopolisViewConsole;

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
