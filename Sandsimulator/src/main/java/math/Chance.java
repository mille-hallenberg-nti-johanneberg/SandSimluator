package math;

//Helper class for returning if the value is inside range
public class Chance {
	public static boolean inRange(float floater, float range) {
		return floater <= range && range != 0;
	}
}
