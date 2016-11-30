package src.konopolis.model;

import java.util.Observable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
    private final String PWD = "root";

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
                String[] casting = rs.getString("casting").split(","); // Several actors
                String[] genres = rs.getString("genres").split(","); // Several genres
                //String[] shows = rs.getString("shows").split(",");
                String[] shows = rs.getString("shows").split(",");
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
    	// Supprimer le bool�en isTaken de la BDD ? Done.
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
    	
    	try {
        	this.createConnection(); // Create connection to DB
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
    		if (addCt != null)
				try {
					addCt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
    		if (addSt != null)
				try {
					addSt.close();
				} catch (SQLException e) {
					e.printStackTrace();
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
