package world;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

import cell.Cell;
import cell.CellPhase;
import cell.CellType;
import cell.recipes.CellRecipe;
import cell.recipes.CellRecipes;
import cell.rules.CellRule;
import math.Chance;

public class CellUpdater {
	// return (rightCol != null) ? rightCol : getLeftCollision();
	private static int width = 0;
	private static int height = 0;

	public static Cell[][] cellPhysics(Cell[][] map, Point cellLocation) {
		// Get width and height
		width = map[0].length;
		height = map.length;

		// Get the cell that is to be moved
		Cell cell = getCell(map, cellLocation.x, cellLocation.y);
		int x = cellLocation.x, y = cellLocation.y;
		// System.out.println(cell);
		if (cell == null)
			return map;

		CellRule rules = cell.getType().getRules();

		// No need to update physics if static
		if (rules.isStatic())
			return map;

		// RNG
		Random rand = new Random();
		int randomDir = rand.nextInt(2);
		float randomMove = rand.nextFloat() * 100f;
		if (Chance.inRange(rand.nextFloat() * 100f, cell.getType().getDeathChance()))
			return setCell(map, x, y, null);

		//Try dispersing liquids
		int dir = -1;
		if (randomDir == 1) dir = 1;
		
		if (!tryMove(map, x, y, x, y + rules.gravity()) && rules.gravity() != 0) {
			for (int spread = 1; spread < rules.spreadFactor(); spread++) {
				//Dispersing failed
				if (!tryMove(map, x, y, x + spread * dir, y)) break;
				
				//Try moving down
				if (tryMove(map, x, y, x + spread * dir, y + rules.gravity()) && rules.gravity() != 0) {
					return switchPlace(map, x, y, x + spread * dir, y + rules.gravity());
				}
			}
			
		}
		
		// Gravity
		if (tryMove(map, x, y, x, y + rules.gravity()) && rules.gravity() != 0) {
			return switchPlace(map, x, y, x, y + rules.gravity());
		}

		// Move diagonally
		if (randomMove <= rules.moveChance() && rules.moveDiagonally()) {
			switch (randomDir) {
			case 0:
				// Move diagonally right
				if (tryMove(map, x, y, x + 1, y + rules.gravity()) && tryMove(map, x, y, x + 1, y))
					return switchPlace(map, x, y, x + 1, y + rules.gravity());

				// Move diagonally left
				if (tryMove(map, x, y, x - 1, y + rules.gravity()) && tryMove(map, x, y, x - 1, y))
					return switchPlace(map, x, y, x - 1, y + rules.gravity());
				break;

			case 1:
				// Move diagonally left
				if (tryMove(map, x, y, x - 1, y + rules.gravity()) && tryMove(map, x, y, x - 1, y))
					return switchPlace(map, x, y, x - 1, y + rules.gravity());

				// Move diagonally right
				if (tryMove(map, x, y, x + 1, y + rules.gravity()) && tryMove(map, x, y, x + 1, y))
					return switchPlace(map, x, y, x + 1, y + rules.gravity());
				break;
			}
		}

		// Spread left and right
		if (randomMove <= rules.moveChance() && rules.moveHorizontally()) {
			if (rules.spreadFactor() != -1) {
				switch (randomDir) {
				case 0:
					// Move right
					if (tryMove(map, x, y, x + 1, y))
						return switchPlace(map, x, y, x + 1, y);

					// Move left
					if (tryMove(map, x, y, x - 1, y))
						return switchPlace(map, x, y, x - 1, y);
					break;

				case 1:
					// Move left
					if (tryMove(map, x, y, x - 1, y))
						return switchPlace(map, x, y, x - 1, y);

					// Move right
					if (tryMove(map, x, y, x + 1, y))
						return switchPlace(map, x, y, x + 1, y);
					break;
				}
			}
		}

		// Returns the location where the cell should be moved.
		return map;
	}

	private static final ArrayList<Point> reactionPoints = new ArrayList<>() {
		{
			add(new Point(0, 1));
			add(new Point(1, 0));
			add(new Point(0, -1));
			add(new Point(-1, 0));
		}
	};

