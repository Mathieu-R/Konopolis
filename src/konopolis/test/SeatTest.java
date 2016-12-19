package konopolis.test;

import org.junit.Test;
import konopolis.model.Seat;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * @author Mathieu R. - Groupe 3
 */

public class SeatTest {
    @Test
    public void equals() throws Exception {
        final Seat seat1 = new Seat(1, 1);
        final Seat seat1Copy = new Seat(1, 1);
        final Seat seat2 = new Seat(1, 2);
        assertFalse(seat1.equals(seat2)); // Seat 1 is not same as Seat 2
        assertTrue(seat1.equals(seat1Copy)); // Seat 1 is the same as Seat1Copy
    }

}