package model;

import java.util.Observable;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * @author Mathieu R. - Groupe 3
 */

/**
 * Model
 * Abstract Class
 * Implements Observer
 * It informs the modifications to the view
 * It makes the request to the DB
 */
public class KonopolisModel extends Observable {
    
	private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private final String DB_URL = "jdbc:mysql://localhost:3306/Konopolis";
    private final String USER = "root";
    private final String PWD = "H1perGl0bulle";

    private ArrayList<Movie> movies_al = new ArrayList<Movie>(); // ArrayList of Movies to be able to manage them
    private ArrayList<Show> shows_al = new ArrayList<Show>(); // ArrayList of Shows => contain every instance of shows for a specific movie
    private ArrayList<Customer> customers_al = new ArrayList<Customer>(); // ArrayList of Customers to be able to manage them
    private ArrayList<Room> rooms_al = new ArrayList<Room>(); // ArrayList of Rooms to be able to manage them
    
    Connection conn = null;
    Statement stmt = null;
    
    public KonopolisModel() {
    	registerDriver(); // Only done at the launching of the app;
    }

    /**
     * loading of the mysql driver
     * To do only once ! During the app launching.
     * @throws ClassNotFoundException
     */
    public void registerDriver() {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("Driver OK");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the connection to the DB (Konopolis)
     * @throws SQLException
     */
    public void createConnection() {
        System.out.println("Connecting to Konopolis DB...");
        try {
            this.conn = DriverManager.getConnection(DB_URL, USER, PWD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create a statement
     * @throws SQLException
     */
    public void createStatement() {
	   try {
           stmt = conn.createStatement();
       } catch (SQLException e) {
           e.printStackTrace();
       }
    }

    /**
     * Retrieve all the movies titles from the db
     * That's what we want at the launching of our app
     * Stored in a HashMap (movies)
     * @return HashMap that contains the id => key and the title => value of the movies
     * @throws SQLException
     */
    public HashMap<Integer, String> retrieveAllMoviesTitles() {
        HashMap<Integer, String> movies = new HashMap<Integer, String>(); // Local HashMap for movies

        String sql = "SELECT movie_id, title" +
                     "FROM tbmovies";

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results
                int movie_id  = rs.getInt("movie_id");
                String title = rs.getString("title");

                System.out.println(title);
                movies.put(movie_id, title);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movies;
    }

    public ArrayList<Movie> getMovies_al() {
		return movies_al;
	}

	public void setMovies_al(ArrayList<Movie> movies_al) {
		this.movies_al = movies_al;
	}

	public ArrayList<Show> getShows_al() {
		return shows_al;
	}

	public void setShows_al(ArrayList<Show> shows_al) {
		this.shows_al = shows_al;
	}

	public ArrayList<Customer> getCustomers_al() {
		return customers_al;
	}

	public void setCustomers_al(ArrayList<Customer> customers_al) {
		this.customers_al = customers_al;
	}

	public ArrayList<Room> getRooms_al() {
		return rooms_al;
	}

	public void setRooms_al(ArrayList<Room> rooms_al) {
		this.rooms_al = rooms_al;
	}

	/**
     * Retrieve movie's info from the db based on it id
     * @param movie_id, the id of the room
     * @throws SQLException
     */
    public void retrieveMovie(int movie_id) {
        
        String sql = "SELECT m.movie_id, mr.room_id, title, description, director," 
					+ "(select group_concat(c.cast) " 
					+ "from tbmoviescasts as mc "
					+ "left join tbcasts as c on mc.cast_id = c.cast_id) as casting,"
			        + "(select group_concat(g.genre) " 
					+ "from tbmoviesgenres as mg "
					+ "left join tbgenres as g on mg.genre_id = g.genre_id) as genres,"
					+ "(select group_concat(show_start) "
					+ "from tbmovies as m "
					+ "left join tbmoviesrooms as mr on m.movie_id = mr.movie_id) as shows,"
			        + "time, language, price "
			        + "from tbmovies as m "
			        + "left join tblanguages as l on m.language_id = l.language_id "
			        + "left join tbmoviesrooms as mr on m.movie_id = mr.room_id "
			        + "where m.movie_id = " + movie_id + " "
        			+ "limit 1"; // we only want the first result => temp. fix, otherwise, we get 2 same results
        
        			// What about 2 rooms for one same movie ?
        			// Consider Group By

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int id  = rs.getInt("movie_id");
                int room_id = rs.getInt("room_id");
                String title = rs.getString("title");
                String description = rs.getString("description");
                String director = rs.getString("director");
                // Split the string of casting ("act1, act2, act3") into Array ["act1", "act2", "act3"]
                ArrayList<String> casting = new ArrayList<String>(Arrays.asList(rs.getString("casting").split(","))); // Several actors
                ArrayList<String> genres = new ArrayList<String>(Arrays.asList(rs.getString("genres").split(","))); // Several genres
                ArrayList<String> shows = new ArrayList<String>(Arrays.asList(rs.getString("shows").split(",")));
                int time = rs.getInt("time");
                String language = rs.getString("language");
                double price = rs.getDouble("price");
  
                // For every date String
                for (String show : shows) {
                	LocalDateTime show_start = stringToLocalDateTime(show);
                	
                	// end of the show => start of the show + time in minutes
            		final LocalDateTime show_end = show_start.plus(time, ChronoUnit.MINUTES);
            		
            		// Add an instance of show => id = movie_id
            		shows_al.add(new Show(show_start, show_end, id, room_id));
                }

                
                // Push every Movie' instance in this ArrayList
                movies_al.add(new Movie(id, title, description, genres, shows_al, director, casting, time, language, price));
                setChanged();
                notifyObservers();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve the room based on a room_id
     * @param room_id, the id of the room
     * @throws SQLException
     */
    public void retrieveRooms(int room_id) {

        String sql = "SELECT movie_room_id, movie_id, room_id, rows, seats_by_row, show_start" 
        		   + "FROM tbmoviesrooms natural join tbrooms "
        		   + "WHERE room_id = " + room_id;

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int id = rs.getInt("room_id");
                int movie_id = rs.getInt("movie_id");
                int rows = rs.getInt("rows");
                int seats_by_row = rs.getInt("seats_by_row");
            	LocalDateTime show_start = stringToLocalDateTime(rs.getString("show_start"));	
                
                // Push every Movie' instance in this ArrayList
                // New Room => we initialize all the room (empty for now !)
            	for (Movie movie : movies_al) { // We search the right room (the one with the right id)
            		if (movie.getId() == movie_id) rooms_al.add(new Room(rows, seats_by_row, movie, id));  
            	}

                retrieveCustomers(id, movie_id, show_start);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieve all the customers of a [movie] that happens in a [room] at a time given [show_start]
     * @param room_id
     * @param movie_id
     * @param show_start
     * @throws SQLException
     */
    public void retrieveCustomers(int room_id, int movie_id, LocalDateTime show_start) {
    	// customer_id should be unique key ?
    	// Supprimer le booléen isTaken de la BDD ? Done.
    	// Redondance entre les tables seats et customers ?
    	
        String sql = "SELECT s.seat_id, s.customer_id, customer_type, sRow, sColumn, "
                   + "FROM tbseats as s "
                   + "natural join tbcustomersseats "
                   + "natural join tbcustomers "
                   + "natural join tbcustomerstype "
                   + "WHERE movie_room_id = "
                   + "(select movie_room_id "
                   + "from tbmovierooms as mr"
                   + "where mr.room_id = " + room_id + " and mr.movie_id = " + movie_id + "and mr.show_start = " + show_start;

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results
            	
            	int seat_id = rs.getInt("seat_id");
                int customer_id = rs.getInt("customer_id");
                String customer_type = rs.getString("customer_type");
                int row = rs.getInt("sRow");
                int column = rs.getInt("sColumn");
                
                //Look for the right Room => create an instance of customer and add it to the customers ArrayList
                for (Room room : rooms_al) {
                	if (room.getId() == room_id) { // Retrieve the right room
                        customers_al.add(new Customer(column, row, room, customer_type, seat_id)); // The instance of customer book the seat
                        setChanged();
                        notifyObservers();
                	}
                }
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Return the id of the customer type (to be able to add the customer to the db (relationnal db))
     * @param type, type of the customer (Junior, Student)
     * @return customer_type_id
     * @throws SQLException
     */
    public int retrieveCustomerTypeId(String type) {
    	String customerTypeId = "SELECT customer_type_id "
				+ "FROM tbcustomerstype "
				+ "WHERE customer_type = " + type;
    	
    	this.createConnection();
    	this.createStatement();
    	
    	ResultSet rs = null;
    	try {
    		rs = stmt.executeQuery(customerTypeId);
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while(rs.next()) {
    			return rs.getInt("customer_type_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
    	return 0;
    }
    
    /**
     * Retrieve the id movie_room_id of a customer (to be able to add a customer to the db (relationnal db))
     * @param movie_id, id of the movie
     * @param room_id, id of the room
     * @param show_start, start date of the movie
     * @return, int => the movie_room_id
     * @throws SQLException
     */
    public int retrieveMovieRoomId(int movie_id, int room_id, LocalDateTime show_start) {
    	String customerTypeId = "SELECT movie_room_id "
    							+ "FROM tbmoviesrooms "
								+ "WHERE movie_id = " + movie_id + " and room_id = " + room_id + " and show_start = " + show_start;
    	
    	this.createConnection();
    	this.createStatement();
    	
    	ResultSet rs = null;
    	try {
    		rs = stmt.executeQuery(customerTypeId);
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while(rs.next()) {
    			return rs.getInt("customer_type_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    	
		return 0;
    }
    
    public int retrieveLanguageId(String language) {
    	String languageId = "SELECT language_id "
    						+ "FROM tblanguages "
    						+ "WHERE language = " + language;
    	
    	this.createConnection();
    	this.createStatement();
    	
    	ResultSet rs = null;
    	
    	try {
    		rs = stmt.executeQuery(languageId);
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while(rs.next()) {
    			return rs.getInt("language_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return 0;
    }
    
    /**
     * retrieve the genre_id based on a genre, if the genre doesn't exist yet, we create it and send back it id; 
     * @param genre, a String that is one of the genre of the movie
     * @return int, the genre id
     * @throws SQLException
     */
    public int retrieveOrCreateGenreId(String genre) {
    	String genreId = "SELECT genre_id "
    						+ "FROM tbgenres "
    						+ "WHERE genre = " + genre;
    	
    	this.createConnection(); // Connect to the DB
    	this.createStatement(); // Create the statement
    	
    	ResultSet rs = null; // Set that will contain all the results
    	
    	try {
    		rs = stmt.executeQuery(genreId);
    	} catch(SQLException queryErr) { // If the query fails (ex: the genre does not exist)
    		queryErr.printStackTrace();
    		
    		PreparedStatement addGr = null;
    		String addGenre = "INSERT INTO tbgenres(genre) " // We create it
    							+ "VALUE (?)";
    		
    		//this.createConnection(); We do not need to create the connection again
    		try { // Beginning of insert query
    			addGr = conn.prepareStatement(addGenre); // Prepared Statement
    			
    			addGr.setString(1, genre);
    			rs = addGr.executeQuery(); // Execute a prepared statement and return the result set;
    			
    			while(rs.next()) {
    				return rs.getInt("genre_id"); // Return the genre_id created	
    			}
    			
    		} catch (SQLException insertErr) { // Error in the insert query
        		insertErr.printStackTrace();
        		if (conn != null) { // Try to rollback DB
        			try {
            			System.out.println("Trying to rollback db");
            			conn.rollback();
            		} catch (SQLException err) {
            			System.out.println("Rollback failed !");
            			err.printStackTrace();
            		}
        		}	
        	} finally {
        		if (addGr != null) { // Try to close preparedStatement
    				try {
    					addGr.close();
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
        		}
    		} // End of finally
    	}						
    	
    	try { // If the first query is OK, so the genre exist
    		while(rs.next()) {
    			return rs.getInt("genre_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close(); // Close the resultSet
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return 0;
    }
    
    /**
     * retrieve the cast_id based on a actor, if the actor doesn't exist yet, we create it and send back it id; 
     * @param actor, a String that is one of the actor of the movie
     * @return int, the cast id
     * @throws SQLException
     */
    public int retrieveOrCreateCastId(String actor) {
    	String castId = "SELECT cast_id "
    						+ "FROM tbcasts "
    						+ "WHERE cast = " + actor;
    	
    	this.createConnection(); // Connect to the DB
    	this.createStatement(); // Create the statement
    	
    	ResultSet rs = null; // Set that will contain all the results
    	
    	try {
    		rs = stmt.executeQuery(castId);
    	} catch(SQLException queryErr) { // If the query fails (ex: the actor does not exist)
    		queryErr.printStackTrace();
    		
    		PreparedStatement addAc = null;
    		String addActor = "INSERT INTO tbcasts(cast) " // We create it
    							+ "VALUE (?)";
    		
    		try { // Beginning of insert query
    			addAc = conn.prepareStatement(addActor); // Prepared Statement
    			
    			addAc.setString(1, actor);
    			rs = addAc.executeQuery(); // Execute a prepared statement and return the result set;
    			
    			while(rs.next()) {
    				return rs.getInt("cast_id"); // Return the cast_id created	
    			}
    			
    		} catch (SQLException insertErr) { // Error in the insert query
        		insertErr.printStackTrace();
        		if (conn != null) { // Try to rollback DB
        			try {
            			System.out.println("Trying to rollback db");
            			conn.rollback();
            		} catch (SQLException err) {
            			System.out.println("Rollback failed !");
            			err.printStackTrace();
            		}
        		}	
        	} finally {
        		if (addAc != null) { // Try to close preparedStatement
    				try {
    					addAc.close();
    				} catch (SQLException e) {
    					e.printStackTrace();
    				}
        		}
    		} // End of finally
    	}						
    	
    	try { // If the first query is OK, so the actor exist
    		while(rs.next()) {
    			return rs.getInt("genre_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close(); // Close the resultSet
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return 0;
    }
    
    /**
     * Add a movie in the db
     * @param movie_id, the id of the movie
     * @param room_id, the id of the room that broadcast the movie
     * @param title, the title of the movie
     * @param description, the description of the movie
     * @param director, the director of the movie
     * @param shows_start, /!\ IMPORTANT => ArrayList of type Date and not LocalDateTime (LocalDateTime cannot be inserted in DB), the starts of every show 
     * @param casting, the casting of the movie
     * @param time, the duration of the movie
     * @param language, the language of the movie 
     * @param price, the price of the movie
     * @param genres, an ArrayList of String that are the genres of the movie
     */
    public void addMovie(int movie_id, int room_id, String title, String description, String director, ArrayList<Date> shows_start, ArrayList<String> casting, int time, String language, double price, ArrayList<String> genres) {
    	PreparedStatement addMv = null;

    	
    	String addMovie = "INSERT INTO tbmovies(movie_id, title, description, director, time, language_id, price) "
    						+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
    	
		this.createConnection();
    	
    	try {
    		
    		addMv = conn.prepareStatement(addMovie);
    		
    		addMv.setInt(1, movie_id);
    		addMv.setString(2, title);
    		addMv.setString(3, description);
    		addMv.setString(4, director);
    		addMv.setInt(5, time);
    		addMv.setInt(6, retrieveLanguageId(language));
    		addMv.setDouble(7, price);
    		
    		addMv.executeUpdate();
    		
    		// If the movie is successfully added to the db, 
    		addGenres(movie_id, genres); // We add the genres
    		addShows(movie_id, room_id, shows_start); // We add the shows
    		addCasting(movie_id, casting); // We add the casting
    		
    		
    		ArrayList<Show> shows = new ArrayList<Show>();
    		
    		// For every start of a show 
    		// We create an instance of Show Class 
    		// That we put in the ArrayList shows
    		// This ArrayList will be added to the Movie instance
    		for (Date show_start: shows_start) {
    			// show_start, show_end, movie_id, room_id
    			shows.add(new Show(dateToLocalDateTime(show_start), dateToLocalDateTime(show_start).plus(time, ChronoUnit.MINUTES), movie_id, room_id)); 
    		}
    		
    		movies_al.add(new Movie(movie_id, title, description, genres, shows, director, casting, time, language, price));
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			System.out.println("Trying to rollback db");
	    			conn.rollback();
	    		} catch (SQLException err) {
	    			System.out.println("Rollback failed !");
	    			err.printStackTrace();
	    		}
			}
			
		} finally {
			if (addMv != null) {
				try {
					addMv.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    /**
     * Add one or more genre(s) associated a movie in the db
     * @param movie_id, the id of the movie
     * @param genres, ArrayList of genres
     */
    public void addGenres(int movie_id, ArrayList<String> genres) {
    	PreparedStatement addGr = null;
    	
    	this.createConnection();
    	
    	try {
    	
    		for (String genre : genres) { // For Each genre
        		addGr = conn.prepareStatement("INSERT INTO tbmoviesgenres(movie_id, genre_id)" // Add the genre associated to the movie in the db
						+ "VALUES (?, ?)");
        		
        		addGr.setInt(1, movie_id);
        		addGr.setInt(2, retrieveOrCreateGenreId(genre));

        		addGr.executeUpdate();	
        	}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			System.out.println("Trying to rollback db");
	    			conn.rollback();
	    		} catch (SQLException err) {
	    			System.out.println("Rollback failed !");
	    			err.printStackTrace();
	    		}
			}
			
		} finally {
			if (addGr != null) {
				try {
					addGr.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    /**
     * Add one or more shows for a movie that is broadcasted in a room
     * @param movie_id, the id of the movie
     * @param room_id, the id of the room
     * @param shows_start, an ArrayList of Date that are the starts of every show
     */
    public void addShows(int movie_id, int room_id, ArrayList<Date> shows_start) {
    	PreparedStatement addSh = null;
    	
    	this.createConnection();
    	
    	try {
    	
    		for (Date show_start : shows_start) { // For Each genre
        		addSh = conn.prepareStatement("INSERT INTO tbmoviesrooms(movie_id, room_id, show_start)" // Add the genre associated to the movie in the db
						+ "VALUES (?, ?, ?)");
        		
        		addSh.setInt(1, movie_id);
        		addSh.setInt(2, room_id);
        		addSh.setDate(3, show_start); 
        		addSh.executeUpdate();	
        	}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			System.out.println("Trying to rollback db");
	    			conn.rollback();
	    		} catch (SQLException err) {
	    			System.out.println("Rollback failed !");
	    			err.printStackTrace();
	    		}
			}
			
		} finally {
			if (addSh != null) {
				try {
					addSh.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    /**
     * add a casting for a movie into the db
     * @param movie_id, the id of the movie
     * @param casting, an ArrayList of the movie casting
     */
    public void addCasting(int movie_id, ArrayList<String> casting) {
    	PreparedStatement addCs = null;
    	
    	this.createConnection();
    	
    	try {
    	
    		for (String actor : casting) { // For Each genre
        		addCs = conn.prepareStatement("INSERT INTO tbmoviescasts(movie_id, cast_id)" // Add the genre associated to the movie in the db
						+ "VALUES (?, ?)");
        		
        		addCs.setInt(1, movie_id);
        		addCs.setInt(2, retrieveOrCreateCastId(actor));

        		addCs.executeUpdate();	
        	}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			System.out.println("Trying to rollback db");
	    			conn.rollback();
	    		} catch (SQLException err) {
	    			System.out.println("Rollback failed !");
	    			err.printStackTrace();
	    		}
			}
			
		} finally {
			if (addCs != null) {
				try {
					addCs.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
    }
    
    /**
     * create a new Customer instance, add it to the ArrayList of customers and insert it into the db
     * @param x, position in x of the customer seat
     * @param y, position in y of the customer seat
     * @param customer_id, id of the customer
     * @param room_id, id of the room where the customer will be
     * @param type, type of customer (Senior, Junior,...)
     * @param seat_id, id of the seat where the customer will be seated on
     * @param movie_id, id of the movie that the customer will watch
     * @param show_start, start date of the movie
     * @throws SQLException
     */
    public void addCustomer(int x, int y, int customer_id, int room_id, String type, int seat_id, int movie_id, LocalDateTime show_start) {
    	
    	PreparedStatement addCt = null;
    	PreparedStatement addSt = null;
    	
    	String addCustomer = "INSERT INTO tbcustomers(seat_id, customer_type_id) "
    						+ "VALUES (?, ?)";
    	
    	String addSeat = "INSERT INTO tbseats(customer_id, sRow, sColumn, movie_room_id) "
    				+ "VALUES (?, ?, ?, ?)";
    	
    	this.createConnection(); // Create connection to DB
    	
    	try {
        	conn.setAutoCommit(false); // The way to do sql transactions => avoid to commit transaction after every request
        	
        	addCt = conn.prepareStatement(addCustomer); // Prepared Request
        	addSt = conn.prepareStatement(addSeat);
        	
        	addCt.setInt(1, seat_id);
        	addCt.setInt(2, retrieveCustomerTypeId(type));
        	addCt.executeUpdate();
        	
        	addSt.setInt(1, customer_id);
        	addSt.setInt(2, y); // Row
        	addSt.setInt(3, x); // Column
        	addSt.setInt(4, retrieveMovieRoomId(movie_id, room_id, show_start));
        	addSt.executeUpdate();
        	
        	conn.commit(); // Commit the transaction
        	
    	} catch (SQLException e) {
    		e.printStackTrace();
    		if (conn != null) {
    			try {
        			System.out.println("Trying to rollback db");
        			conn.rollback();
        		} catch (SQLException err) {
        			System.out.println("Rollback failed !");
        			err.printStackTrace();
        		}
    		}
    		
    	} finally {
    		if (addCt != null) {
				try {
					addCt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    		if (addSt != null) {
				try {
					addSt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		}
    		
    		try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	
    	for (Room room : rooms_al) {
    		if (room.getId() == room_id) {
    			new Customer(x, y, room, type, customer_id);
    			setChanged();
    			notifyObservers();
    		}
    	}
    }
    
    /**
     * Convert a String into a LocalDateTime
     * @param show, the date and time in a String type
     * @return LocalDateTime, the date and time in a LocalDateTime type
     */
    public LocalDateTime stringToLocalDateTime(String show) {
    	// Formatters for date + time
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd kk:mm:ss");
		// start of the show
    	LocalDate show_start_date = LocalDate.parse(show, format); // Date (yyyy-MM-dd)
    	LocalTime show_start_time = LocalTime.parse(show, format); // Time (hh:mm:ss)
    	LocalDateTime show_start = LocalDateTime.of(show_start_date, show_start_time); // Parse in LocalDateTime
    	return show_start;
    }
    
    /**
     * Convert a Date (type) into a LocalDateTime
     * @param show, the date in a Date type 
     * @return, the date in LocalDateTime type
     */
    public LocalDateTime dateToLocalDateTime(Date show) {
    	Instant instant = Instant.ofEpochMilli(show.getTime());
    	return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }
    

    public String getDB_DRIVER() {
        return DB_DRIVER;
    }

    public String getDB_URL() {
        return DB_URL;
    }

    public String getUSER() {
        return USER;
    }

    public String getPWD() {
        return PWD;
    }

}