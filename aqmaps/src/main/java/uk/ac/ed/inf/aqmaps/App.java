package uk.ac.ed.inf.aqmaps;


public class App {
	
	public static void main(String[] args) throws InterruptedException {
		
		BuildAqmap.buildMap(args);
		
		// REMEMBER THAT NOT VISITED SENSORS SHOULD BE MARKED AS GREY
	}
}

