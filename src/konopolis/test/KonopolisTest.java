/**
 * 
 */
package test;

/**
 * @author natha
 *
 */
public class KonopolisTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//Cr�ation du model.
		KonopolisController KonModel = new KonopolisController();
		
		//Cr�ation du controleur
		KonopolisController KonControl = new KonopolisController(KonModel);
		
		//Cr�ation de la vue
		KonopolisView KonView = new KonopolisView(KonModel, KonControl);
		
		public void addView(KonopolisView view) {
	        this.view = view;
	    }
		
		//On r�f�rence notre vue au controleur
		KonControl.addView(KonView);
		
	}

}
