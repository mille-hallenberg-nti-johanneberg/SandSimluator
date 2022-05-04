package cell.recipes;

import cell.CellType;

public class CellRecipe {
	//A CellRecipe is a recipe that takes two ingredients (CellTypes) and decides what those are turned into (CellTypes) 
	
	//The Ingredients needed in order to fulfill the "reaction"
	public final CellType firstIngredient;
	public final CellType secondIngredient;
	
	//The new CellTypes after a "reaction" has happened
	public final CellType firstResult;
	public final CellType secondResult;
	
	//The chance each frame that the "reaction" will happen. 
	public final float reactionChance;
	
	//Constructor for a CellRecipe. Takes two ingredients (CellTypes) and two results (CellTypes)
	CellRecipe(CellType firstIngredient, CellType secondIngredient, CellType firstResult, CellType secondResult, float reactionChance){
		this.firstIngredient = firstIngredient;		//First ingredient required to run the reaction
		this.secondIngredient = secondIngredient;	//Second ingredient required to run the reaction
		
		this.firstResult = firstResult;				//What the first ingredient will turn into after the reaction
		this.secondResult = secondResult;			//What the second ingredient will turn into after the reaction
		
		this.reactionChance = reactionChance;		//The chance of the reaction to happen each frame
	}
}
