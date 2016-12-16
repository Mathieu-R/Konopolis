
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/**
 * @author nathan
 *
 */
public class KonopolisViewGUI extends KonopolisView implements Observer, ActionListener {

	// Instantiate elements of GUI
	private JFrame frame;
	private JPanel panelSelect = new JPanel();
	private JPanel panelDisplay = new JPanel();
	private JPanel panelInfo = new JPanel();
	private JComboBox<String> moviesList = new JComboBox<String>();
	private JComboBox<String> showsList = new JComboBox<String>();
	private JButton config = new JButton("Configuration");
	private JTextArea infos = new JTextArea();
	private JLabel displayRoom = new JLabel();

	private int movie_id = 0;
	private int room_id = 0;
	private int show_id = 0;
	private String enteredType = "";
	private LocalDateTime show_start;
	private ArrayList<String> listTitles = new ArrayList<String>();
	private Room selectedRoom;

    private ImageIcon emptySit = new ImageIcon("img/emptySit.png");
    private ImageIcon takenSit = new ImageIcon("img/takenSit.png");
    private ImageIcon waitingSit = new ImageIcon("img/waitingSit.png");
    private ImageIcon selectionSit = new ImageIcon("img/selectionSit.png");

	public KonopolisViewGUI(KonopolisModel model, KonopolisController control) {
		super(model, control);
		//Define defaultDiplay
		displayRoom .setIcon(new ImageIcon("img/Konopolus_1.0.jpg"));
		displayRoom.setSize(500,250);
		displayRoom.setBackground(Color.WHITE);
		init();
	}

	public void init() {
        frame = new JFrame("Konopolis");

		// Define frame
		frame.setVisible(true);
		frame.setSize(750,750);
		frame.setMinimumSize(new Dimension(750, 750));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.setAlwaysOnTop(false);

		displayRoom.setIcon(new ImageIcon("img/Konopolus_1.0.jpg"));
		displayRoom.setBackground(Color.white);

		// Define panelSelect
		panelSelect.setSize(400, 200);
		//panelSelect.setMinimumSize(new Dimension(500,200));
		panelSelect.setBorder(BorderFactory.createLineBorder(Color.black));
		panelSelect.setLayout(new FlowLayout());
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

		// Give the list to the ComboBox
		moviesList = new JComboBox<String>(titles);
        moviesList.setActionCommand("Movies"); // Action for ActionListener
        moviesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayShows();
            }
        });

		// Create a default show ComboBox
		showsList.setActionCommand("Shows"); // Action for ActionListener
        showsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayRoom();
            }
        });

		showsList.setPreferredSize(new Dimension(300,25));

		//Define ComboBox movies
		moviesList.setPreferredSize(new Dimension(300,25));

		// Define config button
		config.setActionCommand("Config");
		config.addActionListener(this);

		// Add Label, ComboBox + button at panelSelect
        panelSelect.add(selectAMovie);
		panelSelect.add(moviesList);
		panelSelect.add(showsList);
		panelSelect.add(config);

		// Add TextField to the labelDisplay
		infos.setWrapStyleWord(true);
		infos.setEditable(false);
		infos.setSize(new Dimension(500,100));
		infos.setFont(new Font("Roboto", Font.BOLD, 16));
		infos.setBackground(Color.LIGHT_GRAY);
		infos.setBorder(BorderFactory.createCompoundBorder(infos.getBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollInfos = new JScrollPane(infos);
        scrollInfos.setPreferredSize(new Dimension(frame.getWidth(),100));

		//Define panelInfo
		panelInfo.setBackground(Color.lightGray);
        panelInfo.setPreferredSize(new Dimension(500,100));
		panelInfo.add(infos);

		panelDisplay = new JPanel();
		panelDisplay.add(displayRoom);
		panelDisplay.setBackground(Color.WHITE);
		panelDisplay.setPreferredSize(new Dimension(250, 250));
        panelDisplay.setVisible(true);

        JButton confirm = new JButton("Confirmer");
        confirm.setActionCommand("Confirm");
        confirm.addActionListener(this);

        //Define textArea msgUser

        JTextArea msgUser=new JTextArea();
        msgUser.setEditable(false);
        msgUser.setPreferredSize(new Dimension(600,20));
        msgUser.setBackground(Color.LIGHT_GRAY);
        msgUser.setText("Test");


        //Define panelCommand

        JPanel panelCommand = new JPanel();
        panelCommand.add(msgUser,BorderLayout.LINE_START);
        panelCommand.add(confirm, BorderLayout.EAST);
        panelCommand.setBackground(Color.LIGHT_GRAY);
        panelCommand.setBorder(BorderFactory.createLineBorder(Color.black));

        //Add panels to frame

        frame.add(panelSelect,BorderLayout.NORTH);
        frame.add(panelDisplay,BorderLayout.CENTER);
        frame.add(panelInfo,BorderLayout.SOUTH);
        frame.add(panelCommand,BorderLayout.SOUTH);

		//Add panels to frame

		frame.add(panelSelect,BorderLayout.NORTH);
		//frame.add(panelDisplay);
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
        //System.out.println(idMovie);
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
        selectedRoom = control.retrieveRoom(selectedShow.getMovie_id(), selectedShow.getRoom_id(), selectedShow.getShow_start());
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
                seat.setActionCommand("Book");


                if (selectedRoom.getSeats().get(y).get(x).isTaken()) {
                    seat.setIcon(takenSit);
                } else {
                    //seat.setBackground(Color.BLACK);
                    seat.setIcon(emptySit);
                }

                // Event Listener
                int finalX = x;
                int finalY = y;
                seat.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (!(selectedRoom.getSeats().get(finalY).get(finalX).isTaken()) && !(seat.getIcon().equals(waitingSit))) {
                            //System.out.println("mouseentered");
                            seat.setIcon(selectionSit);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(seat.getIcon().equals(selectionSit) && !(seat.getIcon().equals(waitingSit))) {
                            seat.setIcon(emptySit);
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

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("click");
        System.out.println(e.getActionCommand());
        String cmd = e.getActionCommand();
        if (cmd.equals("Movie")) {

        }
        else if (cmd.equals("Show")) {
            displayRoom();
        }
        else if (cmd.equals("Config")) {
            displayAuth();
        }
        else if (cmd.equals("Book")) {
            //
        }
    }

    public void update(Observable arg0, Object arg1) {

    }
}