	// Reactions, such as WATER + SALT = SALT_WATER
	static Cell[][] reaction(Cell[][] map, Point mainCell) {
		Cell cell = getCell(map, mainCell.x, mainCell.y);
		if (cell == null)
			return map;

		Random rand = new Random();

		for (var point : reactionPoints) {
			Cell neighbourCell = getCell(map, mainCell.x + point.x, mainCell.y + point.y);
			if (neighbourCell == null)
				continue;

			CellRecipe recipe = CellRecipes.getMatchingRecipe(cell.getType(), neighbourCell.getType());

			if (recipe != null) {
				float reactionChance = rand.nextFloat() * 100.0f;

				if (reactionChance < recipe.reactionChance) {
					if (recipe.firstResult != null) {
						setCell(map, mainCell.x, mainCell.y, new Cell(recipe.firstResult));
					} else
						setCell(map, mainCell.x, mainCell.y, null);

					if (recipe.secondResult != null) {
						setCell(map, mainCell.x + point.x, mainCell.y + point.y, new Cell(recipe.secondResult));
					} else
						setCell(map, mainCell.x + point.x, mainCell.y + point.y, null);
				}
			}
		}

		// Nothing happened
		return map;
	}

	// Collision
	static boolean collision(Cell[][] map, int x, int y) {
		// Collision if outside bounds
		if (bounds(x, y)) {
			return true;
		}

		// Collision at every cell
		if (map[y][x] != null) {
			return true;
		}

		return false;
	}

	static boolean bounds(int x, int y) {
		// Collision if outside bounds
		return (x < 0 || x >= width || y < 0 || y >= height);
	}

	// Tells a cell if it can move, taking density into account.
	static boolean tryMove(Cell[][] map, int x1, int y1, int x2, int y2) {
		// If outside boundaries, return false
		if (bounds(x2, y2))
			return false;

		Random rand = new Random();

		// Get relevant cells
		Cell firstCell = getCell(map, x1, y1);
		Cell secondCell = getCell(map, x2, y2);

		// If there is nothing, just move.
		if (secondCell == null)
			return true;

		// Get their phase
		CellPhase firstPhase = firstCell.getType().getPhase();
		CellPhase secondPhase = secondCell.getType().getPhase();

		// Get their density
		float firstDensity = firstCell.getType().getDensity();
		float secondDensity = secondCell.getType().getDensity();

		// If it is a solid moving through gas, move without thinking.
		if (firstPhase == CellPhase.SOLID && secondPhase == CellPhase.GAS) {
			return true;
		}

		// If it is a liquid moving through gas, move without thinking.
		if (firstPhase == CellPhase.LIQUID && secondPhase == CellPhase.GAS) {
			return true;
		}

		// Solid moving through liquid)
		if (firstPhase == CellPhase.SOLID && secondPhase == CellPhase.LIQUID) {
			// A formula that says the chance between 0 and 1
			float workingDensity = firstDensity - secondDensity;

			// If the water's density is larger, then stay put.
			if (workingDensity <= 0)
				return false;

			// Returns true if the RNG says it to.
			return rand.nextFloat() < (workingDensity / 100f);
		}

		// Solid moving through liquid)
		if (firstPhase == CellPhase.SOLID && secondPhase == CellPhase.LIQUID) {
			// A formula that says the chance between 0 and 1
			float workingDensity = firstDensity - secondDensity;

			// If the water's density is larger, then stay put.
			if (workingDensity <= 0)
				return false;

			// Returns true if the RNG says it to.
			return rand.nextFloat() < (workingDensity / 100f);
		}

		// Liquid moving through liquid)
		if (firstPhase == CellPhase.LIQUID && secondPhase == CellPhase.SOLID) {
			// A formula that says the chance between 0 and 1
			float workingDensity = firstDensity - secondDensity;

			// If the water's density is larger, then stay put.
			if (workingDensity <= 0)
				return false;

			// Returns true if the RNG says it to.
			return rand.nextFloat() < (workingDensity / 100f);
		}

		// Liquid moving through liquid)
		if (firstPhase == CellPhase.LIQUID && secondPhase == CellPhase.LIQUID) {
			// A formula that says the chance between 0 and 1
			float workingDensity = firstDensity - secondDensity;

			// If the water's density is larger, then stay put.
			if (workingDensity <= 0)
				return false;

			// Returns true if the RNG says it to.
			return rand.nextFloat() < (workingDensity / 100f);
		}

		// If it is another solid, what should happen? DON'T MOVE (UNTIL BETTER
		// SOLUTION)
		// if (firstPhase == CellPhase.SOLID && secondPhase == CellPhase.SOLID) return
		// false;

		// Returns false if:
		/*
		 * Solid moves through solid Liquid moves through solid (todo) no
		 */
		return false;
	}

