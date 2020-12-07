package uk.ac.ed.inf.aqmaps;

public class Marker {
	/**
	 * Returns a String which represents the colour
	 * the sensor will have with the given arguments.
	 * 
	 * This method uses simple control flow to determine the 
	 * range of batteryLevel and give the corresponding colour.
	 * 
	 * @param batteryLevel  level of the sensor's battery
	 * @param reading 		the reading from the sensor as 
	 * 						a String
	 * @return 				colour of the sensor
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
	
	/**
	 * Returns a String which represents the name of the
	 * symbol to use for a Sensor which has the given
	 * batteryLevel and reading.
	 * 
	 * @param batteryLevel	the level of the sensor's battery
	 * @param reading		the reading from the sensor as
	 * @return				the symbol corresponding to the 
	 * 						sensor's attributes
	 */
	protected static String getSymbol(float batteryLevel, String reading) {
		if (batteryLevel < 10) return "cross";
		var readingAsFloat = Float.parseFloat(reading);
		if (readingAsFloat < 128) return "lighthouse";
		return "danger";
	}
	
}
