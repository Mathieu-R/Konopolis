package src.konopolis.model;

import java.sql.*;

/**
 * @author Mathieu R. - Groupe 3
 */

public class DB {

    private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/Konopolis";
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
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the connection the the DB (Konopolis)
     */
    public void createConnection() {
        System.out.println("Connecting to Konopolis DB...");
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Init of app
     * Retrive all the informations to make the app work
     */
    public void init() {
        try {
            this.stmt = conn.createStatement();
            retrieveMovies();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public void retrieveMovies() {
        String sql = "SELECT movie_id, title, description, director, time, language, price" +
                     "FROM tbmovies join tblanguages";

        ResultSet rs = stmt.executeQuery(sql); // Execute the sql query and put the results in the results set

        while (rs.next()) { // While there're still results

            int movie_id  = rs.getInt("movie_id");
            String title = rs.getString("title");
            String description = rs.getString("description");
            String director = rs.getString("director");
            String language = rs.getString("language");
            double price = rs.getDouble("price");

        }
        rs.close();
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
}
