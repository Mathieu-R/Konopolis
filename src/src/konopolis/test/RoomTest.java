package src.konopolis.test;

import java.util.ArrayList;

import org.junit.Test;
import src.konopolis.model.*;

import static org.junit.Assert.*;

/**
 * @author Mathieu R. - Groupe 3
 */

public class RoomTest {

    @org.junit.Test
    public void constructor() {
        Room room1 = null;
        try {
            room1 = new Room(20, 35, 1);
        } catch (TooMuchSeatsException e) {
            e.printStackTrace();
        }
        assertEquals(20, room1.getRows()); // The room1 should have 20 rows
        assertEquals(35, room1.getSeatsByRow()); // The room1 should have 35 columns / seats by row
        assertEquals(700, room1.getTotSeats()); // The room1 should have 700 seats

        Room room2 = null;
        try {
            room2 = new Room(15, 10, 1);
        } catch (TooMuchSeatsException e) {
            e.printStackTrace();
        }
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

    /**
     * Test that an attempt to create an too great room throws TooMuchSeatsException
     */
    @org.junit.Test(expected = TooMuchSeatsException.class)
    public void overSeats() throws TooMuchSeatsException {
        Room overRows = new Room(21, 35, 1); // Too much rows
        Room overSitsByRow = new Room(20, 36, 1); // Too much seats by rows
    }

    /**
     * Test that an attemps to book an unknows seat throws an SeatUnknowsException
     */
    @org.junit.Test(expected = SeatUnknownException.class)
    public void unknownSeat() throws TooMuchSeatsException, SeatUnknownException, SeatTakenException {
        Room room1 = new Room(10, 10, 1);
        room1.giveSeat(11, 10); // This seat doesn't exist

    }

    /**
     * Test that an attempt to book a seat already taken throws a SeatTakenException
     */
    @org.junit.Test(expected = SeatTakenException.class)
    public void takenSeat() throws TooMuchSeatsException, SeatUnknownException, SeatTakenException {
        Room room1 = new Room(10, 10, 1);
        room1.giveSeat(10, 10); // We book the seat a first time
        room1.giveSeat(10, 10); // This seat is already taken

    }

    /**
     * Test that an attempt to cancel the booking of a seat not booked throws a SeatNotTakenException
     */
    @org.junit.Test(expected = SeatNotTakenException.class)
    public void notTakenSeat() throws TooMuchSeatsException, SeatUnknownException, SeatNotTakenException {
        Room room1 = new Room(10, 10, 1);
        room1.cancelSeat(10, 10); // This seat is not taken

    }

    @org.junit.Test
    public void giveSeat() {
        Room room1 = null;
        try {
            room1 = new Room(10, 10, 1);
        } catch (TooMuchSeatsException e) {
            e.printStackTrace();
        }
        try {
            room1.giveSeat(5, 10);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatTakenException e) {
            e.printStackTrace();
        }
        assertTrue(room1.getSeats().get(9).get(4).isTaken()); // The seat should be taken
    }

    @org.junit.Test
    public void cancelSit() {
        Room room1 = null;
        try {
            room1 = new Room(10, 10, 1);
        } catch (TooMuchSeatsException e) {
            e.printStackTrace();
        }
        try {
            room1.giveSeat(5, 10);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatTakenException e) {
            e.printStackTrace();
        }
        try {
            room1.cancelSeat(5, 10);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatNotTakenException e) {
            e.printStackTrace();
        }
        assertFalse(room1.getSeats().get(4).get(9).isTaken()); // The seat shouldn't be taken
    }

    @org.junit.Test
    public void emptyRoom() {
        Room room1 = null;
        try {
            room1 = new Room(10, 10, 1);
        } catch (TooMuchSeatsException e) {
            e.printStackTrace();
        }
        try {
            room1.giveSeat(1, 1);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatTakenException e) {
            e.printStackTrace();
        }
        try {
            room1.giveSeat(5, 10);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatTakenException e) {
            e.printStackTrace();
        }
        try {
            room1.giveSeat(10, 10);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatTakenException e) {
            e.printStackTrace();
        }
        room1.emptyRoom();
        for (ArrayList<Seat> seats: room1.getSeats()) {
            for (Seat seat: seats) {
                assertFalse(seat.isTaken()); // All the seats should be available
            }
        }
    }

    @Test
    public void getSeatsLeft() {
        Room room1 = null;
        Room room2 = null;
        try {
            room1 = new Room(10, 10, 1);
            room2 = new Room(5,5,2);
        } catch (TooMuchSeatsException e) {
            e.printStackTrace();
        }
        try {
            room1.giveSeat(1, 1);
            room2.giveSeat(1,1);
            room2.giveSeat(4,1);
            room2.giveSeat(5,5);
        } catch (SeatUnknownException e) {
            e.printStackTrace();
        } catch (SeatTakenException e) {
            e.printStackTrace();
        }
        assertEquals(99, room1.getSeatsLeft()); // It should have 99 seats left
        assertEquals(22, room2.getSeatsLeft()); // It should have 22 seats left
    }
}