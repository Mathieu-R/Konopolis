package src.konopolis.model;

import java.util.ArrayList;

/**
 * @author Mathieu R. - Groupe 3
 */

public class Room {

    private int id;
    private int totSeats;
    private int rows;
    private int seatsByRow;
    private ArrayList<ArrayList<src.konopolis.model.Seat>> seats = new ArrayList<ArrayList<Seat>>();
    private int movie_id;
    private static double income = 0.0; // Revenus du cinéma

    /**
     * Constructors
     */

    /**
     * Constructor without movie
     * @param rows
     * @param sitsByRow
     * @param id, id of the room
     */
    public Room(int rows, int sitsByRow, int id) {
        if (rows > 20 || sitsByRow > 35) {
            System.out.println("Too much rows or seats by row");
            return;
        }
        
        this.id = id;

        this.rows = rows;
        this.seatsByRow = sitsByRow;

        this.totSeats = rows * sitsByRow;

        initRoom();
    }

    /**
     * Constructor with movie
     * @param rows
     * @param sitsByRow
     * @param movie_id, id of the movie at a given show
     * @param id, id of the room
     */
    public Room(int rows, int sitsByRow, int movie_id, int id) {
        if (rows > 20 || sitsByRow > 35) {
            System.out.println("Too much rows or seats by row");
            return;
        }
        
        this.id = id;

        this.rows = rows;
        this.seatsByRow = sitsByRow;

        this.totSeats = rows * sitsByRow;

        this.movie_id = movie_id;

        initRoom();
    }

    /**
     * Methods
     */
    
    /**
     * Init the seats (ArrayList) with all the coordinates of the seats
     */
    public void initRoom() {
        for (int i = 0; i < rows; i++) {
            ArrayList<Seat> tempList = new ArrayList<Seat>();
            for (int j = 0; j < seatsByRow; j++) {
                tempList.add(j, new Seat(i + 1, j+ 1)); // i + 1, j + 1 => a sit does not have a place in 0
            }
            seats.add(tempList);
        }
    }

    /**
     * Book a seat if it exists and is not already taken
     * @param x, row of the seat
     * @param y, column of the seat
     */
    public void giveSeat(int x, int y) throws SeatUnknownException, SeatTakenException {
        if ((y-1) > rows || (x-1) > seatsByRow) { // If we exceed the room capacity
            throw new SeatUnknownException("This seat doesn't exist");
        }
        if (seats.get(x-1).get(y-1).isTaken()) { // If the seat is taken
            throw new SeatTakenException("This seat is already taken");
        }
        seats.get(x-1).get(y-1).setTaken(true);
    }

    /**
     * Cancel the booking of a seat if it exists and it is already taken
     * @param x, row of the seat
     * @param y, column of the seat
     */
    public void cancelSeat(int x, int y) throws SeatUnknownException, SeatNotTakenException {
        if ((y-1) > rows || (x-1) > seatsByRow) { // If we exceed the room capacity
            throw new SeatUnknownException("This seat doesn't exist");
        }
        if (!seats.get(x-1).get(y-1).isTaken()) { // If the seat is not taken
            throw new SeatNotTakenException("This seat is not taken yet, why cancel this reservation ?, it doesn't make sens !");
        }
        seats.get(x-1).get(y-1).setTaken(false);
    }

    /**
     * Empty the room, which means that no seats are taken
     */
    public void emptyRoom() {
        for (ArrayList<Seat> sitsRow : seats) { // For Each row
            for (Seat seat : sitsRow) { // For Each sit
                seat.setTaken(false); // The seat is not reserved
            }
        }
    }

    /**
     * Show the console representation of the room
     */
    public void displayRoom() {
        for (ArrayList<Seat> sitsRow : seats) { // For Each row
            for (Seat seat : sitsRow) { // For Each seat
                if (seat.isTaken()) System.out.print("[X]"); // If the seat is taken
                else System.out.print("[O]"); // Otherwise, if the seat is available
            }
            System.out.print("\n"); // Turn back to the line for every row
        }

    }



    /**
     * Getters and Setters
     */

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTotSeats() {
        return totSeats;
    }

    public void setTotSeats(int totSeats) {
        this.totSeats = totSeats;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSeatsByRow() {
        return seatsByRow;
    }

    public void setSeatsByRow(int seatsByRow) {
        this.seatsByRow = seatsByRow;
    }

    public ArrayList<ArrayList<Seat>> getSeats() {
        return seats;
    }

    public void setSeats(ArrayList<ArrayList<Seat>> seats) {
        this.seats = seats;
    }

    public int getMovie() {
        return movie_id;
    }

    public void setMovie(int movie_id) {
        this.movie_id = movie_id;
    }

    public static double getIncome() {
        return income;
    }

    public static void setIncome(double income) {
        Room.income = income;
    }

    /**
     * HashCode and Equals
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Room room = (Room) o;

        if (totSeats != room.totSeats) return false;
        if (seatsByRow != room.seatsByRow) return false;
        return seats.equals(room.seats);

    }

    @Override
    public int hashCode() {
        int result = totSeats;
        result = 31 * result + seatsByRow;
        result = 31 * result + seats.hashCode();
        return result;
    }

    /**
     * toString
     */


    @Override
    public String toString() {
        return "Room{" +
                "totSeats=" + totSeats +
                ", rows=" + rows +
                ", seatsByRow=" + seatsByRow +
                ", seats=" + seats +
                ", movie_id=" + movie_id +
                '}';
    }

    public static void main(String[] args) {
        Room room = new Room(10, 20, 1);
        room.displayRoom();
        System.out.println("================");
        //room.giveSeat(1, 2);
        room.displayRoom();
    }
}
