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
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import konopolis.controller.KonopolisController;
import konopolis.model.InvalidUserException;
import konopolis.model.KonopolisModel;
import konopolis.model.Movie;
import konopolis.model.Room;
import konopolis.model.Show;

/**
 * @author nathan
 *
 */
public class KonopolisViewGUI extends KonopolisView implements Observer {

	// Instantiate elements of GUI
	private JFrame frame;
	private JPanel toolbar = new JPanel();
	private JPanel mappingRoom = new JPanel();
	private JPanel descriptionPanel = new JPanel();
	private JPanel bookingPanel = new JPanel(new BorderLayout());
	private JPanel statusbar = new JPanel();
	private JDialog authDialog;
	private JComboBox<String> moviesList = new JComboBox<String>();
	private JComboBox<String> showsList = new JComboBox<String>();
	private JComboBox<String> typesList = new JComboBox<String >();
	private JButton config = new JButton("Configuration");
	private JTextArea status = new JTextArea();
	private JLabel displayRoom = new JLabel();
	private JTextField username;
	private JTextField password;
	private JButton login;

    private Object[][] dataBooking = {};
    private String titlesFields[] = {"Place", "Type"};
    JTable bookBufferTable = new JTable(new DefaultTableModel()); // Table => Buffer of booking
    private ArrayList<ArrayList<Object>> dataCustomer = new ArrayList<ArrayList<Object>>();

	//private int movie_id = 0;
	//private int room_id = 0;
	//private int show_id = 0;
	//private String enteredType = "";
	//private LocalDateTime show_start;
	//private Room selectedRoom;

    private ImageIcon emptySit = new ImageIcon("img/emptySit.png");
    private ImageIcon takenSit = new ImageIcon("img/takenSit.png");
    private ImageIcon selectionSeat = new ImageIcon("img/waitingSit.png");
    private ImageIcon waitingSeat = new ImageIcon("img/selectionSit.png");

