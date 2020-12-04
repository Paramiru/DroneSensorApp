package uk.ac.ed.inf.aqmaps;

import java.time.LocalDate;

import com.mapbox.geojson.Point;

public class IO { 
	
	protected static LocalDate date;
	protected static Point startingPoint;
	protected static String seed;
	protected static String port;
	
	protected static void parseCommandLineArguments(String args[]) throws ArrayIndexOutOfBoundsException {
		if (args.length < 7) {
			System.out.print("Need 7 command line arguments; ");
			System.out.println("Given " + args.length + " in total");
			throw new ArrayIndexOutOfBoundsException();
		} 

		var day = Integer.parseInt(args[0]);
		var month = Integer.parseInt(args[1]);
		var year = Integer.parseInt(args[2]);
		date = LocalDate.of(year, month, day);
		
		var startingLatitude = Double.parseDouble(args[3]);
		var startingLongitude = Double.parseDouble(args[4]);
		startingPoint = Point.fromLngLat(startingLongitude, startingLatitude);
		
		seed = args[5];
		port = args[6];
		
	}
}
