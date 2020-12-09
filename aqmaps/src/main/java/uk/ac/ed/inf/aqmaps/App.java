package uk.ac.ed.inf.aqmaps;

public class App {
	
	public static void main(String[] args) throws InterruptedException {
		BuildAqmap.setUpMap(args);
		BuildAqmap.buildMap();
	}
}