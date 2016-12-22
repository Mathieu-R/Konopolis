package src.konopolis.model;

import java.util.ArrayList;

/**
 * @author Mathieu R. - Groupe 3
 */

public class Room {

    private static int currentId = 0;
    private int id;
    private int totSeats;
    private int rows;
    private int seatsByRow;
    private ArrayList<ArrayList<Seat>> seats = new ArrayList<ArrayList<Seat>>();
    private Movie movie;

    /**
     * Constructors
     */

    /**
     * Constructor without movie
     * @param rows
     * @param seatsByRow
     * @param id, id of the room
     */
    public Room(int rows, int seatsByRow, int id) throws TooMuchSeatsException {
        if (rows > 20 || seatsByRow > 35) {
            throw new TooMuchSeatsException("Too much rows or seats by row");
        }
        
        currentId++;
        this.id = id;
        this.rows = rows;
        this.seatsByRow = seatsByRow;

        this.totSeats = rows * seatsByRow;

        initRoom();
    }

    /**
     * Constructor with movie
     * @param rows
     * @param seatsByRow
     * @param movie, instance of the movie broadcasted in the room
     * @param id, id of the room
     */
    public Room(int rows, int seatsByRow, Movie movie, int id) throws TooMuchSeatsException {
        if (rows > 20 || seatsByRow > 35) {
            throw new TooMuchSeatsException("Trop de rangée ou de siège par rangée");
        }
        
        currentId++;
        this.id = id;

        this.rows = rows;
        this.seatsByRow = seatsByRow;

        this.totSeats = rows * seatsByRow;

        this.movie = movie;

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
        if (y > rows || x > seatsByRow || y <= 0 || x <= 0) { // If we exceed the room capacity
            throw new SeatUnknownException("Ce siège n'existe pas !");
        }
        if (seats.get(y-1).get(x-1).isTaken()) { // If the seat is taken
            throw new SeatTakenException("Ce siège est dékà pris !");
        }
        seats.get(y-1).get(x-1).setTaken(true);
    }

    /**
     * Cancel the booking of a seat if it exists and it is already taken
     * @param x, row of the seat
     * @param y, column of the seat
     */
    public void cancelSeat(int x, int y) throws SeatUnknownException, SeatNotTakenException {
        if (y > rows || x > seatsByRow || y <= 0 || x <= 0) { // If we exceed the room capacity
            throw new SeatUnknownException("Ce siège n'existe pas !");
        }
        if (!seats.get(y-1).get(x-1).isTaken()) { // If the seat is not taken
            throw new SeatNotTakenException("Ce siège n'est pas réservé, pourquoi annuler une réservation imaginaire ?");
        }
        seats.get(y-1).get(x-1).setTaken(false);
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
     * Get the number of seats left
     * @return, int, the number of seats left
     */
    public int getSeatsLeft() {
        int compteur = 0;
        for (ArrayList<Seat> seatsRow : seats) {
            for (Seat seat : seatsRow) {
                if (!(seat.isTaken())) compteur++; // Si le siège n'est pas pris, on incrémente le compteur
            }
        }
        return compteur;
    }

    /**
     * Getters and Setters
     */

    public static int getCurrentId() {
        return currentId;
    }

    public static void setCurrentId(int currentId) {
        Room.currentId = currentId;
    }

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

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
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
                ", movie=" + movie +
                '}';
    }

}
