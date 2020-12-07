package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;


public class BuildAqmap {
	
	protected static List<NoFlyZone> noFlyZones;
	protected static List<Move> chosenMoves = new ArrayList<>();
	
	private static ServerRequest server;
	private static Queue<Sensor> sensorsToVisit;
	private static List<Sensor> visitedSensors = new ArrayList<>();
	private static List<Point> points = new ArrayList<>();
	
	private static int moveNumber = 1;
	private static Location currentLocation;
	private static Sensor nextSensorToVisit;
	private static Location nextGoalLocationForTheDrone;
	

	protected static void setUpMap(String[] args) throws InterruptedException {
//		System.out.println("Setting up server...");
		server = new ServerRequest(args);
//		System.out.println("Server set up");
//		System.out.println("Getting no-fly-zones");
		noFlyZones = server.getNoFlyZones();
//		System.out.println("No-fly-zones have been obtained from server");
		sensorsToVisit = Utils.getSensorsToVisitInOrder(server.getSensors(), IO.startingPoint);
		currentLocation = IO.startingPoint;
		points.add(currentLocation.getGeojsonPoint());
		nextSensorToVisit = sensorsToVisit.poll();
		nextGoalLocationForTheDrone = nextSensorToVisit.getLocationFromSensor();
	}
	
	protected static void collectSensors() throws InterruptedException {
//		System.out.println("Drone starting to move beep beep beep");
		while (moveNumber <= Constants.MAX_MOVES && nextSensorToVisit != null) {
			var optimalMove = makeOptimalMove();
			currentLocation = optimalMove.getEndLocation();
			
			var distanceToSensorTarget = Utils.getDistance(currentLocation, nextGoalLocationForTheDrone);
			if (distanceToSensorTarget < Constants.SENSOR_DISTANCE) { 
				// sensor is in range so we collect it
				visitedSensors.add(nextSensorToVisit);
				optimalMove.setAssociatedSensor(nextSensorToVisit.location);
				// make drone focus on the next sensor
				nextSensorToVisit = sensorsToVisit.poll();
				if (nextSensorToVisit != null ) { nextGoalLocationForTheDrone = nextSensorToVisit.getLocationFromSensor(); }
			}
			chosenMoves.add(optimalMove);
			points.add(optimalMove.getEndLocation().getGeojsonPoint());
			moveNumber++;
		}
	}
	protected static void goBackToStartingLocation() {
		// Tell the drone it must focus on the starting Location
		nextGoalLocationForTheDrone = IO.startingPoint;
		while (moveNumber <= Constants.MAX_MOVES) {
			
			var optimalMove = makeOptimalMove();
			chosenMoves.add(optimalMove);
			points.add(optimalMove.getEndLocation().getGeojsonPoint());
			currentLocation = optimalMove.getEndLocation();
			
			var distanceToStartingPoint = Utils.getDistance(currentLocation, IO.startingPoint);
			if (distanceToStartingPoint < Constants.MOVE_LENGTH) { 
				break;
			}
			moveNumber++;
		}
	}
	protected static String createGeojsonMap() throws InterruptedException {
		var lineString = LineString.fromLngLats(points);
		var features = Utils.createFeaturesList(visitedSensors, lineString);
		
		while (!sensorsToVisit.isEmpty()) {
			var notVisitedSensor = sensorsToVisit.poll();
			var featureOfNotVisitedSensor = notVisitedSensor.getGreySensorAsFeature();
			features.add(featureOfNotVisitedSensor);
		}
		
		var fc = FeatureCollection.fromFeatures(features);
		var jsonString = fc.toJson();
		return jsonString;
	}
	
	protected static void writeFiles(String jsonString) {
		IO.writeReadingFile(jsonString);
		IO.writeFlightpathFile(chosenMoves);
	}
	
	protected static void buildMap() throws InterruptedException {
		collectSensors();
//		System.out.println("Finished checking Sensors");
//		System.out.println("Going back to starting position");
		goBackToStartingLocation();
		var jsonString = createGeojsonMap();
		writeFiles(jsonString);
		
	}
	
