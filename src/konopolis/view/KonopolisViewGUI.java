
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
import java.time.format.DateTimeParseException;
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

    private HashMap<Seat, Customer> givenSeats= new HashMap<Seat, Customer>();
    double income = 0;

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

    JLabel loadingAdmin;

    private ImageIcon emptySeat = new ImageIcon("img/emptySeat.png");
    private ImageIcon takenSeat = new ImageIcon("img/takenSeat.png");
    private ImageIcon waitingSeat = new ImageIcon("img/waitingSeat.png");
    private ImageIcon selectionSeat = new ImageIcon("img/selectionSeat.png");

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
        JLabel selectAMovie = new JLabel("Sélectionnez un film: "); // Label

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
        books.setFont(new Font("Arial", Font.BOLD,15));

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
        password = new JPasswordField();
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
        description.setWrapStyleWord(true);
        description.setLineWrap(true);
        description.setPreferredSize(new Dimension(700, 100));

        JLabel genresLabel = new JLabel("Genres (séparés par une virgule): "); // some

        genres = new JTextField();
        genres.setPreferredSize(new Dimension(700, 30));

        JLabel datesLabel = new JLabel("Dates de séance (veuillez suivre ce pattern: dd-MM-yyyy HH:mm - séparés par une virgule) : "); // some

        dates = new JTextField();

        JLabel roomsLabel = new JLabel("Choisissez une salle: ");

        rooms = new JComboBox();
        control.retrieveAllRooms();
        for (Room room : control.getRooms_al()) { // Show every room
            rooms.addItem(room.getId() + ") " + room.getRows() + " rangées - " + room.getSeatsByRow() + " sièges par rangée - " + room.getTotSeats() + " places");
        }

        JLabel directorLabel = new JLabel("Choisissez un réalisateur: ");

        director = new JTextField();
        director.setPreferredSize(new Dimension(700, 30));

        JLabel actorsLabel = new JLabel("Acteurs (séparés par une virgule): ");

        actors = new JTextField();

        JLabel timeLabel = new JLabel("Durée (en minutes): ");

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
        // Fill the Combobox with movie's titles | types
        control.retrieveAllMoviesTitles().entrySet().forEach(movie -> moviesList.addItem(movie.getValue()));
        control.retrieveTypes().forEach(type -> typesList.addItem(type));
        /*frame.revalidate();
        frame.repaint(); // refresh movies list
        frame.pack();*/
        moviesList.addActionListener(new ActionListener() { // When we click on a movie in the list
            @Override
            public void actionPerformed(ActionEvent e) {
                givenSeats.clear();
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
    }

    /**
     * Fill the ComboBox of Shows
     * @param: The movie of which you want to have the shows
     */
    private void displayShows(Movie movie) {

        showsList.removeAllItems(); // BUG FIXED

        for(Show sh: movie.getShows()){ // Show the shows list
            //System.out.println(sh.getShow_start());
            showsList.addItem("Salle n°" + sh.getRoom_id() + " - " + control.dateInFrench(sh.getShow_start()));
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
        JLabel duration = new JLabel("Durée: " + movie.getTime() + "min");
        duration.setForeground(Color.white);
        descriptionPanel.add(duration);

        // Price
        JLabel price = new JLabel("Prix: " + movie.getPrice() + "€");
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
                        seat.setIcon(new ImageIcon("img/seniorSeat.png"));
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    } else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Normal") == 0)  {
                        seat.setIcon(new ImageIcon("img/normalSeat.png"));
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    } else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Junior") == 0)  {
                        seat.setIcon(new ImageIcon("img/juniorSeat.png"));
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    } else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("Etudiant") == 0)  {
                        seat.setIcon(new ImageIcon("img/etuSeat.png"));
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    } else if (seat.getIcon().equals(waitingSeat) && typesList.getSelectedItem().toString().compareTo("VIP") == 0) {
                        seat.setIcon(new ImageIcon("img/vipSeat.png"));
                        String type = typesList.getSelectedItem().toString();
                        addToBookBuffer(finalX, finalY, type, movie_id, room_id, show_start);
                    } else if (!(seat.getIcon().equals(emptySeat)) && !(seat.getIcon().equals(takenSeat))) {
                        seat.setIcon(emptySeat);
                        String type = typesList.getSelectedItem().toString();
                        removeFromBookBuffer(finalX, finalY, type, movie_id);

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
        String roomStatus = (selectedRoom.getSeatsLeft() == 0) ? "Fermé" : "Libre"; // If no seats left "Fermé" otherwise "Libre"
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
        givenSeats.put(new Seat(x + 1, y + 1), new Customer(type)); // add to buffer
        double moviePrice = 0.0;
        String listBooks = "";

        // Price
        for (Movie movie : model.getMovies_al()) {
            if (movie.getId() == movie_id) {
                moviePrice = movie.getPrice();
                break;
            }
        }

        income = 0.0;
        for (Map.Entry<Seat, Customer> entry : givenSeats.entrySet()) { // Write the list of booking
            double reducedPrice = (moviePrice - (moviePrice * entry.getValue().getReduction()));
            listBooks += "[" + entry.getValue().getFullType() + "] Siège " + entry.getKey().getRow() + ", rangée " + entry.getKey().getColumn() + ", Prix: " + reducedPrice + "€" + "\n";
            income += reducedPrice;
        }
        books.setText(listBooks + "\n" + income + "€");
        books.validate();
        books.repaint();
    }

    /**
     * Remove a ticket from buffer
     * @param x, the seat of the row
     * @param y, the row
     * @param type, type of the people
     * @param movie_id, id of the movie
     */
    private void removeFromBookBuffer(int x, int y, String type, int movie_id) {
        givenSeats.remove(new Seat(x + 1, y + 1)); // remove from the buffer by key
        String listBooks = "";
        income = 0.0; // affect income variable to 0

        double moviePrice = 0.0;

        // Price
        for (Movie movie : model.getMovies_al()) {
            if (movie.getId() == movie_id) {
                moviePrice = movie.getPrice();
            }
        }

        if (givenSeats.size() > 0) {
            for (Map.Entry<Seat, Customer> entry : givenSeats.entrySet()) { // Write the list of booking
                double reducedPrice = (moviePrice - (moviePrice * entry.getValue().getReduction()));
                listBooks += "[" + entry.getValue().getFullType() + "] Siège " + entry.getKey().getRow() + ", rangée " + entry.getKey().getColumn() + ", Prix: " + reducedPrice + "€" + "\n";
                income += reducedPrice;
            }
            books.setText(listBooks + "\n" + income + "€");
            books.revalidate();
            books.repaint();
        } else {
            books.setText("");
        }
    }

    /*
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog( // popup dialog box that show an error message
                frame, // reference frame
                e.getMessage() + " (Siège: " + x + ", " + y + " )", // message to show
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
        loadingAdmin.setVisible(true);
        authDialog.revalidate();
        authDialog.repaint();
        try {
            control.authUser(username.getText().trim(), password.getText().trim());
            toolbar.add(movieConfigButton); // if admin is authentified => add movie config button
            toolbar.repaint();
            toolbar.revalidate();
            authDialog.setVisible(false); // hide auth dialog
        } catch (InvalidUserException e) {
            JOptionPane.showMessageDialog( // popup dialog box that show an error message
                    frame, // reference frame
                    e.getMessage(), // message to show
                    "Erreur: Authentification", // title of window
                    JOptionPane.ERROR_MESSAGE // error message
            );
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
        if (selectedRoom == null) { // when we add a movie, room == null. We want to avoid that
            for (Room room : control.getRooms_al()) {
                if (room.getId() == room_id) {
                    selectedRoom = room;
                }
            }
        }
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
        if (selectedRoom == null) { // when we add a movie, room == null. We want to avoid that
            for (Room room : control.getRooms_al()) {
                if (room.getId() == room_id) {
                    selectedRoom = room;
                }
            }
        }
        displayRoom(selectedRoom, movie_id, room_id, show_start); // display the room
        displayStatus(selectedRoom); // display the status of the room
    }

    /**
     * Event listeners
     */
    private void addEventListeners() {
        /*moviesList.addActionListener(new ActionListener() { // When we click on a movie in the list
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
        });*/

        showsList.addActionListener(new ActionListener() { // When we click on a show in the list
            @Override
            public void actionPerformed(ActionEvent e) {
                givenSeats.clear();
                books.setText("");
                books.revalidate();
                books.repaint();
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
            public void actionPerformed(ActionEvent e) {
                for (Map.Entry<Seat, Customer> entry : givenSeats.entrySet()) {
                    makeBook(entry.getKey().getRow(), entry.getKey().getColumn(), entry.getValue().getFullType(), control.retrieveMovieId((String)moviesList.getSelectedItem()), control.getShows_al().get(showsList.getSelectedIndex()).getRoom_id(), control.getShows_al().get(showsList.getSelectedIndex()).getShow_start());
                }
                SplashScreen splash = new SplashScreen(600, new ImageIcon("img/giphy.gif"), 400, 400);
                income = 0.0; // set back the total of booking to 0
                givenSeats.clear(); // Clear the buffer
                books.setText(""); // Erase the booking panel content
                books.validate();
                books.repaint();
                showsListHandler(); // refresh the room and its status
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
                boolean error = false;

                // Fields validation
                if (title.getText().trim().equals("")) error = true;
                if (description.getText().trim().equals("")) error = true;
                if (director.getText().trim().equals("")) error = true;
                if (dates.getText().trim().equals("")) error = true;
                if (actors.getText().trim().equals("")) error = true;
                if (genres.getText().trim().equals("")) error = true;
                if (time.getText().trim().equals("")) error = true;

                // If error on one or more fields
                if (error) {
                    JOptionPane.showMessageDialog( // popup dialog box that show an error message
                            frame, // reference frame
                            "Erreur sur un ou plusieurs champs !", // message
                            "Erreur: Ajout de film", // title of window
                            JOptionPane.ERROR_MESSAGE // error message
                    );
                    return;
                }

                ArrayList<LocalDateTime> showsAl = new ArrayList<LocalDateTime>();
                for(String date : dates.getText().split(",")) { // construct the ArrayList of shows
                    try {
                        LocalDateTime LDTdate = control.makeDateFromString(date.trim());
                        showsAl.add(LDTdate);
                    } catch (DateTimeParseException errParse) { // If error on parsing date
                        JOptionPane.showMessageDialog( // popup dialog box that show an error message
                                frame, // reference frame
                                "Erreur sur le format de la date ! Format requis (dd-MM-yyyy HH:mm)", // message
                                "Erreur: format de la date", // title of window
                                JOptionPane.ERROR_MESSAGE // error message
                        );
                        return;
                    }
                }

                ArrayList<String> castingAl = new ArrayList<String>(Arrays.asList(actors.getText().trim().split(","))); // construct the ArrayList of actors
                ArrayList<String> genresAl = new ArrayList<String>(Arrays.asList(genres.getText().trim().split(","))); // construct the ArrayList of genres

                control.addMovie( // add the movie
                        //Movie.getCurrentId() + 1, // next movie id
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
                //init();
                for (ActionListener al : moviesList.getActionListeners()) {
                    moviesList.removeActionListener(al);
                }
                moviesList.removeAllItems();
                typesList.removeAllItems();
                displayMovies(); // refresh the list of movies
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
