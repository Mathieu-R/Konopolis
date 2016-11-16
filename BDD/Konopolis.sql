CREATE table tbMovies (
	movie_id int auto_increment PRIMARY KEY,
	title CHAR(30),
	description MEDIUMTEXT,
	director CHAR(30),
	time int,
	language_id int,
	price double
);

-- Associations avec la table Movies --

CREATE table tbGenres (
	genre_id int auto_increment PRIMARY KEY,
	genre CHAR(30)
);

CREATE table tbMoviesGenres (
	movie_genre_id int auto_increment PRIMARY KEY,
	movie_id int,
	genre_id int,
	FOREIGN KEY (movie_id) REFERENCES tbMovies (movie_id) on update cascade,
	FOREIGN KEY (genre_id) REFERENCES tbMovies (genre_id) on update cascade
);

CREATE table tbCasts (
	cast_id int auto_increment PRIMARY KEY,
	cast CHAR(30)	
);

CREATE table tbMoviesCasts (
	movie_cast_id int auto_increment PRIMARY KEY,
	movie_id int,
	cast_id int,	
	FOREIGN KEY (movie_id) REFERENCES tbMovies (movie_id) on update cascade,
	FOREIGN KEY (cast_id) REFERENCES tbCasts (cast_id) on update cascade 
);

-- fin --

CREATE table tbRooms (
	room_id int auto_increment PRIMARY KEY,
	movie_id int NOT NULL,
	rows int, 
	seats_by_row int,
	incomes: double,
	CONSTRAINT chkRow CHECK (rows between 1 and 20),
	CONSTRAINT chkColumn CHECK (seats_by_row between 1 and 35),
	FOREIGN KEY (movie_id) REFERENCES tbMovies (movie_id) ) on update cascade	
);

CREATE table tbSeats (
	seat_id int auto_increment PRIMARY KEY,
	room_id int,
	client_id,
	row Drows,
	column DColumn,
	isTaken tinyint,
	CONSTRAINT chkRow CHECK (row between 1 and 20),
	CONSTRAINT chkColumn CHECK (column between 1 and 35),
	CONSTRAINT chkIsTaken CHECK (isTaken in (0, 1)),
	FOREIGN KEY (room_id) REFERENCES tbRooms (room_id) on update cascade,
	FOREIGN KEY (client_id) REFERENCES tbClients (client_id) on update cascade
);

CREATE table tbClients (
	client_id int auto_increment PRIMARY KEY,
	seat_id int,
	client_type_id int,
	reduction double,
	FOREIGN KEY (seat_id) REFERENCES tbSeats(seat_id) on update cascade,
	FOREIGN KEY (client_type_id) REFERENCES tbClientsType(client_type_id) on update cascade
);

CREATE table tbClientsType (
	client_type_id int auto_increment PRIMARY KEY,
	client_type CHAR(30)
);