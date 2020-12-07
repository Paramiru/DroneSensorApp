package uk.ac.ed.inf.aqmaps;

import org.locationtech.jts.geom.Coordinate;

import com.mapbox.geojson.Feature;

public class NoFlyZone {
	
	private com.mapbox.geojson.Polygon geoJsonPolygon;
	private org.locationtech.jts.geom.Polygon jtsPolygon;
	
	/**
	 * Class constructor specifying the no-fly-zone to create.
	 * @param feature the no-fly-zone represented as a feature.
	 */
	public NoFlyZone(Feature feature) {
		var noFlyZoneGeometry = feature.geometry();
		var noFlyZonePolygon = (com.mapbox.geojson.Polygon) noFlyZoneGeometry;
		this.geoJsonPolygon = noFlyZonePolygon;
		this.makeJtsPolygon();
	}
	
	/**
	 * Method to convert the GeoJson Polygon representing the
	 * no-fly-zone to a JTS Polygon.
	 */
	protected void makeJtsPolygon() {
		var geoJsonPoints = this.geoJsonPolygon.coordinates().get(0);
		var shell = new Coordinate[geoJsonPoints.size()];
		for (int i = 0; i < geoJsonPoints.size(); i++) {
			var point = geoJsonPoints.get(i);
			var coordinate = new Coordinate(point.latitude(), point.longitude());
			shell[i] = coordinate;
		}
		this.jtsPolygon = Utils.geometryFactory.createPolygon(shell);
	}
	
	/**
	 * @return 	a GeoJson Polygon representing the no-fly-zone
	 * 			from the given class
	 */
	protected com.mapbox.geojson.Polygon getGeojsonPolygon() {
		return this.geoJsonPolygon;
		
	}
	
	/**
	 * @return 	a JTS Polygon representing the no-fly-zone
	 * 			from the given class
	 */
	protected org.locationtech.jts.geom.Polygon getJtsPolygon() {
		return this.jtsPolygon;
	}

}
