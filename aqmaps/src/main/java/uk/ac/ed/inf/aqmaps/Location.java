package uk.ac.ed.inf.aqmaps;

import org.locationtech.jts.geom.Coordinate;

import com.mapbox.geojson.Point;


public class Location {

	private double lng;
	private double lat;
	
	public Location(double lat, double lng) {
		this.lng = lng;
		this.lat = lat;
	}
	
	public org.locationtech.jts.geom.Point getJtsPoint() {
		var coord = new Coordinate(this.lat, this.lng);
		var point = Utils.geometryFactory.createPoint(coord);
		return point;
	}
	
	public com.mapbox.geojson.Point getGeojsonPoint() {
		return Point.fromLngLat(this.lng, this.lat);
	}
	
	public double latitude() {
		return this.lat;
	}
	
	public double longitude() {
		return this.lng;
	}

}
