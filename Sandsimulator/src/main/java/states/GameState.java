package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import input.InputManager;
import main.GameCamera;
import main.Handler;
import world.World;

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
	public void render(ShapeRenderer sr, GameCamera camera) {
		world.render(sr, camera);
	}

	@Override
	public void renderUi(SpriteBatch batch, GameCamera camera) {
		world.renderUi(batch, camera);
	}
}
