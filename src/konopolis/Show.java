package src.konopolis.model;

import java.time.LocalDate;

/**
 * @author Mathieu R. - Groupe 3
 */
public class Show {

    private LocalDate show_start;
    private LocalDate show_end;
    private Movie movie;
    private Room room;

    public Show(LocalDate show_start, LocalDate show_end, Movie movie, Room room) {
        this.show_start = show_start;
        this.show_end = show_end;
        this.movie = movie;
        this.room = room;
    }
}
