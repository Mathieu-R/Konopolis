CREATE table tbLanguages (
  language_id int auto_increment PRIMARY KEY,
  language char(30)
)

CREATE table tbMovies (
	movie_id int auto_increment PRIMARY KEY,
	title CHAR(30),
	description MEDIUMTEXT,
	director CHAR(30),
	time int,
	language_id int,
	price double,
  FOREIGN KEY (language_id) REFERENCES tbLanguages(language_id) ON UPDATE CASCADE
)

-- Associations avec la table Movies --

CREATE table tbGenres (
	genre_id int auto_increment PRIMARY KEY,
	genre CHAR(30)
)

CREATE table tbMoviesGenres (
	movie_genre_id int auto_increment PRIMARY KEY,
	movie_id int,
	genre_id int,
	FOREIGN KEY (movie_id) REFERENCES tbMovies(movie_id) ON UPDATE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES tbGenres(genre_id) ON UPDATE CASCADE
)

CREATE table tbCasts (
	cast_id int auto_increment PRIMARY KEY,
	cast CHAR(30)	
)

CREATE table tbMoviesCasts (
	movie_cast_id int auto_increment PRIMARY KEY,
	movie_id int,
	cast_id int,	
	FOREIGN KEY (movie_id) REFERENCES tbMovies(movie_id) ON UPDATE CASCADE,
	FOREIGN KEY (cast_id) REFERENCES tbCasts(cast_id) ON UPDATE CASCADE
)

-- fin --

CREATE table tbRooms (
	room_id int auto_increment PRIMARY KEY,
	movie_id int NOT NULL,
	show_start DATETIME,
	show_end DATETIME,
	rows int, 
	seats_by_row int,
	incomes double,
	CONSTRAINT chkRow CHECK (rows between 1 and 20),
	CONSTRAINT chkColumn CHECK (seats_by_row between 1 and 35),
	FOREIGN KEY (movie_id) REFERENCES tbMovies(movie_id) ON UPDATE CASCADE
)

CREATE table tbCustomersType (
  customer_type_id int auto_increment PRIMARY KEY,
  customer_type CHAR(30)
)

CREATE table tbSeats (
  seat_id int auto_increment PRIMARY KEY,
  room_id int,
  customer_id int,
  sRow int,
  sColumn int,
  isTaken tinyint,
  CONSTRAINT chkRow CHECK (sRow between 1 and 20),
  CONSTRAINT chkColumn CHECK (sColumn between 1 and 35),
  CONSTRAINT chkIsTaken CHECK (isTaken in (0, 1)),
  FOREIGN KEY (room_id) REFERENCES tbRooms(room_id) ON UPDATE CASCADE
)

CREATE table tbCustomers (
	customer_id int auto_increment PRIMARY KEY,
	seat_id int,
	customer_type_id int,
	reduction double,
	FOREIGN KEY (customer_type_id) REFERENCES tbCustomersType(customer_type_id) ON UPDATE CASCADE
)

CREATE table tbCustomersSeats (
  customer_id int,
  seat_id int,
  PRIMARY KEY (customer_id, seat_id), # Composite Key
  FOREIGN KEY (customer_id) REFERENCES tbCustomers(customer_id) ON UPDATE CASCADE,
  FOREIGN KEY (seat_id) REFERENCES tbSeats(seat_id) ON UPDATE CASCADE
)