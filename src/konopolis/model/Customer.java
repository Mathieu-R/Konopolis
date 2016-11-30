package src.konopolis.model;

import java.util.ArrayList;

/**
 * @author Nathan D. - Groupe 3
 *
 */
public class Customer {
	private static int id = 0; // ID of the customer, id is for the whole theater
	private int seat_id;
	private Room room;
	private double reduction = 0.0;
	private char type;
	private int age;

	public Customer(int x, int y, Room room) {

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

	public Customer(int x, int y, Room room, String type) {
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
	
	public Customer(int x, int y, Room room, String type, double reduction) {
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

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Customer customer = (Customer) o;

        if (type != customer.type) return false;
        if (age != customer.age) return false;
        return seat.equals(customer.seat);

    }

    @Override
    public int hashCode() {
        int result = seat.hashCode();
        result = 31 * result + (int) type;
        result = 31 * result + age;
        return result;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "seat=" + seat +
                ", reduction=" + reduction +
                ", type=" + type +
                ", room=" + room +
                ", age=" + age +
                '}';
    }
}
