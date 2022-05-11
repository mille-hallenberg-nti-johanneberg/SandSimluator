package cell.recipes;

import java.util.ArrayList;
import java.util.Arrays;

import cell.CellType;

public class CellRecipes {
	//This class is a collection of different CellRecipes but also contains functions that loads the recipes and gets related recipes.
	
	//Just create an ArrayList full of recipes
	@SuppressWarnings("serial")
	static final ArrayList<CellRecipe> recipes = new ArrayList<CellRecipe>() {{
		add(new CellRecipe(CellType.WATER, CellType.SALT, CellType.SALT_WATER, null, 1f));
		add(new CellRecipe(CellType.WATER, CellType.SOIL, null, CellType.MUD, 2f));		
		add(new CellRecipe(CellType.WATER, CellType.LAVA, CellType.STEAM, CellType.ROCK, 95f));		
		add(new CellRecipe(CellType.STEAM, CellType.STEAM, CellType.WATER, null, 2f));		
	}};
	
	//Returns recipes containing the parameter, which means that it returns all recipes that contains the parameter.
	public static ArrayList<CellRecipe> getRelevantRecipies(CellType cellType){
		var relevantRecipes = new ArrayList<CellRecipe>();
		
		for (var recipe : recipes) {	
			if (cellType == recipe.firstIngredient) {
				relevantRecipes.add(recipe);
			}
	      }
		
		return relevantRecipes;
	}
	
	//Returns the recipe that matches both parameters. Makes it possible to see what the result should be for instance.
	public static CellRecipe getMatchingRecipe(CellType reactant1, CellType reactant2) {
		if (reactant1 == null || reactant2 == null) return null;
		
		for (var recipe : reactant1.getRecipes()) {
			if (reactant2 == recipe.secondIngredient) {
				return recipe;
			}
		}
		
		return null;
	}
	
	//Loads all CellRecipe(s) (called when Application is starting)
	public static void create() {
		//Hugo Blommaskog hjälpte mig med detta
		Arrays.asList(CellType.values())
		.stream()
		.forEach(cellType -> cellType.setRecipes(getRelevantRecipies(cellType)));
	}
	
}