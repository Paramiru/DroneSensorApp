package uk.ac.ed.inf.aqmaps;

public class Marker {
	/**
	 * Control flow with else if to determine
	 * in which range the double is found.
	 * 
	 * @param batteryLevel float
	 * @return corresponding colour as a String 
	 */
	protected static String getColour(float batteryLevel, String reading) {
		// Do not consider sensor's reading if battery < 10%
		if (batteryLevel < 10) return "#000000";
		var readingAsFloat = Float.parseFloat(reading);
		if (readingAsFloat < 32) {
			return "#00ff00";
		} else if (readingAsFloat < 64) {
			return "#40ff00";
		} else if (readingAsFloat < 96) {
			return "#80ff00";
		} else if (readingAsFloat < 128) {
			return "#c0ff00";
		} else if (readingAsFloat < 160) {
			return "#ffc000";
		} else if (readingAsFloat < 192) {
			return "#ff8000";
		} else if (readingAsFloat < 224) {
			return "#ff4000";
		} else {
			return "#ff0000";
		}
	}
	
	protected static String getSymbol(float batteryLevel, String reading) {
		if (batteryLevel < 10) return "cross";
		var readingAsFloat = Float.parseFloat(reading);
		if (readingAsFloat < 128) return "lighthouse";
		// 128 <= reading < 256 so return "danger"
		return "danger";
	}
	
}
