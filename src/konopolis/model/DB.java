package src.konopolis.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author Mathieu R. - Groupe 3
 */

public class DB {

    private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/Konopolis";
    private final String USER = "root";
    private final String PWD = "root";

    Connection conn = null;
    Statement stmt = null;

    public DB() {
        registerDriver();
    }

    /**
     * loading of the mysql driver
     * To do only once ! During the app launching.
     * @throws ClassNotFoundException
     */
    public void registerDriver() {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Driver OK");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the connection to the DB (Konopolis)
     */
    public void createConnection() {
        System.out.println("Connecting to Konopolis DB...");
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all the movies titles from the db
     * That's what we want at the launching of our app
     * Stored in a HashMap (movies)
     * @return HashMap that contains the id and the title of the movies
     */
    public HashMap<Integer, String> retrieveAllMoviesTitles() {
        HashMap<Integer, String> movies = new HashMap<Integer, String>(); // Local HashMap for movies

        String sql = "SELECT movie_id, title" +
                     "FROM tbmovies";

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results
                int movie_id  = rs.getInt("movie_id");
                String title = rs.getString("title");

                System.out.println(title);
                movies.put(movie_id, title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    /**
     * Retrieve movie's info from the db
     */
    public void retrieveMovie(int movie_id) {
        String sql = "SELECT m.movie_id, title, description, director, cast, genre, time, language, price" 
        		+ "FROM tbmovies as m inner join tblanguages as l on m.language_id = l.language_id"
                + "inner join tbmoviescasts as mc on m.movie_id = mc.movie_id"
                + "inner join tbcasts as c on mc.cast_id = c.cast_id "
                + "inner join tbmoviesgenres as mg on m.movie_id = mg.movie_id"
                + "inner join tbgenres as g on mg.genre_id = g.genre_id" 
        		+ "WHERE m.movie_id = " + movie_id;
        
        String sql2 = "SELECT m.movie_id, mr.room_id, title, description, director," 
					+ "(select group_concat(c.cast) " 
					+ "from tbmoviescasts as mc "
					+ "left join tbcasts as c on mc.cast_id = c.cast_id) as casting,"
			        + "(select group_concat(g.genre) " 
					+ "from tbmoviesgenres as mg "
					+ "left join tbgenres as g on mg.genre_id = g.genre_id) as genres,"
					+ "(select group_concat(show_start) "
					+ "from tbmovies as m "
					+ "left join tbmoviesrooms as mr on m.movie_id = mr.movie_id) as shows,"
			        + "time, language, price "
			        + "from tbmovies as m "
			        + "left join tblanguages as l on m.language_id = l.language_id "
			        + "left join tbmoviesrooms as mr on m.movie_id = mr.room_id "
			        + "where m.movie_id = " + movie_id + " "
        			+ "limit 1"; // we only want the first result => temp. fix, otherwise, we get 2 same results
        
        			// What about 2 rooms for one same movie ?
        			// Consider Group By

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql2);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int id  = rs.getInt("movie_id");
                int room_id = rs.getInt("room_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String director = rs.getString("director");
                // Split the string of casting ("act1, act2, act3") into Array ["act1", "act2", "act3"]
                String[] casting = rs.getString("casting").split(","); // Several actors
                String[] genres = rs.getString("genres").split(","); // Several genres
                //String[] shows = rs.getString("shows").split(",");
                String[] shows = rs.getString("shows").split(",");
                int time = rs.getInt("time");
                String language = rs.getString("language");
                double price = rs.getDouble("price");
                
                // ArrayList of Shows => contain all instance of shows for a specific movie
                ArrayList<Show> shows_al = new ArrayList<Show>();
                
                // For every date String
                for (String show : shows) {
                	// Parse it in LocalDate
                	DateTimeFormatter format_date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                	DateTimeFormatter format_time = DateTimeFormatter.ofPattern("kk:mm:ss");
            		// start of the show
                	LocalDate show_start_date = LocalDate.parse(show, format_date); // Date (yyyy-MM-dd)
                	LocalTime show_start_time = LocalTime.format(show, format_time); // Time (hh:mm:ss)
                	LocalDateTime show_start = show_start_date.of(show_start_date, show_start_time);
            		// end of the show => start of the show + time in minutes
            		final LocalDate show_end = LocalDate.parse(show, format).plus(time, ChronoUnit.MINUTES);
            		
            		// Add an instance of show => id = movie_id
            		shows_al.add(new Show(show_start, show_end, id, room_id));
                }
                
                // ArrayList of movies to be able to manage them
                ArrayList<Movie> movies = new ArrayList<Movie>();
                
                // Push every Movie' instance in this ArrayList
                movies.add(new Movie(id, title, description, genres, shows_al, director, casting, time, language, price));
                

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all the rooms that publish a movie
     * @param movie_id
     */
    public void retrieveRooms(int movie_id) {

        String sql = "SELECT room_id, rows, seats_by_row, incomes" +
                "FROM tbrooms" +
                "WHERE movie_id = " + movie_id;

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int room_id = rs.getInt("room_id");
                int rows = rs.getInt("rows");
                int seats_by_row = rs.getInt("seats_by_row");
                double incomes = rs.getDouble("incomes");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Retrieve all the customers of a [movie] that happens in a [room] at a time given ([show_start])
     * @param room_id
     * @param movie_id
     * @param show_start
     */
    public void retrieveCustomers(int room_id, int movie_id, LocalDate show_start) {
        String sql = "SELECT customer_id, customer_type, sRow, sColumn, isTaken" +
                "FROM tbcustomers join tbcustomersseats join tbseats join tbcustomerstype join tbrooms" +
                "WHERE room_id = " + room_id + "and movie_id = " + movie_id + "and show_start = " + show_start;

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int customer_id = rs.getInt("customer_id");
                String customer_type = rs.getString("customer_type");
                int row = rs.getInt("sRow");
                int column = rs.getInt("sColumn");
                boolean isTaken = rs.getBoolean("isTaken");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getDB_DRIVER() {
        return DB_DRIVER;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public String getUSER() {
        return USER;
    }

    public String getPWD() {
        return PWD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DB db = (DB) o;

        return DB_URL.equals(db.DB_URL);

    }

    @Override
    public int hashCode() {
        return DB_URL.hashCode();
    }

    @Override
    public String toString() {
        return "DB{" +
                "DB_DRIVER='" + DB_DRIVER + '\'' +
                ", DB_URL='" + DB_URL + '\'' +
                ", USER='" + USER + '\'' +
                ", PWD='" + PWD + '\'' +
                '}';
    }

    public static void main(String[] args) {
        final DB db = new DB();
        //db.registerDriver();
        db.createConnection();
        db.retrieveMovie(1);
    }
}
