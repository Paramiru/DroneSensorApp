package uk.ac.ed.inf.aqmaps;

import com.mapbox.geojson.Point;

public class Constants {

	protected static final Point TOP_LEFT = Point.fromLngLat(-3.192473, 55.946233);
	protected static final Point TOP_RIGHT = Point.fromLngLat(-3.184319, 55.946233);
	protected static final Point BOTTOM_LEFT = Point.fromLngLat(-3.192473, 55.942617);
	protected static final Point BOTTOM_RIGHT = Point.fromLngLat(-3.184319, 55.942617);
	protected static final String SERVER = "http://localhost:";
	protected static final String NULL_READING = "-1";
	protected static final double MOVE_LENGTH = 0.0003;
	protected static final int NUMBER_OF_SENSORS = 33;
	protected static final int MAX_MOVES = 150;
	


}