	// Get cell at X, Y
	static Cell getCell(Cell[][] map, int x, int y) {
		if (bounds(x, y))
			return null;

		return map[y][x];
	}

	static Cell[][] setCell(Cell[][] map, int x, int y, Cell newCell) {
		if (bounds(x, y)) {
			return map;
		}

		map[y][x] = newCell;

		return map;
	}

	static Cell[][] moveCell(Cell[][] map, int fromX, int fromY, int toX, int toY) {
		if (bounds(toX, toY)) {
			return map;
		}

		map[toY][toX] = map[fromY][fromX];
		map[fromY][fromX] = null;

		return map;
	}

	static Cell[][] switchPlace(Cell[][] map, int x1, int y1, int x2, int y2) {
		/*
		 * if (x1 < 0 || x1 >= width || y1 < 0 || y1 >= height) return map; if (x2 < 0
		 * || x2 >= width || y2 < 0 || y2 >= height) return map;
		 */
		if (bounds(x1, y2) || bounds(x2, y2))
			return map;

		Cell temp = map[y1][x1];
		map[y1][x1] = map[y2][x2];
		map[y2][x2] = temp;

		return map;
	}

	static Cell[][] fill(Cell[][] map, int originX, int originY, int width, CellType type) {
		for (int y = originY - (int) Math.floor(width / 2.0f); y < Math.ceil(originY + width / 2.0); y++) {
			for (int x = originX - (int) Math.floor(width / 2.0f); x < Math.ceil(originX + width / 2.0); x++) {
				if (type == null)
					map = CellUpdater.setCell(map, x, y, null);
				else if (CellUpdater.getCell(map, x, y) == null)
					map = CellUpdater.setCell(map, x, y, new Cell(type));
			}
		}
		return map;
	}

	public static Point cellMove(Cell[][] map, Point cellLocation) {
		Random rand = new Random();
		int randomDir = rand.nextInt(2);
		float randomMove = rand.nextFloat() * 100f;

		// Get width and height
		width = map[0].length;
		height = map.length;

		// Get the cell that is to be moved
		Cell cell = getCell(map, cellLocation.x, cellLocation.y);
		if (cell == null)
			return cellLocation;
		CellRule rules = cell.getType().getRules();

		// RULES

		// Static
		if (rules.isStatic())
			return cellLocation;

		// Gravity
		if (!collision(map, cellLocation.x, cellLocation.y + rules.gravity()) && rules.gravity() != 0)
			return new Point(cellLocation.x, cellLocation.y + rules.gravity());

		// Move diagonally
		if (randomMove <= rules.moveChance()) {
			switch (randomDir) {
			case 0:
				// Move diagonally right
				if (!collision(map, cellLocation.x + 1, cellLocation.y + rules.gravity())
						&& !collision(map, cellLocation.x + 1, cellLocation.y))
					return new Point(cellLocation.x + 1, cellLocation.y + rules.gravity());

				// Move diagonally left
				if (!collision(map, cellLocation.x - 1, cellLocation.y + rules.gravity())
						&& !collision(map, cellLocation.x - 1, cellLocation.y))
					return new Point(cellLocation.x - 1, cellLocation.y + rules.gravity());
				break;

			case 1:
				// Move diagonally left
				if (!collision(map, cellLocation.x - 1, cellLocation.y + rules.gravity())
						&& !collision(map, cellLocation.x - 1, cellLocation.y))
					return new Point(cellLocation.x - 1, cellLocation.y + rules.gravity());

				// Move diagonally right
				if (!collision(map, cellLocation.x + 1, cellLocation.y + rules.gravity())
						&& !collision(map, cellLocation.x + 1, cellLocation.y))
					return new Point(cellLocation.x + 1, cellLocation.y + rules.gravity());
				break;
			}
		}

		// Spread left and right
		if (randomMove <= rules.moveChance()) {
			if (rules.spreadFactor() != -1) {
				switch (randomDir) {
				case 0:
					// Move right
					if (!collision(map, cellLocation.x + 1, cellLocation.y)
							&& !collision(map, cellLocation.x + 1, cellLocation.y))
						return new Point(cellLocation.x + 1, cellLocation.y);

					// Move left
					if (!collision(map, cellLocation.x - 1, cellLocation.y)
							&& !collision(map, cellLocation.x - 1, cellLocation.y))
						return new Point(cellLocation.x - 1, cellLocation.y);
					break;

				case 1:
					if (!collision(map, cellLocation.x - 1, cellLocation.y)
							&& !collision(map, cellLocation.x - 1, cellLocation.y))
						return new Point(cellLocation.x - 1, cellLocation.y);

					// Move diagonally left
					if (!collision(map, cellLocation.x + 1, cellLocation.y)
							&& !collision(map, cellLocation.x + 1, cellLocation.y))
						return new Point(cellLocation.x + 1, cellLocation.y);
					break;
				}
			}
		}

		// Returns the location where the cell should be moved.
		return cellLocation;
	}

}

