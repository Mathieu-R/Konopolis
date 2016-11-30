package src.konopolis.test;

import java.util.ArrayList;

import src.konopolis.model.Room;
import src.konopolis.model.Seat;

import static org.junit.Assert.*;

/**
 * @author Mathieu R. - Groupe 3
 */

public class RoomTest {
    @org.junit.Test
    public void constructor() throws Exception {
        Room overRows = new Room(21, 35, 1);
        Room overSitsByRow = new Room(20, 36, 1);
        Room room1 = new Room(20, 35, 1);
        assertEquals(20, room1.getRows()); // The room1 should have 20 rows
        assertEquals(35, room1.getSeatsByRow()); // The room1 should have 35 columns / seats by row
        assertEquals(700, room1.getTotSeats()); // The room1 should have 700 seats

        Room room2 = new Room(15, 10, 1);
        assertFalse(room1.equals(room2)); // The seats are not the same

        for (ArrayList<Seat> rows: room1.getSeats()) {
            for (int i = 0; i < rows.size() - 1; i++) { // -1 ? Because, otherwise we would exceed the room capacity
                assertFalse(rows.get(i).equals(rows.get(i + 1))); // Seats are not the same (different seats)
            }
        }

        final Seat seat1 = room1.getSeats().get(0).get(0); // First Row, first column
        assertEquals(1, seat1.getRow()); // seat1 should be row 1
        assertEquals(1, seat1.getColumn()); // seat1 should be column 1
        final Seat seat2 = room1.getSeats().get(9).get(24);
        assertEquals(10, seat2.getRow()); // seat2 should be row 10
        assertEquals(25, seat2.getColumn()); // seat2 should be column 25
    }

    @org.junit.Test
    public void giveSit() throws Exception {
        Room room1 = new Room(10, 10, 1);
        room1.giveSeat(5, 10);
        assertTrue(room1.getSeats().get(4).get(9).isTaken()); // The seat should be taken
    }

    @org.junit.Test
    public void cancelSit() throws Exception {
        Room room1 = new Room(10, 10, 1);
        room1.giveSeat(5, 10);
        room1.cancelSeat(5, 10);
        assertFalse(room1.getSeats().get(4).get(9).isTaken()); // The seat shouldn't be taken
    }

    @org.junit.Test
    public void emptyRoom() throws Exception {
        Room room1 = new Room(10, 10, 1);
        room1.giveSeat(1, 1);
        room1.giveSeat(5, 10);
        room1.giveSeat(10, 10);
        room1.emptyRoom();
        for (ArrayList<Seat> seats: room1.getSeats()) {
            for (Seat seat: seats) {
                assertFalse(seat.isTaken()); // All the seats should be available
            }
        }
    }

    @org.junit.Test
    public void displayRoom() throws Exception {
        Room room1 = new Room(10, 10, 1);
        room1.giveSeat(1, 1);
        room1.displayRoom();
        /* No Test */
    }



}