package uk.ac.ed.inf.aqmaps;

import java.util.ArrayList;

import org.locationtech.jts.geom.Coordinate;

import com.mapbox.geojson.Feature;

public class NoFlyZone {
	
	private com.mapbox.geojson.Polygon geoJsonPolygon;
	private org.locationtech.jts.geom.Polygon jtsPolygon;
	
	public NoFlyZone(Feature feature) {
		var noFlyZoneGeometry = feature.geometry();
		var noFlyZonePolygon = (com.mapbox.geojson.Polygon) noFlyZoneGeometry;
		this.geoJsonPolygon = noFlyZonePolygon;
		this.makeJtsPolygon();
	}

	protected void makeJtsPolygon() {
		var geoJsonPoints = this.geoJsonPolygon.coordinates().get(0);
		var shell = new Coordinate[geoJsonPoints.size()];
		// remember points in geoJson are (lng,lat)
		for (int i = 0; i < geoJsonPoints.size(); i++) {
			var point = geoJsonPoints.get(i);
			var coordinate = new Coordinate(point.latitude(), point.longitude());
			shell[i] = coordinate;
		}
		this.jtsPolygon = Utils.geometryFactory.createPolygon(shell);
		
	}
	
	protected com.mapbox.geojson.Polygon getGeojsonPolygon() {
		return this.geoJsonPolygon;
		
	}
	
	protected org.locationtech.jts.geom.Polygon getJtsPolygon() {
		return this.jtsPolygon;
	}

}
