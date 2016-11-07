package konopolis;

public class Sit {

    /**
     * x, coordinate in x of the sit
     * y, coordinate in y of the sit
     * isTaken, boolean, to know if the sit is taken
     */
    private int x;
    private int y;
    private boolean isTaken;

    /**
     * constructor
     * @param x, coordinate in x of the sit
     * @param y, coordinate in y of the sit
     */
    public Sit(int x, int y) {
        this.x = x;
        this.y = y;
        this.isTaken = false;
    }

    /**
     * Getters and Setters
     */

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

        Sit sit = (Sit) o;

        if (x != sit.x) return false;
        return y == sit.y;

    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    /**
     * toString
     */

    @Override
    public String toString() {
        return "Sit{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
