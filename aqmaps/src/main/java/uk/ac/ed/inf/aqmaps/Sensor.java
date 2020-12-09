package uk.ac.ed.inf.aqmaps;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;

public class Sensor {
	
	// location --> What3Words encoding
	protected String location;
	// 0.0 <= battery <= 100.0
	private float battery;
	// 0.0 <= reading <= 255.0
	private String reading;
	
	/**
	 * Returns the location of the sensor using the field 
	 * conntaining the What3Words encoding after parsing it
	 * in order to get the location as an instance of our
	 * Location class
	 * 
	 * @return location representing the sensor's position
	 * @throws InterruptedException
	 */
	protected Location getLocationFromSensor() throws InterruptedException {
		var sensorCoordinates = ServerRequest.getWordsAddress(location).coordinates;
		var lat = sensorCoordinates.lat;
		var lng = sensorCoordinates.lng;
		var location = new Location(lat, lng);
		return location;
	}
	
	/**
	 * Returns the feature corresponding to the instance of
	 * the Sensor class.
	 * 
	 * Adds the five properties (the sensor should have accoding to
	 * the coursework specification) to the corresponding feature
	 * created.
	 * 
	 * @return
	 * @throws InterruptedException
	 */
	protected Feature getSensorAsFeature() throws InterruptedException {
		var colour = Marker.getColour(this.battery, this.reading);
		var symbol = Marker.getSymbol(this.battery, this.reading);
		var location = getLocationFromSensor();
		var geojsonPoint = location.getGeojsonPoint();
		var feature = Feature.fromGeometry((Geometry) geojsonPoint);
		feature.addStringProperty("marker-size", "medium");
		feature.addStringProperty("location", this.location);
		feature.addStringProperty("rgb-string", colour);
		feature.addStringProperty("marker-color", colour);
		feature.addStringProperty("marker-symbol", symbol);
		return feature;
	}
	
	/**
	 * Returns the feature corresponding to the instance of 
	 * the Sensor class if it has not been visited so that it 
	 * has a grey colour and no symbol.
	 * 
	 * @return feature from the Sensor instance from which it
	 * 		   is being called if it has not been visited.
	 * @throws InterruptedException
	 */
	protected Feature getGreySensorAsFeature() throws InterruptedException {
		var colour = "#aaaaaa";
		var location = getLocationFromSensor();
		var geojsonPoint = location.getGeojsonPoint();
		var feature = Feature.fromGeometry((Geometry) geojsonPoint);
		feature.addStringProperty("marker-size", "medium");
		feature.addStringProperty("location", this.location);
		feature.addStringProperty("rgb-string", colour);
		feature.addStringProperty("marker-color", colour);
		return feature;
	}
	
}
