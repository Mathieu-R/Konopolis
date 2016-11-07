package konopolis;

import java.util.ArrayList;

public class Room {

    private static int id;
    private int totSits;
    private int rows;
    private int sitsByRow;
    private ArrayList<Sit> sits = new ArrayList<Sit>();
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
        for (int i = 1; i <= totSits; i++) {
            sits[i] = new Sit(i * 10 + 3, i * 10 + 3);
            //sits[i].setX(i * 10 + 3); // x position of the sit, 10 is for the size of the sit, 3 is for the margin between 2 sits
            //sits[i].setY(i * 10 + 3); // x position of the sit, 10 is for the size of the sit, 3 is for the margin between 2 sits
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
        if (sits.contains(sit)) { // If the sits ArrayList contains the sit
            final int index = sits.indexOf(sit); // Index of the sit
            sits[index].isTaken = true; // The sit is reserved
        }
    }

    /**
     * Annule la réservation d'un siège
     * @param sit, un siège donné
     */
    public void cancelSit(Sit sit) {
        if (sits.contains(sit)) { // If the sits ArrayList contains the sit
            final int index = sits.indexOf(sit); // Index of the sit
            sits[index].isTaken = false; // The sit is NOT reserved anymore
        }
    }

    /**
     * Vide la salle, c'est à dire qu'aucun siège n'est réservé
     */
    public void emptyRoom() {
        for (sit : sits) { // For Each sit
            sit.isTaken = false; // The sit is not reserved
        }
    }

    /**
     * Affiche la représentation console de la salle
     */
    public void displayRoom() {


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

    public int getSitsByRow() {
        return sitsByRow;
    }

    public void setSitsByRow(int sitsByRow) {
        this.sitsByRow = sitsByRow;
    }

    public ArrayList<Sit> getSits() {
        return sits;
    }

    public void setSits(ArrayList<Sit> sits) {
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
}
