CREATE DOMAIN Drows INT check(@col between (1,20)) NOT NULL;
CREATE DOMAIN DColumn INT check(@col between (1, 35)) NOT NULL;
CREATE DOMAIN DBool TINYINT check(@col in (0, 1)) NOT NULL;

CREATE table tbMovies {
	movie_id int default AUTOINCREMENT,
	title char(30),
	description MEDIUMTEXT,
	director char(30),
	time int,
	language_id int,
	price double
    CONSTRAINT pkMovieId PRIMARY KEY (movie_id)
};

-- Associations avec la table Movies --

CREATE table tbGenres {
	genre_id int default AUTOINCREMENT,
	genre char(30),
	CONSTRAINT pkGenreId PRIMARY KEY (genre_id)	
};

CREATE table tbMoviesGenres {
	movie_genre_id int default AUTOINCREMENT,
	movie_id int,
	genre_id int,
	CONSTRAINT pkMovieGenreId PRIMARY KEY (movie_genre_id),	
	CONSTRAINT fkMovieId FOREIGN KEY (movie_id) REFERENCES tbMovies (movie_id) on update cascade,
	CONSTRAINT fkGenreId FOREIGN KEY (genre_id) REFERENCES tbMovies (genre_id) on update cascade
};

CREATE table tbCasts {
	cast_id int default AUTOINCREMENT,
	cast char(30),
	CONSTRAINT pkCastId PRIMARY KEY (cast_id)	
};

CREATE table tbMoviesCasts {
	movie_cast_id int default AUTOINCREMENT,
	movie_id int,
	cast_id int,
	CONSTRAINT pkMovieCastId PRIMARY KEY (movie_cast_id),	
	CONSTRAINT fkMovieId FOREIGN KEY (movie_id) REFERENCES tbMovies (movie_id) on update cascade,
	CONSTRAINT fkCastId FOREIGN KEY (cast_id) REFERENCES tbCasts (cast_id) on update cascade 
};


CREATE table tbRooms {
	room_id int default AUTOINCREMENT,
	movie_id int NOT NULL,
	rows Drows, -- Custom domain
	seats_by_row DColumn,
	incomes: double,
	CONSTRAINT pkRoomId PRIMARY KEY (room_id),
	CONSTRAINT fkMovieId FOREIGN KEY (movie_id) REFERENCES tbMovies (movie_id) )on update cascade	
} 

CREATE table tbSeats {
	seat_id int default AUTOINCREMENT,
	room_id int,
	client_id,
	row Drows,
	column DColumn,
	isTaken Dbool,
	CONSTRAINT pkSeatId PRIMARY KEY (seat_id),
	CONSTRAINT fkRoomId FOREIGN KEY (room_id) REFERENCES tbRooms (room_id) on update cascade,
	CONSTRAINT fkClientId FOREIGN KEY (client_id) REFERENCES tbClients (client_id) on update cascade
}

CREATE table tbClients {
	client_id int default AUTOINCREMENT,
	seat_id int,
	client_type_id int,
	reduction double,
	CONSTRAINT pkClientId PRIMARY KEY (client_id),
	CONSTRAINT fkSeatId FOREIGN KEY (seat_id) REFERENCES tbSeats(seat_id) on update cascade,
	CONSTRAINT fkClientTypeId FOREIGN KEY (client_type_id) REFERENCES tbClientsType(client_type_id) on update cascade
}

CREATE table tbClientsType {
	client_type_id int default AUTOINCREMENT,
	client_type char(30),
	CONSTRAINT pkClientTypeId PRIMARY KEY (client_type_id)
}