	protected static Move makeOptimalMove() {
		var possibleMoves = BuildAqmapUtils.getPossibleMoves(currentLocation);
		BuildAqmapUtils.filterPossibleMoves(currentLocation, possibleMoves);
		
		var newPossibleMoves = possibleMoves;
		newPossibleMoves.removeIf(pair -> BuildAqmapUtils.isGoingToPreviousPosition(pair));
		if (newPossibleMoves.size() > 0) { possibleMoves = newPossibleMoves; }
		
		var optimalMove = BuildAqmapUtils.getOptimalMove(currentLocation, nextGoalLocationForTheDrone, moveNumber, possibleMoves);
		return optimalMove;
	}
//	protected static Location getEndLocation(Location start, Integer angle) {
//		// given start location and angle, calculate what the end location will be
//		var angleInRadians = Math.toRadians(angle);
//		var newLatitude = start.latitude() + Constants.MOVE_LENGTH * Math.sin(angleInRadians);
//		var newLongitude = start.longitude() + Constants.MOVE_LENGTH * Math.cos(angleInRadians);
//		var endLocation = new Location(newLatitude, newLongitude);
//		return endLocation;
//	}
//	protected static boolean isInConfinedArea(Location location) {
//		var point = location.getJtsPoint();
//		return point.within(Constants.confinedArea);
//	}
//	protected static boolean doesIntersectWithNoFlyZones(Location start, Location end) {
//		var coordinates = new Coordinate[] {start.getJtsCoordinate(), end.getJtsCoordinate()};
//		var line = Utils.geometryFactory.createLineString(coordinates);
//		for (NoFlyZone zone : noFlyZones) {
//			if (line.intersects(zone.getJtsPolygon())) return true;
//		}
//		return false;
//	}
//	protected static boolean isGoingToPreviousPosition(Pair<Integer, Location> possibleMove) {
//		
//		for (Move previousMove : chosenMoves) {
//			var possibleEndLocation = possibleMove.getValue1();
//			var distanceToPossibleEndLocation = Utils.getDistance(previousMove.getEndLocation(), possibleEndLocation);
//			// if end location is too close to a previous move's end 
//			// location then do not consider that possible move
//			if (distanceToPossibleEndLocation < Constants.EPSILON) return true;
//		}
//		return false;
//	}
//	protected static void filterPossibleMoves(Location start, List<Pair<Integer, Location>> possibleMoves) {
//		possibleMoves.removeIf(
//				pair -> !isInConfinedArea(pair.getValue1())  
//				|| doesIntersectWithNoFlyZones(start, pair.getValue1()) 
//				);
//	}
//	protected static List<Pair<Integer, Location>> getPossibleMoves(Location start) {
//		var possibleMoves = new ArrayList<Pair<Integer, Location>>();
//		for (int angle = 0; angle <= 350; angle += 10) {
//			var endLocation = getEndLocation(start, angle); 
//			var move = new Pair<Integer, Location>(angle, endLocation);
//			possibleMoves.add(move);
//		}
//		return possibleMoves;
//	}
//	protected static Move getOptimalMove(Location moveStartLocation, Location nextSensorLocation, Integer moveNumber, List<Pair<Integer, Location>> filteredPossibleMoves) {
//		
//		Move optimalMove = null;
//		var minDistance = Double.MAX_VALUE;
//		
//		for (Pair<Integer, Location> move : filteredPossibleMoves) {
//			var moveEndLocation = move.getValue1();
//			var distanceFromMoveToSensor = Utils.getDistance(moveEndLocation, nextSensorLocation);
//			
//			if (distanceFromMoveToSensor < minDistance) {
//				minDistance = distanceFromMoveToSensor;
//				optimalMove = new Move(moveStartLocation, moveEndLocation, moveNumber, move.getValue0());
//			}
//			
//		}
//		return optimalMove;
//	}
}