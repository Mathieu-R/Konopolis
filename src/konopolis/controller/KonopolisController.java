/**
 * 
 */
package src.konopolis.controller;

import src.konopolis.model.KonopolisModel;
import src.konopolis.view.KonopolisView;

/**
 * @author natha
 *
 */
public abstract class KonopolisController {
	 
	private KonopolisModel model;
	private int inputs;
	
	 //Je cr�e une vue  
	private KonopolisView view = null;
	 	
	 //Cr�ation du constructeur
	public KonopolisController(KonopolisModel m) {
	       this.model = m;
	}
	
	public void setInputs(int inputs) {
		this.inputs = inputs;
		control();
	}


	public abstract void control();
	
	public void addView(KonopolisView view) {
		this.view = view;
	}
}
