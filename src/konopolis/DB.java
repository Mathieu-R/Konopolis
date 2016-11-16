package konopolis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost/Konopolis";
    private final String USER = "root";
    private final String PWD = "root";

    public DB() {
        registerDriver();
    }

    /**
     * Chargement du driver mysql
     * A faire une seule fois ! Au lancement de l'app.
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
     * Crée la connection à la db Konopolis
     */
    public void createConnection() {
        System.out.println("Connecting to Konopolis DB...");
        try {
            Connection conn = DriverManager.getConnection(DB_URL, USER, PWD);
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
}
