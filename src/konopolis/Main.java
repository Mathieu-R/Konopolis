/**
 * 
 */
package konopolis;
import java.util.Date;
/**
 * @author natha
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String [] genre=new String[5];
		genre[0]="Amour";
		@SuppressWarnings("deprecation")
		Date date=new Date(23,2,2015);
		Movie mov=new Movie("Chabada",date,15);
		Room ro1 =new Room(10,10,mov);
		Seat s1=new Seat(9,4);
		Seat s2=new Seat(9,3);
		Customer c1=new Customer(s1,ro1);
		Customer c2=new Customer(s2,ro1);
		ro1.displayRoom();
		System.out.println(ro1.getIncome());
		
		
	}

}
