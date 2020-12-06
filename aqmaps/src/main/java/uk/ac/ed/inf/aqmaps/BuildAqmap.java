package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;
import java.util.List;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Geometry;

public class BuildAqmap {
	
	
	protected static void buildMap(String[] args) throws InterruptedException {
		var server = new ServerRequest(args);
		var sensors = server.getSensors();
		var s = Utils.getSensorsToVisitInOrder(sensors, IO.startingPoint);
		// get possible moves (start, end)
		// get optimal move (start, end)
	


	}
	
	

}


//1. Closest point and check if it intersects with a noFlyZone
//- If it intersects then use function to surround area.
//- otherwise, calculate angle to go there and use modulus so that it is a multiple of 10
//2. use moves until we are within 0.0002 degrees of th sensor
//3. add sensor to our visited list
//4. remove sensor from non-visited list
//5. keep checking how many moves we have and how many we need to return to the centre. If
//this value is quite close then go to the starting point even if we have not visited all
//sensors.
//6. get a feature list with all the visited sensors as Feature
//7. add not visited sensors as Features with grey colour though.
//8. take points and use them to create lineString which will be added to our feature list
//9. get feature collection.
//