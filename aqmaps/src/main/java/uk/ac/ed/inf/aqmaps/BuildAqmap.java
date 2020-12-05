package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;

public class BuildAqmap {
	
	protected static String buildMap(List<Sensor> sensors, Point currentPosition) throws InterruptedException {
		var endPosition = currentPosition;
		var points = new ArrayList<Point>();
		var sensorsNotVisited = new ArrayList<Sensor>();
		var sensorsVisited = new ArrayList<Sensor>();		
		
		sensorsNotVisited.addAll(sensors);
		points.add(currentPosition);

		while (!sensorsNotVisited.isEmpty()) {
			var closestSensor = Utils.getClosestSensorToPoint(sensorsNotVisited, currentPosition);
			var pointOfClosestSensor = closestSensor.getPointFromSensor();
			sensorsNotVisited.remove(closestSensor);
			points.add(pointOfClosestSensor);
			sensorsVisited.add(closestSensor);
			// move drone to position
			currentPosition = pointOfClosestSensor;
			
		}
		points.add(endPosition);
		
		var features = new ArrayList<Feature>();
		for (Sensor sensor : sensorsVisited) {
			var f = sensor.getSensorAsFeature();
			features.add(f);
		}


		var pathFromPoints = (Geometry) LineString.fromLngLats(points);
		var lineStringAsFeature = Feature.fromGeometry(pathFromPoints);
		features.add(lineStringAsFeature);
		
		var fc = FeatureCollection.fromFeatures(features);
		var fcString = fc.toJson();
		return fcString;
	}

}
