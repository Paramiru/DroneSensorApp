package uk.ac.ed.inf.aqmaps;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;

public class Sensor {
	// location --> What3Words encoding
	private String location;
	// 0.0 <= battery <= 100.0
	private float battery;
	// 0.0 <= reading <= 255.0
	private String reading;
	
	protected Feature getSensorAsFeature() throws InterruptedException {
		var point = ServerRequest.getWordsAddress(location).getCoordsAsPoint();
		var feature = Feature.fromGeometry((Geometry) point);
		feature.addStringProperty("marker-size", "medium");
		feature.addStringProperty("location", this.location);
		var colour = Colour.get_colour(this.battery, Float.parseFloat(this.reading));
		feature.addStringProperty("rgb-string", colour);
		feature.addStringProperty("marker-color", colour);
//		TODO
//		feature.addStringProperty("marker-symbol", value);
		
		return feature;
	}
	

	
	@Override
	public String toString() {
		return "Sensor at \t" + location + "\twith battery\t" 
				+ battery + "\tand reading\t" + reading;
	}
	
}
