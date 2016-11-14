/**
 * 
 */
package konopolis;

import java.util.ArrayList;

/**
 * @author natha
 *
 */
public class Customer {
	private Seat place;
	double reduction=0.0;
	char type;
	private ArrayList<Customer> listCustomers = new ArrayList<Customer>();
	public Customer(){
		place.setRow(0);
		place.setColumn(0);
		place.setTaken(false);
		reduction=0.0;
		type='C';
	}
	public Customer(Seat pla){
		place=new Seat(pla.getRow(),pla.getColumn());
		reduction=0.0;
		type='C';
	}
	public void addGroup(Customer client){
		listCustomers.add(client);
	}
	public void bookSeats(Room ro){
		if(listCustomers.size()==0){
			System.out.println("La liste de clients eest vide");
			return;
		}else{
			for(int c=0;c<listCustomers.size();c++){
				for(int i=0;i<ro.getRows();i++){
					for(int j=0;j<ro.getSeatsByRow();j++){
						if(ro.getAllSeats().get(listCustomers.get(c).place.getRow()).get(listCustomers.get(c).place.getColumn()).isTaken()){
							System.out.println("Siège"+listCustomers.get(c).place.getRow()+","+listCustomers.get(c).place.getColumn()+" déja pris");
							return;
						}else{
							System.out.println("OK");
						}
					}
				}
			}
			for(int c=0;c<listCustomers.size();c++){
				for(int i=0;i<ro.getRows();i++){
					for(int j=0;j<ro.getSeatsByRow();j++)
						ro.getAllSeats().get(listCustomers.get(c).place.getRow()).get(listCustomers.get(c).place.getColumn()).setTaken(true);
				}
			}
			
		}
		
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
