package uk.ac.ed.inf.aqmaps;


public class App {
	
	public static void main(String[] args) throws InterruptedException {
		var server = new ServerRequest(args);
//		var noFlyZones = server.getNoFlyZones();
		var sensors = server.getSensors();
		var dronePath = BuildAqmap.buildMap(sensors, IO.startingPoint);
		System.out.println(dronePath);

		
		
		
		// REMEMBER THAT NOT VISITED SENSORS SHOULD BE MARKED AS GREY
	}
}

//	Check closest sensor. If you need to cross a noFlyZone then check another one.