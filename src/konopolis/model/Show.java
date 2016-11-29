package src.konopolis.model;

import java.time.LocalDate;

/**
 * @author Mathieu R. - Groupe 3
 */
public class Show {

    private LocalDate show_start;
    private LocalDate show_end;
    private int movie_id;
    private int room_id;

    public Show(LocalDate show_start, LocalDate show_end, int movie_id, int room_id) {
        this.show_start = show_start;
        this.show_end = show_end;
        this.movie_id = movie_id;
        this.room_id = room_id;
    }
}
