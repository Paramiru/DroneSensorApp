package uk.ac.ed.inf.aqmaps;

public class Marker {
	/**
	 * Control flow with else if to determine
	 * in which range the double is found.
	 * 
	 * @param batteryLevel float
	 * @return corresponding colour as a String 
	 */
	protected static String get_colour(float batteryLevel, float reading) {
		// Do not consider sensor's reading if battery < 10%
		if (batteryLevel < 10) return "#000000";
		if (reading < 32) {
			return "#00ff00";
		} else if (reading < 64) {
			return "#40ff00";
		} else if (reading < 96) {
			return "#80ff00";
		} else if (reading < 128) {
			return "#c0ff00";
		} else if (reading < 160) {
			return "#ffc000";
		} else if (reading < 192) {
			return "#ff8000";
		} else if (reading < 224) {
			return "#ff4000";
		} else {
			return "#ff0000";
		}
	}
	
	protected static String getSymbol(float batteryLevel, float reading) {
		if (batteryLevel < 10) return "cross";
		if (reading < 128) return "lighthouse";
		// 128 <= reading < 256 so return "danger"
		return "danger";
	}
	
}
