
package src.konopolis.view;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Movie;
import src.konopolis.model.Room;
import src.konopolis.model.Show;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @author nathan
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
	private ArrayList<String> listTitles = new ArrayList<String>();
	private String [] shows = new String [50];
	
	
	public KonopolisViewGUI(KonopolisModel model, KonopolisController control) {
		super(model, control);
		//Define defaultDiplay
		displayRoom .setIcon(new ImageIcon("img/Konopolus_1.0.jpg"));
		displayRoom.setSize(500,250);
		displayRoom.setBackground(Color.WHITE);
		init();
	}

	public void update(Observable arg0, Object arg1) {

	}

	public void init() {
		
		//Define frame
		frame = new JFrame("Konopolis");
		frame.setVisible(true);
		frame.setSize(1200,900);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.setAlwaysOnTop(false);
		
		
		// Define panelSelect
		panelSelect = new JPanel();
		panelSelect.setSize(400, 200);
		panelSelect.setMinimumSize(new Dimension(500,200));
		panelSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		panelSelect.setVisible(true);
		
		// Label
		JLabel selectAMovie = new JLabel("Sélectionnez un film: ");

		//Fill the ArrayList with movie's titles
		for (Map.Entry<Integer, String> entry : control.retrieveAllMoviesTitles().entrySet()) {
			listTitles.add(entry.getValue());
		}
		
		//Fill a simple array of titles
		String [] titles = new String [listTitles.size()];
		
		for(int i = 0 ; i<listTitles.size() ; i++){
			titles[i] = listTitles.get(i);
		}
		 
		//Give the list to the ComboBox
		moviesList=new JComboBox<String>(titles);
		//moviesList.setSelectedIndex();
		
		//Create a default show ComboBox
		showsList=new JComboBox<String>();
		showsList.setPreferredSize(new Dimension(300,25));

		//Define ComboBox movies
		moviesList.setPreferredSize(new Dimension(300,25));

		moviesList.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent eventSource) {
				displayShows();
	    	}
		});

		// Define config button
		config = new JButton("Configuration");

		config.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAuth();
            }
        });// TODO => Ajouter event pop nouvelle fenêtre config
		
		// Add Label, ComboBox + button at panelSelect
        panelSelect.add(selectAMovie);
		panelSelect.add(moviesList);
		panelSelect.add(showsList);
		panelSelect.add(config);
		
		//Add TextField to the pabelDisplay
		infos = new JTextArea();
		infos.setWrapStyleWord(true);
		infos.setEditable(false);
		infos.setSize(new Dimension(200,500));
		infos.setFont(new Font("Roboto", Font.BOLD,16));
		infos.setBackground(Color.LIGHT_GRAY);
		infos.setBorder(BorderFactory.createCompoundBorder(
		        infos.getBorder(),
		        BorderFactory.createEmptyBorder(10, 10, 10, 10)
                )
        );
		
		//Define panelDisplay
		
		panelDisplay = new JPanel();
		panelDisplay.setSize(500,500);
		panelDisplay.setBackground(Color.WHITE);
		panelDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
		panelDisplay.setVisible(true);
		panelDisplay.setMinimumSize(500, 500);
				
		//Define panelInfo
				
		panelInfo = new JPanel();
		panelInfo.setBackground(Color.lightGray);
		panelInfo.add(infos);
		
		//Add panels to frame
		
		frame.add(panelSelect,BorderLayout.NORTH);
		frame.add(panelInfo, BorderLayout.SOUTH);
		frame.validate();
		frame.pack();

        displayShows();
		
	}
		/**
		 * Fill the ComboBox of Shows
		 */
		private void displayShows() {
			//System.out.println(moviesList.getSelectedItem() + " - " + moviesList.getSelectedIndex());
			String choiceMovie = (String) moviesList.getSelectedItem();
			int idMovie = control.retrieveMovieId(choiceMovie);
			System.out.println(idMovie);
			Movie movie = control.retrieveMovie(idMovie);
			showsList.removeAllItems();
			infos.setText(
			        movie.getDescription() +
                    " Prix: " + movie.getPrice()
            );
			
			for(Show sh: movie.getShows()){
				showsList.addItem("Salle n°" + sh.getRoom_id() + " - " + control.dateInFrench(sh.getShow_start()));
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
			Room selectedRoom = control.retrieveRoom(selectedShow.getMovie_id(), selectedShow.getRoom_id(), selectedShow.getShow_start());
			panelDisplay = new JPanel(new GridLayout(selectedRoom.getRows(), selectedRoom.getSeatsByRow()));

            for (int i = 0; i < selectedRoom.getRows(); i++) {
                for (int j = 0; j < selectedRoom.getSeatsByRow(); j++) {
                    // Button
                    JButton seatButton = new JButton(i + ", " + j);
                    seatButton.setBorder(BorderFactory.createEmptyBorder());
                    seatButton.setBorderPainted(false);
                    if (selectedRoom.getSeats().get(i).get(j).isTaken()) {
                        seatButton.setBackground(Color.decode("#e74c3c"));
                    } else {
                        seatButton.setBackground(Color.gray);
                    }
                    seatButton.setPreferredSize(new Dimension(75, 75));
                    seatButton.setMargin(new Insets(0, 0, 0, 0));

                    // Panel for buttons
                    JPanel panelButtons = new JPanel();
                    panelButtons.setPreferredSize(new Dimension(75, 75));
                    panelButtons.setBorder(BorderFactory.createEmptyBorder());
                    panelButtons.add(seatButton);

                    // Add to the Map
                    panelDisplay.add(panelButtons);
                }
            }
            // Add the Map to the Jframe
            frame.add(panelDisplay,BorderLayout.CENTER);
		}

        private void displayAuth() {
            // TODO auth form
            // if ok => config
        }
}
