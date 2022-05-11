package input;

import java.util.ArrayList;

import com.badlogic.gdx.InputProcessor;

//An InputManager that implements the InputProcessor from the libGDX library. This class is reading input from
//the user
public class InputManager implements InputProcessor{
	
	private Keys[] keys = new Keys[256];
	
	//When the InputManager is updated from the main loop, the keys inside the ArrayList is updated in order to falsefie the "pressedDown"
	private ArrayList<Keys> processDown = new ArrayList<Keys>();
	//When the InputManager is updated from the main loop, the keys inside the ArrayList is updated in order to falsefie the "released"
	private ArrayList<Keys> processUp = new ArrayList<Keys>();
	
	//Position of mouse on Window
	public int mouseX, mouseY;
	
	//Constructor for InputManager. Map the key codes with Keys correctly
	public InputManager() {
		for (var key : Keys.values()) {
			keys[key.code] = key; 
		}
	}

	@Override
	//Is called whenever a key is pressed. 
	public boolean keyDown(int keycode) {
		try {
			keys[keycode].pressed = true;
			keys[keycode].down = true;
			
			processDown.add(keys[keycode]);
		}
		catch (Exception e) {
			System.out.println("Key not is not supported");
		}
		return false;
	}

	@Override
	//Is called whenever a key is released.
	public boolean keyUp(int keycode) {
		try {
			keys[keycode].pressed = false;
			keys[keycode].up = true;
			
			processUp.add(keys[keycode]);
		}
		catch (Exception e) {
			System.out.println("Key is not supported");
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	//Is called when a mouse button is pressed.
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		try {
			keys[button].pressed = true;
			keys[button].down = true;
			
			processUp.add(keys[button]);
		}
		catch (Exception e) {
			System.out.println("Key is not supported");
		}
		return false;
	}

	@Override
	//Is called when a mouse button is released.
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		try {
			keys[button].pressed = false;
			keys[button].up = true;
			
			processUp.add(keys[button]);
		}
		catch (Exception e) {
			System.out.println("Key is not supported");
		}
		return false;
	}

	@Override
	//When mouse is moving and mouse button is pressed
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		Mouse.setX(screenX);
		Mouse.setY(screenY);
		return false;
	}

	@Override
	//When mouse is moving
	public boolean mouseMoved(int screenX, int screenY) {
		Mouse.setX(screenX);
		Mouse.setY(screenY);
		//System.out.println("Mouse: " + screenX + " " + screenY);
		return false;
	}

	@Override
	//Called when a scroll happens (scroll wheel for instance)
	public boolean scrolled(float amountX, float amountY) {
		Mouse.scrollUp = amountY > 0;
		Mouse.scrollDown = amountY < 0;
		return false;
	}
	
	//Updates the state of the keys. 
	public void update() {
		Mouse.scrollUp = false;
		Mouse.scrollDown = false;
		for (var key: processDown) {
			key.down = false;
		}
		processDown.clear();
		
		for (var key: processUp) {
			key.up = false;
		}
		processUp.clear();
	}
}


