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
}
