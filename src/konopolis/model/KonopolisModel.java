package src.konopolis.model;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author Mathieu R. - Groupe 3
 */

/**
 * Model
 * Implements Observer
 * It informs the modifications to the view
 * It makes the request to the DB
 */
public class KonopolisModel extends Observable {
    
	private final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private String DB_URL = "jdbc:mysql://localhost:3306/konopolis?autoReconnect=true&useSSL=false"; // auto reconnection and no ssl connection (not prod ready)
    private final String USER = "root";
    private final String PWD = "root";

	private ArrayList<Movie> movies_al = new ArrayList<Movie>(); // ArrayList of Movies to be able to manage them
    private ArrayList<Show> shows_al = new ArrayList<Show>(); // ArrayList of Shows => contain every instance of shows for a specific movie
    private ArrayList<Customer> customers_al = new ArrayList<Customer>(); // ArrayList of Customers to be able to manage them
    private ArrayList<Room> rooms_al = new ArrayList<Room>(); // ArrayList of Rooms to be able to manage them
    
    Connection conn = null;
    Statement stmt = null;
    
    public KonopolisModel() {
    	registerDriver(); // Only done at the launching of the app;
    	//notifyObservers();
    }

    /**
     * loading of the mysql driver
     * To do only once ! During the app launching.
     * @throws ClassNotFoundException
     */
    public void registerDriver() {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create the connection to the DB (Konopolis)
     * @throws SQLException
     */
    public void createConnection() {
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

    public void createUser(String username, String password) {
        PreparedStatement createUser = null;
        String makeUser = "INSERT INTO tbadmins(username, hash) VALUES (?,?)";

        this.createConnection();
        try {
            String hash = BCrypt.hashpw(password, BCrypt.gensalt(16)); // hash password

            createUser = conn.prepareStatement(makeUser);
            createUser.setString(1, username);
            createUser.setString(2, hash);
            createUser.executeUpdate();
        } catch(SQLException e) {
            e.printStackTrace();
        } finally {
            pstatementCloser(createUser);
            connectionCloser(conn);
        }
    }

    public synchronized boolean authUser(String username, String password) throws InvalidUserException {
        PreparedStatement getUser = null;
        String auth = "SELECT username, hash FROM tbadmins where username = ?";
        
        this.createConnection();
        ResultSet rs = null;
        try {
            getUser = conn.prepareStatement(auth);
            getUser.setString(1, username);
            rs = getUser.executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            if (!rs.isBeforeFirst()) { // if the user does not exist
                throw new InvalidUserException("Cette utilisateur / mot de passe n'existe pas !");
            }

            while(rs.next()) {
                rs.getString("username");
                final String hash = rs.getString("hash");

                if (BCrypt.checkpw(password, hash)) { // Verify if the password match the hash in db
                    return true;
                }
                throw new InvalidUserException("Cette utilisateur / mot de passe n'existe pas !");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            pstatementCloser(getUser);
            connectionCloser(conn);
        }
        return false;
    }

    /**
     * Retrieve all the movies titles from the db
     * That's what we want at the launching of our app
     * Stored in a HashMap (movies)
     * @return HashMap that contains the id => key and the title => value of the movies
     * @throws SQLException
     */
    public synchronized LinkedHashMap<Integer, String> retrieveAllMoviesTitles() {
        LinkedHashMap<Integer, String> movies = new LinkedHashMap<Integer, String>(); // Local HashMap for movies

        String sql = "SELECT movie_id, title " +
                     "FROM tbmovies";
        
        this.createConnection();
        this.createStatement();

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

    /**
     * Retrieve movie's info from the db based on it id
     * @param movie_id, the id of the room
     * @throws SQLException
     */
    
    public synchronized Movie retrieveMovie(int movie_id) {
        Movie movie = null;
        PreparedStatement getMv = null;
        
	    String sql = "SELECT m.movie_id, mr.room_id, title, description, director," 
					+ "(select group_concat(cast) " 
					+ "from tbmoviescasts "
					+ "natural join tbcasts "
                    + "where movie_id = ? ) as casting, "
			        + "(select group_concat(genre) " 
					+ "from tbmoviesgenres "
					+ "natural join tbgenres "
                    + "where movie_id = ? ) as genres, "
					+ "(select group_concat(show_start) "
					+ "from tbmovies "
					+ "natural join tbmoviesrooms "
                    + "where movie_id = ? ) as shows, "
			        + "time, language, price "
			        + "from tbmovies as m "
			        + "natural join tblanguages "
			        + "natural join tbmoviesrooms as mr "
			        + "where m.movie_id = ? "
	    			+ "limit 1"; // we only want the first result => temp. fix, otherwise, we get 2 same results
        
        this.createConnection();
        ResultSet rs = null; // Execute the sql query and put the results in the results set

        try {
            getMv = conn.prepareStatement(sql);
            getMv.setInt(1, movie_id);
            getMv.setInt(2, movie_id);
            getMv.setInt(3, movie_id);
            getMv.setInt(4, movie_id);
            rs = getMv.executeQuery();
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

                shows_al.clear(); // clear old shows before adding new one

                // For every date String
                for (String show : shows) {
                	LocalDateTime show_start = stringToLocalDateTime(show);

                	// end of the show => start of the show + time in minutes
            		final LocalDateTime show_end = show_start.plus(time, ChronoUnit.MINUTES);

            		// Add an instance of show => id = movie_id
            		shows_al.add(new Show(show_start, show_end, id, room_id));
                }

                movie = new Movie(id, title, description, genres, shows_al, director, casting, time, language, price);
                // Push every Movie' instance in this ArrayList
                //movies_al.clear(); // A changer, il faudrait vérifier si le film existe déjà, dans ce cas ne pas faire la requête une seconde fois
                if (!movies_al.contains(movie)) movies_al.add(movie); // if the movie is not present, we add it

                //setChanged();
                //notifyObservers();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    /**
     * Retrieve the room based on a room_id
     * @param room_id, the id of the room
     * @throws SQLException
     * @throws TooMuchSeatsException
     */
    public synchronized Room retrieveRoom(int movie__id, int room_id, LocalDateTime show__start) {
        Room room = null;
        PreparedStatement getRm = null;
    	//rooms_al.clear();
        String sql = "SELECT movie_room_id, movie_id, room_id, rows, seats_by_row, show_start " 
        		   + "FROM tbmoviesrooms natural join tbrooms "
        		   + "WHERE movie_id = ? and room_id = ? and show_start = ? ";
        
        this.createConnection();

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            getRm = conn.prepareStatement(sql);
            getRm.setInt(1, movie__id);
            getRm.setInt(2, room_id);
            getRm.setTimestamp(3, Timestamp.valueOf(show__start));
            rs = getRm.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int id = rs.getInt("room_id");
                int movie_id = rs.getInt("movie_id");
                int rows = rs.getInt("rows");
                int seats_by_row = rs.getInt("seats_by_row");
                
            	for (Movie movie : movies_al) { // We search the right room (the one with the right id)
            		if (movie.getId() == movie_id) try {
                        room = new Room(rows, seats_by_row, movie, id); // New Room => we initialize all the room (empty for now !)
                        rooms_al.add(room);
                    } catch (TooMuchSeatsException e) {
            		    // SetChanged ? // notifyObserver ?
                        setChanged();
                        notifyObservers(e.getMessage());
                    }
                }
                retrieveCustomers(id, movie_id, show__start); // We retrieve all the customers for this room
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return room;
    }

    /**
     * Retrieve all the rooms of the theather
     * @throws SQLException
     * @throws TooMuchSeatsException
     */
    public synchronized void retrieveAllRooms() {
        rooms_al.clear();

        String sql = "SELECT room_id, rows, seats_by_row "
        		   + "FROM tbrooms ";
        
        this.createConnection();
        this.createStatement();

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int id = rs.getInt("room_id");
                int rows = rs.getInt("rows");
                int seats_by_row = rs.getInt("seats_by_row");
                
                // Push every Room instance in the ArrayList of Room
                try {
                    rooms_al.add(new Room(rows, seats_by_row, id));
                } catch (TooMuchSeatsException e) {
                    e.printStackTrace();
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
     * Retrieve all the customers of a [movie] that happens in a [room] at a time given [show_start]
     * @param room_id
     * @param movie_id
     * @param show_start
     * @throws SQLException
     */
    public synchronized void retrieveCustomers(int room_id, int movie_id, LocalDateTime show_start) {
    	// customer_id should be unique key ?
    	// Supprimer le booléen isTaken de la BDD ? Done.
    	// Redondance entre les tables seats et customers ?
        PreparedStatement getCt = null;
    	
        String sql = "SELECT c.customer_id, customer_type, sRow, sColumn "
                   + "FROM tbcustomers as c "
                   + "natural join tbcustomerstype "
                   + "WHERE movie_room_id = "
                   + "(select movie_room_id "
                   + "from tbmoviesrooms as mr "
                   + "where mr.room_id = ? and mr.movie_id = ? and mr.show_start = ?)";
        
        this.createConnection();

        ResultSet rs = null; // Execute the sql query and put the results in the results set

        try {
            getCt = conn.prepareStatement(sql);

            getCt.setInt(1, room_id);
            getCt.setInt(2, movie_id);
            getCt.setTimestamp(3, Timestamp.valueOf(show_start));

             rs = getCt.executeQuery();
        } catch (SQLException e) {
            //e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                int customer_id = rs.getInt("customer_id");
                String customer_type = rs.getString("customer_type");
                int row = rs.getInt("sRow");
                int column = rs.getInt("sColumn");
                
                //Look for the right Room => create an instance of customer and add it to the customers ArrayList
                for (Room room : rooms_al) {
                	if (room.getId() == room_id) { // Retrieve the right room
                        try {
                            customers_al.add(new Customer(column, row, room, customer_type, customer_id)); // The instance of customer book the seat
                        } catch (SeatUnknownException e) {
                            e.getMessage();
                        } catch (SeatTakenException e) {
                            e.getMessage();
                        }
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
    
    public synchronized ArrayList<String> retrieveTypes() {
        ArrayList<String> types = new ArrayList<String>();
        String selectTypes = "SELECT customer_type FROM tbcustomerstype";

        this.createConnection();
        this.createStatement();

        ResultSet rs = null; // Execute the sql query and put the results in the results set
        try {
            rs = stmt.executeQuery(selectTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            while (rs.next()) { // While there're still results

                String type = rs.getString("customer_type"); // get the genre
                types.add(type); // push the genre in the ArrayList
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types; // We return the ArrayList of genres
    }
    
    /**
     * Return the id of the customer type (to be able to add the customer to the db (relationnal db))
     * @param type, type of the customer (Junior, Student)
     * @return customer_type_id
     * @throws SQLException
     */
    public synchronized int retrieveCustomerTypeId(String type) {
        int customer_id = 0;
        PreparedStatement getCtId = null;
        ResultSet rs = null;

    	String customerTypeId = "SELECT customer_type_id "
				+ "FROM tbcustomerstype "
				+ "WHERE customer_type = ?";
    	
    	this.createConnection();

    	try {
    	    getCtId = conn.prepareStatement(customerTypeId);
    	    getCtId.setString(1, type);
    		rs = getCtId.executeQuery();

    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while(rs.next()) {
    			customer_id = rs.getInt("customer_type_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customer_id;
    }
    
    /**
     * Retrieve the id movie_room_id of a customer (to be able to add a customer to the db (relationnal db))
     * @param movie_id, id of the movie
     * @param room_id, id of the room
     * @param show_start, start date of the movie
     * @return, int => the movie_room_id
     * @throws SQLException
     */
    public synchronized int retrieveMovieRoomId(int movie_id, int room_id, LocalDateTime show_start) {
        int movie_room_id = 0;
        PreparedStatement getMrId = null;

    	String movieRoomId = "SELECT movie_room_id "
    							+ "FROM tbmoviesrooms "
								+ "WHERE movie_id = ? and room_id = ? and show_start = ?";
    	
    	this.createConnection();
    	
    	ResultSet rs = null;
    	try {
    	    getMrId = conn.prepareStatement(movieRoomId);

    	    getMrId.setInt(1, movie_id);
    	    getMrId.setInt(2, room_id);
    	    getMrId.setTimestamp(3, Timestamp.valueOf(show_start));

    		rs = getMrId.executeQuery();
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
    		while(rs.next()) {
    			movie_room_id = rs.getInt("movie_room_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return movie_room_id;
    }
    
    public synchronized int retrieveLanguageId(String language) throws RuntimeException {
        int language_id = 0;
        PreparedStatement getLgId = null;
    	String languageId = "SELECT language_id "
    						+ "FROM tblanguages "
    						+ "WHERE language = ? ";
    	
    	this.createConnection();
    	
    	ResultSet rs = null;
    	
    	try {
    	    getLgId = conn.prepareStatement(languageId);
            getLgId.setString(1, language);
    		rs = getLgId.executeQuery();
    	} catch(SQLException e) {
            setChanged();
            notifyObservers("Cette langue n'existe pas !");
            throw new RuntimeException(e);
    	}
    	
    	try {
    		while(rs.next()) {
    			language_id = rs.getInt("language_id");
    		}
    	} catch(SQLException e) {
    		e.printStackTrace();
    	}
    	
    	try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
		return language_id;
    }

    public synchronized ArrayList<String> retrieveAllLanguages() {
        ArrayList<String> languages = new ArrayList<String>();
        PreparedStatement getLg = null;
        String languagesSQL = "SELECT * "
                + "FROM tblanguages";

        this.createConnection();

        ResultSet rs = null;

        try {
            getLg = conn.prepareStatement(languagesSQL);
            rs = getLg.executeQuery();
        } catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            while(rs.next()) {
                String language = rs.getString("language");
                languages.add(language);
            }
        } catch(SQLException e) {
            e.printStackTrace();
        }

        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return languages;
    }
    
    /**
     * retrieve the genre_id based on a genre, if the genre doesn't exist yet, we create it and send back it id; 
     * @param genre, a String that is one of the genre of the movie
     * @return int, the genre id
     * @throws SQLException
     */
    public synchronized int retrieveOrCreateGenreId(String genre) {
        int genre_id = 0;
        PreparedStatement getGrId = null;
        PreparedStatement addGr = null;

    	String genreId = "SELECT genre_id "
    						+ "FROM tbgenres "
    						+ "WHERE genre = ? ";
    	
    	this.createConnection(); // Connect to the DB
    	
    	ResultSet rs = null; // Set that will contain all the results
    	
    	try {
    		getGrId = conn.prepareStatement(genreId);
    		getGrId.setString(1, genre);
    		rs = getGrId.executeQuery();
    	} catch(SQLException queryErr) { // If the query fails 
    		//queryErr.printStackTrace();
    	}						
    	
    	try { 
            if (!rs.isBeforeFirst()) { // return false if rs is empty or the cursor is not before the first record
                rs.beforeFirst();
                String addGenre = "INSERT INTO tbgenres(genre) " // We create it
                                    + "VALUE (?)";
                
                try { // Beginning of insert query
                    addGr = conn.prepareStatement(addGenre, Statement.RETURN_GENERATED_KEYS); // Prepared Statement
                    addGr.setString(1, genre);
                    addGr.executeUpdate(); // Execute a prepared statement;

                    rs = addGr.getGeneratedKeys(); // get the last inserted id

                    while (rs.next()) {
                        genre_id = rs.getInt(1); // retrieve the genre_id just inserted
                    }
                    
                } catch (SQLException insertErr) { // Error in the insert query
                    insertErr.printStackTrace();
                }
            } else {
                while (rs.next()) {
                    genre_id = rs.getInt("genre_id");
                }
            }

    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    	    pstatementCloser(addGr);
    	    //connectionCloser(conn);
        }
        return genre_id;
    }
    
    /**
     * retrieve the cast_id based on a actor, if the actor doesn't exist yet, we create it and send back it id; 
     * @param actor, a String that is one of the actor of the movie
     * @return int, the cast id
     * @throws SQLException
     */
    public synchronized int retrieveOrCreateCastId(String actor) {
        int cast_id = 0;
        PreparedStatement getCtId = null;
        PreparedStatement addAc = null;

    	String castId = "SELECT cast_id "
                            + "FROM tbcasts "
    						+ "WHERE cast = ? ";
    	
    	this.createConnection(); // Connect to the DB
    	
    	ResultSet rs = null; // Set that will contain all the results
    	
    	try {
    	    getCtId = conn.prepareStatement(castId);
    	    getCtId.setString(1, actor);
    		rs = getCtId.executeQuery();
    	} catch(SQLException queryErr) { // If the query fails (ex: the actor does not exist)
    		//queryErr.printStackTrace();
    	}						
    	
    	try { // If the first query is OK, so the actor exist
    		
            if (!rs.isBeforeFirst()) { // return false if rs is empty

                String addActor = "INSERT INTO tbcasts(cast) " // We create it
                                    + "VALUE (?)";
                
                try { // Beginning of insert query
                    addAc = conn.prepareStatement(addActor, Statement.RETURN_GENERATED_KEYS); // Prepared Statement
                    addAc.setString(1, actor);
                    addAc.executeUpdate();// Execute a prepared statement and return the result set;

                    rs = addAc.getGeneratedKeys(); // get the last inserted id

                    while (rs.next()) {
                        cast_id = rs.getInt(1); // retrieve the cast_id just inserted
                    }
                    
                } catch (SQLException insertErr) { // Error in the insert query
                    insertErr.printStackTrace();
                }
            
            } else {
                if (rs.next())
                    cast_id = rs.getInt("cast_id");
            }

    	} catch(SQLException e) {
    		e.printStackTrace();
    	} finally {
    	    pstatementCloser(addAc);
    	    //connectionCloser(conn);
        }

		return cast_id;
    }

    /**
     * Retrieve a movie_id based on its name
     * @param title
     * @return
     */
    public synchronized int retrieveMovieId(String title) {
        int movie_id = 0;
        PreparedStatement getMvId = null;

        String getMovieId = "SELECT movie_id from tbmovies " +
                            "WHERE title = ?";
        this.createConnection();
        ResultSet rs = null;

        try {
            getMvId = conn.prepareStatement(getMovieId);
            getMvId.setString(1, title);
            rs = getMvId.executeQuery();
        } catch (SQLException e) {
            //e.printStackTrace();
        }
        try {
            if (rs.next()) {
                movie_id = (rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (getMvId != null) {
                try {
                    getMvId.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return movie_id;
    }

    /**
     * Add a movie in the db
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
    public synchronized void addMovie(/*int movie_id,*/ int room_id, String title, String description, String director, ArrayList<LocalDateTime> shows_start, ArrayList<String> casting, int time, String language, double price, ArrayList<String> genres) {
    	PreparedStatement addMv = null;

    	String addMovie = "INSERT INTO tbmovies(title, description, director, time, language_id, price) "
    						+ "VALUES (?, ?, ?, ?, ?, ?)";
    	
		this.createConnection();
    	
    	try {
    		
    		addMv = conn.prepareStatement(addMovie);
    		
    		addMv.setString(1, title);
    		addMv.setString(2, description);
    		addMv.setString(3, director);
    		addMv.setInt(4, time);
    		addMv.setInt(5, retrieveLanguageId(language.toLowerCase().trim()));
    		addMv.setDouble(6, price);
    		
    		addMv.executeUpdate();

    		int movie_id = retrieveMovieId(title);
    		// If the movie is successfully added to the db, 
    		addGenres(movie_id, genres); // We add the genres
    		addShows(movie_id, room_id, shows_start); // We add the shows
    		addCasting(movie_id, casting); // We add the casting
    		
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			conn.rollback();
	    		} catch (SQLException err) {
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
    public synchronized void addGenres(int movie_id, ArrayList<String> genres) {
    	PreparedStatement addGr = null;
    	
    	this.createConnection();
    	
    	try {
    	
    		for (String genre : genres) { // For Each genre
        		addGr = conn.prepareStatement("INSERT INTO tbmoviesgenres(movie_id, genre_id) " // Add the genre associated to the movie in the db
						+ "VALUES (?, ?)");
        		
        		addGr.setInt(1, movie_id);
        		addGr.setInt(2, retrieveOrCreateGenreId(genre.trim()));

        		addGr.executeUpdate();	
        	}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			conn.rollback();
	    		} catch (SQLException err) {
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
    public synchronized void addShows(int movie_id, int room_id, ArrayList<LocalDateTime> shows_start) {
    	PreparedStatement addSh = null;
    	
    	this.createConnection();
    	
    	try {
    	
    		for (LocalDateTime show_start : shows_start) { // For Each genre
        		addSh = conn.prepareStatement("INSERT INTO tbmoviesrooms(movie_id, room_id, show_start) " // Add the genre associated to the movie in the db
						+ "VALUES (?, ?, ?)");
        		
        		addSh.setInt(1, movie_id);
        		addSh.setInt(2, room_id);
        		addSh.setTimestamp(3, Timestamp.valueOf(show_start));
        		addSh.executeUpdate();	
        	}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			conn.rollback();
	    		} catch (SQLException err) {
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
    public synchronized void addCasting(int movie_id, ArrayList<String> casting) {
    	PreparedStatement addCs = null;
    	
    	this.createConnection();
    	
    	try {
    	
    		for (String actor : casting) { // For Each genre
        		addCs = conn.prepareStatement("INSERT INTO tbmoviescasts(movie_id, cast_id) " // Add the genre associated to the movie in the db
						+ "VALUES (?, ?)");
        		
        		addCs.setInt(1, movie_id);
        		addCs.setInt(2, retrieveOrCreateCastId(actor.trim()));

        		addCs.executeUpdate();	
        	}
    		
    	} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
	    			conn.rollback();
	    		} catch (SQLException err) {
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
     * insert a new customer into the db
     * @param x
     * @param y
     * @param type
     * @param movie_id
     * @param room_id
     * @param show_start
     *
     */
    public synchronized void addCustomer(int x, int y, String type, int movie_id, int room_id, LocalDateTime show_start) {
        PreparedStatement addCt = null;

        String addSeat = "INSERT INTO tbcustomers(sRow, sColumn, movie_room_id, customer_type_id) "
                + "VALUES (?, ?, ?, ?)";

        this.createConnection(); // Create connection to DB

        try {
            addCt = conn.prepareStatement(addSeat);

            addCt.setInt(1, y); // Row
            addCt.setInt(2, x); // Column
            addCt.setInt(3, retrieveMovieRoomId(movie_id, room_id, show_start));
            addCt.setInt(4, retrieveCustomerTypeId(type));
            addCt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
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

            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Convert a String into a LocalDateTime
     * @param show, the date and time in a String type
     * @return LocalDateTime, the date and time in a LocalDateTime type
     */
    private synchronized LocalDateTime stringToLocalDateTime(String show) {
    	// Formatters for date + time
    	DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		// start of the show
    	//LocalDate show_start_date = LocalDate.parse(show, format); // Date (yyyy-MM-dd)
    	//LocalTime show_start_time = LocalTime.parse(show, format); // Time (hh:mm:ss)
    	//LocalDateTime show_start = LocalDateTime.of(show_start_date, show_start_time); // Parse in LocalDateTime
    	LocalDateTime show_start = LocalDateTime.parse(show, format);
    	return show_start;
    }
    
    /**
     * Convert a Date (type) into a LocalDateTime
     * @param show, the date in a Date type 
     * @return, the date in LocalDateTime type
     */
    private synchronized LocalDateTime dateToLocalDateTime(Date show) {
    	Instant instant = Instant.ofEpochMilli(show.getTime());
    	return LocalDateTime.ofInstant(instant, ZoneOffset.UTC);
    }

    /**
     * close a prepared statement
     * @param pstmt, a prepared statement
     */
    private void pstatementCloser(PreparedStatement pstmt) {
        if (pstmt != null) {
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * close a connection to the db
     * @param connection, a connection to the db
     */
    private void connectionCloser(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Getters and Setters
     */


    public synchronized String getDB_DRIVER() {
        return DB_DRIVER;
    }

    public synchronized String getDB_URL() {
        return DB_URL;
    }

    public synchronized void setDB_URL(String DB_URL) {
        this.DB_URL = DB_URL;
    }

    public synchronized String getUSER() {
        return USER;
    }

    public synchronized String getPWD() {
        return PWD;
    }
    
    public synchronized ArrayList<Movie> getMovies_al() {
		return movies_al;
	}

	public synchronized void setMovies_al(ArrayList<Movie> movies_al) {
		this.movies_al = movies_al;
	}

	public synchronized ArrayList<Show> getShows_al() {
		return shows_al;
	}

	public synchronized void setShows_al(ArrayList<Show> shows_al) {
		this.shows_al = shows_al;
	}

	public synchronized ArrayList<Customer> getCustomers_al() {
		return customers_al;
	}

	public synchronized void setCustomers_al(ArrayList<Customer> customers_al) {
		this.customers_al = customers_al;
	}

	public synchronized ArrayList<Room> getRooms_al() {
		return rooms_al;
	}

	public synchronized void setRooms_al(ArrayList<Room> rooms_al) {
		this.rooms_al = rooms_al;
	}

	public synchronized Connection getConn() {
		return conn;
	}

	public synchronized void setConn(Connection conn) {
		this.conn = conn;
	}

	public synchronized Statement getStmt() {
		return stmt;
	}

	public synchronized void setStmt(Statement stmt) {
		this.stmt = stmt;
	}
	
}
