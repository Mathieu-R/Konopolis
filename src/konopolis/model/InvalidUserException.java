package src.konopolis.model;

/**
 * @author Mathieu R. - Groupe 3
 */
public class InvalidUserException extends Exception {
  public InvalidUserException() {
    super();
  }

  public InvalidUserException(String message) {
    super(message);
  }
}