/*
 * Cell cellUnderMe = getCell(map, cellLocation.x, cellLocation.y - 1); if
 * (cellUnderMe != null) { if(cellUnderMe.getType().getDensity() <
 * cell.getType().getDensity()) { map = switchPlace(map, cellLocation.x,
 * cellLocation.y, cellLocation.x, cellLocation.y - 1); cell = getCell(map,
 * cellLocation.x, cellLocation.y); rules = cell.getType().getRules(); } }
 */

//RULES
/*
 * //Gravity if (!collision(map, cellLocation.x, cellLocation.y +
 * rules.gravity()) && rules.gravity() != 0) { return moveCell(map,
 * cellLocation.x, cellLocation.y, cellLocation.x, cellLocation.y +
 * rules.gravity()); }
 * 
 * //Move diagonally if (randomMove <= rules.moveChance() &&
 * rules.moveDiagonally()) { switch(randomDir) { case 0: //Move diagonally right
 * if (!collision(map, cellLocation.x + 1, cellLocation.y + rules.gravity()) &&
 * !collision(map, cellLocation.x + 1, cellLocation.y)) return moveCell(map,
 * cellLocation.x, cellLocation.y, cellLocation.x + 1, cellLocation.y +
 * rules.gravity());
 * 
 * //Move diagonally left if (!collision(map, cellLocation.x - 1, cellLocation.y
 * + rules.gravity()) && !collision(map, cellLocation.x - 1, cellLocation.y))
 * return moveCell(map, cellLocation.x, cellLocation.y, cellLocation.x - 1,
 * cellLocation.y + rules.gravity()); break;
 * 
 * case 1: //Move diagonally left if (!collision(map, cellLocation.x - 1,
 * cellLocation.y + rules.gravity()) && !collision(map, cellLocation.x - 1,
 * cellLocation.y)) return moveCell(map, cellLocation.x, cellLocation.y,
 * cellLocation.x - 1, cellLocation.y + rules.gravity());
 * 
 * //Move diagonally right if (!collision(map, cellLocation.x + 1,
 * cellLocation.y + rules.gravity()) && !collision(map, cellLocation.x + 1,
 * cellLocation.y)) return moveCell(map, cellLocation.x, cellLocation.y,
 * cellLocation.x + 1, cellLocation.y + rules.gravity()); break; } }
 * 
 * //Spread left and right if (randomMove <= rules.moveChance() &&
 * rules.moveHorizontally()) { if (rules.spreadFactor() != -1) {
 * switch(randomDir) { case 0: //Move right if (!collision(map, cellLocation.x +
 * 1, cellLocation.y)) return moveCell(map, cellLocation.x, cellLocation.y,
 * cellLocation.x + 1, cellLocation.y);
 * 
 * //Move left if (!collision(map, cellLocation.x - 1, cellLocation.y)) return
 * moveCell(map, cellLocation.x, cellLocation.y, cellLocation.x - 1,
 * cellLocation.y); break;
 * 
 * case 1: //Move left if (!collision(map, cellLocation.x - 1, cellLocation.y))
 * return moveCell(map, cellLocation.x, cellLocation.y, cellLocation.x - 1,
 * cellLocation.y);
 * 
 * //Move right if (!collision(map, cellLocation.x + 1, cellLocation.y)) return
 * moveCell(map, cellLocation.x, cellLocation.y, cellLocation.x + 1,
 * cellLocation.y); break; } } }
 */
