package konopolis;

import java.util.Scanner;

public class KonopolisViewCons extends KonopolisView {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to Konopolis\t");
		
		System.out.println("Witch movie do you want to see ?\t");
		String MovieResearch = scan.nextLine();
		System.out.println("You take "+MovieResearch);
	}

}
