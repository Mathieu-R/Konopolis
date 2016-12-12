/**
 * 
 */
package konopolis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import konopolis.controller.KonopolisController;
import konopolis.model.KonopolisModel;

/**
 * @author natha
 *
 */
public class KonopolisViewGUI extends KonopolisView implements Observer {
	
	//Instantiate elements of GUI
	private JFrame frame;
	private JPanel panelSelect;
	private JPanel panelDisplay;
	private JComboBox moviesList;
	private JComboBox showsList;
	private JButton config;
	
	private JLabel defaultDisplay=new JLabel();

	
	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private String enteredType = "";
	private LocalDateTime show_start;
	private ArrayList<String> listTitles=new ArrayList();
	private String [] titles=new String[50];
	private String [] shows = new String [50];
	
	
	public KonopolisViewGUI(KonopolisModel model, KonopolisController control) {
		super(model, control);
		//Define defaultDiplay
		defaultDisplay.setIcon(new ImageIcon("img/Konopolus_1.0.jpg"));
		init();
	}

	@Override
	
	public void update(Observable arg0, Object arg1) {		
	}

	@Override
	public void init() {
		
		//Define frame
		frame=new JFrame("Konopolis");
		frame.setVisible(true);
		frame.setSize(1000,1000);
		frame.setMinimumSize(new Dimension(500,500));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.setAlwaysOnTop(true);
		
		
		//Define panelSelect
		
		panelSelect=new JPanel();
		panelSelect.setSize(400, 200);
		panelSelect.setMinimumSize(new Dimension(500,200));
		panelSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		panelSelect.setVisible(true);
		
		//Define panelDisplay
			
		panelDisplay=new JPanel();
		panelDisplay.setBackground(Color.WHITE);
		panelDisplay.add(defaultDisplay);
		panelDisplay.setSize(400,250);
		panelDisplay.setMinimumSize(new Dimension(500,250));
		panelDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
		panelDisplay.setVisible(true);
		
		
		
		//Fill the ArrayList with movie's titles
		
		for (Map.Entry<Integer, String> movieEntry: control.retrieveAllMoviesTitles().entrySet()) {
			listTitles.add(movieEntry.getKey()+"."+movieEntry.getValue());
        }
		
		//Fill a simple array of titles
		
		for(int i =0;i<listTitles.size();i++){
			titles[i]=listTitles.get(i);
		
		}
		 
		//Give the list to the ComboBox
		
		moviesList=new JComboBox(titles);
		
		//Define ComboBox movies
		
		moviesList.setPreferredSize(new Dimension(175,25));
		moviesList.addActionListener(new ActionListener() {
			   public void actionPerformed(ActionEvent eventSource) {
				   
				   //choiceMovie contains the selected movie
				   String choiceMovie = (String) moviesList.getSelectedItem();
				   
				   //Display the choice
				   System.out.println(choiceMovie);
				   
				   //===========================================================================================>Split choiceMovie to get ID
			   }
		});
		
		//Create a default show ComboBox
		
		String[]shows={"Veuillez choisir un film"};
		showsList=new JComboBox(shows);
		
		//Define ComboBox movies
		
		showsList.setPreferredSize(new Dimension(175,25));
		
		//Define config button
		
		config = new JButton("Configuration");
		config.addActionListener(null);//=====================================================================> Ajouter event pop nuvelle fenêtre config
		
		//Add ComboBox + button at panelSelect
		
		panelSelect.add(moviesList);
		panelSelect.add(showsList);
		panelSelect.add(config);
		
		//Add panels to frame
		
		frame.add(panelSelect,BorderLayout.NORTH);
		frame.add(panelDisplay);
		frame.validate();
		frame.pack();
		
	}
}
