package uk.ac.ed.inf.aqmaps;

public class Colour {
	/**
	 * Control flow with else if to determine
	 * in which range the double is found.
	 * 
	 * @param batteryLevel float
	 * @return corresponding colour as a String 
	 */
	public static String get_colour(float batteryLevel) {
		if (batteryLevel < 10) return "#000000";
		if (batteryLevel < 32) {
			return "#00ff00";
		} else if (batteryLevel < 64) {
			return "#40ff00";
		} else if (batteryLevel < 96) {
			return "#80ff00";
		} else if (batteryLevel < 128) {
			return "#c0ff00";
		} else if (batteryLevel < 160) {
			return "#ffc000";
		} else if (batteryLevel < 192) {
			return "#ff8000";
		} else if (batteryLevel < 224) {
			return "#ff4000";
		} else {
			return "#ff0000";
		}
	}
}
