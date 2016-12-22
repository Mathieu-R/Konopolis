
package src.konopolis.view;

import src.konopolis.controller.KonopolisController;
import src.konopolis.model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author Nathan & Mathieu R.
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

    private HashMap<Seat,String> givenSeats= new HashMap<Seat,String>();
    private HashMap<Seat,Customer> custList= new HashMap<Seat,Customer>();
    private JTextArea books = new JTextArea(20,10);
    private JButton confirm = new JButton("confirmer");

    private JButton movieConfigButton = new JButton("Ajouter un film");

    private String selectedMovie = "";

    /* Variables for movie dialog */
    private JDialog movieDialog;
    private JTextField title;
    private JTextArea description;
    private JTextField genres;
    private JTextField dates;
    private JComboBox rooms;
    private JTextField director;
    private JTextField actors;
    private JTextField time;
    private JComboBox languages;
    private JSpinner price;
    private JButton addMovie = new JButton("Ajouter un film");
    
    private static double income=0;

    private JLabel loadingAdmin;
    

    private ImageIcon emptySeat = new ImageIcon("img/emptySit.png");
    private ImageIcon takenSeat = new ImageIcon("img/takenSit.png");
    private ImageIcon waitingSeat = new ImageIcon("img/waitingSit.png");
    private ImageIcon selectionSeat = new ImageIcon("img/selectionSit.png");

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
        new Thread(this).start();
	}

    /**
     * Create the frame and all his panels
     */
	public void init() {
	    // Create main Frame
        frame = new JFrame("Konopolis"); // Title
		frame.setVisible(true);
		frame.setSize(1500,900);
		frame.setMinimumSize(new Dimension(1500, 900));
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try { // Icon
            frame.setIconImage(ImageIO.read(new File("img/Konopolis_1.0.jpg")));
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
		makeNewMovieDialog();

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

    /**
     * Create the toolbar where the lists of movies, shows and clients will be displayed
     */
	private void makeToolbar() {
        // Define the toolbar
        toolbar.setSize(400, 200);
        toolbar.setBorder(BorderFactory.createLineBorder(Color.black));
        toolbar.setLayout(new FlowLayout());
        toolbar.setVisible(true);

        // Label
        JLabel selectAMovie = new JLabel("S�lectionnez un film: "); // Label

        // Fill the Combobox with movie's titles | types
        displayMovies();

        moviesList.setPreferredSize(new Dimension(300,25));
        moviesList.setSelectedIndex(0);

        // Create a default show ComboBox
        showsList.setPreferredSize(new Dimension(300,25));

        typesList.setPreferredSize(new Dimension(300, 25));

        // Add Label, ComboBoxs + Button at the toolbar
        toolbar.add(selectAMovie);
        toolbar.add(moviesList);
        toolbar.add(showsList);
        toolbar.add(typesList);
        toolbar.add(config);
    }

    /**
     * Create a panel who will contain the descriptiion of the selected movie
     */
    private void makeDescriptionPanel() {
        // Define descriptionPanel
        //descriptionPanel.setForeground(Color.white);
        descriptionPanel.setLayout(new BoxLayout(descriptionPanel, BoxLayout.PAGE_AXIS));
        descriptionPanel.setBorder(new EmptyBorder(new Insets(10,5,10,5)));
        descriptionPanel.setBackground(Color.BLACK);
        descriptionPanel.setPreferredSize(new Dimension(250, 900));
        descriptionPanel.setMinimumSize(new Dimension(250, 900));
    }

    /**
     * Create a panel where all the current selections will be displayed
     */
    private void makeBookingPanel() {
        JPanel bookingBuffer = new JPanel(new BorderLayout());
        bookingPanel.setBackground(Color.lightGray);
        bookingPanel.setPreferredSize(new Dimension(300, 500));
        bookingPanel.setMaximumSize(new Dimension(300, 500));

        books.setPreferredSize(new Dimension(300, 500));
        books.setMaximumSize(new Dimension(300, 500));
        books.setEditable(false);
        books.setVisible(true);
        books.setFont(new Font("Arial", Font.BOLD,18));

        bookingBuffer.setMaximumSize(new Dimension(290, 500));
        bookingBuffer.add(new JScrollPane(books)); // scroll
        bookingBuffer.add(confirm, BorderLayout.SOUTH);
        bookingPanel.add(bookingBuffer);
    }

    /**
     * Create the bar of status
     */
    private void makeStatusBar() {
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

        statusbar.add(msgUser,BorderLayout.LINE_START);
    }

    /**
     * Draw the room following the movie selected.
     * If there is no movies in the list,the logo of the software will be displayed
     */
    public void makeRoomMapping() {
	    if(moviesList.getItemCount()==0) { 
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
	    }else{
	    	 String title = moviesList.getSelectedItem().toString(); // get the title selected
             if (!selectedMovie.equals(title)) { // if movie is not selected
                 selectedMovie = title;
                 int idMovie = control.retrieveMovieId(title); // retrieve movie id
                 Movie movie = control.retrieveMovie(idMovie); // retrieve movie
                 displayDescription(movie); // display the description
                 displayShows(movie); // display the shows
                 int index = (showsList.getSelectedIndex() < 0) ? 0 : showsList.getSelectedIndex();
                 Show selectedShow = control.getShows_al().get(index); // Select the show
                 int movie_id = selectedShow.getMovie_id();
                 int room_id = selectedShow.getRoom_id();
                 LocalDateTime show_start = selectedShow.getShow_start();
                 Room selectedRoom = control.retrieveRoom(movie_id, room_id, show_start); // Select the room and its customers
                 displayRoom(selectedRoom, movie_id, room_id, show_start); // display the room
                 displayStatus(selectedRoom); // display the status of the room
             }
        }
    }

    private void makeAuthDialog() {
        authDialog = new JDialog(frame, "Connection");
        authDialog.setMinimumSize(new Dimension(500, 300));
        authDialog.setForeground(Color.white);
        authDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        authDialog.setLocationRelativeTo(null);
        JPanel authPanel = new JPanel(new FlowLayout());
        JLabel banner = new JLabel("Espace r�serv� aux Admins, veuillez vous connecter !");
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
        loadingAdmin = new JLabel(new ImageIcon("img/loading3.gif"));

        authPanel.add(banner);
        authPanel.add(usernameLabel);
        authPanel.add(username);
        authPanel.add(passwordLabel);
        authPanel.add(password);
        authPanel.add(login);
        authPanel.add(loadingAdmin);
        authDialog.add(authPanel);
        authDialog.setVisible(false);
        loadingAdmin.setVisible(false);
    }

    /**
     * Create a window where the user must authentify himself to make configurations
     */
    private void makeNewMovieDialog() {

        movieDialog = new JDialog(frame, "Ajouter un film"); // Dialogue box
        movieDialog.setMinimumSize(new Dimension(900, 900)); // Size
        movieDialog.setForeground(Color.white); // Background
        movieDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE); // Hide on close
        movieDialog.setLocationRelativeTo(null); // centered
        JPanel moviePanel = new JPanel(new GridLayout(0, 1)); // New panel

        JLabel titleLabel = new JLabel("Titre du film: ");

        title = new JTextField();
        title.setPreferredSize(new Dimension(700, 30));
        title.setMinimumSize(new Dimension(700, 30));

        JLabel descriptionLabel = new JLabel("Description: ");

        description = new JTextArea();
        description.setPreferredSize(new Dimension(700, 100));

        JLabel genresLabel = new JLabel("Genres (s�par�s par une virgule): "); // some

        genres = new JTextField();
        genres.setPreferredSize(new Dimension(700, 30));

        JLabel datesLabel = new JLabel("Dates de s�ance (veuillez suivre ce pattern: dd-MM-yyyy HH:mm) : "); // some

        dates = new JTextField();

        JLabel roomsLabel = new JLabel("Choisissez une salle: ");

        rooms = new JComboBox();
        control.retrieveAllRooms();
        for (Room room : control.getRooms_al()) { // Show every room
            rooms.addItem(room.getId() + ") " + room.getRows() + " rang�es - " + room.getSeatsByRow() + " si�ges par rang�e - " + room.getTotSeats() + " places");
        }

        JLabel directorLabel = new JLabel("Choisissez un r�alisateur: ");

        director = new JTextField();
        director.setPreferredSize(new Dimension(700, 30));

        JLabel actorsLabel = new JLabel("Acteurs: ");

        actors = new JTextField();

        JLabel timeLabel = new JLabel("Dur�e (en minutes): ");

        time = new JTextField();

        JLabel languagesLabel = new JLabel("Langue: ");

        languages = new JComboBox();
        control.retrieveAllLanguages().forEach(languages::addItem);

        JLabel priceLabel = new JLabel("Prix (en €): ");

        SpinnerNumberModel priceSpinnerModel = new SpinnerNumberModel(10, 1, 30, 1);
        price = new JSpinner(priceSpinnerModel);

        // Add every elements to the panel
        moviePanel.add(titleLabel);
        moviePanel.add(title);
        moviePanel.add(descriptionLabel);
        moviePanel.add(description);
        moviePanel.add(genresLabel);
        moviePanel.add(genres);
        moviePanel.add(datesLabel);
        moviePanel.add(dates);
        moviePanel.add(roomsLabel);
        moviePanel.add(rooms);
        moviePanel.add(directorLabel);
        moviePanel.add(director);
        moviePanel.add(actorsLabel);
        moviePanel.add(actors);
        moviePanel.add(timeLabel);
        moviePanel.add(time);
        moviePanel.add(languagesLabel);
        moviePanel.add(languages);
        moviePanel.add(priceLabel);
        moviePanel.add(price);
        moviePanel.add(addMovie);

        // Add the panel to the JDialog
        movieDialog.add(moviePanel);
        movieDialog.setVisible(false); // Hide the JDialog
    }

    /**
     * Fill the ComboBox of Movies
     */
    private void displayMovies() {
	    // Remove everything from combobox => the case it's a refresh of the list
    	
    	 moviesList = new JComboBox();
         typesList = new JComboBox();
        // Fill the Combobox with movie's titles | types
        control.retrieveAllMoviesTitles().entrySet().forEach(movie -> moviesList.addItem(movie.getValue()));
        control.retrieveTypes().forEach(type -> typesList.addItem(type));
        
       
    }

    /**
     * Fill the ComboBox of Shows
     * @param: The movie of which you want to have the shows
     */
    private void displayShows(Movie movie) {

        showsList.removeAllItems(); // BUG FIXED

        for(Show sh: movie.getShows()){ // Show the shows list

            showsList.addItem("Salle n�" + sh.getRoom_id() + " - " + control.dateInFrench(sh.getShow_start()));
        }
        showsList.setSelectedIndex(0); // Select the first item of the list
    }

    /**
     * Add the description to the descriptionPanel
     * @param movie: The movie object of which you want a description
     */
    private void displayDescription(Movie movie) {
        descriptionPanel.removeAll();
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
        JLabel duration = new JLabel("Dur�e: " + movie.getTime() + "min");
        duration.setForeground(Color.white);
        descriptionPanel.add(duration);

        // Price
        JLabel price = new JLabel("Prix: " + movie.getPrice() + " �");
        price.setForeground(Color.white);
        descriptionPanel.add(price);
    }

    /**
     * Display the room at the moment of the show
     * All the parameters are used to identify a specific show of the database
     *
     * @param selectedRoom  the room to display
     * @param movie_id: the movie who is shown
     * @param room_id: the id of the room where the movie gonna be shown
     * @param show_start: the time when the movie start
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

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // Button
                JButton seat = new JButton();
                seat.setOpaque(true); // Opaque
                seat.setBorderPainted(false); // No painted border
                seat.setContentAreaFilled(false);

                if (selectedRoom.getSeats().get(y).get(x).isTaken()) { // If the room is taken
                    seat.setIcon(takenSeat);
                } else { // Otherwise
                    seat.setIcon(emptySeat);
                }

                // Event Listener
                int finalX = x;
                int finalY = y;
                seat.addMouseListener(new MouseAdapter() { // Mouse events
                    @Override
                    public void mouseEntered(MouseEvent e) { // When mouse is over the seat
                        // If the seat is not taken nor clicked
                        if (seat.getIcon().equals(emptySeat)) {
                            seat.setIcon(waitingSeat);
                        }
                    }

                    @Override
                    public void mouseExited(MouseEvent e) { // When mouse exit the "over state"
                        // If the seat is selected or
                        if(seat.getIcon().equals(waitingSeat)) {
                            seat.setIcon(emptySeat);
                        }
                    }
                });

                seat.addActionListener(e -> { // on click on a seat
                	
                	if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Senior") == 0)  {
                        seat.setIcon(new ImageIcon("img/seniorSit.png"));
                       
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    }else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Normal") == 0)  {
                        seat.setIcon(new ImageIcon("img/normalSit.png"));
                    
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    }else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Junior") == 0)  {
                        seat.setIcon(new ImageIcon("img/juniorSit.png"));
                      
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    }else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Etudiant") == 0)  {
                        seat.setIcon(new ImageIcon("img/etuSit.png"));
                        String type = typesList.getSelectedItem().toString();
                      
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    }else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("VIP") == 0)  {
                        seat.setIcon(new ImageIcon("img/vipSit.png"));
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    }else if(seat.getIcon().equals(takenSeat)){
                        //Rien
                    }else{seat.setIcon(emptySeat);
                    removeFromBookBuffer(finalX, finalY);
                    }
                });
                // Add to the Map
				mappingRoom.add(seat);
            }
        }
        mappingRoom.repaint(); // Repaint
        mappingRoom.revalidate();
        frame.add(mappingRoom, BorderLayout.CENTER);  // Add the Map to the Jframe
    }

    /**
     * Display the status of the room
     * @param selectedRoom, the room (of a show) selected
     */
    private void displayStatus(Room selectedRoom) {
        String roomStatus = (selectedRoom.getSeatsLeft() == 0) ? "Ferm�" : "Libre"; // If no seats left "Ferm�" otherwise "Libre"
        JLabel roomStatusLabel = new JLabel("Status: " + roomStatus);
        JLabel totSeats = new JLabel("Places totales: " + selectedRoom.getTotSeats());
        JLabel seatsLeft = new JLabel("Places restantes: " + selectedRoom.getSeatsLeft());

        statusbar.removeAll();
        statusbar.add(roomStatusLabel); // status
        statusbar.add(totSeats); // total seats
        statusbar.add(seatsLeft); // seats left
    }

    /**
     * Add ticket to a buffer (before booking)
     * @param x, seat of the row
     * @param y, row
     * @param type, type of the people
     * @param movie_id, id of the movie
     * @param room_id, if of the room
     * @param show_start, beginning date of the show
     */
    private void addToBookBuffer(int x, int y, String type, int movie_id, int room_id, LocalDateTime show_start) {
        givenSeats.put(new Seat(x + 1, y + 1), type); // add to buffer
        String listBooks = "";
        Movie selectMovie= control.retrieveMovie(movie_id);
        switch(type){
        	case "VIP": custList.put(new Seat(x + 1, y + 1),new Customer("VIP"));
        		break;
        	case "Junior": custList.put(new Seat(x + 1, y + 1),new Customer("Junior"));
        		break;
        	case "Etudiant"	:	custList.put(new Seat(x + 1, y + 1),new Customer("Etudiant"));
        		break;
        	case "Normal"	:	custList.put(new Seat(x + 1, y + 1),new Customer("Normal"));
    			break;
        	case "Senior" : 	custList.put(new Seat(x + 1, y + 1),new Customer("Senior"));
        		break;
        }
       
        income=0;
        for (Map.Entry<Seat, String> entry : givenSeats.entrySet()) { // Write the list of booking
        	
            listBooks += "[" + entry.getValue() + "] Si�ge " + entry.getKey().getRow() + ", " + entry.getKey().getColumn() +",Prix:"+(selectMovie.getPrice()-selectMovie.getPrice()*custList.get(entry.getKey()).getReduction())+ "�\n";
            income+=selectMovie.getPrice()-selectMovie.getPrice()*custList.get(entry.getKey()).getReduction();
         
        }
        books.setText(listBooks+"\n");
       
        books.append(income+"�");
        books.validate();
        books.repaint();
    }

    /**
     * Remove a ticket from buffer
     * @param x, the seat of the row
     * @param y, the row
     */
    private void removeFromBookBuffer(int x, int y) {
        givenSeats.remove(new Seat(x + 1, y + 1)); // remove from the buffer
        //books.removeAll();
        String listBooks = "";
        int i=0;
        String title = (String)moviesList.getSelectedItem();
        int idMovie = control.retrieveMovieId(title);
        Movie selectMovie= control.retrieveMovie(idMovie);
       income=0;
       if(givenSeats.size()>0){
	        for (Map.Entry<Seat, String> entry : givenSeats.entrySet()) { // Rewrite the list of booking
	        	listBooks += "[" + entry.getValue() + "] Si�ge " + entry.getKey().getRow() + ", " + entry.getKey().getColumn() +",Prix:"+(selectMovie.getPrice()-selectMovie.getPrice()*custList.get(entry.getKey()).getReduction())+ "�\n";
	            income+=selectMovie.getPrice()-selectMovie.getPrice()*custList.get(entry.getKey()).getReduction();
	        	
	        }
	        books.setText(listBooks+"\n");
	        books.append(income+"�");
	        books.validate();
	        books.repaint();
       }else{
    	   books.setText("");
	        
       }
       
       
    }

    /**
     * Order the tickets
     * @param x, row of the seat
     * @param y, row
     * @param type, type of the people
     * @param movie_id, id of the movie
     * @param room_id, id of the room
     * @param show_start, beginning date of the show
     */
    private void makeBook(int x, int y, String type, int movie_id, int room_id, LocalDateTime show_start) {
        try {
            control.addCustomer(x, y, 0, room_id, type, movie_id, show_start);
            SplashScreen splash = new SplashScreen(600, new ImageIcon("img/giphy.gif"), 400, 400);
            books.setText(""); // Erase the booking panel content
            books.validate();
            books.repaint();
            showsListHandler(); // refresh the room and its status
           
        } catch (Exception e) {
            JOptionPane.showMessageDialog( // popup dialog box that show an error message
                frame, // reference frame
                e.getMessage() + " (Si�ge: " + x + ", " + y + " )", // message to show
                "Erreur: Commande de tickets",
                JOptionPane.ERROR_MESSAGE // error message
            );
        }

    }

    /**
     * Display the authentication JDialog
     */
    private void displayAuth() {
        authDialog.setVisible(true);
    }

    /**
     * Authentify an admin
     */
    private void auth() {
        try {
            loadingAdmin.setVisible(true);
            control.authUser(username.getText().trim(), password.getText().trim());
            toolbar.add(movieConfigButton); // if admin is authentified => add movie config button
            toolbar.repaint();
            toolbar.revalidate();
            authDialog.setVisible(false); // hide auth dialog
        } catch (InvalidUserException e) {
            authDialog.add(new JLabel(e.getMessage())); // add label to auth dialog box
        } finally {
            loadingAdmin.setVisible(false);
        }

    }

    /**
     * Handle the click on a show of the list
     * @param e, ActionEvent => event when we click
     */
    private void showsListHandler(ActionEvent e) {
        JComboBox comboBox = (JComboBox) e.getSource(); // get the comboBox
        // show selected, sometimes, index == -1 instead 0 (for first element) so we hardcode it
        int index = (comboBox.getSelectedIndex() < 0) ? 0 : comboBox.getSelectedIndex();
        Show selectedShow = control.getShows_al().get(index); // Select the show
        int movie_id = selectedShow.getMovie_id();
        int room_id = selectedShow.getRoom_id();
        LocalDateTime show_start = selectedShow.getShow_start();
        Room selectedRoom = control.retrieveRoom(movie_id, room_id, show_start); // Select the room and its customers
        displayRoom(selectedRoom, movie_id, room_id, show_start); // display the room
        displayStatus(selectedRoom); // display the status of the room
    }

    /**
     * Refresh the room and its status
     */
    private void showsListHandler() {
        // show selected, sometimes, index == -1 instead 0 (for first element) so we hardcode it
        int index = (showsList.getSelectedIndex() < 0) ? 0 : showsList.getSelectedIndex();
        Show selectedShow = control.getShows_al().get(index); // Select the show
        int movie_id = selectedShow.getMovie_id();
        int room_id = selectedShow.getRoom_id();
        LocalDateTime show_start = selectedShow.getShow_start();
        Room selectedRoom = control.retrieveRoom(movie_id, room_id, show_start); // Select the room and its customers
        displayRoom(selectedRoom, movie_id, room_id, show_start); // display the room
        displayStatus(selectedRoom); // display the status of the room
    }

    /**
     * Event listeners
     */
    private void addEventListeners() {
        moviesList.addActionListener(new ActionListener() { // When we click on a movie in the list
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource(); // get JComboBox
                String title = comboBox.getSelectedItem().toString(); // get the title selected
                if (!selectedMovie.equals(title)) { // if movie is not selected
                    selectedMovie = title;
                    int idMovie = control.retrieveMovieId(title); // retrieve movie id
                    Movie movie = control.retrieveMovie(idMovie); // retrieve movie
                    displayDescription(movie); // display the description
                    displayShows(movie); // display the shows
                }
            }
        });

        showsList.addActionListener(new ActionListener() { // When we click on a show in the list
            @Override
            public void actionPerformed(ActionEvent e) {
                showsListHandler(e);
            }
        });

        config.addActionListener(new ActionListener() { // When we click on the auth button
            @Override
            public void actionPerformed(ActionEvent e) {
                displayAuth();
            }
        });

        login.addActionListener(new ActionListener() { // When we try to login
            @Override
            public void actionPerformed(ActionEvent e) {
                auth();
            }
        });

        confirm.addActionListener(new ActionListener(){ // When we click on confirm button (to order some tickets)
            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (Map.Entry<Seat, String> entry : givenSeats.entrySet()) {
         
                    makeBook(entry.getKey().getRow(), entry.getKey().getColumn(), entry.getValue(), control.retrieveMovieId((String)moviesList.getSelectedItem()), control.getShows_al().get(showsList.getSelectedIndex()).getRoom_id(), control.getShows_al().get(showsList.getSelectedIndex()).getShow_start());
                }
                income=0;
                custList.clear();
                givenSeats.clear();
            }
        });

        movieConfigButton.addActionListener(new ActionListener() { // When we click on "add a movie" button
            @Override
            public void actionPerformed(ActionEvent e) {
                movieDialog.setVisible(true);
            }
        });

        addMovie.addActionListener(new ActionListener() { // When we add a movie
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<LocalDateTime> showsAl = new ArrayList<LocalDateTime>();
                for(String date : dates.getText().split(",")) { // construct the ArrayList of shows
                   showsAl.add(control.makeDateFromString(date.trim()));
                }

                ArrayList<String> castingAl = new ArrayList<String>(Arrays.asList(actors.getText().trim().split(","))); // construct the ArrayList of actors
                ArrayList<String> genresAl = new ArrayList<String>(Arrays.asList(genres.getText().trim().split(","))); // construct the ArrayList of genres

                control.addMovie( // add the movie
                        Movie.getCurrentId() + 1, // next movie id
                        rooms.getSelectedIndex() + 1,
                        title.getText().trim(),
                        description.getText().trim(),
                        director.getText().trim(),
                        showsAl,
                        castingAl,
                        Integer.parseInt(time.getText().trim()),
                        languages.getSelectedItem().toString(),
                        (int) price.getValue(),
                        genresAl
                );
                movieDialog.setVisible(false); // hide the movie JDialog
                displayMovies(); // refresh the list of movies //TODO
            }
        });
    }

    @Override
    public void update(Observable arg0, Object arg1) {

    }

    @Override
    public void run() {
        init();
    }
}
