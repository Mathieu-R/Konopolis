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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import konopolis.controller.KonopolisController;
import konopolis.model.KonopolisModel;
import konopolis.model.Room;
import konopolis.model.Show;

/**
 * @author natha
 *
 */
public class KonopolisViewGUI extends KonopolisView implements Observer ,Runnable {
	
	//Instantiate elements of GUI
	private JFrame frame;
	//JPanels
	private JPanel panelSelect;
	private JPanel panelDisplay;
	private JPanel panelInfo;
	private JPanel panelCommand;
	//Comboxes
	private JComboBox moviesList;
	private JComboBox<String> showsList;
	//Buttons
	private JButton config;
	private JButton confirm;
	
	private JTextArea infos;
	private JTextArea msgUser;
	
	
	private JScrollPane scrollInfos;
	
	private JLabel displayRoom =new JLabel();

	
	private ArrayList<String> listTitles=new ArrayList();
	public KonopolisViewGUI(KonopolisModel model, KonopolisController control) {
		super(model, control);
		//Define defaultDiplay
	}

	@Override
	
	public void update(Observable arg0, Object arg1) {		
	}

	public void run() {
		// TODO Auto-generated method stub
		init();
	}
	@Override
	public void init() {
		//Define frame
		frame=new JFrame("Konopolis");
		frame.setVisible(true);
		frame.setSize(750,750);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.setAlwaysOnTop(true);
		
		displayRoom.setIcon(new ImageIcon("img/Konopolus_1.0.jpg"));
		displayRoom.setBackground(Color.WHITE);
		//Define panelSelect
		
		panelSelect=new JPanel();
		panelSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		panelSelect.setLayout(new FlowLayout());
		panelSelect.setVisible(true);
		
		
		
		//Fill the ArrayList with movie's titles
		
		for (Map.Entry<Integer, String> entry : control.retrieveAllMoviesTitles().entrySet()) {
		    
			listTitles.add(entry.getValue());
				
		}
		
		//Fill a simple array of titles
		String [] titles =new String [listTitles.size()];
		
		for(int i =0;i<listTitles.size();i++){
			titles[i]=listTitles.get(i);
		
		}
		 
		//Give the list to the ComboBox
		
		moviesList=new JComboBox<String>(titles);
		moviesList.setSelectedIndex(0);
		
		//Create a default show ComboBox
		
		showsList=new JComboBox<String>();
		showsList.addItem("Veuillez choisir un film");
		showsList.setPreferredSize(new Dimension(300,25));
		
		
		//Define ComboBox movies
		
		moviesList.setPreferredSize(new Dimension(250,25));
		moviesList.addActionListener(new ActionListener() {
			  
				public void actionPerformed(ActionEvent eventSource) {
					
					displayShows();
					
			   }
		});

		//Define config button
		
		config = new JButton("Configuration");
		config.setPreferredSize(new Dimension(125,25));
		config.addActionListener(null);//=====================================================================> Ajouter event pop nuvelle fenêtre config
		
		//Add ComboBox + button at panelSelect
		
		panelSelect.add(moviesList,BorderLayout.WEST);
		panelSelect.add(showsList,BorderLayout.CENTER);
		panelSelect.add(config,BorderLayout.EAST);
		panelSelect.setPreferredSize(new Dimension(50,35));
		panelSelect.setBorder(new EmptyBorder(0, 0, 100, 0));
		
		//Add TextField to the pabelDisplay
		
		infos=new JTextArea(10,10);
		infos.setEditable(false);
		infos.setFont(new Font("Roboto",Font.BOLD,16));
		infos.setBackground(Color.LIGHT_GRAY);
		infos.setBorder(BorderFactory.createCompoundBorder(
		        infos.getBorder(), 
		        BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		infos.setLineWrap(true);
		infos.setWrapStyleWord(true);
		
		//Define content of scrollInfos
		
		scrollInfos=new JScrollPane(infos);
		scrollInfos.setPreferredSize(new Dimension(frame.getWidth(),200));
		
		//Define panelDisplay
		
				panelDisplay=new JPanel();
				panelDisplay.setBackground(Color.WHITE);
				panelDisplay.setPreferredSize(new Dimension(250,250));
				panelDisplay.setBorder(BorderFactory.createLineBorder(Color.black));
				panelDisplay.setVisible(true);
				panelDisplay.add(displayRoom,BorderLayout.CENTER);
		//Define confirm button
				
				confirm=new JButton("Confirmer");
				confirm.setActionCommand("Confirm");
				
		//Define textArea msgUser
				
				msgUser=new JTextArea();
				msgUser.setEditable(false);
				msgUser.setPreferredSize(new Dimension(600,20));
				msgUser.setBackground(Color.LIGHT_GRAY);
				msgUser.setText("Test");
				
		//Define panelInfo
				
				panelInfo=new JPanel();
				panelInfo.setPreferredSize(new Dimension(500,200));
				panelInfo.setBackground(Color.lightGray);
				panelInfo.add(scrollInfos);
				
		//Define panelCommand
				
				panelCommand=new JPanel();
				panelCommand.add(msgUser,BorderLayout.LINE_START);
				panelCommand.add(confirm, BorderLayout.EAST);
				panelCommand.setBackground(Color.LIGHT_GRAY);
				panelCommand.setBorder(BorderFactory.createLineBorder(Color.black));
		
		//Add panels to frame
		
		frame.add(panelSelect,BorderLayout.NORTH);
		frame.add(panelDisplay,BorderLayout.CENTER);
		frame.add(panelInfo,BorderLayout.SOUTH);
		frame.add(panelCommand,BorderLayout.SOUTH);
		frame.validate();
	

		
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
			
			infos.setText(control.getMovies_al().get(0).getDescription());
			infos.append("\nPrix:"+control.getMovies_al().get(0).getPrice());
			
			for(Show sh: control.getShows_al()){
				showsList.addItem("Salle "+sh.getRoom_id()+"-"+control.dateInFrench(sh.getShow_start()));
			} 		
			//Select the first item of the list
			showsList.setSelectedIndex(0);
			
			showsList.addActionListener(new ActionListener() {
				  
				public void actionPerformed(ActionEvent eventSource) {
					
				if((String)moviesList.getSelectedItem()=="Veuillez choisir un film")	{
					
				}else{
					displayRoom();
				}
			   }
		});
		}
		/**
		 * Display the room at the moment of the show
		 */
		private void displayRoom() {
			
			int size = 0;
	        int rows = 0;
	        int columns = 0;

	        // Select the show
	        Show selectedShow = control.getShows_al().get(showsList.getSelectedIndex());
	        // Select the room and its customers
	        Room selectedRoom = control.retrieveRoom(selectedShow.getMovie_id(), selectedShow.getRoom_id(), selectedShow.getShow_start());
	        // size of room (rows, seats by row)
	        rows = selectedRoom.getRows();
	        columns = selectedRoom.getSeatsByRow();
	        // size of every seat of the mapping
	        if (rows >= 30 || columns >= 30) {
	            size = 25;
	        } else if (rows > 20 && rows < 30 || columns > 20 && columns < 30) {
	            size = 50;
	        } else {
	            size = 75;
	        }
	        System.out.println(size);
	        // Remove the last mapping
	        // Then, new Grid (will contain the mapping of the room)
	        panelDisplay.removeAll();
	        panelDisplay.setLayout(new GridLayout(rows, columns));
	        panelDisplay.setPreferredSize(new Dimension(rows * size, columns * size));

	        //JButton[][] grid = new JButton[selectedRoom.getRows()][selectedRoom.getSeatsByRow()]; // grid of JButtons
	        for (int y = 0; y < rows; y++) {
	            for (int x = 0; x < columns; x++) {
	                // Button
	                JButton seat = new JButton();
	                seat.setOpaque(false);
	                seat.setBorderPainted(false);
	                seat.setContentAreaFilled(false);
	                
	                
	                ImageIcon emptySit =new ImageIcon("img/emptySit.png");
	                ImageIcon takenSit = new ImageIcon("img/takenSit.png");
	                ImageIcon waitingSit=new ImageIcon("img/waitingSit.png");
	                ImageIcon selectionSit=new ImageIcon("img/selectionSit.png");

	                if (selectedRoom.getSeats().get(y).get(x).isTaken()) {
	                    seat.setIcon(takenSit);
	                } else {
	                    seat.setIcon(emptySit);
	                }

	                // Event Listener
	                int finalX = x;
	                int finalY = y;
	                seat.addMouseListener(new MouseAdapter() {
	                	boolean clicked=false;
	                	String change;
	                    @Override
	                    public void mouseEntered(MouseEvent e) {
	                        if (!(selectedRoom.getSeats().get(finalY).get(finalX).isTaken() && seat.getIcon().equals(emptySit))) {
	                        	change+="\n"+seat.getIcon().toString()+"-->";
	                            seat.setIcon(selectionSit);;
	                            change+=seat.getIcon().toString();
	                            System.out.println(change);
	                        }
	                    }

	                    @Override
	                    public void mouseExited(MouseEvent e) {
	                        if(seat.getIcon().equals(selectionSit) && !clicked) {
	                        	change+="\n"+seat.getIcon().toString()+"-->";
	                            seat.setIcon(emptySit);
	                            change+=seat.getIcon().toString();
	                            System.out.println(change);
	                            clicked=false;
	                        }
	                    }
	                    
	                    public void mouseClicked(MouseEvent e){
	                    	 if (!(selectedRoom.getSeats().get(finalY).get(finalX).isTaken() && seat.getIcon().equals(emptySit))) {
	                    		 	change+="\n"+seat.getIcon().toString()+"-->";
	                    		 	seat.setIcon(waitingSit);
	                    		 	change+=seat.getIcon().toString();
		                            System.out.println(change);
		                            clicked=true;
		                      
		                           
		                     } 
	                    	 else if(seat.getIcon().equals(waitingSit) ){
	                    		 change+="\n"+seat.getIcon().toString()+"-->";
		                    	 seat.setIcon(emptySit);
		                    	 change+=seat.getIcon().toString();
		                          System.out.println(change);
		                    	 msgUser.setText("Siège rendu");
		                     }
	                    	 else{
			                        msgUser.setText("Siège déja alloué");	
			                 }
	                    	
	                    	 
	                    }
	                    
	                });

	                // Add to the Map
					panelDisplay.add(seat);
	            }
	        }
	        panelDisplay.repaint();
	        panelDisplay.revalidate();
	        // Add the Map to the Jframe
	        frame.add(panelDisplay, BorderLayout.CENTER);
	    }

	    private void displayAuth() {
	        // TODO auth form
	        // if ok => config
	    }

	    public void actionPerformed(ActionEvent e){
	        String cmd = e.getActionCommand();
	        if (cmd.equals("Show")) {
	            displayRoom();
	        }
	        else if (cmd.equals("Config")) {
	            displayAuth();
	        }else if(cmd.equals("Confirm")){
	        	//
	        }
			
		}


		
	
}
