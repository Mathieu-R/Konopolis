/**
 * 
 */
package konopolis.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import konopolis.controller.KonopolisController;
import konopolis.model.KonopolisModel;
import konopolis.model.Room;
import konopolis.model.Show;

/**
 * @author natha
 *
 */
public class KonopolisViewGUI extends KonopolisView implements Observer {
	
	//Instantiate elements of GUI
	private JFrame frame;
	private JPanel panelSelect;
	private JPanel panelDisplay;
	private JPanel panelInfo;
	private JComboBox moviesList;
	private JComboBox<String> showsList;
	private JButton config;
	private JTextArea infos;
	
	private JLabel displayRoom =new JLabel();

	
	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private String enteredType = "";
	private LocalDateTime show_start;
	private ArrayList<String> listTitles=new ArrayList();
	private String [] shows = new String [50];
	
	
	public KonopolisViewGUI(KonopolisModel model, KonopolisController control) {
		super(model, control);
		//Define defaultDiplay
		displayRoom .setIcon(new ImageIcon("img/Konopolus_1.0.jpg"));
		displayRoom.setSize(500,250);
		displayRoom.setBackground(Color.WHITE);
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
		frame.setSize(1200,900);
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
		
		
		
		//Fill the ArrayList with movie's titles
		
		for (Map.Entry<Integer, String> entry : control.retrieveAllMoviesTitles().entrySet()) {
		    
			listTitles.add(entry.getValue());
				
		}
		
		//Fill a simple array of titles
		String [] titles =new String [listTitles.size()+1];
		titles[0] = "Sélectionnez un film";
		
		for(int i =0;i<listTitles.size();i++){
			titles[i+1]=listTitles.get(i);
		
		}
		 
		//Give the list to the ComboBox
		
		moviesList=new JComboBox<String>(titles);
		moviesList.setSelectedIndex(1);
		
		//Create a default show ComboBox
		
		showsList=new JComboBox<String>();
		showsList.addItem("Veuillez choisir un film");
		showsList.setPreferredSize(new Dimension(300,25));
		
		
		//Define ComboBox movies
		
		moviesList.setPreferredSize(new Dimension(300,25));
		moviesList.addActionListener(new ActionListener() {
			  
				public void actionPerformed(ActionEvent eventSource) {
					
					displayShows();
					
			   }
		});

		//Define config button
		
		config = new JButton("Configuration");
		config.addActionListener(null);//=====================================================================> Ajouter event pop nuvelle fenêtre config
		
		//Add ComboBox + button at panelSelect
		
		panelSelect.add(moviesList);
		panelSelect.add(showsList);
		panelSelect.add(config);
		
		//Add TextField to the pabelDisplay
		
		infos=new JTextArea();
		infos.setEditable(false);
		infos.setSize(new Dimension(200,500));
		infos.setFont(new Font("Roboto",Font.BOLD,16));
		infos.setBackground(Color.LIGHT_GRAY);
		infos.setBorder(BorderFactory.createCompoundBorder(
		        infos.getBorder(), 
		        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		
		//Define panelDisplay
		
				panelDisplay=new JPanel();
			
				panelDisplay.setSize(500,250);
				panelDisplay.setBackground(Color.WHITE);
				panelDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
				panelDisplay.setVisible(true);
				panelDisplay.add(displayRoom,BorderLayout.CENTER);
				
		//Define panelInfo
				
				panelInfo=new JPanel();
				panelInfo.setBackground(Color.lightGray);
				panelInfo.add(infos);
		
		//Add panels to frame
		
		frame.add(panelSelect,BorderLayout.NORTH);
		frame.add(panelDisplay,BorderLayout.CENTER);
		frame.add(panelInfo, BorderLayout.SOUTH);
		frame.validate();
		frame.pack();
		
	}
		/**
		 * Fill the ComboBox of Shows
		 */
		public void displayShows(){
			
			String choiceMovie =(String)moviesList.getSelectedItem();
			
			int idMovie=control.retrieveMovieId(choiceMovie);
			
			System.out.println(idMovie);
			
			control.retrieveMovie(idMovie);
			
			showsList.removeAllItems();
			
			infos.setText(control.getMovies_al().get(0).getDescription()+" 	Prix:"+control.getMovies_al().get(0).getPrice());
			
			for(Show sh: control.getShows_al()){
				showsList.addItem("Salle "+sh.getRoom_id()+"-"+control.dateInFrench(sh.getShow_start()));
			} 		
			//Select the first item of the list
			showsList.setSelectedIndex(0);
			
			showsList.addActionListener(new ActionListener() {
				  
				public void actionPerformed(ActionEvent eventSource) {
					
					displayRoom();
					
			   }
		});
		}
		/**
		 * Display the room at the moment of the show
		 */
		private void displayRoom() {
			
			Show selectedShow = control.getShows_al().get(showsList.getSelectedIndex());
			
			System.out.println(showsList.getSelectedIndex());
			
			Room selectedRoom = control.retrieveRoom(selectedShow.getMovie_id(),selectedShow.getRoom_id(), selectedShow.getShow_start());
			
			panelDisplay=new JPanel(new GridLayout(selectedRoom.getRows(),selectedRoom.getSeatsByRow()));
			
		}
	
}
