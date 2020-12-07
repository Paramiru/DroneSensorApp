package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.locationtech.jts.geom.GeometryFactory;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;


public class Utils {

	protected static final GeometryFactory geometryFactory = new GeometryFactory();

	/**
	 * Returns the square of a given number n
	 * @param n number to be squared
	 * @return  square of number n
	 */
	private static <T extends Number> double getSquare(T n) {
		return n.doubleValue() * n.doubleValue();
	}
	
	/**
	 * Returns the Euclidean distance between two
	 * points p1 and p2 defined as instances of 
	 * our Location class.
	 * 
	 * @param p1 location 1
	 * @param p2 location 2
	 * @return   euclidean distance between location
	 * 			 1 and location 2
	 */
	protected static <T extends Number> double getDistance(Location p1, Location p2) {
		if (p1 == null || p1 == null) throw new IllegalArgumentException("Points must be not null");
		var x1 = p1.latitude();
		var y1 = p1.longitude();
		var x2 = p2.latitude();
		var y2 = p2.longitude();
		return Math.sqrt(getSquare(x1-x2) + getSquare(y1-y2));
	}

	/**
	 * Returns a list of features made up of the features
	 * corresponding to every sensor (given as an instance
	 * of Sensor class ) which is going to be visited by 
	 * the drone and the feature corresponding to the path
	 * made by the drone given as a LineString object.
	 * 
	 * @param sensors     list of sensors to be visited by
	 *                    drone 
	 * @param lineString  LineString object corresponding
	 *  				  to the path performed by the drone
	 * @return            list of features corresponding to 
	 * 					  the sensors and lineString.
	 * @throws InterruptedException
	 */
	protected static List<Feature> createFeaturesList(List<Sensor> sensors, LineString lineString) throws InterruptedException {
		var features = new ArrayList<Feature>();
		for (Sensor sensor : sensors) {
			features.add(sensor.getSensorAsFeature());
		}
		var line = Feature.fromGeometry((Geometry) lineString);
		features.add(line);
		return features;
	}
	
	/**
	 * Returns list of sensors in greedy order given a starting
	 * position. That is, it taked the closest sensor to the 
	 * location, then the closest sensor to that location and
	 * so on until there are no more sensors.
	 * 
	 * @param sensors          list of sensors as instances of 
	 * 	                       the Sensor class to be visited
	 * @param currentLocation  location from which the drone starts
	 * @return                 Queue with the sensors to visit 
	 * 						   in order
	 * @throws InterruptedException
	 */
	protected static Queue<Sensor> getSensorsToVisitInOrder(List<Sensor> sensors, Location currentLocation) throws InterruptedException {
		var sensorsInOrder = new LinkedList<Sensor>();
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