	public KonopolisViewGUI(KonopolisModel model, KonopolisController control) {
		super(model, control);
        try { // Apply the system appearance
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        init();
	}

	public void init() {
	    // Create main Frame
        frame = new JFrame("Konopolis"); // Title
		frame.setVisible(true);
		frame.setSize(1500,900);
		frame.setMinimumSize(new Dimension(1500, 900));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try { // Icon
            frame.setIconImage(ImageIO.read(new File("img/Konopolus_1.0.jpg")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        frame.setLayout(new BorderLayout());
		frame.setResizable(true);
		frame.setAlwaysOnTop(false);

		// Make all the panel
		makeToolbar();
		makeRoomMapping();
		makeDescriptionPanel();
		makeStatusBar();
		makeBookingPanel();
		makeAuthDialog();

        // Add panels to the frame
        frame.add(toolbar, BorderLayout.NORTH);
        frame.add(mappingRoom, BorderLayout.CENTER);
        frame.add(descriptionPanel, BorderLayout.WEST);
        frame.add(bookingPanel, BorderLayout.EAST);
        frame.add(statusbar, BorderLayout.SOUTH);

		frame.validate(); // Validate
		frame.pack(); // Pack

		// Show the list of shows for the movie selected
        //displayShows(0);
        addEventListeners(); // Launch the events listeners
	}

	public void makeToolbar() {
        // Define the toolbar
        toolbar.setSize(400, 200);
        toolbar.setBorder(BorderFactory.createLineBorder(Color.black));
        toolbar.setLayout(new FlowLayout());
        toolbar.setVisible(true);

        // Label
        JLabel selectAMovie = new JLabel("Sélectionnez un film: "); // Label

        // Fill the Combobox with movie's titles | types
        control.retrieveAllMoviesTitles().entrySet().forEach(movie -> moviesList.addItem(movie.getValue()));
        control.retrieveTypes().forEach(type -> typesList.addItem(type));

        moviesList.setPreferredSize(new Dimension(300,25));
        moviesList.setActionCommand("Movies"); // Action for ActionListener
        moviesList.setSelectedIndex(0);

        // Create a default show ComboBox
        showsList.setPreferredSize(new Dimension(300,25));
        showsList.setActionCommand("Shows"); // Action for ActionListener

        typesList.setPreferredSize(new Dimension(300, 25));

        // Define config button
        config.setActionCommand("Config");

        // Add Label, ComboBoxs + Button at the toolbar
        toolbar.add(selectAMovie);
        toolbar.add(moviesList);
        toolbar.add(showsList);
        toolbar.add(typesList);
        toolbar.add(config);
    }

    public void makeDescriptionPanel() {
        // Define descriptionPanel
        //descriptionPanel.setForeground(Color.white);
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.PAGE_AXIS));
        descriptionPanel.setBorder(new EmptyBorder(new Insets(10,5,10,5)));
        descriptionPanel.setBackground(Color.BLACK);
        descriptionPanel.setPreferredSize(new Dimension(250, 900));
        descriptionPanel.setMinimumSize(new Dimension(250, 900));
    }

    public void makeBookingPanel() {
        bookingPanel.setBackground(Color.lightGray);
        bookingPanel.setPreferredSize(new Dimension(300, 900));
        bookingPanel.setMinimumSize(new Dimension(300, 900));

        bookBufferTable.setPreferredSize(new Dimension(280, 500));
        //bookBufferTable.addColumn();
        bookingPanel.add(new JScrollPane(bookBufferTable));
    }

    public void makeStatusBar() {
        statusbar.setBackground(Color.LIGHT_GRAY);
        statusbar.setBorder(BorderFactory.createLineBorder(Color.black));

        status.setWrapStyleWord(true);
        status.setEditable(false);
        status.setSize(new Dimension(300,100));
        status.setFont(new Font("Roboto", Font.BOLD, 16));
        status.setBackground(Color.LIGHT_GRAY);
        status.setBorder(BorderFactory.createCompoundBorder(status.getBorder(), BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JScrollPane scrollInfos = new JScrollPane(status);
        scrollInfos.setPreferredSize(new Dimension(frame.getWidth(),100));

        JTextArea msgUser = new JTextArea();
        msgUser.setEditable(false);
        msgUser.setPreferredSize(new Dimension(600,20));
        msgUser.setBackground(Color.LIGHT_GRAY);
        msgUser.setText("Test");

        //JPanel statusbar = new JPanel();
        statusbar.add(msgUser,BorderLayout.LINE_START);
    }

    public void makeRoomMapping() {
        // Icon when there is no show selected
        displayRoom.setIcon(new ImageIcon("img/Konopolis_1.0.jpg"));
        displayRoom.setBackground(Color.white);

        // Panel that will contain the mapping of the room
        //mappingRoom = new JPanel();
        mappingRoom.add(displayRoom);
        mappingRoom.setBackground(Color.WHITE);
        mappingRoom.setPreferredSize(new Dimension(900, 900));
        mappingRoom.setMinimumSize(new Dimension(900, 900));
        mappingRoom.setVisible(true);
    }

    private void makeAuthDialog() {
        authDialog = new JDialog(frame, "Connection");
        authDialog.setMinimumSize(new Dimension(500, 300));
        authDialog.setForeground(Color.white);
        authDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        authDialog.setLocationRelativeTo(null);
        JPanel authPanel = new JPanel(new FlowLayout());
        //authDialog.setAlwaysOnTop(true);
        JLabel banner = new JLabel("Espace réservé aux Admins, veuillez vous connecter !");
        banner.setFont(new Font("Roboto", Font.CENTER_BASELINE, 16));
        banner.setForeground(Color.red);
        banner.setHorizontalAlignment(SwingConstants.CENTER);
        banner.setVerticalAlignment(SwingConstants.TOP);
        JLabel usernameLabel = new JLabel("Nom d'utilisateur: ");
        usernameLabel.setPreferredSize(new Dimension(100, 30));
        username = new JTextField();
        username.setPreferredSize(new Dimension(300, 30));
        username.setMinimumSize(new Dimension(100, 50));
        JLabel passwordLabel = new JLabel("Mot de passe: ");
        passwordLabel.setPreferredSize(new Dimension(100, 30));
        password = new JTextField();
        password.setPreferredSize(new Dimension(300, 30));
        login = new JButton("Se connecter");
        authPanel.add(banner);
        authPanel.add(usernameLabel);
        authPanel.add(username);
        authPanel.add(passwordLabel);
        authPanel.add(password);
        authPanel.add(login);
        authDialog.add(authPanel);
        authDialog.setVisible(false);
    }

    private void makeConfigDialog() {

    }

    private void makeNewMovieDialog() {

    }

    /**
     * Fill the ComboBox of Shows
     */
    private void displayShows(Movie movie) {

        showsList.removeAllItems();

        for(Show sh: movie.getShows()){
            System.out.println(sh.getShow_start());
            showsList.addItem("Salle n°" + sh.getRoom_id() + " - " + control.dateInFrench(sh.getShow_start()));
        }
        //Select the first item of the list
        //showsList.setSelectedIndex(0);
    }

    private void displayDescription(Movie movie) {
        // Title
        JLabel title = new JLabel("<html><u>" + movie.getTitle() + "</u></html>");
        title.setHorizontalAlignment(SwingConstants.CENTER); // align-center
        title.setForeground(Color.white); // color
        title.setFont(new Font("Arial", Font.BOLD, 24)); // font
        descriptionPanel.add(title);

        // Description
        JLabel description = new JLabel("<html>" + movie.getDescription() + "</html>");
        description.setForeground(Color.white);
        descriptionPanel.add(description);

        // Director
        JLabel director = new JLabel("<html><p>" + movie.getDirector() + "</p></html>");
        director.setHorizontalAlignment(SwingConstants.CENTER);
        director.setForeground(Color.lightGray);
        descriptionPanel.add(director);

        // Acteurs
        //JLabel actors = new JLabel("Acteurs principaux: ");
        //descriptionPanel.add(actors);
        for (String actor : movie.getCasting()){
            JLabel curActor = new JLabel(actor);
            curActor.setForeground(Color.white);
            descriptionPanel.add(curActor);
        }

        // Genres
        JLabel genres = new JLabel("Genres: ");
        descriptionPanel.add(genres);
        for(String genre : movie.getGenres()){
            JLabel curGenre = new JLabel("> " + genre);
            curGenre.setForeground(Color.white);
            descriptionPanel.add(curGenre);
        }

        // Language
        JLabel language = new JLabel("Langue: " + movie.getLanguage());
        language.setForeground(Color.white);
        descriptionPanel.add(language);

        // Duration
        JLabel duration = new JLabel("Durée: " + movie.getTime() + "min");
        duration.setForeground(Color.white);
        descriptionPanel.add(duration);

        // Price
        JLabel price = new JLabel("Prix: " + movie.getPrice() +"€");
        price.setForeground(Color.white);
        descriptionPanel.add(price);


    }

    /**
     * Display the room at the moment of the show
     */
    private void displayRoom(Room selectedRoom, int movie_id, int room_id, LocalDateTime show_start) {
        int size = 0;
        int rows = 0;
        int columns = 0;
        // size of room (rows, seats by row)
        rows = selectedRoom.getRows();
        columns = selectedRoom.getSeatsByRow();
        // size of every seat of the mapping
        if (rows >= 30 || columns >= 30) {
            size = 15;
        } else if (rows > 20 && rows < 30 || columns > 20 && columns < 30) {
            size = 30;
        } else {
            size = 45;
        }
        // Remove the last mapping
        // Then, new Grid (will contain the mapping of the room)
        mappingRoom.removeAll();
        mappingRoom.setLayout(new GridLayout(rows, columns));
        mappingRoom.setPreferredSize(new Dimension(rows * size, columns * size));

        //JButton[][] grid = new JButton[selectedRoom.getRows()][selectedRoom.getSeatsByRow()]; // grid of JButtons
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // Button
                JButton seat = new JButton();
                seat.setOpaque(true);
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
                        if (!(selectedRoom.getSeats().get(finalY).get(finalX).isTaken()) && !(seat.getIcon().equals(waitingSeat))) {
                            //System.out.println("mouseentered");
                            seat.setIcon(selectionSeat);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        if(seat.getIcon().equals(selectionSeat) && !(seat.getIcon().equals(waitingSeat))) {
                            seat.setIcon(emptySit);
                        }
                    }
                });

                seat.addActionListener(e -> {
                    if (!(seat.getIcon().equals(takenSit))) {
                        seat.setIcon(waitingSeat);

                        String type = typesList.getSelectedItem().toString();
                        System.out.println(type);
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    } else if (seat.getIcon().equals(waitingSeat)) {
                        seat.setIcon(emptySit);
                        removeFromBookBuffer(finalX, finalY);
                    }
                });
                // Add to the Map
				mappingRoom.add(seat);
            }
        }
        mappingRoom.repaint();
        mappingRoom.revalidate();
        // Add the Map to the Jframe
        frame.add(mappingRoom, BorderLayout.CENTER);
    }

