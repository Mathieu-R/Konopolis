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

	public Customer(int x, int y, Room room){

		reduction = 0.0;
        this.room = room;

		try {
            this.room.giveSeat(x, y);
            this.room.setIncome(this.room.getIncome() + this.room.getMovie().getPrice() * this.reduction);
            listCustomers.add(this);
        } catch (SeatUnknownException e) {
            System.out.println(e.getMessage());
		} catch (SeatTakenException e) {
            System.out.println(e.getMessage());
        }

	}

	public Customer(Seat pla, Room room, int age){
		place = new Seat(pla.getRow(),pla.getColumn());
		this.room = room;
		this.age = age;
		if(age>60){
			this.reduction = 0.7;
		} else if(age<12) {
			this.reduction=0.5;
		} else{
			this.reduction=0.0;
		}
		listCustomers.add(this);
		
	}

	public Room getRoom() {
		return room;
	}
	public void setRoom(Room ro) {
		this.room = ro;
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
	public Seat getPlace() {
		return place;
	}
	public void setPlace(Seat place) {
		this.place = place;
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
}
