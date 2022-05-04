package cell;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.Color;

import cell.recipes.CellRecipe;
import cell.rules.CellRule;
import cell.rules.LiquidRule;
import cell.rules.SandRule;
import cell.rules.StaticRule;
import math.ColorGradient;

public enum CellType {
	//A CellType Enum tells a Cell what properties it has. For instance, what Rules it should follow when moving, what colour its
	//default colour is or what phase the Cell has.
	
	//It is of the type Enum because it is a great way to have many a many collections of similar properties with different values.
	
	SAND(CellPhase.SOLID, 25, 0, 0, 0, new ColorGradient(Color.YELLOW, Color.GOLD), new SandRule()),
	
	
	SOIL(CellPhase.SOLID, 10f, 0, 0, 0, new ColorGradient(Color.BROWN, new Color(163 / 255f, 90 / 255f, 0, 1)), new SandRule() {
		@Override
		//When overriding, I override a value in a CellRule instance. For this example I want the soil to have similar 
		//physics like the sand, but I want it to have a lower moveChance.
		public float moveChance(){
			return 20f;
		}
		{}}),
	
	MUD(CellPhase.SOLID, 25f, 0, 0, 0, new ColorGradient(new Color(92/255f, 64/255f, 51/255f, 1), new Color(66 / 255f, 36 / 255f, 7 / 255f, 1)), new SandRule() {
		@Override
		public float moveChance(){
			return 15f;
		}
		{}}),
	
	ROCK(CellPhase.SOLID, 95f, 0, 0, 0, new ColorGradient(Color.GRAY, Color.DARK_GRAY), new SandRule()),
	
	SALT(CellPhase.SOLID, 15f, 0, 0, 0, new ColorGradient(Color.WHITE, Color.LIGHT_GRAY), new SandRule()),
	
	WATER(CellPhase.LIQUID, 10f, 0, 0, 0, new ColorGradient(new Color(0, 0, 1f, 0.7f), new Color(0, 0, 1f, 0.7f)), new LiquidRule()),

	OIL(CellPhase.LIQUID, 5f, 0, 0, 0, new ColorGradient(new Color(92 / 255f, 64 / 255f, 27 / 255f, 0.7f), new Color(92 / 255f, 64 / 255f, 27 / 255f, 0.7f)), new LiquidRule()),
	
	LAVA(CellPhase.LIQUID, 25f, 0, 0, 0, new ColorGradient(Color.RED, Color.RED), new LiquidRule() {
		@Override
		public float moveChance(){
			return 5f;
		}
		
		{}}),
	
	SALT_WATER(CellPhase.LIQUID, 15f, 0, 0, 0, new ColorGradient(Color.ROYAL, Color.ROYAL), new LiquidRule()),
	
	STEAM(CellPhase.GAS, 0.1f, 0.05f, 0, 0, new ColorGradient(new Color(0.7f, 0.7f, 0.7f, 0.5f), new Color(0.8f, 0.8f, 0.8f, 1)), new LiquidRule() {
		@Override
		public int gravity(){
			return 1;
		}
		
		@Override
		public float moveChance(){
			return 50f;
		}
		
		{}}),
	
	STATIC(CellPhase.SOLID, 100f, 0, 0, 0, new ColorGradient(new Color(0.22f, 0.2f, 0.176f, 1), new Color(0.17f, 0.15f, 0.12f, 1)), new StaticRule());
	
	//Properties of a CellType
	private CellPhase phase;
	private float density;
	private float deathChance;
	private float flammabilityChance;
	private float fireConsumeChance;
	private ColorGradient defaultGradient;
	private CellRule rules;
	
	//Recipes related to this CellType 
	private ArrayList<CellRecipe> recipes = new ArrayList<>();
	
	//Constructor
	private CellType(CellPhase phase, float density, float deathChance, float flammabilityChance, float fireConsumeChance, ColorGradient gradient, CellRule rules) {
		if (density > 100f) density = 100f;
		if (density < 0f) density = 0f;
		this.density = density;
		this.flammabilityChance = flammabilityChance;
		this.fireConsumeChance = fireConsumeChance;
		
		this.deathChance = deathChance;
		this.defaultGradient = gradient;
		this.rules = rules;
		this.phase = phase;
	}
	
	//Getters and setters
	public CellPhase getPhase() {
		return phase;
	}
	
	public float getDensity() {
		return density;
	}
	
	public float getDeathChance() {
		return deathChance;
	}
	
	public Color getColor(float point){
		return defaultGradient.getColor(point);
	}

	public CellRule getRules() {
		return rules;
	}
	
	public ArrayList<CellRecipe> getRecipes(){
		return recipes;
	}
	
	public void setRecipes(ArrayList<CellRecipe> recipes) {
		this.recipes = recipes;
	}

	public void setRules(CellRule rules) {
		this.rules = rules;
	}

	public float getFlammabilityChance() {
		return flammabilityChance;
	}

	public float getFireConsumeChance() {
		return fireConsumeChance;
	}
}
