package uk.ac.ed.inf.aqmaps;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IO { 
	
	protected static Date date;
	protected static Location startingPoint;
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
		startingPoint = new Location(startingLatitude, startingLongitude);
		
		seed = args[5];
		port = args[6];
		
	}

	protected static void writeReadingFile(String stringToWrite) {
		var filename = "readings-" + date.getDay() + "-" + date.getMonth() 
			+ "-" + date.getYear() + ".geojson";
		try {
	    	var writer = new FileWriter(filename);
//	    	System.out.println("File " + filename + " created successfully.");
			writer.append(stringToWrite);
		    writer.close();
		} catch (IOException e) {
			System.out.println("File could not be created");
			e.printStackTrace();
		}
	}
	
	protected static void writeFlightpathFile(List<Move> moves) {
		var filename = "flightpath-" + date.getDay() + "-" + date.getMonth() 
			+ "-" + date.getYear() + ".txt";
		var stringToWrite = "";
		for (Move move : moves) {
			stringToWrite += move.getMoveNumber() + ",";
			stringToWrite += move.getStartLocation().longitude() + ",";
			stringToWrite += move.getStartLocation().latitude() + ",";
			stringToWrite += move.getAngle() + ",";
			stringToWrite += move.getEndLocation().longitude() + ",";
			stringToWrite += move.getEndLocation().latitude() + ",";
			stringToWrite += move.getLocationOfAssociatedSensor() + "\n";
		}
		try {
	    	var writer = new FileWriter(filename);
//	    	System.out.println("File " + filename + " created successfully.");
			writer.append(stringToWrite);
		    writer.close();
		} catch (IOException e) {
			System.out.println("File could not be created");
			e.printStackTrace();
		}
		
	}

}


