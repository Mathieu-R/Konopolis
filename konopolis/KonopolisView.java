package konopolis;

import java.util.Observable;
import java.util.Observer;
import javax.swing.*;



/**
 * 
 * @author Sébastien H.- Groupe 3
 *
 */
public class KonopolisView extends JFrame implements Observer{
	JPanel indexPanel = new JPanel();
	
	private JPanel container = new JPanel();
	private JTextField research = new JTextField(20);
	private JButton LaunchResearch = new JButton("Recherche");
	
	public KonopolisView(){
		
		
		
		this.setTitle("Konopolis");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(600,200);
		this.setVisible(true);
	}




	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

}
