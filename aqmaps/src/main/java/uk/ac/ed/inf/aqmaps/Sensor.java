package uk.ac.ed.inf.aqmaps;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.Point;

public class Sensor {
	// location --> What3Words encoding
	protected String location;
	// 0.0 <= battery <= 100.0
	private float battery;
	// 0.0 <= reading <= 255.0
	private String reading;
	
	protected Point getPointFromSensor() throws InterruptedException {
		var point = ServerRequest.getWordsAddress(location).getCoordsAsPoint();
		return point;
	}
	
	protected Feature getSensorAsFeature() throws InterruptedException {
		var colour = Marker.get_colour(this.battery, Float.parseFloat(this.reading));
		var symbol = Marker.getSymbol(this.battery, Float.parseFloat(this.reading));
		var point = getPointFromSensor();
		var feature = Feature.fromGeometry((Geometry) point);
		feature.addStringProperty("marker-size", "medium");
		feature.addStringProperty("location", this.location);
		feature.addStringProperty("rgb-string", colour);
		feature.addStringProperty("marker-color", colour);
		feature.addStringProperty("marker-symbol", symbol);

		return feature;
	}
	
	@Override
	public String toString() {
		return "Sensor at \t" + location;
	}

	
}
