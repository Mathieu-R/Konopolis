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
}
