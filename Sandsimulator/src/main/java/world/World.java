package world;

import java.awt.Point;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;

import cell.Cell;
import cell.CellType;
import input.InputManager;
import input.Keys;
import input.Mouse;
import main.Game;
import main.GameCamera;
import math.OpenSimplexNoise;

public class World {

	private float cellSize = 1f;

	public int width, height;
	public Cell[][] world;

	GameCamera camera;
	ShapeRenderer sr;
	
	//Size of Noise
	private static final double FEATURE_SIZE = 24;

	//World Constructor, takes Width and Height and creates a new Environment
	public World(int width, int height) {
		this.width = width;
		this.height = height;

		world = new Cell[height][width];
		System.out.println(world.length);
		createWorld();
	}

	//Creates an Environment from a seed
	public void createWorld() {
		OpenSimplexNoise noise = new OpenSimplexNoise(235);
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				double noiseValue = noise.eval(x / FEATURE_SIZE, y / FEATURE_SIZE);

				if (noiseValue > 0.8f) {
					world[y][x] = new Cell(CellType.SALT_WATER);
				} else if (noiseValue > 0.6f) {
					world[y][x] = new Cell(CellType.WATER);
				} else if (noiseValue > 0.3) {
					world[y][x] = new Cell(CellType.STATIC);
				} else if (noiseBorder(noiseValue, noise.eval(x / FEATURE_SIZE, (y - 3) / FEATURE_SIZE), 0.3)) {
					world[y][x] = new Cell(CellType.SOIL);
				}
			}
		}

	}
	
	//Support function for createWorld(). Returns if there should be an edge or not.
	boolean noiseBorder(double value1, double value2, double threshold) {
		if (value2 <= threshold)
			return false;
		return Math.ceil(value2 - threshold) - Math.ceil(value1 - threshold) > 0;
	}
	
	//Update values
	int iterations;
	int updateFrame;
	int simSpeed = 1;
	int simFrame = 0;
	
	//Is called from GameState
	public void update(InputManager input) {
		controls(input);
		if (simSpeed != 0) {
			if (simFrame % simSpeed == 0)
				updateWorld();
			simFrame += 1;
			simFrame %= simSpeed;
		}
	}

	//Controls
	int currentCellType = 0;
	int currentControlType = 0;
	int cursorSize = 1;
	
	//Controls for World
	void controls(InputManager input) {

		if (Keys.NUM_1.pressed)
			currentControlType = 0;
		if (Keys.NUM_2.pressed)
			currentControlType = 1;
		if (Keys.NUM_3.pressed)
			currentControlType = 2;

		if (Keys.RIGHT.down)
			currentControlType++;
		if (Keys.LEFT.down)
			currentControlType--;

		currentControlType %= 3;
		if (currentControlType < 0)
			currentControlType = 2;

		if (Keys.UP.down || Mouse.scrollUp) {
			switch (currentControlType) {
			case 0:
				currentCellType -= 1;
				break;

			case 1:
				cursorSize += 1;
				break;

			case 2:
				simSpeed += 1;
				if (simSpeed > 10)
					simSpeed = 10;
				break;
			}
		}

		if (Keys.DOWN.down || Mouse.scrollDown) {
			switch (currentControlType) {
			case 0:
				currentCellType += 1;
				break;

			case 1:
				cursorSize -= 1;
				if (cursorSize < 1)
					cursorSize = 1;
				break;

			case 2:
				simSpeed -= 1;
				if (simSpeed < 0)
					simSpeed = 0;
				break;
			}
		}

		if (currentCellType < 0)
			currentCellType = cellTypeList.length - 1;
		if (currentCellType >= cellTypeList.length)
			currentCellType = 0;

		if (Keys.SPACE.pressed || Keys.MOUSE_RIGHT.pressed) {
			var worldPos = Game.getGameCamera().unproject(new Vector3(Mouse.getX(), Mouse.getY(), 0));
			world = CellUpdater.fill(world, (int) Math.floor(worldPos.x), (int) Math.floor(worldPos.y), cursorSize,
					cellTypeList[(currentCellType + 3) % (cellTypeList.length)]);
		}

		if (Keys.Q.pressed || Keys.MOUSE_LEFT.pressed) {
			var worldPos = Game.getGameCamera().unproject(new Vector3(Mouse.getX(), Mouse.getY(), 0));
			world = CellUpdater.fill(world, (int) Math.floor(worldPos.x), (int) Math.floor(worldPos.y), cursorSize,
					null);
		}
	}
	
	//Iterates the World and updates positions, states and CellTypes
	void updateWorld() {
		//Current frame
		updateFrame++;
		updateFrame %= 2;

		if (iterations < 400) {
			world[220][125] = new Cell(CellType.SALT);
		}

		iterations++;

		//Updates the World in an unorganized order by creating two List<Integer>s containing Integers ranging from 0 to width/height.
		//These lists are scrambled.
		List<Integer> randomY = IntStream.rangeClosed(0, height - 1).boxed().collect(Collectors.toList());
		Collections.shuffle(randomY);
		List<Integer> randomX = IntStream.rangeClosed(0, width - 1).boxed().collect(Collectors.toList());
		Collections.shuffle(randomX);
		
		//Iterates lists and updates every Cell
		for (int iterateY = 0; iterateY < height; iterateY++) {

			for (int iterateX = 0; iterateX < width; iterateX++) {
				int x = randomX.get(iterateX);
				int y = randomY.get(iterateY);

				Cell current = world[y][x];

				// Only continue if there is an available cell AND if it has not been updated
				if (current == null)
					continue;
				if (current.frameUpdated == updateFrame)
					continue;

				//Tell the Cell which frame it was updated. It cannot be updated twice!
				current.frameUpdated = updateFrame;

				//Reactions for current Cell
				world = CellUpdater.reaction(world, new Point(x, y));
				//Move Physics for current Cell
				world = CellUpdater.cellPhysics(world, new Point(x, y));
			}
		}
	}
	
	//Renders world and interface
	public void render(ShapeRenderer sr, GameCamera camera) {
		renderWorld(sr, camera);
		renderOther(sr, camera);
	}

	//Cell "Hierarchy" for the player to choose between Cells.
	final CellType[] cellTypeList = { CellType.STATIC, CellType.SAND, CellType.SOIL, CellType.WATER, CellType.ROCK,
			CellType.SALT, CellType.WATER, CellType.LAVA };

	//Renders user interface
	public void renderUi(SpriteBatch batch, GameCamera camera) {
		String str = "";
		// Render Inventory
		for (int i = 0; i < 7; i++) {
			if (i == 3) {
				Game.font.setColor(Color.WHITE);
			} else {
				Game.font.setColor(Color.GRAY);
			}
			Game.font.draw(batch, cellTypeList[(i + currentCellType) % (cellTypeList.length)].toString(), 8,
					Gdx.graphics.getHeight() - (32 + i * 16));
		}
		
		//Renders the current simulation speed
		Game.font.setColor(Color.BLUE);
		Game.font.draw(batch, str, 8, Gdx.graphics.getHeight() - 16 * 9);
		if (simSpeed == 0)
			Game.font.draw(batch, "SIM SPEED: DON'T SIMULATE", 8, Gdx.graphics.getHeight() - 16 * 10);
		else
			Game.font.draw(batch, "SIM SPEED: EVERY " + simSpeed + " FRAME", 8, Gdx.graphics.getHeight() - 16 * 10);
		
		//Renders the current Controller state
		Game.font.setColor(Color.CYAN);
		str = "CURRENT CONTROLLER: ";
		switch (currentControlType) {
		case 0:
			str += "CELL CHOOSER";
			break;
		case 1:
			str += "CURSOR SIZE";
			break;
		case 2:
			str += "SIM SPEED";
			break;
		}
		
		//Renders Controls
		Game.font.draw(batch, str, 8, Gdx.graphics.getHeight() - 16 * 11);

		Game.font.setColor(Color.YELLOW);
		Game.font.draw(batch, "CONTROLS: ", 8, Gdx.graphics.getHeight() - 16 * 13);

		Game.font.draw(batch, "WASD, ARROWS: 	MOVE CAMERA", 8, Gdx.graphics.getHeight() - 16 * 14);

		Game.font.draw(batch, "UP, DOWN, SCROLL:CHANGE VALUE OF CONTROL TYPE", 8, Gdx.graphics.getHeight() - 16 * 15);
		Game.font.draw(batch, "RIGHT, LEFT: 	SCROLL CONTROL TYPE", 8, Gdx.graphics.getHeight() - 16 * 16);
		Game.font.draw(batch, "1...3: 			CHOOSE CONTROL TYPE", 8, Gdx.graphics.getHeight() - 16 * 17);

		Game.font.draw(batch, "SPACE, LEFT M: 	PLACE CELL", 8, Gdx.graphics.getHeight() - 16 * 18);
		Game.font.draw(batch, "Q, RIGHT M: 		DELETE CELL", 8, Gdx.graphics.getHeight() - 16 * 19);
		
		Game.font.draw(batch, "R: 				RESET WORLD", 8, Gdx.graphics.getHeight() - 16 * 20);

	}

	//Renders the World (Environment, Border)
	void renderWorld(ShapeRenderer sr, GameCamera camera) {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				Cell current = world[y][x];

				if (current == null)
					continue;

				sr.setColor(current.getColor());
				sr.rect(x * cellSize, y * cellSize, cellSize, cellSize);
			}
		}
	}

	//Renders things like Cursor
	void renderOther(ShapeRenderer sr, GameCamera camera) {
		sr.setColor(Color.GREEN);
		sr.set(ShapeType.Line);
		sr.rect(0, 0, width, height);

		var worldPos = Game.getGameCamera().unproject(new Vector3(Mouse.getX(), Mouse.getY(), 0));
		int x = (int) Math.floor(worldPos.x), y = (int) Math.floor(worldPos.y);

		sr.rect(x - cursorSize / 2, y - cursorSize / 2, cursorSize, cursorSize);

		sr.set(ShapeType.Filled);
	}
}
