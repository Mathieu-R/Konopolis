/**
 * 
 */
package controller;

/**
 * @author natha
 *
 */
public abstract class KonopolisController {
	 
	private KonopolisModel model;
	private int inputs;
	
	 //Je crée une vue  
	private KonopolisView view = null;
	 	
	 //Création du constructeur
	public KonopolisController(KonopolisModel m) {
	       this.model = m;
	}
	
	public void setInputs(int inputs) {
		this.inputs = inputs;
		control();
	}


	public abstract void control();
}
