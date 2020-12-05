package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

public class App {
	
	public static void main(String[] args) throws InterruptedException {
		var server = new ServerRequest(args);
//		var noFlyZones = server.getNoFlyZones();
		var sensors = server.getSensors();
		var features = new ArrayList<Feature>();
		for (Sensor sensor : sensors) {
			features.add(sensor.getSensorAsFeature());
		}
		var featureCollection = FeatureCollection.fromFeatures(features);
		var jsonString = featureCollection.toJson();
		System.out.println(jsonString);

	}
}
