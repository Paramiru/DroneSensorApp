package uk.ac.ed.inf.aqmaps;

public class App {
	
	public static void main(String[] args) throws InterruptedException {
		var server = new ServerRequest(args);
		var noFlyZones = server.getNoFlyZones();
		var sensors = server.getSensors();

	}
}
