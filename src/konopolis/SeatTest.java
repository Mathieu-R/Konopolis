package konopolis;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.testng.Assert.assertFalse;

public class SeatTest {
    @Test
    public void equals() throws Exception {
        final Seat seat1 = new Seat(1, 1);
        final Seat seat1Copy = new Seat(1, 1);
        final Seat seat2 = new Seat(1, 2);
        assertFalse(seat1.equals(seat2));
        assertTrue(seat1.equals(seat1Copy));
    }

}