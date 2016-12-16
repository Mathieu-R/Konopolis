
package src.konopolis.view;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
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

import static javafx.application.Application.launch;

/**
 * @author Mathieu
 */
public class KonopolisViewGUI extends KonopolisView implements Observer {

    ComboBox moviesList = new ComboBox(); // choiceBox => list of movies
    ComboBox showsList = new ComboBox(); // choiceBox => list of shows (for a movie)
    Button configButton = new Button("Configuration");
    Pane mappingRoom = new Pane();
    Group booking = new Group();

    Room selectedRoom;

    public KonopolisViewGUI(KonopolisController controller, KonopolisModel model) {
        super(model, controller);
        init();
    }

    @Override
    public void init() {
        launch();
    }

    public void start(Stage stage) throws Exception {
        stage.setWidth(1200); // 1200px
        stage.setHeight(900); // 900 px
        stage.setTitle("Konopolis");

        Group root = new Group(); // Root
        Scene scene = new Scene(root); // Scene
        scene.setFill(Paint.valueOf("#2c3e50")); // Background

        Group config = new Group(); // Config

        Label selectAMovie = new Label("Sélectionnez un film"); // Label
        selectAMovie.setFont(new Font("Roboto", 16));

        //moviesList = new ComboBox(); // choiceBox => list of movies
        control.retrieveAllMoviesTitles().forEach((key, value) -> {
            moviesList.getItems().add(value);
        });
        // onclick on a movie => launch the displayShows method
        moviesList.setOnAction(event -> displayShows(moviesList.getSelectionModel().getSelectedIndex()));

        //showsList = new ComboBox(); // choiceBox => list of shows (for a movie)
        showsList.setOnAction(event -> displayRoom(showsList.getSelectionModel().getSelectedIndex()));

        // Add elements to the config group
        config.getChildren().addAll(selectAMovie, moviesList, showsList, configButton);

        // Init
        root.getChildren().add(config); // Add the children group to the root group
        stage.setScene(scene); // Add the scene to the stage
        stage.show(); // Open the curtains
    }

    private void displayShows(int moviesListNumber) {
        int movie_id = (new ArrayList<Integer>(control.getMoviesTitles().keySet())).get(moviesListNumber - 1); // get the right id according its position in the LinkedHashMap
        Movie movie = control.retrieveMovie(movie_id);
        showsList.getItems().clear();
        /*infos.setText(
                movie.getDescription() +
                        " Prix: " + movie.getPrice()
        );*/

        for(Show sh: movie.getShows()){
            showsList.getItems().add("Salle n°" + sh.getRoom_id() + " - " + control.dateInFrench(sh.getShow_start()));
        }
        //Select the first item of the list
        //showsList.setSelectedIndex(0);
    }

    /**
     * Display the room at the moment of the show
     */
    private void displayRoom(int showsListNumber) {
        int size = 0;
        int rows = 0;
        int columns = 0;

        // Select the show
        Show selectedShow = control.getShows_al().get(showsListNumber - 1);
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
        //panelDisplay.setLayout(new GridLayout(rows, columns));
        //panelDisplay.setPreferredSize(new Dimension(rows * size, columns * size));

        //JButton[][] grid = new JButton[selectedRoom.getRows()][selectedRoom.getSeatsByRow()]; // grid of JButtons
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                // Button
                Button seat = new Button((y + 1) + ", " + (x + 1));
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
        mappingRoom.setStyle("-fx-box-shadow: 0 0 2px rgba(0, 0, 0, .2); -fx-border-radius: 3px"); // CSS style
        booking.getChildren().add(mappingRoom);
        //panelDisplay.repaint();
        //panelDisplay.revalidate();
        // Add the Map to the Jframe
        //frame.add(panelDisplay, BorderLayout.CENTER);
    }

    private void booking() {
        // TODO booking seats
    }

    private void auth() {
        // TODO auth admin
    }

    @Override
    public void update(Observable o, Object arg) {

    }

    public static void main(String[] args) {
        launch(args);
    }
}
