package uk.ac.ed.inf.aqmaps;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class IO { 
	
	protected static Date date;
	protected static Location startingPoint;
	protected static String seed;
	protected static String port;
	
	/**
	 * Uses the command line arguments to set up
	 * the date, the starting location, the seed 
	 * and the port.
	 * 
	 * @param args the arguments to be parsed
	 * @throws ArrayIndexOutOfBoundsException if the given arguments
	 * 										  are less than 7
	 */
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

	/**
	 * Creates the readings file corresponding to the date given
	 * with the command line arguments and writes to it the json
	 * string representing the air quality map given as argument.
	 * 
	 * @param stringToWrite string which will be written to the 
	 * 						created readings file
	 */
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
	
	/**
	 * Creates the flightpath file corresponding to the date given
	 * with the command line arguments and writes to it every single
	 * move the drone performed following the format given in the
	 * coursework specification.
	 * 
	 * @param moves list of moves performed by the drone 					
	 */
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


