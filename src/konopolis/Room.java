/**
 * 
 */
package konopolis;

import java.util.ArrayList;

/**
 * @author natha
 *
 */
public class Room {
	 private static int id;
	    private int totSeats;
	    private int rows;
	    private int sitsByRow;
	    private ArrayList<ArrayList<Seat>> sits = new ArrayList<ArrayList<Seat>>();
	    private double cost;
	    private static double income=0.0;
	    private Movie movie;

	    /**
	     * Constructors
	     */

	    public Room(int rows, int sitsByRow,Movie movie) {
	        if (rows > 20 || sitsByRow > 35) {
	            System.out.println("Trop de rangées et/ou de sièges par rangée");
	            return;
	        }

	        this.rows = rows;
	        this.sitsByRow = sitsByRow;
	        this.movie=movie;

	        this.totSeats = rows * sitsByRow;

	        // Init the sits (ArrayList) with all the coordinates of the sits
	        for (int i = 0; i < rows; i++) {
	            ArrayList<Seat> tempList = new ArrayList<Seat>();
	            for (int j = 0; j < sitsByRow; j++) {
	                tempList.add(j, new Seat(i + 1, j+ 1));
	                //sits.(i).set(j, new Seat(i + 1, j + 1)); // i + 1, j + 1 => a sit does not have a place 0
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
	    public void giveSeat(Seat sit) {
	        for (ArrayList<Seat> sitsRow : sits) { // For every row
	            if (sitsRow.contains(sit)) { // If the row ArrayList contains the sit
	                final int index = sitsRow.indexOf(sit); // Index of the sit
	                sitsRow.get(index).setTaken(true); // The sit is reserved
	            }
	        }
	    }

	    public int getSitsByRow() {
			return sitsByRow;
		}

		public void setSitsByRow(int sitsByRow) {
			this.sitsByRow = sitsByRow;
		}

		public ArrayList<ArrayList<Seat>> getSits() {
			return sits;
		}

		public void setSits(ArrayList<ArrayList<Seat>> sits) {
			this.sits = sits;
		}

		public Movie getMovie() {
			return movie;
		}

		public void setMovie(Movie movie) {
			this.movie = movie;
		}

		/**
	     * Annule la réservation d'un siège
	     * @param sit, un siège donné
	     */
	    public void cancelSeat(Seat sit) {
	        for (ArrayList<Seat> sitsRow : sits) { // For every row
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
	        for (ArrayList<Seat> sitsRow : sits) { // For Each sit
	            for (Seat sit : sitsRow) {
	                sit.setTaken(false); // The sit is not reserved
	            }
	        }
	    }

	    /**
	     * Affiche la représentation console de la salle
	     */
	    public void displayRoom() {
	        for (ArrayList<Seat> sitsRow : sits) {
	            for (Seat sit : sitsRow) {
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
	        return sitsByRow;
	    }

	    public void setSeatsByRow(int sitsByRow) {
	        this.sitsByRow = sitsByRow;
	    }

	    public ArrayList<ArrayList<Seat>> getAllSeats() {
	        return sits;
	    }
	    public Seat getSeat(int row,int column){
	    	return getAllSeats().get(row).get(column);
	    }

	    public void setSeats(ArrayList<ArrayList<Seat>> sits) {
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

	        if (totSeats != room.totSeats) return false;
	        if (sitsByRow != room.sitsByRow) return false;
	        return sits.equals(room.sits);

	    }

	    @Override
	    public int hashCode() {
	        int result = totSeats;
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
	                "totSeats=" + totSeats +
	                ", sitsByRow=" + sitsByRow +
	                ", sits=" + sits +
	                ", cost=" + cost +
	                '}';
	    }
}
