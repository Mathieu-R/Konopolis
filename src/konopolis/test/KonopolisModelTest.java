package src.konopolis.test;

import org.junit.*;
import src.konopolis.model.InvalidUserException;
import src.konopolis.model.KonopolisModel;
import src.konopolis.model.Room;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class KonopolisModelTest {

  private KonopolisModel model;

  public KonopolisModelTest() {
    this.model = new KonopolisModel();
  }

  /**
   * Before a test
   * Set the DB of test
   * Drop tables, add tables and some data to make the test
   */
  @Before
  public void before() {
    model.setDB_URL("jdbc:mysql://localhost:3306/konopolisTest?autoReconnect=true&useSSL=false");
    model.createConnection();

    String s;
    StringBuffer sb = new StringBuffer();

    try {
      FileReader freader = new FileReader(new File("bdd/KonopolisTest_data.sql")); // read the data from sql script
      BufferedReader br = new BufferedReader(freader);

      while ((s = br.readLine()) != null) { // parse the buffer
        sb.append(s); // add each line to the StringBuffer
      }

      br.close(); // close the buffer

      String[] instr = sb.toString().split(";"); // split each request (;)

      model.createStatement();

      for (String inst : instr) {
        // remove blank space around
        // ensure we do not execute any empty statement
        if (!inst.trim().equals("")) {
          model.getStmt().executeUpdate(inst);
          // System.out.println(">> " + inst);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("================================================");
      System.out.println(sb.toString());
    } finally {
      try {
        model.getConn().close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * After a test
   * Set back the production db
   */
  @After
  public void after() {
    // set back the production DB
    model.setDB_URL("jdbc:mysql://localhost:3306/konopolis?autoReconnect=true&useSSL=false");
  }

  @Test
  public void authUser() throws Exception {
    assertTrue(model.authUser("admin", "monsuperpassword"));
  }

  @Test(expected = InvalidUserException.class)
  public void authUserWrongPassword() throws InvalidUserException {
    // assertThrown
    assertFalse(model.authUser("admin", "brol"));

  }

  @Test
  public void retrieveOrCreateGenreId() throws Exception {
    int action = model.retrieveOrCreateGenreId("Action");
    int comedy = model.retrieveOrCreateGenreId("Comédie");
    int drama = model.retrieveOrCreateGenreId("Drame");
    assertEquals(1, action); // action should be id 1
    assertEquals(2, comedy); // etc...
    assertEquals(3, drama);
  }

  @Test
  public void retrieveAllMoviesTitles() throws Exception {
    final LinkedHashMap<Integer, String> moviesList = model.retrieveAllMoviesTitles(); // retrieve all the titles
    moviesList.forEach((key, value) -> {
      assertEquals("Samba", value);
    });
  }

  @Test
  public void retrieveMovie() throws Exception {
    assertEquals("Samba", model.retrieveMovie(1).getTitle()); // Samba should be the movie with the id 1
  }

  @Test
  public void retrieveRoom() throws Exception {
    retrieveMovie();
    // movie_id 1 => Samba, room_id => 2 (10 rows, 10 seats by row), show =>
    // December the 19th, at 3:30 p.m.
    LocalDateTime show = makeDate(19, 12, 2016, 15, 30);
    assertEquals("Samba", model.retrieveRoom(1, 2, show).getMovie().getTitle()); // Name should be samba
    assertEquals(100, model.retrieveRoom(1, 2, show).getTotSeats()); // There should be 100 seats
  }

  @Test
  public void retrieveAllRooms() throws Exception {

    model.retrieveAllRooms();
    assertEquals(450, model.getRooms_al().get(0).getRows() * model.getRooms_al().get(0).getSeatsByRow()); // 450 seats
    assertEquals(15, model.getRooms_al().get(0).getRows()); // Room 1 => 15 rows
    assertEquals(15, model.getRooms_al().get(3).getRows()); // Room 4 => 15 rows
    assertEquals(8, model.getRooms_al().get(3).getSeatsByRow()); // Room 4 => 8 seats by row
  }

  @Test
  public void retrieveCustomers() throws Exception {
    // addMovie(); // add a movie and its shows first
    // addCustomer(); // add customer
    model.retrieveCustomers(1, 1, makeDate(19, 12, 2016, 15, 30));
    for (Room room : model.getRooms_al()) {
      if (room.getId() == 1) {
        // TO DO
      }
    }
  }

  @Test
  public void retrieveTypes() throws Exception {
    ArrayList<String> types = model.retrieveTypes(); // get types of people
    assertTrue(types.contains("Etudiant"));
    assertTrue(types.contains("VIP"));
    assertFalse(types.contains("Bébé"));
  }

  @Test
  public void retrieveCustomerTypeId() throws Exception {
    assertEquals(2, model.retrieveCustomerTypeId("Etudiant")); // Etudiant => id 2
    assertEquals(4, model.retrieveCustomerTypeId("VIP")); // VIP => id 4
  }

  @Test
  public void retrieveMovieRoomId() throws Exception {
    // movie_id 1 => Samba, room_id => 2 (10 rows, 10 seats by row), show =>
    // December the 19th, at 3:30 p.m.
    assertEquals(1, model.retrieveMovieRoomId(1, 2, makeDate(19, 12, 2016, 15, 30))); // id 1
  }

  @Test
  public void retrieveLanguageId() throws Exception {
    assertEquals(1, model.retrieveLanguageId("Français"));
  }

  @Test
  public void retrieveOrCreateCastId() throws Exception {
    assertEquals(2, model.retrieveOrCreateCastId("Omar Sy")); // id 2
    assertEquals(3, model.retrieveOrCreateCastId("Tahar Rahim")); // id 3
    assertEquals(4, model.retrieveOrCreateCastId("Alain Chabat")); // new actor => id 4
  }

  @Test
  public void retrieveMovieId() throws Exception {
    assertEquals(1, model.retrieveMovieId("Samba")); // Samba has id 1
  }

  /**
   * Add a movie to the db
   *
   * @throws Exception
   */
  private void addMovie() throws Exception {
    // Description
    String description = "Samba, sénégalais en France depuis 10 ans, collectionne les petits boulots ;" +
        " Alice est une cadre supérieure épuisée par un burn out." +
        " Lui essaye par tous les moyens d'obtenir ses papiers," +
        " alors qu'elle tente de se reconstruire par le bénévolat dans une association." +
        " Chacun cherche à sortir de son impasse jusqu'au jour où leurs destins se croisent... " +
        "Entre humour et émotion, leur histoire se fraye un autre chemin vers le bonheur. " +
        "Et si la vie avait plus d'imagination qu'eux ?";
    // Shows
    LocalDateTime show1 = makeDate(19, 12, 2016, 15, 30);
    LocalDateTime show2 = makeDate(20, 12, 2016, 17, 15);
    ArrayList<LocalDateTime> shows = new ArrayList<LocalDateTime>();
    shows.add(show1);
    shows.add(show2);
    // Casting
    ArrayList<String> casting = new ArrayList<String>();
    casting.add("Omar Sy");
    casting.add("Charlotte Gainsbourg");
    casting.add("Tahar Rahim");
    // genres
    ArrayList<String> genres = new ArrayList<String>();
    genres.add("Comédie");
    genres.add("Drame");
    // ADD MOVIE METHOD
    model.addMovie(2, "Samba", description, "Eric Toledano", shows, casting, 118, "Français", 10, genres);
  }

  /**
   * Create a Date object from a day, month, year, hours and minutes
   * month - 1 because month is "0 based" so it begins from 0 but the user begin
   * by 1.
   *
   * @return LocalDateTime, a LocalDateTime constructed from the parameters passed
   *         in the function
   */
  private LocalDateTime makeDate(int day, int month, int year, int hours, int minutes) {
    return LocalDateTime.of(year, month, day, hours, minutes);
  }

  /**
   * create a new room
   *
   * @param rows,       number of rows
   * @param seatsByRow, number of seat by rows
   * @throws SQLException
   */
  private void createRoom(int rows, int seatsByRow) throws SQLException {
    PreparedStatement addRo = null;
    model.createConnection();
    addRo = model.getConn().prepareStatement("INSERT into tbrooms(rows, seats_by_row) VALUES (?, ?)");
    addRo.setInt(1, rows);
    addRo.setInt(2, seatsByRow);
    addRo.executeUpdate(); // insert room
    addRo.close(); // close preparedStatement
    model.getConn().close(); // close connection
  }

  /**
   * create an user
   */
  private void createUser() throws Exception {
    model.createUser("admin", "monsuperpassword");
  }

  /**
   * Show a string
   *
   * @param str
   */
  private void show(String str) {
    System.out.println(str);
  }

}
