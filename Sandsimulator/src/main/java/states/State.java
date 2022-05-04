package states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import input.InputManager;
import main.GameCamera;
import main.Handler;

public abstract class State {
	
	//A State is the current "Game State". For instance, there is a state for Option Menus, the Game or a Main Menu.
	protected Handler handler;
	private static State currentState = null;
	
	//Getters and setters
	public static void setState(State state) {
		currentState = state;
	}
	
	public static State getState() {
		return currentState;
	}
	
	public State(Handler handler) {
		this.handler = handler;
	}
	
	//Every State that extends this class gets these functions, that are in turned called from the Game class.
	public abstract void update(InputManager input);
	
	public abstract void render(ShapeRenderer sr, GameCamera camera);

	public abstract void renderUi(SpriteBatch batch, GameCamera camera);
}
