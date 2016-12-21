
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

    private HashMap<Seat,String> givenSeats= new HashMap<Seat,String>();
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
        new Thread(this).start();
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

	public void makeToolbar() {
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
        //moviesList.setActionCommand("Movies"); // Action for ActionListener
        moviesList.setSelectedIndex(0);

        // Create a default show ComboBox
        showsList.setPreferredSize(new Dimension(300,25));
        //showsList.setActionCommand("Shows"); // Action for ActionListener

        typesList.setPreferredSize(new Dimension(300, 25));

        // Define config button
        //config.setActionCommand("Config");

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

    private void makeNewMovieDialog() {

        movieDialog = new JDialog(frame, "Ajouter un film");
        movieDialog.setMinimumSize(new Dimension(900, 900));
        movieDialog.setForeground(Color.white);
        movieDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        movieDialog.setLocationRelativeTo(null);
        JPanel moviePanel = new JPanel(new GridLayout(0, 1));

        JLabel titleLabel = new JLabel("Titre du film: ");
        //titleLabel.setPreferredSize(new Dimension(80, 30));

        title = new JTextField();
        title.setPreferredSize(new Dimension(700, 30));
        title.setMinimumSize(new Dimension(700, 30));

        JLabel descriptionLabel = new JLabel("Description: ");
        //descriptionLabel.setPreferredSize(new Dimension(60, 30));

        description = new JTextArea();
        description.setPreferredSize(new Dimension(700, 100));

        JLabel genresLabel = new JLabel("Genres (séparés par une virgule): "); // some
        //genresLabel.setPreferredSize(new Dimension(200, 30));

        genres = new JTextField();
        genres.setPreferredSize(new Dimension(700, 30));

        JLabel datesLabel = new JLabel("Dates de séance (veuillez suivre ce pattern: dd-MM-yyyy HH:mm) : "); // some
        //datesLabel.setPreferredSize(new Dimension(400, 30));
        //datesLabel.setMinimumSize(new Dimension(400, 30));

        dates = new JTextField();
        //genres.setPreferredSize(new Dimension(300, 30));

        JLabel roomsLabel = new JLabel("Choisissez une salle: ");
        //roomsLabel.setPreferredSize(new Dimension(130, 30));

        rooms = new JComboBox();
        control.retrieveAllRooms();
        for (Room room : control.getRooms_al()) { // Show every room
            rooms.addItem(room.getId() + ") " + room.getRows() + " rangées - " + room.getSeatsByRow() + " sièges par rangée - " + room.getTotSeats() + " places");
        }

        JLabel directorLabel = new JLabel("Choisissez un réalisateur: ");
        //descriptionLabel.setPreferredSize(new Dimension(130, 30));

        director = new JTextField();
        director.setPreferredSize(new Dimension(700, 30));

        JLabel actorsLabel = new JLabel("Acteurs: ");
        //actorsLabel.setPreferredSize(new Dimension(80, 30));

        actors = new JTextField();
        //actors.setPreferredSize(new Dimension(700, 30));

        JLabel timeLabel = new JLabel("Durée (en minutes): ");
        //timeLabel.setPreferredSize(new Dimension(110, 30));

        time = new JTextField();
        //time.setPreferredSize(new Dimension(700, 30));

        JLabel languagesLabel = new JLabel("Langue: ");
        //languagesLabel.setPreferredSize(new Dimension(80, 30));

        languages = new JComboBox();
        control.retrieveAllLanguages().forEach(languages::addItem);

        JLabel priceLabel = new JLabel("Prix (en €): ");
        //priceLabel.setPreferredSize(new Dimension(100, 30));

        SpinnerNumberModel priceSpinnerModel = new SpinnerNumberModel(10, 1, 30, 1);
        price = new JSpinner(priceSpinnerModel);
        //price.setPreferredSize(new Dimension(300, 30));

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

        movieDialog.add(moviePanel);
        movieDialog.setVisible(false);
    }

    /**
     * Fill the ComboBox of Movies
     */
    private void displayMovies() {
	    // Remove everything from combobox => the case it's a refresh of the list
        typesList.removeAll();
        // Fill the Combobox with movie's titles | types
        control.retrieveAllMoviesTitles().entrySet().forEach(movie -> moviesList.addItem(movie.getValue()));
        control.retrieveTypes().forEach(type -> typesList.addItem(type));
    }

    /**
     * Fill the ComboBox of Shows
     */
    private void displayShows(Movie movie) {

        showsList.removeAllItems(); // BUG FIX

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
        System.out.println("x: " + x + " <=> " + "y: " + y);
        givenSeats.put(new Seat(x + 1, y + 1), type);
        System.out.println("adding to buffer");
        String listBooks = "";
        for (Map.Entry<Seat, String> entry : givenSeats.entrySet()) {
            listBooks += "Siège " + entry.getKey().getRow() + ", " + entry.getKey().getColumn() + " <=> client: " + entry.getValue() + "\n";
        }
        books.setText(listBooks);
        frame.validate();
        frame.repaint();
    }

    private void removeFromBookBuffer(int x, int y) {
        givenSeats.remove(new Seat(x + 1, y + 1));
        //books.removeAll();
        String listBooks = "";
        for (Map.Entry<Seat, String> entry : givenSeats.entrySet()) {
            listBooks += "Siège " + Math.addExact( (int) entry.getKey().getRow(), 1) + "," + Math.addExact( (int) entry.getKey().getColumn(), 1) + " client:" + entry.getValue() + "\n";
        }
        books.setText(listBooks);
    }

    private void makeBook(int x, int y, String type, int movie_id, int room_id, LocalDateTime show_start) {
        control.addCustomer(x, y, 0, room_id, type, movie_id, show_start);
    }

    private void displayAuth() {
        authDialog.setVisible(true);
    }

    private void auth() {
        try {
            control.authUser(username.getText().trim(), password.getText().trim());
            toolbar.add(movieConfigButton);
            toolbar.repaint();
            toolbar.revalidate();
            authDialog.setVisible(false);
        } catch (InvalidUserException e) {
            authDialog.add(new JLabel(e.getMessage()));
        }

    }

    private void addEventListeners() {
        moviesList.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JComboBox comboBox = (JComboBox) e.getSource(); // get JComboBox
                String title = comboBox.getSelectedItem().toString(); // get the title selected
                if (!selectedMovie.equals(title)) { // if movie is not selected
                    selectedMovie = title;
                    int idMovie = control.retrieveMovieId(title); // retrieve movie id
                    System.out.println("FROM LISTENER => idMovie: " + idMovie);
                    Movie movie = control.retrieveMovie(idMovie); //
                    displayShows(movie);
                    displayDescription(movie);
                }
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

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                auth();
            }
        });

        confirm.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                for (Map.Entry<Seat, String> entry : givenSeats.entrySet()) {
                    makeBook(entry.getKey().getRow(), entry.getKey().getColumn(), entry.getValue(), control.retrieveMovieId((String)moviesList.getSelectedItem()), control.getShows_al().get(showsList.getSelectedIndex()).getRoom_id(), control.getShows_al().get(showsList.getSelectedIndex()).getShow_start());
                }
                //init();
            }
        });

        movieConfigButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                movieDialog.setVisible(true);
            }
        });

        addMovie.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<LocalDateTime> showsAl = new ArrayList<LocalDateTime>();
                for(String date : dates.getText().split(",")) {
                   showsAl.add(control.makeDateFromString(date.trim()));
                }

                ArrayList<String> castingAl = new ArrayList<String>(Arrays.asList(actors.getText().trim().split(",")));
                ArrayList<String> genresAl = new ArrayList<String>(Arrays.asList(genres.getText().trim().split(",")));

                control.addMovie(
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
                movieDialog.setVisible(false);
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
