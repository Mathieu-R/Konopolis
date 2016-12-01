package model;

public class SeatNotTakenException extends Exception {
    public SeatNotTakenException() {
        super();
    }

    public SeatNotTakenException(String message) {
        super(message);
    }
}
