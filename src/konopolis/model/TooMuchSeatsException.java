package konopolis.model;

public class TooMuchSeatsException extends Exception {
    public TooMuchSeatsException() {
        super();
    }

    public TooMuchSeatsException(String message) {
        super(message);
    }
}
