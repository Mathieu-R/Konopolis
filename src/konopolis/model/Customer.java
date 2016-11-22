package src.konopolis.model;

import java.util.ArrayList;

/**
 * @author Nathan D. - Groupe 3
 *
 */
public class Customer {
	private Seat place;
	private double reduction=0.0;
	private char type;
	private Room ro;
	private int age;
	private ArrayList<Customer> listCustomers = new ArrayList<Customer>();

	public Customer(){
		place.setRow(0);
		place.setColumn(0);
		place.setTaken(false);
		reduction = 0.0;
		type = 'C';
	}

	public Customer(Seat pla, Room ro){

		reduction = 0.0;



		if(!(ro.getSeat(pla.getRow(),pla.getColumn()).isTaken())){
			place = new Seat(pla.getRow(),pla.getColumn());
			this.ro = ro;
			this.ro.giveSeat(place);
			this.ro.setIncome(this.ro.getIncome() + ro.getMovie().getPrice() * this.reduction);
			listCustomers.add(this);
		}
	}

	public Customer(Seat pla, Room ro, int age){
		place = new Seat(pla.getRow(),pla.getColumn());
		this.ro = ro;
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
		return ro;
	}
	public void setRoom(Room ro) {
		this.ro = ro;
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
