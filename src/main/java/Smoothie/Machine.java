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
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Machine {
	public Machine() {

	}
	Scanner userIn = new Scanner(System.in);
	public HashMap<String, Double> ingredQuants = new HashMap<String, Double>();

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
				recipeMap.put(items[0], recipes);
		}
		return recipeMap;
	}

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
//		} finally {
//			try {
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
		return ingredQuants;
	}

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

	public String getSmoothieChoice(HashMap<String, ArrayList<Food>> ingredients) {
			String choiceName;
			while (true) {
				System.out.println("\nWhich smoothie would you like to choose?");
				printRecipes(loadRecipes(), loadPrices());
				String choice = userIn.nextLine();
				if (choice.toLowerCase().contains("protein")) {
					choiceName = "protein";
					break;
				} else if (choice.toLowerCase().contains("kiwi")) {
					choiceName = "kiwi";
					break;
				} else if (choice.toLowerCase().contains("banana")) {
					choiceName = "banana";
					break;
				} else if (choice.toLowerCase().contains("green")) {
					choiceName = "green";
					break;
				} else {
					System.out.println("Please choose one of the following.");
				}
			}
			String recipe = "";
			if (choiceName.equals("protein")) {
				if((ingredQuants.get("protein") != 0) && (ingredQuants.get("strawberry") != 0) && (ingredQuants.get("banana") != 0)){
				recipe = printRecipeInstruction(ingredients.get("Protein Power"));
				} else {
					System.out.println("Insufficient quantities to make that smoothie.");
				}
			} else if (choiceName.equals("kiwi")) {
				if((ingredQuants.get("kiwi") != 0) && (ingredQuants.get("strawberry") != 0)){
				recipe = printRecipeInstruction(ingredients.get("Kiwi Strawberry"));
				} else {
					System.out.println("Insufficient quantities to make that smoothie.");
				}
			} else if (choiceName.equals("banana")) {
				if((ingredQuants.get("banana") != 0) && (ingredQuants.get("strawberry") != 0)){
				recipe = printRecipeInstruction(ingredients.get("Strawberry Banana"));
				} else {
					System.out.println("Insufficient quantities to make that smoothie.");
				}
			} else if (choiceName.equals("green")) {
				if((ingredQuants.get("spinach") != 0) && (ingredQuants.get("blueberry") != 0) && (ingredQuants.get("strawberry") != 0)){
				recipe = printRecipeInstruction(ingredients.get("Green Blast"));
				} else {
					System.out.println("Insufficient quantities to make that smoothie.");
				}
			}

			return recipe;
		
	}

	public String printRecipeInstruction(ArrayList<Food> ingredList) {
		StringBuilder s = new StringBuilder();
		s.append("Recipe Instructions:");
		for (Food ingred : ingredList) {
			ingredQuants.put(ingred.toString(), ingredQuants.get(ingred.toString()) - 1);
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
		s.append("\n Add ice to the blender and start blending!");
		return s.toString();
	}

	public String printRecipes(HashMap<String, ArrayList<Food>> recipeMap, HashMap<String, BigDecimal> prices) {
		StringBuilder s = new StringBuilder();
		for (String key : recipeMap.keySet()) {
			System.out.println(key + ": " + recipeMap.get(key) + " $" + prices.get(key));
			s.append(key).append(": ").append(recipeMap.get(key)).append(" $").append(prices.get(key));
		}
		return s.toString();

	}

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

	public Food getIngredientParams(String ingredient) {
		ingredient = ingredient.toLowerCase();
		Food food = new Ingredient(ingredient, 1, false);
		return food;
	}

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


}
