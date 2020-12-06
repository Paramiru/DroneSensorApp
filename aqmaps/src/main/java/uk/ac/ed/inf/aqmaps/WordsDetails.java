package uk.ac.ed.inf.aqmaps;

public class WordsDetails {

	String country;
	Square square;
	
	public static class Square {
		Coordinates southwest;
		Coordinates northeast;
	}
	
	String nearestPlace;
	Coordinates coordinates;
	
	public static class Coordinates {
		double lng;
		double lat;
	}
	
	String words;
	String language;
	String map;
	
	
}
