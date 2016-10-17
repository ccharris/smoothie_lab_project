package Smoothie;
public class Main {

	public static void main(String[] args) {
		Machine machine = new Machine();
		System.out.println(machine.printListIngredients(machine.getIngredients()));
		System.out.println(machine.getSmoothieChoice(machine.loadRecipes()));		
	}

}
