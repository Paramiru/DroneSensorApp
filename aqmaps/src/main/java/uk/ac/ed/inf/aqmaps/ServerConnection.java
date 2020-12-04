package uk.ac.ed.inf.aqmaps;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class ServerConnection {
	
	private static final HttpClient client = HttpClient.newHttpClient(); 
	
	public ServerConnection(String[] args) {
		IO.parseArguments(args);
	}
	
	protected String getNoFlyZones() throws InterruptedException {
		var path = "/buildings/no-fly-zones.geojson";
		return getRequest(path);
	}
	
	protected String getSensorsToVisit() throws InterruptedException {
		var path = "/maps/" + IO.date.getYear() + "/" + IO.date.getMonth() 
			+ "/" + IO.date.getDay() + "/" + "air-quality-data.json";
		return getRequest(path);
		
	}

	protected String getRequest(String path) throws InterruptedException {
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
