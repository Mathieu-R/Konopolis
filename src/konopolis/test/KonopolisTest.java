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
		
		//Création du model.
		KonopolisController KonModel = new KonopolisController();
		
		//Création du controleur
		KonopolisController KonControl = new KonopolisController(KonModel);
		
		//Création de la vue
		KonopolisView KonView = new KonopolisView(KonModel, KonControl);
		
		public void addView(KonopolisView view) {
	        this.view = view;
	    }
		
		//On référence notre vue au controleur
		KonControl.addView(KonView);
		
	}

}
