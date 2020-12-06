package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javatuples.Pair;
import org.javatuples.Triplet;
import org.locationtech.jts.geom.Coordinate;


public class BuildAqmap {
	
	protected static ServerRequest server;
	protected static List<NoFlyZone> noFlyZones;
	protected static List<Sensor> sensors;
	//					  Triplet<Angle  , Location, MoveNumber>
	protected static List<Triplet<Integer, Location, Integer>> chosenMoves = new ArrayList<>();

	protected static void buildMap(String[] args) throws InterruptedException {
		server = new ServerRequest(args);
		noFlyZones = server.getNoFlyZones();
		sensors = Utils.getSensorsToVisitInOrder(server.getSensors(), IO.startingPoint);
		var nextSensor = sensors.get(0);
		var nextSensorLocation = nextSensor.getLocationFromSensor();
		var start = IO.startingPoint;
		var possibleMoves = getPossibleMoves(start);
		filterPossibleMoves(start, possibleMoves);
	}
	

	protected static Location getEndLocation(Location start, Integer angle) {
		// given start location and angle, calculate what the end location will be
		var newLatitude = start.latitude() + Constants.MOVE_LENGTH * Math.sin(angle);
		var newLongitude = start.longitude() + Constants.MOVE_LENGTH * Math.cos(angle);
		var endLocation = new Location(newLatitude, newLongitude);
		return endLocation;
	}
	protected static boolean isInConfinedArea(Location location) {
		var point = location.getJtsPoint();
		return point.within(Constants.confinedArea);
	}
	protected static boolean doesIntersectWithNoFlyZones(Location start, Location end) {
		var coordinates = new Coordinate[] {start.getJtsCoordinate(), end.getJtsCoordinate()};
		var line = Utils.geometryFactory.createLineString(coordinates);
		for (NoFlyZone zone : noFlyZones) {
			if (line.intersects(zone.getJtsPolygon())) return true;
		}
		return false;
	}
	protected static List<Pair<Integer, Location>> getPossibleMoves(Location start) {
		var possibleMoves = new ArrayList<Pair<Integer, Location>>();
		for (int angle = 0; angle <= 350; angle += 10) {
			var endLocation = getEndLocation(start, angle); 
			var move = new Pair<Integer, Location>(angle, endLocation);
			possibleMoves.add(move);
		}
		return possibleMoves;
	}
	protected static void filterPossibleMoves(Location start, List<Pair<Integer, Location>> possibleMoves) {
		possibleMoves.removeIf(pair -> !isInConfinedArea(pair.getValue1())  
				|| doesIntersectWithNoFlyZones( start, pair.getValue1() ) );
	}
	
	

}
