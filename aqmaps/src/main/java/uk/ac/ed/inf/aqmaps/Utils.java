package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import org.locationtech.jts.geom.GeometryFactory;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;


public class Utils {

	protected static final GeometryFactory geometryFactory = new GeometryFactory();

	private static <T extends Number> double getSquare(T n) {
		return n.doubleValue() * n.doubleValue();
	}
	
	protected static <T extends Number> double getDistance(Location p1, Location p2) {
		if (p1 == null || p1 == null) throw new IllegalArgumentException("Points must be not null");
		var x1 = p1.latitude();
		var y1 = p1.longitude();
		var x2 = p2.latitude();
		var y2 = p2.longitude();
		return Math.sqrt(getSquare(x1-x2) + getSquare(y1-y2));
	}
	// TODO add LineString to featureCollection
	protected static String getMapAsJson(List<Sensor> sensors) throws InterruptedException {
		var features = new ArrayList<Feature>();
		for (Sensor sensor : sensors) {
			features.add(sensor.getSensorAsFeature());
		}
		var featureCollection = FeatureCollection.fromFeatures(features);
		var jsonString = featureCollection.toJson();
		return jsonString;
	}
	
	protected static List<Sensor> getSensorsToVisitInOrder(List<Sensor> sensors, Location currentLocation) throws InterruptedException {
		var sensorsInOrder = new ArrayList<Sensor>();
		while (!sensors.isEmpty()) {
			Sensor closestSensor = null;
			int indexOfClosestSensor = -1;
			var minDistance = Double.MAX_VALUE;
			for (int i = 0; i < sensors.size(); i++) {
				var currentSensor = sensors.get(i);
				var locationOfCurrentSensor = currentSensor.getLocationFromSensor();
				var distanceToPosition = Utils.getDistance(currentLocation, locationOfCurrentSensor);
				if (distanceToPosition < minDistance) {
					minDistance = distanceToPosition;
					indexOfClosestSensor = i;
					closestSensor = currentSensor;
				}
			}
			currentLocation = closestSensor.getLocationFromSensor();
			sensorsInOrder.add(closestSensor);
			sensors.remove(indexOfClosestSensor);
		}
		return sensorsInOrder;
	}
	

	
}


