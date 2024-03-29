package src.konopolis.model;

import java.time.LocalDateTime;

/**
 * @author Mathieu R. - Groupe 3
 */
public class Show {

  private LocalDateTime show_start;

  private LocalDateTime show_end;
  private int movie_id;
  private int room_id;

  public Show(LocalDateTime show_start, LocalDateTime show_end, int movie_id, int room_id) {
    this.show_start = show_start;
    this.show_end = show_end;
    this.movie_id = movie_id;
    this.room_id = room_id;
  }

  public LocalDateTime getShow_start() {
    return show_start;
  }

  public void setShow_start(LocalDateTime show_start) {
    this.show_start = show_start;
  }

  public LocalDateTime getShow_end() {
    return show_end;
  }

  public void setShow_end(LocalDateTime show_end) {
    this.show_end = show_end;
  }

  public int getMovie_id() {
    return movie_id;
  }

  public void setMovie_id(int movie_id) {
    this.movie_id = movie_id;
  }

  public int getRoom_id() {
    return room_id;
  }

  public void setRoom_id(int room_id) {
    this.room_id = room_id;
  }
}
