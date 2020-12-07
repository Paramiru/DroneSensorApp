package uk.ac.ed.inf.aqmaps;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;

public class Constants {

	// Class containing the constants being used 
	// during the whole project
	protected static final Coordinate TOP_LEFT = new Coordinate(55.946233, -3.192473);
	protected static final Coordinate TOP_RIGHT = new Coordinate(55.946233, -3.184319);
	protected static final Coordinate BOTTOM_LEFT = new Coordinate(55.942617, -3.192473);
	protected static final Coordinate BOTTOM_RIGHT = new Coordinate(55.942617, -3.184319);
	protected static final String SERVER = "http://localhost:";
	protected static final String NULL_READING = "-1";
	protected static final double MOVE_LENGTH = 0.0003;
	protected static final double SENSOR_DISTANCE = 0.0002;
	protected static final double EPSILON = 0.00001;
	protected static final int NUMBER_OF_SENSORS = 33;
	protected static final int MAX_MOVES = 150;
	protected static final Coordinate[] polygon = new Coordinate[] { Constants.TOP_LEFT, 
			Constants.TOP_RIGHT, Constants.BOTTOM_RIGHT, Constants.BOTTOM_LEFT, Constants.TOP_LEFT };
	protected static final Polygon confinedArea = Utils.geometryFactory.createPolygon(polygon);

}
