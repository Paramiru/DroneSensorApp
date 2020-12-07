package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.locationtech.jts.geom.Coordinate;

public class BuildAqmapUtils {

	protected static Location getEndLocation(Location start, Integer angle) {
		// given start location and angle, calculate what the end location will be
		var angleInRadians = Math.toRadians(angle);
		var newLatitude = start.latitude() + Constants.MOVE_LENGTH * Math.sin(angleInRadians);
		var newLongitude = start.longitude() + Constants.MOVE_LENGTH * Math.cos(angleInRadians);
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
		for (NoFlyZone zone : BuildAqmap.noFlyZones) {
			if (line.intersects(zone.getJtsPolygon())) return true;
		}
		return false;
	}
	protected static boolean isGoingToPreviousPosition(Pair<Integer, Location> possibleMove) {
		for (Move previousMove : BuildAqmap.chosenMoves) {
			var possibleEndLocation = possibleMove.getValue1();
			var distanceToPossibleEndLocation = Utils.getDistance(previousMove.getEndLocation(), possibleEndLocation);
			// if end location is too close to a previous move's end 
			// location then do not consider that possible move
			if (distanceToPossibleEndLocation < Constants.EPSILON) return true;
		}
		return false;
	}
	protected static void filterPossibleMoves(Location start, List<Pair<Integer, Location>> possibleMoves) {
		possibleMoves.removeIf(
				pair -> !isInConfinedArea(pair.getValue1())  
				|| doesIntersectWithNoFlyZones(start, pair.getValue1()) 
				);
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
	protected static Move getOptimalMove(Location moveStartLocation, Location nextSensorLocation, Integer moveNumber, List<Pair<Integer, Location>> filteredPossibleMoves) {
		
		Move optimalMove = null;
		var minDistance = Double.MAX_VALUE;
		
		for (Pair<Integer, Location> move : filteredPossibleMoves) {
			var moveEndLocation = move.getValue1();
			var distanceFromMoveToSensor = Utils.getDistance(moveEndLocation, nextSensorLocation);
			
			if (distanceFromMoveToSensor < minDistance) {
				minDistance = distanceFromMoveToSensor;
				optimalMove = new Move(moveStartLocation, moveEndLocation, moveNumber, move.getValue0());
			}
			
		}
		return optimalMove;
	}

}
