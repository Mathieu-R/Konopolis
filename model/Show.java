package model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Mathieu R. - Groupe 3
 */
public class Show {

    private LocalDateTime show_start;
    private Movie movie;
    private Room room;

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public Show(LocalDateTime show_start, Movie movie, Room room) {
        this.setShow_start(show_start);
        this.movie = movie;
        this.room = room;
    }

	public Show(LocalDateTime show_start2, LocalDateTime show_end, int id, int room_id) {
		// TODO Auto-generated constructor stub
	}

	public boolean contains(ArrayList<Show> listShows,Movie movie) {
		// TODO Auto-generated method stub
		boolean isContaining = false;
		for(int i=0;i<listShows.size();i++){
			if(listShows.get(i).movie== movie){
				isContaining= true;
			}
		}
		return isContaining;
		
	}

	public LocalDateTime getShow_start() {
		return show_start;
	}

	public void setShow_start(LocalDateTime show_start) {
		this.show_start = show_start;
	}
}
