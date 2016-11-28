package src.konopolis.model;

import java.util.ArrayList;

/**
 * @author Nathan D. - Groupe 3
 *
 */
public class Customer {
	private Seat seat;
	private double reduction = 0.0;
	private char type;
	private Room room;
	private int age;
	private ArrayList<Customer> listCustomers = new ArrayList<Customer>();

	public Customer() {
		seat.setRow(0);
		seat.setColumn(0);
		seat.setTaken(false);
		reduction = 0.0;
		type = 'C';
	}

<<<<<<< HEAD:src/konopolis/model/Customer.java
	public Customer(int x, int y, Room room){
=======
	public Customer(int x, int y, Room room) {

>>>>>>> 9ca86019dabcd4f02cb3e9da53b2f674c6a177f1:src/konopolis/Customer.java
		reduction = 0.0;
        this.room = room;

		try {
            this.room.giveSeat(x, y);
            Room.setIncome(Room.getIncome() + this.room.getMovie().getPrice() * this.reduction);
            listCustomers.add(this);
        } catch (SeatUnknownException e) {
            System.out.println(e.getMessage());
		} catch (SeatTakenException e) {
            System.out.println(e.getMessage());
        }

	}

	public Customer(int x,int y, Room room, int age) {
		this.room = room;
		
		try {
            this.room.giveSeat(x, y);
            this.age = age;
        	if (age > 60) {
    			this.reduction = 0.7;
    		} else if (age < 12) {
    			this.reduction = 0.5;
    		} else {
    			this.reduction = 0.0;
    		}
			Room.setIncome(Room.getIncome() + this.room.getMovie().getPrice() * this.reduction);
            listCustomers.add(this);
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

	public ArrayList<Customer> getListCustomers() {
		return listCustomers;
	}

	public void setListCustomers(ArrayList<Customer> listCustomers) {
		this.listCustomers = listCustomers;
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
                ", listCustomers=" + listCustomers +
                '}';
    }
}
