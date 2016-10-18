package Smoothie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

public class Machine {
	public Machine() {

	}

	Scanner userIn = new Scanner(System.in);
	public HashMap<String, Double> ingredQuants = new HashMap<String, Double>();
	public BigDecimal priceTotal = new BigDecimal(0.00);
	public ArrayList<String> choicesList = new ArrayList<String>();
	public HashMap<String, Double> ingredQuantsListBeforeCheckout = new HashMap<String, Double>();

	// load recipes from a file
	public HashMap<String, ArrayList<Food>> loadRecipes() {
		final File recipeFile = new File("src/main/resources/recipes.csv");
		HashMap<String, ArrayList<Food>> recipeMap = new HashMap<String, ArrayList<Food>>();
		final InputStream recipeStream;
		try {
			recipeStream = new FileInputStream(recipeFile);

		} catch (FileNotFoundException e) {
			System.out.println("Couldn't find the file: " + recipeFile.getAbsolutePath());
			return recipeMap;
		}
		Scanner input = new Scanner(recipeStream);
		while (input.hasNextLine()) {
			String[] items = input.nextLine().split(",");
			ArrayList<Food> recipes = new ArrayList<>();
			for (int i = 1; i < items.length; i++) {
				recipes.add(getIngredientParams(items[i]));
			}
			recipes.add(getIngredientParams("ice"));
			recipeMap.put(items[0], recipes);
		}
		return recipeMap;
	}

