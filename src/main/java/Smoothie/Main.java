package Smoothie;

public class Main {

	public static void main(String[] args) {
		Machine machine = new Machine();
		System.out.println("Welcome to the Smoothie Machine!\n");
		boolean smoothieOpen = true;
		machine.getIngredients();
		machine.createOriginalList();
		machine.loadPrices();
		machine.printRecipes(machine.loadRecipes(), machine.loadPrices());
		while (smoothieOpen) {
			int response = machine.displayMenuOptions();
			if (response == 1) {
				System.out.println(machine.printListIngredients(machine.ingredQuantsListBeforeCheckout));
			}
			if (response == 2) {
				machine.viewRecipes();
				machine.setPage();
			}
			if (response == 3) {
				machine.setPage();
				System.out.println(machine.selectRecipe());
			}
			if (response == 4) {
				System.out.println(machine.getSmoothieChoice(machine.loadRecipes()));
			}
			if (response == 5) {
				System.out.println(machine.showCart());
				machine.cartOption();
			}
			if (response == 6) {
				smoothieOpen = false;
				break;
			}
			if (response > 6 || response < 1){
				System.out.println("Must choose a valid menu choice.");
			}

		}
		System.out.println("Your total comes to: $" + machine.finalPrice());
		System.out.println("Thanks for visiting the Smoothie Shop! Enjoy!");
	}

}
