package uk.ac.ed.inf.aqmaps;

public class App {
	
	public static void main(String[] args) throws InterruptedException {
		var server = new ServerConnection(args);
		var noFlyZones = server.getNoFlyZones();
		var sensorsToVisit = server.getSensorsToVisit();
	}
}
