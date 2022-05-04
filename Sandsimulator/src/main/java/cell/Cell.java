package cell;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;

public class Cell {
	//A Cell is a particle in an environment with properties. A Cell has a CellType telling it what properties it got.
	
	//Unique properties for a Cell.
	private CellType type;
	private Color color;
	
	//Keeps track of which frame it was updated. A Cell cannot be updated twice each iteration!
	public int frameUpdated;
	
	public boolean isBurning;
	
	//Constructor (Takes CellType)
	public Cell(CellType type){
		Random rand = new Random();
		
		this.setType(type);
		this.setColor(type.getColor(rand.nextFloat()));
	}

	//Getters and setters
	public CellType getType() {
		return type;
	}

	public void setType(CellType type) {
		this.type = type;
	}

	public Color getColor() {
		return this.color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}