package konopolis;

/**
 * @author Mathieu R. - Groupe 3
 */

public class Seat {

    /**
     * row, row of the sit
     * column, column of the sit in the row
     * isTaken, boolean, to know if the sit is taken
     */
    private int row;
    private int column;
    private boolean isTaken;

    /**
     * constructor
     * @param row, row of the sit
     * @param column, column of the sit in the row
     * By default, the sit is available
     */
    public Seat(int row, int column) {
        this.row = row;
        this.column = column;
        this.isTaken = false;
    }

    /**
     * Getters and Setters
     */

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }

    /**
     * HashCode and Equals
     */

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seat sit = (Seat) o;

        if (row != sit.row) return false;
        return column == sit.column;

    }

    @Override
    public int hashCode() {
        int result = row;
        result = 31 * result + column;
        return result;
    }
}
