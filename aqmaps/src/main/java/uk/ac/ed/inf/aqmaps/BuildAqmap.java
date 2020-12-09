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
	
	/**
	 * Sets the server up so that we retrieve the sensors,
	 * the no fly zones and initialises the variables
	 * we will use later.
	 * 
	 * @param args command line arguments
	 * @throws InterruptedException
	 */
	protected static void setUpMap(String[] args) throws InterruptedException {
//		System.out.println("Setting up server...");
		server = new ServerRequest(args);
//		System.out.println("Server set up");
//		System.out.println("Getting no-fly-zones");
		noFlyZones = server.getNoFlyZones();
//		System.out.println("No-fly-zones have been obtained from server");
		sensorsToVisit = Utils.getSensorsToVisitInOrder(server.getSensors(), IO.startingLocation);
		currentLocation = IO.startingLocation;
		points.add(currentLocation.getGeojsonPoint());
		nextSensorToVisit = sensorsToVisit.poll();
		nextGoalLocationForTheDrone = nextSensorToVisit.getLocationFromSensor();
	}
	
	/**
	 * Returns the most optimal move for the drone to
	 * take so that it gets closer to the next sensor
	 * (or starting position) taking into consideration
	 * the constraints (no fly zones and confined area)
	 * 
	 * It also takes into account the previous moves so 
	 * that the drone does not go back and forth to the
	 * same place wasting its moves. However, if the 
	 * only possiblity is going back, it will do so since
	 * there are some special cases which have to be taken
	 * into account (imagine going to a zone which has the
	 * same place to enter and exit.
	 * 
	 * @return most optimal move
	 */
	protected static Move makeGreedyMove() {
		var possibleMoves = BuildAqmapUtils.getPossibleMoves(currentLocation);
		BuildAqmapUtils.filterPossibleMoves(currentLocation, possibleMoves);
		
		var newPossibleMoves = possibleMoves;
		newPossibleMoves.removeIf(pair -> BuildAqmapUtils.isGoingToPreviousPosition(pair));
		if (newPossibleMoves.size() > 0) { possibleMoves = newPossibleMoves; }
		
		var optimalMove = BuildAqmapUtils.getOptimalMove(currentLocation, nextGoalLocationForTheDrone, moveNumber, possibleMoves);
		return optimalMove;
	}
	
	/**
	 * makes the drone go from one sensor to the next
	 * as long as it has enough moves or it has not 
	 * finished checking every sensor retrieved from
	 * the WebServer.
	 * 
	 * @throws InterruptedException
	 */
	protected static void collectSensors() throws InterruptedException {
//		System.out.println("Drone starting to move beep beep beep");
		while (moveNumber <= Constants.MAX_MOVES && nextSensorToVisit != null) {
			var optimalMove = makeGreedyMove();
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
			// Adds move to the list which we will print in the end
			chosenMoves.add(optimalMove);
			points.add(optimalMove.getEndLocation().getGeojsonPoint());
			moveNumber++;
		}
	}
	
	/**
	 * If the drone has not done the maximum number of moves it can do,
	 * try to make it go back to the origin. We do so in a similar way
	 * to how we go to the sensors, but this time we consider a different
	 * length from the origin (Constants.MOVE_LENGTH instead of 
	 * Constants.SENSOR_DISTANCE) to finish.
	 */
	protected static void goBackToStartingLocation() {
		// Tell the drone it must focus on the starting Location
		nextGoalLocationForTheDrone = IO.startingLocation;
		while (moveNumber <= Constants.MAX_MOVES) {
			
			var optimalMove = makeGreedyMove();
			chosenMoves.add(optimalMove);
			points.add(optimalMove.getEndLocation().getGeojsonPoint());
			currentLocation = optimalMove.getEndLocation();
			
			var distanceToStartingPoint = Utils.getDistance(currentLocation, IO.startingLocation);
			if (distanceToStartingPoint < Constants.MOVE_LENGTH) { 
				break;
			}
			moveNumber++;
		}
	}
	
	/**
	 * Returns the corresponding GeoJson map taking into account the points
	 * through which the drone has gone through (so that we can render the
	 * path as a LineString), the sensors it has visited and also the ones
	 * it has not visited (which will be grey-coloured and without any
	 * symbol.
	 * 
	 * @return json string of the corresponding map
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Writes the two requested files in the coursework.
	 * 
	 * @param jsonString string representing the GeoJson map
	 */
	protected static void writeFiles(String jsonString) {
		IO.writeReadingFile(jsonString);
		IO.writeFlightpathFile(chosenMoves);
	}
	
	/**
	 * Method which completes the map collecting the sensors
	 * and trying to go back to the origin using the methods
	 * previously defined.
	 * 
	 * @throws InterruptedException
	 */
	protected static void buildMap() throws InterruptedException {
		collectSensors();
//		System.out.println("Finished checking Sensors");
//		System.out.println("Going back to starting position");
		goBackToStartingLocation();
		var jsonString = createGeojsonMap();
		writeFiles(jsonString);
		
	}
	
	
}