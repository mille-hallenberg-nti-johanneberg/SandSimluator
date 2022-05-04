package input;

import com.badlogic.gdx.Input;

public enum Keys{
	//Contains all Keys that are supported in this project. Contains what key on the keyboard it is related to and different states
	//such as pressed (is the key is pressed), down (moment the key is pressed down), up (moment the key is released)
	SPACE(Input.Keys.SPACE),
	CTRL_L(Input.Keys.CONTROL_LEFT),
	
	UP(Input.Keys.UP),
	DOWN(Input.Keys.DOWN),
	LEFT(Input.Keys.LEFT),
	RIGHT(Input.Keys.RIGHT),
	
	W(Input.Keys.W),
	A(Input.Keys.A),
	S(Input.Keys.S),
	D(Input.Keys.D),
	
	P(Input.Keys.P),
	O(Input.Keys.O),
	R(Input.Keys.R),
	Q(Input.Keys.Q),
	
	NUM_1(Input.Keys.NUM_1),
	NUM_2(Input.Keys.NUM_2),
	NUM_3(Input.Keys.NUM_3),
	NUM_4(Input.Keys.NUM_4),
	NUM_5(Input.Keys.NUM_5),
	NUM_6(Input.Keys.NUM_6),
	NUM_7(Input.Keys.NUM_7),
	NUM_8(Input.Keys.NUM_8),
	NUM_9(Input.Keys.NUM_9),
	NUM_0(Input.Keys.NUM_0),
	
	MOUSE_LEFT(Input.Buttons.LEFT),
	MOUSE_RIGHT(Input.Buttons.RIGHT);
	
	public int code = 0;
	public boolean pressed, down, up;
	
	private Keys(int keyCode) {
		this.code = keyCode;
	}
}