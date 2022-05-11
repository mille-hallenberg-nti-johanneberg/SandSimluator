package input;

//This class is just so I can easily access the information from the mouse
public class Mouse {
	//Position of mouse on Window
	private static int x;
	private static int y;
	
	//Scroll values
	public static boolean scrollUp;
	public static boolean scrollDown;
	
	//Getters and setters
	protected static void setX(int x) {
		Mouse.x = x;
	}
	protected static void setY(int y) {
		Mouse.y = y;
	}
	
	public static int getX() {
		return x;
	}
	public static int getY() {
		return y;
	}
}
