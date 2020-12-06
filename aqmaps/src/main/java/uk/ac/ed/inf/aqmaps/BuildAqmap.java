package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.javatuples.Pair;

import org.locationtech.jts.geom.Coordinate;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;


public class BuildAqmap {
	
	protected static ServerRequest server;
	protected static List<NoFlyZone> noFlyZones;
	protected static Queue<Sensor> sensorsToVisit;
	protected static List<Move> chosenMoves = new ArrayList<>();
	protected static List<Sensor> visitedSensors = new ArrayList<>();
	protected static List<Point> points = new ArrayList<>();

	protected static void buildMap(String[] args) throws InterruptedException {
		System.out.println("Setting up server...");
		server = new ServerRequest(args);
		System.out.println("Server set up");
		System.out.println("Getting no-fly-zones");
		noFlyZones = server.getNoFlyZones();
		System.out.println("No-fly-zones have been obtained from server");
		sensorsToVisit = Utils.getSensorsToVisitInOrder(server.getSensors(), IO.startingPoint);
		
		var moveNumber = 0;
		var currentPosition = IO.startingPoint;
		var nextSensor = sensorsToVisit.poll();
		var nextSensorLocation = nextSensor.getLocationFromSensor();
		
		points.add(currentPosition.getGeojsonPoint());
		
		while (moveNumber <= Constants.MAX_MOVES) {
			var possibleMoves = getPossibleMoves(currentPosition);
			filterPossibleMoves(currentPosition, possibleMoves);
			
			var newPossibleMoves = possibleMoves;
			newPossibleMoves.removeIf(pair -> isGoingToPreviousPosition(pair));
			if (newPossibleMoves.size() > 0) { possibleMoves = newPossibleMoves; }
			
			var optimalMove = getOptimalMove(currentPosition, nextSensorLocation, ++moveNumber, possibleMoves);
			System.out.println("Move number " + moveNumber);
			chosenMoves.add(optimalMove);
			points.add(optimalMove.getEndLocation().getGeojsonPoint());
			
			currentPosition = optimalMove.getEndLocation();
			
			var distanceToSensorTarget = Utils.getDistance(currentPosition, nextSensorLocation);
			if (distanceToSensorTarget < Constants.SENSOR_DISTANCE) { 
				// add sensor to visited sensors
				visitedSensors.add(nextSensor);
				if (sensorsToVisit.size() == 0) break;
				nextSensor = sensorsToVisit.poll();
				nextSensorLocation = nextSensor.getLocationFromSensor();
			}
		}
		
		System.out.println("Finished checking Sensors");
		while (moveNumber <= Constants.MAX_MOVES) {
			System.out.println("Going back to starting position");
			// go back to IO.startingPoint;
			var possibleMoves = getPossibleMoves(currentPosition);
			filterPossibleMoves(currentPosition, possibleMoves);
			
			var newPossibleMoves = possibleMoves;
			newPossibleMoves.removeIf(pair -> isGoingToPreviousPosition(pair));
			if (newPossibleMoves.size() > 0) possibleMoves = newPossibleMoves;
			
			var optimalMove = getOptimalMove(currentPosition, IO.startingPoint, ++moveNumber, possibleMoves);
			System.out.println("Move number " + moveNumber);
			chosenMoves.add(optimalMove);
			points.add(optimalMove.getEndLocation().getGeojsonPoint());
			
			currentPosition = optimalMove.getEndLocation();
			
			var distanceToStartingPoint = Utils.getDistance(currentPosition, IO.startingPoint);
			if (distanceToStartingPoint < Constants.MOVE_LENGTH) { 
				break;
			}
		}
		
		var lineString = LineString.fromLngLats(points);
		var features = Utils.createFeaturesList(visitedSensors, lineString);
		
		while (!sensorsToVisit.isEmpty()) {
			var notVisitedSensor = sensorsToVisit.poll();
			var featureOfNotVisitedSensor = notVisitedSensor.getGreySensorAsFeature();
			features.add(featureOfNotVisitedSensor);
		}
		
		var fc = FeatureCollection.fromFeatures(features);
		var jsonString = fc.toJson();

		System.out.println(jsonString);
		
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
	protected static boolean isGoingToPreviousPosition(Pair<Integer, Location> possibleMove) {
		
		for (Move previousMove : chosenMoves) {
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
