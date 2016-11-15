package konopolis;

import java.util.ArrayList;

public class Room {

    private static int id;
    private int totSeats;
    private int rows;
    private int seatsByRow;
    private ArrayList<ArrayList<Seat>> seats = new ArrayList<ArrayList<Seat>>();
    private double cost;
    private static double income = 0.0;

    /**
     * Constructors
     */

    public Room(int rows, int sitsByRow) {
        if (rows > 20 || sitsByRow > 35) {
            System.out.println("Trop de rangées et/ou de sièges par rangée");
            return;
        }

        this.rows = rows;
        this.seatsByRow = sitsByRow;

        this.totSeats = rows * sitsByRow;

        // Init the seats (ArrayList) with all the coordinates of the seats
        for (int i = 0; i < rows; i++) {
            ArrayList<Seat> tempList = new ArrayList<Seat>();
            for (int j = 0; j < sitsByRow; j++) {
                tempList.add(j, new Seat(i + 1, j+ 1));
                //seats.(i).set(j, new Seat(i + 1, j + 1)); // i + 1, j + 1 => a sit does not have a place 0
            }
            seats.add(tempList);
        }
    }

    /**
     * Methods
     */


    /*public void giveSeat(Seat seat) {
        for (ArrayList<Seat> seatsRow : seats) { // For every row
            if (seatsRow.contains(seat)) { // If the row ArrayList contains the sit
                final int index = seatsRow.indexOf(seat); // Index of the sit
                seatsRow.get(index).setTaken(true); // The sit is reserved
            }
        }
    }*/
    /**
     * Réserve un siège si celui-ci existe bien dans la salle et n'est pas déjà prit
     * @param x, rangée du siège
     * @param y, colonne du siège
     */
    public void giveSeat(int x, int y) {
        if ((y-1) > rows || (x-1) > seatsByRow) {
            System.out.println("Ce siège n'existe pas");
            return;
        }
        if (seats.get(x-1).get(y-1).isTaken()) {
            System.out.println("Ce siège est déjà prit");
            return;
        }
        seats.get(x-1).get(y-1).setTaken(true);
    }

    /**
     * Annule la réservation d'un siège si celui-ci existe et est réservé
     * @param x, rangée du siège
     * @param y, colonne du siège
     */
    public void cancelSeat(int x, int y) {
        if ((y-1) > rows || (x-1) > seatsByRow) {
            System.out.println("Ce siège n'existe pas");
            return;
        }
        if (!seats.get(x-1).get(y-1).isTaken()) {
            System.out.println("Ce siège n'est pas encore prit");
            return;
        }
        seats.get(x-1).get(y-1).setTaken(false);
    }

    /**
     * Vide la salle, c'est à dire qu'aucun siège n'est réservé
     */
    public void emptyRoom() {
        for (ArrayList<Seat> sitsRow : seats) { // For Each sit
            for (Seat seat : sitsRow) {
                seat.setTaken(false); // The seat is not reserved
            }
        }
    }

    /**
     * Affiche la représentation console de la salle
     */
    public void displayRoom() {
        for (ArrayList<Seat> sitsRow : seats) {
            for (Seat seat : sitsRow) {
                if (seat.isTaken()) System.out.print("[X]");
                else System.out.print("[O]");
            }
            System.out.print("\n"); // Turn back to the line for every row
        }

    }



    /**
     * Getters and Setters
     */

    public static int getId() {
        return id;
    }

    public static void setId(int id) {
        Room.id = id;
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

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
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
                ", seatsByRow=" + seatsByRow +
                ", seats=" + seats +
                ", cost=" + cost +
                '}';
    }

    public static void main(String[] args) {
        Room room = new Room(10, 20);
        room.displayRoom();
        System.out.println("================");
        room.giveSeat(1, 2);
        room.displayRoom();
    }
}
