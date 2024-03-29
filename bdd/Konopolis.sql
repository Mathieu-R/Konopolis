CREATE table tbAdmins (
	username CHAR(30) PRIMARY KEY NOT NULL,
	hash VARCHAR(255) NOT NULL
);

CREATE table tbLanguages (
  language_id int auto_increment PRIMARY KEY NOT NULL,
  language CHAR(30) NOT NULL
);

CREATE table tbMovies (
	movie_id int auto_increment PRIMARY KEY NOT NULL,
	title CHAR(50) NOT NULL,
	description MEDIUMTEXT NOT NULL,
	director CHAR(30) NOT NULL,
	time int NOT NULL,
	language_id int NOT NULL,
	price double NOT NULL,
	CONSTRAINT mvUnique UNIQUE (title),
  FOREIGN KEY (language_id) REFERENCES tbLanguages(language_id) ON UPDATE CASCADE
);

CREATE table tbGenres (
	genre_id int auto_increment PRIMARY KEY NOT NULL,
	genre CHAR(30) NOT NULL,
	CONSTRAINT grUnique UNIQUE (genre)
);

CREATE table tbMoviesGenres (
	movie_genre_id int auto_increment PRIMARY KEY NOT NULL,
	movie_id int NOT NULL,
	genre_id int NOT NULL,
	FOREIGN KEY (movie_id) REFERENCES tbMovies(movie_id) ON UPDATE CASCADE,
	FOREIGN KEY (genre_id) REFERENCES tbGenres(genre_id) ON UPDATE CASCADE
);

CREATE table tbCasts (
	cast_id int auto_increment PRIMARY KEY NOT NULL,
	cast CHAR(30)	NOT NULL,
	CONSTRAINT ctUnique UNIQUE (cast)
);

CREATE table tbMoviesCasts (
	movie_cast_id int auto_increment PRIMARY KEY NOT NULL,
	movie_id int NOT NULL,
	cast_id int NOT NULL,
	FOREIGN KEY (movie_id) REFERENCES tbMovies(movie_id) ON UPDATE CASCADE,
	FOREIGN KEY (cast_id) REFERENCES tbCasts(cast_id) ON UPDATE CASCADE
);

CREATE table tbMoviesRooms(
	movie_room_id int auto_increment NOT NULL PRIMARY KEY NOT NULL,
	movie_id int NOT NULL NOT NULL,
	room_id int NOT NULL NOT NULL,
	show_start TIMESTAMP NOT NULL,
	FOREIGN KEY (movie_id) REFERENCES tbMovies(movie_id) ON UPDATE CASCADE,
	FOREIGN KEY (room_id) REFERENCES tbRooms(room_id) ON UPDATE CASCADE
);

CREATE table tbRooms (
	room_id int auto_increment PRIMARY KEY NOT NULL,
	rows int NOT NULL,
	seats_by_row int NOT NULL,
	CONSTRAINT chkRow CHECK (rows between 1 and 20),
	CONSTRAINT chkColumn CHECK (seats_by_row between 1 and 35)
);

CREATE table tbCustomersType (
  customer_type_id int auto_increment PRIMARY KEY NOT NULL,
  customer_type CHAR(30) NOT NULL,
  reduction double NOT NULL,
  CONSTRAINT chkReduction CHECK (reduction between 0.0 and 1.0)
);

CREATE table tbCustomers (
  customer_id int auto_increment PRIMARY KEY NOT NULL,
  sRow int NOT NULL,
  sColumn int NOT NULL,
  movie_room_id int NOT NULL,
	customer_type_id int NOT NULL,
  CONSTRAINT chkRow CHECK (sRow between 1 and 20),
  CONSTRAINT chkColumn CHECK (sColumn between 1 and 35),
  FOREIGN KEY (movie_room_id) REFERENCES tbMoviesRooms(movie_room_id) ON UPDATE CASCADE,
	FOREIGN KEY (customer_type_id) REFERENCES tbCustomersType(customer_type_id) ON UPDATE CASCADE
);

