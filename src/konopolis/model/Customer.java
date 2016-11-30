package src.konopolis.model;

import java.util.ArrayList;

/**
 * @author Nathan D. - Groupe 3
 *
 */
public class Customer {
	private static int id = 0; // ID of the customer, id is for the whole theater
	private Room room;
	private double reduction = 0.0;
	private char type;
	private int age;

	public Customer(int x, int y, Room room, int id) {
		this.id = id;
		reduction = 0.0;
        this.room = room;

		try {
            this.room.giveSeat(x, y);
            Room.setIncome(Room.getIncome() + this.room.getMovie().getPrice() * this.reduction);
        } catch (SeatUnknownException e) {
            System.out.println(e.getMessage());
		} catch (SeatTakenException e) {
            System.out.println(e.getMessage());
        }

	}

	public Customer(int x, int y, Room room, String type, int id) {
		this.id = id;
		this.room = room;
		
		try {
            this.room.giveSeat(x, y);
            this.type = type.charAt(0); // First charachter of the type
            
            switch(this.type) {
            	case 'J': this.reduction = 0.5;
            	case 'E': this.reduction = 0.3;
            	case 'S': this.reduction = 0.4;
            	case 'V': this.reduction = 0.7;
            }
			Room.setIncome(Room.getIncome() + this.room.getMovie().getPrice() * this.reduction);
        } catch (SeatUnknownException e) {
            System.out.println(e.getMessage());
		} catch (SeatTakenException e) {
            System.out.println(e.getMessage());
        }
		
	}
	
	public Customer(int x, int y, Room room, String type, double reduction, int id) {
		this.id = id;
		this.room = room;
		
		try {
            this.room.giveSeat(x, y);
            this.type = type.charAt(0); // First charachter of the type
            this.reduction = reduction;
         
			Room.setIncome(Room.getIncome() + this.room.getMovie().getPrice() * this.reduction);
        } catch (SeatUnknownException e) {
            System.out.println(e.getMessage());
		} catch (SeatTakenException e) {
            System.out.println(e.getMessage());
        }
		
	}

	public static int getId() {
		return id;
	}

	public static void setId(int id) {
		Customer.id = id;
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

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + age;
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
		if (age != other.age)
			return false;
		if (type != other.type)
			return false;
		return true;
	}

    @Override
    public String toString() {
        return "Customer{" +
                ", reduction=" + reduction +
                ", type=" + type +
                ", room=" + room +
                ", age=" + age +
                '}';
    }
}
