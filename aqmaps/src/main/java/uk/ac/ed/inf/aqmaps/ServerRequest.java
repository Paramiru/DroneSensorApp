package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

public class ServerRequest {
	
	private static final HttpClient client = HttpClient.newHttpClient(); 
	
	public ServerRequest(String[] args) {
		IO.parseArguments(args);
	}
	
	protected List<NoFlyZone> getNoFlyZones() throws InterruptedException {
		var path = "/buildings/no-fly-zones.geojson";
		var source = getRequest(path);
		var fc = FeatureCollection.fromJson(source);
		var features = fc.features();
		var noFlyZones = new ArrayList<NoFlyZone>();
		for (Feature feature : features) {
			var noFlyZone = new NoFlyZone(feature);
			noFlyZones.add(noFlyZone);
		}
		return noFlyZones;
	}
	
	protected List<Sensor> getSensors() throws InterruptedException {
		var path = "/maps/" + IO.date.getYear() + "/" + IO.date.getMonth() 
			+ "/" + IO.date.getDay() + "/" + "air-quality-data.json";
		
		var source = getRequest(path);
		
		Type listType = new TypeToken<ArrayList<Sensor>>() {}.getType();
		ArrayList<Sensor> sensors = new Gson().fromJson(source, listType);
		return sensors;
	}
	
	protected static WordsDetails getWordsAddress(String words) throws InterruptedException {
		var splittedWords = words.split("\\.");
		var path = "/words/" + splittedWords[0] + "/" + splittedWords[1] 
		+ "/" + splittedWords[2] + "/" + "details.json";
	
		var source = getRequest(path);
		
		var address = new Gson().fromJson(source, WordsDetails.class);
		return address;
	}

	protected static String getRequest(String path) throws InterruptedException {
		var urlString = Constants.SERVER + IO.port + path;
		var request = HttpRequest.newBuilder()
				.uri(URI.create(urlString))
				.build();
		var responseBody = "";
		try {
			var response = client.send(request, BodyHandlers.ofString());
			if (response.statusCode() == 200) {
				responseBody = response.body();
			} else if (response.statusCode() == 404){
				System.out.println("Could not find anything");
			} else {
				System.out.println("Unable to connect to " + Constants.SERVER + 
						" at port " + IO.port + ".");
			}
		} catch (ConnectException e) {
			System.out.println(e);
			System.out.println("Fatal error: Unable to connect to " + Constants.SERVER
					+ " at port " + IO.port + ".");
			System.out.println("Have you checked the server is running?");
			System.exit(1); // Exit the application
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return responseBody;
	}
	
}
