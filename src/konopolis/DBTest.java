package konopolis;

import org.junit.Test;

public class DBTest {

    @Test
    public void registerDriver() throws Exception {
        // registerDriver est appelé dans le constructeur
        DB db = new DB();
    }

    @Test
    public void createConnection() throws Exception {
        DB db = new DB();
        db.createConnection();
    }

}