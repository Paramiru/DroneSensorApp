package uk.ac.ed.inf.aqmaps;

public class Move {
	private Location start;
	private Location end;
	private int moveNumber;
	private int angle; 
	private String locationOfAssociatedSensor = "null";

	public Move(Location start, Location end, int number, int angle) {
		this.start = start;
		this.end = end;
		this.moveNumber = number;
		this.angle = angle;
	}
	
	public int getAngle() { return this.angle; }
	public int getMoveNumber() { return this.moveNumber; }
	public Location getStartLocation() { return this.start; }
	public Location getEndLocation() { return this.end; }
	public void setAssociatedSensor(String location) { this.locationOfAssociatedSensor = location; }
	public String getLocationOfAssociatedSensor() { return this.locationOfAssociatedSensor; }
}
