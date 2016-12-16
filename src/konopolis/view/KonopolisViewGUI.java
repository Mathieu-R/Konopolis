
package src.konopolis.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import src.konopolis.controller.KonopolisController;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Movie;
import src.konopolis.model.Room;
import src.konopolis.model.Show;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Mathieu
 */
public class KonopolisViewGUI extends Application implements Observer {

    public static KonopolisController control;
    public static KonopolisModel model;

    ComboBox moviesList; // choiceBox => list of movies
    ComboBox showsList; // choiceBox => list of shows (for a movie)
    Button configButton;
    Pane mappingRoom;
    Group booking;

    BorderPane container;
    TilePane descriptionPanel;

    Room selectedRoom;

    public KonopolisViewGUI(/*KonopolisController controller, KonopolisModel model*/) {
        /*this.control = controller;
        this.model = model;
        model.addObserver(this);*/
        //launch(args);
    }

    public void addObserverLazy() {
        model.addObserver(this);
    }

    public static void initialize(String[] args) {
        launch(args);
    }

    public void start(Stage stage) throws Exception {

        moviesList = new ComboBox(); // choiceBox => list of movies
        showsList = new ComboBox(); // choiceBox => list of shows (for a movie)
        configButton = new Button("Configuration");
        mappingRoom = new Pane();
        booking = new Group();

        stage.setWidth(1200); // 1200px
        stage.setHeight(900); // 900 px
        stage.setTitle("Konopolis");

        Group root = new Group(); // Root
        Scene scene = new Scene(root); // Scene
        scene.setFill(Paint.valueOf("#2c3e50")); // Background

        // Container
        container = new BorderPane();

        // Top Bar
        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15,12,15,12));
        hbox.setSpacing(10);
        hbox.setPrefWidth(1200);
        hbox.setStyle("-fx-background-color: #fff; -fx-display: flex; -fx-justify-content: center");

        //Group config = new Group(); // Config

        Label selectAMovie = new Label("Sélectionnez un film: "); // Label
        selectAMovie.setFont(new Font("Roboto", 16));

        //moviesList = new ComboBox(); // choiceBox => list of movies
        control.retrieveAllMoviesTitles().forEach((key, value) -> {
            moviesList.getItems().add(value);
        });

        moviesList.getSelectionModel().select(0);
        displayShows(0);
        displayRoom(0);

        hbox.getChildren().addAll(selectAMovie, moviesList, showsList, configButton);

        // Infos box
        TilePane descriptionPanel = new TilePane();
        descriptionPanel.setPadding(new Insets(5, 10, 5, 10));
        descriptionPanel.setStyle("-fx-background-color: #fff; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,.2), 10, 1.0, 0, 0); -fx-height: 100%; -fx-width: 250px");

        // Add elements to the container
        container.setTop(hbox);
        container.setRight(descriptionPanel);
        // Add elements to the config group
        //config.getChildren().add(selectAMovie /*, moviesList, showsList, configButton*/);

        // Init
        root.getChildren().add(container); // Add the children group to the root group
        stage.setScene(scene); // Add the scene to the stage
        stage.show(); // Open the curtains

        // Events
        // onclick on a movie => launch the displayShows method
        moviesList.setOnAction(event -> displayShows(moviesList.getSelectionModel().getSelectedIndex()));

        //showsList = new ComboBox(); // choiceBox => list of shows (for a movie)
        showsList.setOnAction(event -> displayRoom(showsList.getSelectionModel().getSelectedIndex()));
    }

    private void displayShows(int moviesListNumber) {
        System.out.println(moviesListNumber);
        int movie_id = (new ArrayList<Integer>(control.getMoviesTitles().keySet())).get(moviesListNumber); // get the right id according its position in the LinkedHashMap
        Movie movie = control.retrieveMovie(movie_id);
        showsList.getItems().clear();

        for(Show sh: movie.getShows()){
            showsList.getItems().add("Salle n°" + sh.getRoom_id() + " - " + control.dateInFrench(sh.getShow_start()));
        }

        displayDescription(movie);
        //Select the first item of the list
        showsList.getSelectionModel().select(0);
    }

    private void displayDescription(Movie movie) {
        ArrayList<Label> descriptionLabels = new ArrayList<Label>();
        descriptionLabels.add(new Label("Title: " + movie.getTitle()));
        descriptionLabels.add(new Label("Description: "));
        descriptionLabels.add(new Label(movie.getDescription()));
        descriptionLabels.add(new Label("Réalisateur: " + movie.getDirector()));
        descriptionLabels.add(new Label("Acteurs principaux: "));
        for (String acteur : movie.getCasting()){
            descriptionLabels.add(new Label("> " + acteur));
        }
        descriptionLabels.add(new Label("Genres: "));
        for(String genre : movie.getGenres()){
            descriptionLabels.add(new Label("> " + genre));
        }
        descriptionLabels.add(new Label("Langue: " + movie.getLanguage()));
        descriptionLabels.add(new Label("Durée: " + movie.getTime() + "min"));
        descriptionLabels.add(new Label("Prix: " + movie.getPrice() +"€"));

        descriptionPanel.getChildren().addAll(descriptionLabels);
    }

    /**
     * Display the room at the moment of the show
     */
    private void displayRoom(int showsListNumber) {
        int size = 0;
        int rows = 0;
        int columns = 0;

        // Select the show
        Show selectedShow = control.getShows_al().get(showsListNumber);
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
        mappingRoom.getChildren().removeAll();
        mappingRoom.setPrefSize(rows * size, columns * size);

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // Button
                Button seat = new Button();
                seat.setStyle("transition: color .2s;");

                if (selectedRoom.getSeats().get(y).get(x).isTaken()) {
                    seat.setStyle("-fx-background-color: #e74c3c"); // red
                } else {
                    seat.setStyle("-fx-background-color: lightgray");
                }

                // Event Listener
                int finalX = x;
                int finalY = y;

                seat.setOnAction(event -> {
                    booking();
                });

                seat.setOnMouseEntered(event -> {
                    if (!(selectedRoom.getSeats().get(finalY).get(finalX).isTaken())) {
                        seat.setStyle("-fx-background-color: #27ae60"); // green
                    }
                });

                seat.setOnMouseExited(event -> {
                    if(seat.getBackground().getFills().get(1).equals(Color.web("#27ae60"))) {
                        seat.setStyle("-fx-background-color: lightgray");
                    }
                });

                // Add to the mappingRoom Pane
                mappingRoom.getChildren().add(seat);
            }
        }
        //mappingRoom.setStyle("-fx-effect: three-pass-box, rgba(0, 0, 0, .2), 10, 0, 0, 0; -fx-background-radius: 3px"); // CSS style
        container.setCenter(mappingRoom);
    }

    private void booking() {
        // TODO booking seats
    }

    private void auth() {
        Stage stage = new Stage();
        stage.setTitle("Se connecter");
        stage.setWidth(500);
        stage.setHeight(250);
        Group root = new Group();
        Scene scene = new Scene(root);

        Group auth = new Group();
        Label userLabel = new Label("Nom d'utilisateur");
        Label pwLabel = new Label("Nom d'utilisateur");
        TextField username = new TextField();
        TextField password = new TextField();
        Button login = new Button("Se connecter");

        login.setOnAction(event -> makeAuth());


        auth.getChildren().addAll(userLabel, username, pwLabel, password, login);
        stage.setScene(scene);
        stage.show();
        // TODO auth admin
    }

    private boolean makeAuth() {
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void main(String[] args) {

    }
}
