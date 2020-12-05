package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;

public class Utils {

	private static <T extends Number> double getSquare(T n) {
		return n.doubleValue() * n.doubleValue();
	}
	
	protected static <T extends Number> double getDistance(Point p1, Point p2) {
		if (p1 == null || p1 == null) throw new IllegalArgumentException("Points must be not null");
		var x1 = p1.latitude();
		var y1 = p1.longitude();
		var x2 = p2.latitude();
		var y2 = p2.longitude();
	
		return Math.sqrt(getSquare(x1-x2) + getSquare(y1-y2));
	}
	
	protected static String getMapAsJson(List<Sensor> sensors) throws InterruptedException {
		var features = new ArrayList<Feature>();
		for (Sensor sensor : sensors) {
			features.add(sensor.getSensorAsFeature());
		}
		var featureCollection = FeatureCollection.fromFeatures(features);
		var jsonString = featureCollection.toJson();
		return jsonString;
	}
	
	protected static Sensor getClosestSensorToPoint(List<Sensor> sensors, Point currentPosition) throws InterruptedException {
		if (sensors.size() < 1) throw new IllegalArgumentException("List of Sensors must be non-empty!");
		var sensorWithMinDistance = sensors.get(0);
		var sensorWithMinDistanceAsPoint = sensorWithMinDistance.getPointFromSensor();
		var minDistanceToCurrentPosition = getDistance(sensorWithMinDistanceAsPoint, currentPosition);
		for (Sensor sensor : sensors) {
			var newSensorAsPoint = sensor.getPointFromSensor();
			var distanceToCurrentPosition = getDistance(newSensorAsPoint, currentPosition);
			if (distanceToCurrentPosition < minDistanceToCurrentPosition) {
				minDistanceToCurrentPosition = distanceToCurrentPosition;
				sensorWithMinDistance = sensor;
						
			}
		}
		return sensorWithMinDistance;
	}
	
	
	
	
}

