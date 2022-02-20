package src.konopolis.model;

/**
 * @author Nathan D. - Groupe 3
 */
public class Customer {
  private static int currentId = 0;
  private int id; // ID of the customer, id is for the whole theater
  private Room room;
  private double reduction = 0.0;
  private char type;
  private String fullType;

  /**
   * Constructor that add a customer according its place in a room. The customer
   * has an id
   *
   * @param x,    seat of the row
   * @param y,    row
   * @param room, the room
   * @param id,   id of the customer
   * @throws SeatUnknownException
   * @throws SeatTakenException
   */
  public Customer(int x, int y, Room room, int id) throws SeatUnknownException, SeatTakenException {
    currentId++;
    this.id = id;
    this.room = room;

    reduction = 0.0;

    this.room.giveSeat(x, y);
  }

  /**
   * Constructor that add a customer according its place in a room. The customer
   * has an id and a type
   *
   * @param x,    seat of the row
   * @param y,    row
   * @param room, the room
   * @param type, the type of the customer
   * @param id,   id of the customer
   * @throws SeatUnknownException
   * @throws SeatTakenException
   */
  public Customer(int x, int y, Room room, String type, int id) throws SeatUnknownException, SeatTakenException {
    currentId++;
    this.id = id;
    this.room = room;

    this.room.giveSeat(x, y);
    this.type = type.charAt(0); // First charachter of the type

    switch (this.type) {
      case 'J':
        this.reduction = 0.5;
        break;
      case 'E':
        this.reduction = 0.3;
        break;
      case 'S':
        this.reduction = 0.4;
        break;
      case 'V':
        this.reduction = 0.7;
        break;
      case 'N':
        this.reduction = 0.0;
        break;
    }

  }

  /**
   * Constructor that add a customer according its place in a room. The customer
   * has a type but no id
   *
   * @param x,    the seat of the row
   * @param y,    the row
   * @param room, the room
   * @param type, the type of the customer (Senior, VIP,...)
   * @throws SeatUnknownException
   * @throws SeatTakenException
   */
  public Customer(int x, int y, Room room, String type) throws SeatUnknownException, SeatTakenException {
    currentId++;
    this.room = room;

    this.room.giveSeat(x, y);
    this.type = type.charAt(0); // First charachter of the type

    switch (this.type) {
      case 'J':
        this.reduction = 0.5;
        break;
      case 'E':
        this.reduction = 0.3;
        break;
      case 'S':
        this.reduction = 0.4;
        break;
      case 'V':
        this.reduction = 0.7;
        break;
      case 'N':
        this.reduction = 0.0;
        break;
    }
  }

  /**
   * Construcor that create a customer only to calculate the reduction, nothing
   * else
   *
   * @param type, the type of the customer
   */
  public Customer(String type) {
    this.fullType = type;
    this.type = type.charAt(0); // First charachter of the type

    switch (this.type) {
      case 'J':
        this.reduction = 0.5;
        break;
      case 'E':
        this.reduction = 0.3;
        break;
      case 'S':
        this.reduction = 0.4;
        break;
      case 'V':
        this.reduction = 0.7;
        break;
      case 'N':
        this.reduction = 0.0;
        break;
    }
  }

  /**
   * Constructor that add a customer according its place in a room. The customer
   * has an id, a type and a reduction
   *
   * @param x,         the seat of the row
   * @param y,         the row
   * @param room,      the room
   * @param type,      the type of the customer (Senior, VIP,...)
   * @param reduction, the reduction of the seat
   * @param id,        its id
   * @throws SeatUnknownException
   * @throws SeatTakenException
   */
  public Customer(int x, int y, Room room, String type, double reduction, int id)
      throws SeatUnknownException, SeatTakenException {
    currentId++;
    this.id = id;
    this.room = room;

    this.room.giveSeat(x, y);
    this.type = type.charAt(0); // First charachter of the type
    this.reduction = reduction;

  }

  public static int getCurrentId() {
    return currentId;
  }

  public static void setCurrentId(int currentId) {
    Customer.currentId = currentId;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public Room getRoom() {
    return room;
  }

  public void setRoom(Room room) {
    this.room = room;
  }

  public double getReduction() {
    return reduction;
  }

  public void setReduction(double reduction) {
    this.reduction = reduction;
  }

  public char getType() {
    return type;
  }

  public void setType(char type) {
    this.type = type;
  }

  public String getFullType() {
    return fullType;
  }

  public void setFullType(String fullType) {
    this.fullType = fullType;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + type;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Customer other = (Customer) obj;
    return type == other.type;
  }

  @Override
  public String toString() {
    return "Customer{" +
        ", reduction=" + reduction +
        ", type=" + type +
        ", room=" + room +
        '}';
  }
}
