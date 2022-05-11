package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import input.InputManager;
import main.Handler;
import world.World;

//This class is the State where the user interacts with the world.
public class GameState extends State{
	
	//State for the Game
	private World world;
	
	public GameState(Handler handler) {
		super(handler);
		world = new World(250, 250);
		handler.setWorld(world);
	}
	
	@Override
	public void update(InputManager input) {
		world.update(input);
	}

	@Override
	public void render(ShapeRenderer sr) {
		world.render(sr);
	}

	@Override
	public void renderUi(SpriteBatch batch) {
		world.renderUi(batch);
	}
}
