package uk.ac.ed.inf.aqmaps;

import com.mapbox.geojson.Point;

public class Utils {

	private static <T extends Number> double getSquare(T n) {
		return n.doubleValue() * n.doubleValue();
	}
	
	protected static <T extends Number> double getDistance(Point p1, Point p2) {
		var x1 = p1.latitude();
		var y1 = p1.longitude();
		var x2 = p2.latitude();
		var y2 = p2.longitude();
	
		return Math.sqrt(getSquare(x1-x2) + getSquare(y1-y2));
	}
}