    private void displayStatus(Room selectedRoom) {
        String roomStatus = (selectedRoom.getSeatsLeft() == 0) ? "Fermé" : "Libre";
        JLabel roomStatusLabel = new JLabel("Status: " + roomStatus);
        JLabel totSeats = new JLabel("Places totales: " + selectedRoom.getTotSeats());
        JLabel seatsLeft = new JLabel("Places restantes: " + selectedRoom.getSeatsLeft());

        statusbar.removeAll();
        statusbar.add(roomStatusLabel);
        statusbar.add(totSeats);
        statusbar.add(seatsLeft);
    }

    private void addToBookBuffer(int x, int y, String type, int movie_id, int room_id, LocalDateTime show_start) {
        //Object data = new Object(){x, y, type, movie_id, room_id, show_start};
        //dataCustomer.add();
        JComboBox<String> typesListofUser = typesList;
        typesListofUser.setSelectedItem(type);
        Object[] data = {(x + ", " + y), typesListofUser};

        DefaultTableModel model = (DefaultTableModel) bookBufferTable.getModel();
        model.addRow(data);
    }

    private void removeFromBookBuffer(int x, int y) {

    }

    private void makeBook(int x, int y, String type, int movie_id, int room_id, LocalDateTime show_start) {
        control.addCustomer(x, y, 0, room_id, type, movie_id, show_start);
    }

