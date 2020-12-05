package uk.ac.ed.inf.aqmaps;

import com.mapbox.geojson.Point;

public class IO { 
	
	protected static Date date;
	protected static Point startingPoint;
	protected static String seed;
	protected static String port;
	
	protected static void parseArguments(String args[]) throws ArrayIndexOutOfBoundsException {
		if (args.length < 7) {
			var detailMessage = "\n\tNeed 7 command line arguments. Given: " + 
					args.length + " arguments in total";
			throw new ArrayIndexOutOfBoundsException(detailMessage);
		} 

		date = new Date(args[2], args[1], args[0]);
		
		var startingLatitude = Double.parseDouble(args[3]);
		var startingLongitude = Double.parseDouble(args[4]);
		startingPoint = Point.fromLngLat(startingLongitude, startingLatitude);
		
		seed = args[5];
		port = args[6];
		
	}

//	TODO protected static void writeFiles() {}

}


