package Smoothie;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Machine {
	public Machine() {

	}

	public ArrayList<String> getIngredients() {
		ArrayList<String> ingredients = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("src/main/resources/ingredients.csv"));
			String line;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ingredients;
	}

	public HashMap<String, ArrayList<Food>> loadRecipes() {
		final File recipeFile = new File("src/main/resources/recipes.csv");
		HashMap<String, ArrayList<Food>> recipeMap = new HashMap<String, ArrayList<Food>>();
		final InputStream recipeStream;
		try {
			recipeStream = new FileInputStream(recipeFile);

		} catch (FileNotFoundException e) {
			System.out.println("Couldn't fine the file: " + recipeFile.getAbsolutePath());
			return recipeMap;
		}

		try (Scanner input = new Scanner(recipeStream)) {
			while (input.hasNextLine()) {
				String[] items = input.nextLine().split(",");
				ArrayList<Food> recipes = new ArrayList<>();
				for (int i = 1; i < items.length; i++) {
					recipes.add(getIngredientParams(items[i]));
				}
				recipeMap.put(items[0], recipes);
			}
		}
		return recipeMap;
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
			s.close();
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
			s.close();
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
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
		return others;
	}

}
