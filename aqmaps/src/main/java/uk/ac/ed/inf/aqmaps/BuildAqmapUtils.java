package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import org.javatuples.Pair;
import org.locationtech.jts.geom.Coordinate;

public class BuildAqmapUtils {

	/**
	 * Returns the location in which the drone will be if
	 * it moved with a given direction starting in the 
	 * given location.
	 * 
	 * @param start  location from which we want to get the
	 * 			     end location
	 * @param angle  direction the drone will use to move
	 * @return       location to which the drone will arrive
	 * 			     if it started in the start location and
	 *               moved in the direction given
	 */
	protected static Location getEndLocation(Location start, Integer angle) {
		// given start location and angle, calculate what the end location will be
		var angleInRadians = Math.toRadians(angle);
		var newLatitude = start.latitude() + Constants.MOVE_LENGTH * Math.sin(angleInRadians);
		var newLongitude = start.longitude() + Constants.MOVE_LENGTH * Math.cos(angleInRadians);
		var endLocation = new Location(newLatitude, newLongitude);
		return endLocation;
	}
	
	/**
	 * Checks if a specific point (given as an instance of the 
	 * Location class) can be found inside the confined area
	 * extracted from the WebServer.
	 * 
	 * @param location  location to check
	 * @return          true if location is inside the confined area
	 * 		            false otherwise
	 */
	private static boolean isInConfinedArea(Location location) {
		var point = location.getJtsPoint();
		return point.within(Constants.CONFINED_AREA);
	}
	
	/**
	 * Checks if the line starting at "start" and finishing at 
	 * "end" intersects with any of the no fly zones so that 
	 * we know the drone cannot go from that start location to 
	 * the end location given as arguments.
	 * 
	 * @param start location from which the drone would start its
	 *              move
	 * @param end   location to which the drone will go
	 * @return      true if it intersects. false otherwise
	 */
	private static boolean doesIntersectWithNoFlyZones(Location start, Location end) {
		var coordinates = new Coordinate[] {start.getJtsCoordinate(), end.getJtsCoordinate()};
		var line = Utils.geometryFactory.createLineString(coordinates);
		for (NoFlyZone zone : BuildAqmap.noFlyZones) {
			if (line.intersects(zone.getJtsPolygon())) return true;
		}
		return false;
	}
	
	/**
	 * Checks if the drone is going close to any of the previous
	 * moves it has already performed.
	 * 
	 * @param possibleMove  move being taken into consideration
	 * @return				true if going to a previous position.
	 * 						false otherwise.
	 */
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
	
	/**
	 * Method which filters the possible moves the drone could do
	 * using our other methods to check that those moves do not
	 * intersect any no fly zone and they are inside the confined
	 * area.
	 * 
	 * @param start          start location of the move the drone
	 * 						 would perform.
	 * @param possibleMoves  list of moves the drone could perform
	 * 						 without any restriction.
	 */
	protected static void filterPossibleMoves(Location start, List<Pair<Integer, Location>> possibleMoves) {
		possibleMoves.removeIf(
				pair -> !isInConfinedArea(pair.getValue1())  
				|| doesIntersectWithNoFlyZones(start, pair.getValue1()) 
				);
	}
	
	/**
	 * Returns every possible move the drone could perform
	 * from a given location.
	 * 
	 * @param start  location in which the drone is
	 * @return       list of possible moves it could perform
	 */
	protected static List<Pair<Integer, Location>> getPossibleMoves(Location start) {
		var possibleMoves = new ArrayList<Pair<Integer, Location>>();
		for (int angle = 0; angle <= 350; angle += 10) {
			var endLocation = getEndLocation(start, angle); 
			var move = new Pair<Integer, Location>(angle, endLocation);
			possibleMoves.add(move);
		}
		return possibleMoves;
	}
	
	/**
	 * Using the possible moves the drone could make, it returns
	 * the one which gets closer to the location the drone has as
	 * a target.
	 * 
	 * @param moveStartLocation     location at which the drone starts
	 * @param nextSensorLocation    location of the sensor the drone is
	 * 								focusing on
	 * @param moveNumber			number of the move
	 * @param filteredPossibleMoves moves the drone could do without
	 * 								leaving the confined area or crossing
	 * 								a no fly zone.
	 * @return
	 */
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
