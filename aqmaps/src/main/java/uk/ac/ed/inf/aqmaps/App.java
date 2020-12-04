package uk.ac.ed.inf.aqmaps;


import java.time.LocalDate;

import com.mapbox.geojson.Point;

public class App {
	
	public static void main(String[] args) {
		
		var day = Integer.parseInt(args[0]);
		var month = Integer.parseInt(args[1]);
		var year = Integer.parseInt(args[2]);
		var date = LocalDate.of(year, month, day);
		System.out.println(date);
		System.out.println(date.getDayOfMonth());
		
		
		System.out.println(date.getMonthValue());
		System.out.println(date.getYear());
		
		var latitude = Double.parseDouble(args[3]);
		var longitude = Double.parseDouble(args[4]);
		var starting_point = Point.fromLngLat(longitude, latitude);
		
		var seed = args[5];
		var port = args[6];
		var uri = "http://localhost:" + port;
	}
}
