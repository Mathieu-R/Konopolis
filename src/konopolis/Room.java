package konopolis;

import java.util.ArrayList;

public class Room {

    private static int id;
    private int totSits;
    private int rows;
    private int sitsByRow;
    private ArrayList<ArrayList<Sit>> sits = new ArrayList<ArrayList<Sit>>();
    private double cost;
    private static double income;

    /**
     * Constructors
     */

    public Room(int rows, int sitsByRow) {
        if (rows > 20 || sitsByRow > 35) {
            System.out.println("Trop de rangées et/ou de sièges par rangée");
            return;
        }

        this.rows = rows;
        this.sitsByRow = sitsByRow;

        this.totSits = rows * sitsByRow;

        // Init the sits (ArrayList) with all the coordinates of the sits
        for (int i = 0; i < rows; i++) {
            ArrayList<Sit> tempList = new ArrayList<Sit>();
            for (int j = 0; j < sitsByRow; j++) {
                tempList.add(j, new Sit(i + 1, j+ 1));
                //sits.(i).set(j, new Sit(i + 1, j + 1)); // i + 1, j + 1 => a sit does not have a place 0
            }
            sits.add(tempList);
        }
    }

    /**
     * Methods
     */

    /**
     * Réserve un siège si celui-ci existe bien dans la salle
     * @param sit, un siège donné
     */
    public void giveSit(Sit sit) {
        for (ArrayList<Sit> sitsRow : sits) { // For every row
            if (sitsRow.contains(sit)) { // If the row ArrayList contains the sit
                final int index = sitsRow.indexOf(sit); // Index of the sit
                sitsRow.get(index).setTaken(true); // The sit is reserved
            }
        }
    }

    /**
     * Annule la réservation d'un siège
     * @param sit, un siège donné
     */
    public void cancelSit(Sit sit) {
        for (ArrayList<Sit> sitsRow : sits) { // For every row
            if (sitsRow.contains(sit)) { // If the row ArrayList contains the sit
                final int index = sitsRow.indexOf(sit); // Index of the sit
                sitsRow.get(index).setTaken(false); // The sit is NOT reserved anymore
            }
        }
    }

    /**
     * Vide la salle, c'est à dire qu'aucun siège n'est réservé
     */
    public void emptyRoom() {
        for (ArrayList<Sit> sitsRow : sits) { // For Each sit
            for (Sit sit : sitsRow) {
                sit.setTaken(false); // The sit is not reserved
            }
        }
    }

    /**
     * Affiche la représentation console de la salle
     */
    public void displayRoom() {
        for (ArrayList<Sit> sitsRow : sits) {
            for (Sit sit : sitsRow) {
                if (sit.isTaken()) System.out.print("[X]");
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

    public int getTotSits() {
        return totSits;
    }

    public void setTotSits(int totSits) {
        this.totSits = totSits;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getSitsByRow() {
        return sitsByRow;
    }

    public void setSitsByRow(int sitsByRow) {
        this.sitsByRow = sitsByRow;
    }

    public ArrayList<ArrayList<Sit>> getSits() {
        return sits;
    }

    public void setSits(ArrayList<ArrayList<Sit>> sits) {
        this.sits = sits;
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

        if (totSits != room.totSits) return false;
        if (sitsByRow != room.sitsByRow) return false;
        return sits.equals(room.sits);

    }

    @Override
    public int hashCode() {
        int result = totSits;
        result = 31 * result + sitsByRow;
        result = 31 * result + sits.hashCode();
        return result;
    }

    /**
     * toString
     */

    @Override
    public String toString() {
        return "Room{" +
                "totSits=" + totSits +
                ", sitsByRow=" + sitsByRow +
                ", sits=" + sits +
                ", cost=" + cost +
                '}';
    }

    public static void main(String[] args) {
        Room room = new Room(10, 20);
        room.displayRoom();
        System.out.println("================");
        room.giveSit(new Sit(1, 2));
        room.displayRoom();
    }
}