    private void displayAuth() {
        authDialog.setVisible(true);
    }

   /* private void auth() {
        System.out.println("auth");
        try {
            control.authUser(username.getText().trim(), password.getText().trim());
            // TODO
            // show options
            // show movie config
        } catch (InvalidUserException e) {
            // TODO
            // Exception handler
        }

    }*/

    private void addEventListeners() {
        moviesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource(); // get JComboBox
                String title = comboBox.getSelectedItem().toString(); // get the title selected
                int idMovie = control.retrieveMovieId(title); // retrieve movie id
                System.out.println("FROM LISTENER => idMovie: " + idMovie);
                Movie movie = control.retrieveMovie(idMovie); //
                displayShows(movie);
                displayDescription(movie);
            }
        });

        showsList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource(); // get the comboBox
                int index = comboBox.getSelectedIndex(); // show selected
                System.out.println("showsListNumber: " + index);
                Show selectedShow = control.getShows_al().get(index); // Select the show
                int movie_id = selectedShow.getMovie_id();
                int room_id = selectedShow.getRoom_id();
                LocalDateTime show_start = selectedShow.getShow_start();
                Room selectedRoom = control.retrieveRoom(movie_id, room_id, show_start); // Select the room and its customers
                displayRoom(selectedRoom, movie_id, room_id, show_start);
                displayStatus(selectedRoom);
            }
        });

        config.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAuth();
            }
        });

      /*  login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auth();
            }
        });*/
    }

    public void update(Observable arg0, Object arg1) {

    }
}