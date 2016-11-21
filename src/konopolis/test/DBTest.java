package src.konopolis.test;

import org.junit.Test;

/**
 * @author Mathieu R. - Groupe 3
 */

public class DBTest {

    @Test
    public void registerDriver() throws Exception {
        // registerDriver is called in the constructor
        DB db = new DB();
    }

    @Test
    public void createConnection() throws Exception {
        DB db = new DB();
        db.createConnection();
    }

}