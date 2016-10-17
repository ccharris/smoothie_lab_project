package Smoothie;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		Machine machine = new Machine();
		System.out.println(machine.printListIngredients(machine.getIngredients()));
		boolean smoothieOpen = true;
		boolean firstThrough = true;
		while (smoothieOpen) {
			if(!firstThrough){
				System.out.println(machine.printListIngredients(machine.ingredQuants));
			}
			System.out.println(machine.getSmoothieChoice(machine.loadRecipes()));
			System.out.println("Would you like to make another smoothie?");
			Scanner uIn = new Scanner(System.in);
			String response = uIn.nextLine();
			if(response.contains("n")){
				smoothieOpen = false;
			}
			firstThrough = false;
		}
		System.out.println("Thanks for visiting the Smoothie Shop! Enjoy!");
	}

}