	// get ingredients from a file
	public HashMap<String, Double> getIngredients() {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/main/resources/ingredients.csv"));
			String line;
			while ((line = reader.readLine()) != null) {
				String[] items = line.split(":");
				double d = Double.parseDouble(items[1]);

				String ingred = items[0];
				ingredQuants.put(ingred, d);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ingredQuants;
	}

	public void createOriginalList() {
		for (Entry<String, Double> entry : ingredQuants.entrySet()) {
			ingredQuantsListBeforeCheckout.put(entry.getKey(), (Double) entry.getValue());
		}
	}

	// returns a string that is a list of all ingredients and their quantities
	public String printListIngredients(HashMap<String, Double> ingredients) {
		ArrayList<String> allIngredients = new ArrayList<String>();
		ArrayList<Double> ingredNums = new ArrayList<Double>();
		for (String key : ingredients.keySet()) {
			allIngredients.add(key);
			ingredNums.add(ingredients.get(key));
		}
		StringBuilder sb = new StringBuilder();
		sb.append("Our Ingredients:\n");
		for (int i = 0; i < allIngredients.size(); i++) {
			if (i == 2 || i == 5 || i == 8 || i == 11 || i == 14 || i == 17 || i == 20) {
				sb.append(allIngredients.get(i)).append(": ").append(ingredNums.get(i)).append(" in stock.");
			} else if (i % 3 != 0) {
				sb.append(allIngredients.get(i)).append(": ").append(ingredNums.get(i)).append(" in stock.");
				sb.append(" | ");
			} else if (i % 3 == 0) {
				sb.append("\n");
				sb.append(allIngredients.get(i)).append(": ").append(ingredNums.get(i)).append(" in stock.");
				sb.append(" | ");
			}
		}
		return sb.toString();
	}

	// how to view all recipes
	public void viewRecipes() {
		printRecipes(loadRecipes(), loadPrices());
	}

	// subtract ingredients for each recipe
	public void subtractIngred(String recipeName) {
		List<Food> ingredList = loadRecipes().get(recipeName);
		for (Food ingred : ingredList) {
			ingredQuants.put(ingred.toString(), ingredQuants.get(ingred.toString()) - 1);
		}

	}

	// gets user input to choose recipe
	public String selectRecipe() {
		String confirm = "";
		while (true) {
			System.out.println("\nWhich smoothie would you like to choose? Press b to go back");
			printRecipes(loadRecipes(), loadPrices());
			String choice = userIn.nextLine();
			if (choice.toLowerCase().equals("b")) {
				break;
			} else if(loadRecipes().get(choice)!= null){
				if (canAdd(choice)){
					choicesList.add(choice);
					subtractIngred(choice);
					confirm = ("Okay, added smoothie " + choice + "!");
					break;
				}
			} 
			else {
				System.out.println("Please choose one of the following.");
			}
		}
		return confirm;

	}

	// prints recipe and adjusts price totals based on their smoothie choice
	public String getSmoothieChoice(HashMap<String, ArrayList<Food>> ingredients) {
		String recipe = "";
		for (String choice : choicesList) {
			if (choice != null) {
				if (canMake(choice)) {
					recipe = (recipe + "Now making " + choice + "\n" + printRecipeInstruction(ingredients.get(choice)));
					priceTotal = (priceTotal.add(loadPrices().get(choice)));
					for (Entry<String, Double> entry : ingredQuants.entrySet()) {
						ingredQuantsListBeforeCheckout.put(entry.getKey(), (Double) entry.getValue());
					}
				} else {
					System.out.println("Insufficient Quantities to make that smoothie.");
				}
			}
		}

		return recipe;

	}

	public boolean canMake(String choiceName) {
		List<Food> ingredList = loadRecipes().get(choiceName);
		boolean canMake = true;
		for (Food ingred : ingredList) {
			double quant = ingredQuants.get(ingred.toString());
			if (quant < 0) {
				canMake = false;
			}
		}

		return canMake;
	}

	public boolean canAdd(String choiceName) {
		List<Food> ingredList = loadRecipes().get(choiceName);
		boolean canAdd = true;
		for (Food ingred : ingredList) {
			double quant = ingredQuants.get(ingred.toString());
			if (quant == 0) {
				canAdd = false;
			}
		}
		return canAdd;
	}

	// prints recipe instructions
	public String printRecipeInstruction(ArrayList<Food> ingredList) {
		StringBuilder s = new StringBuilder();
		s.append("\nRecipe Instructions:");
		for (Food ingred : ingredList) {

			if (getPittable().contains(ingred.toString())) {
				s.append("\n Pit the ").append(ingred).append(",\n Cut the ").append(ingred).append(",\n Add the ")
						.append(ingred).append(" to the blender.");
			} else if (getPeelable().contains(ingred.toString())) {
				s.append("\n Peel the ").append(ingred).append(",\n Cut the ").append(ingred).append(",\n Add the ")
						.append(ingred).append(" to the blender.");
			} else if (getOthers().contains(ingred.toString())) {
				s.append("\n Prepare the ").append(ingred).append(",\n Add the ").append(ingred)
						.append(" to the blender.");
			} else {
				s.append("\n Cut the ").append(ingred).append(",\n Add the ").append(ingred).append(" to the blender.");
			}
		}
		s.append("\n Add the ice to the blender and start blending!");
		return s.toString();
	}

	// takes recipes and prices and prints out each recipe and their prices.
	public String printRecipes(HashMap<String, ArrayList<Food>> recipeMap, HashMap<String, BigDecimal> prices) {
		StringBuilder s = new StringBuilder();
		for (Entry<String, ArrayList<Food>> entry : recipeMap.entrySet()) {
			String key = entry.getKey();
			System.out.println("\n" + key + ": " + recipeMap.get(key) + " $" + prices.get(key));
			s.append("\n").append(key).append(": ").append(recipeMap.get(key)).append(" $").append(prices.get(key));
			for (Food food : entry.getValue()) {
				if (ingredQuants.get(food.toString()) == 0) {
					System.out.println("Insufficient Quantities of " + food + " for this smoothie.");
				} else {
					System.out.print(food + " #: " + ingredQuants.get(food.toString()) + " ");
					s.append(food).append(" number in stock: ").append(ingredQuants.get(food.toString()));
				}
			}
		}

		return s.toString();

	}

	// load prices from a file
	public HashMap<String, BigDecimal> loadPrices() {
		String name;
		HashMap<String, BigDecimal> priceList = new HashMap<String, BigDecimal>();
		try {
			Scanner s = new Scanner(new File("src/main/resources/smoothiePrices.txt"));
			while (s.hasNextLine()) {
				BigDecimal b = new BigDecimal(0.00);
				String[] items = s.nextLine().split(":");
				for (int i = 1; i < items.length; i++) {
					b = new BigDecimal(items[i]);
				}
				priceList.put(items[0], b);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return priceList;

	}

	// creates a new ingredient class based on ingredients in the recipe
	public Food getIngredientParams(String ingredient) {
		ingredient = ingredient.toLowerCase();
		Food food = new Ingredient(ingredient, 1, false);
		return food;
	}

	// gets a list of pittable fruit from a file
	public List<String> getPittable() {
		List<String> pittable = new ArrayList<String>();
		try {
			Scanner s = new Scanner(new File("src/main/resources/pittableFruit.txt"));
			while (s.hasNextLine()) {
				pittable.add(s.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return pittable;
	}

	// gets a list of peelable fruit from a file
	public List<String> getPeelable() {
		List<String> peelable = new ArrayList<String>();
		try {
			Scanner s = new Scanner(new File("src/main/resources/peelableFruit.txt"));
			while (s.hasNextLine()) {
				peelable.add(s.nextLine());
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return peelable;
	}

	// gets a list of other ingredient from file
	public List<String> getOthers() {
		List<String> others = new ArrayList<String>();
		try {
			Scanner s = new Scanner(new File("src/main/resources/otherIngredients.txt"));
			while (s.hasNextLine()) {
				others.add(s.nextLine());
			}

		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return others;
	}

	// returns the price
	public BigDecimal finalPrice() {
		return priceTotal;
	}

	// displays menu options and asks user to pick
	public int displayMenuOptions() {
		int choice = 0;
		Scanner menuIn = new Scanner(System.in);
		while (true) {
			System.out.println("What would you like to do?" + "\n1)Browse Ingredients" + "\n2)Browse Recipes"
					+ "\n3)Choose Smoothie" + "\n4)Make Smoothie(s)" + "\n5)View Cart" + "\n6)Quit");
			String userMenuChoice = menuIn.nextLine();
			try {
				choice = Integer.parseInt(userMenuChoice);
				break;
			} catch (NumberFormatException e) {
				System.out.println("Error, must enter 1-6.");
			}
		}

		return choice;
	}

	public String showCart() {
		StringBuilder cart = new StringBuilder();
		cart.append("Your Shopping Cart:\n");
		Set<String> unique = new HashSet<String>(choicesList);
		for (String key : unique) {
			cart.append(key + ": " + Collections.frequency(choicesList, key) + "\n");
		}
		cart.append("Your current total is: $").append(finalPrice());

		return cart.toString();
	}

	public void cartOption() {
		ArrayList<String> choicesCopy = new ArrayList<>();
		while (true) {
			System.out.println("What would you like to do?\n" + " 1) Return to main smoothie menu\n"
					+ " 2) Modify smoothie quantities\n" + " 3) Remove a smoothie entirely\n");
			String choice = userIn.nextLine();
			if (choice.contains("1")) {
				break;
			} else if (choice.contains("2")) {
				System.out.println("Which smoothie would you like to modify?");
				String smoothChoice = userIn.nextLine();
				if (choicesList.contains(smoothChoice)) {
					System.out.println("How make of " + smoothChoice + " would you like to have in your cart?");
					Scanner numIn = new Scanner(System.in);
					int num = numIn.nextInt();
					System.out.println("Okay, changing quantity of " + smoothChoice + " to " + num + ".");
					for (String cho : choicesList) {
						if (!cho.contains(smoothChoice)) {
							choicesCopy.add(cho);
						}
					}
					for (int i = 1; i <= num; i++) {
						choicesCopy.add(smoothChoice);
					}
					break;
				} else {
					System.out.println("Sorry that choice is not valid.");
				}
			} else if (choice.contains("3")) {
				System.out.println("Okay, which smoothie would you like to remove?");
				String choose = userIn.nextLine();
				if (choicesList.contains(choose)) {
					System.out.println("Okay, removing " + choose);
					for (String cho : choicesList) {
						if (!cho.contains(choose)) {
							choicesCopy.add(cho);
						}
					}
					break;
				} else {
					System.out.println("Sorry that choice is not valid.");
				}
			}
		}
		choicesList = choicesCopy;
	}

}
