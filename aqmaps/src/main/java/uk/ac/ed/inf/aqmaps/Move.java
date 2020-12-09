package uk.ac.ed.inf.aqmaps;

public class Move {
	
	private Location start;
	private Location end;
	private int moveNumber;
	private int angle; 
	private String locationOfAssociatedSensor = "null";

	/**
	 * Class constructor to build a move from the given
	 * arguments.
	 * 
	 * @param start	  the start location of the move
	 * @param end	  the end locatin achieved by the drone 
	 * 				  and that move
	 * @param number  the move number
	 * @param angle   the angle / direction used by the drone
	 * 				  to move from start to end
	 */
	protected Move(Location start, Location end, int number, int angle) {
		this.start = start;
		this.end = end;
		this.moveNumber = number;
		this.angle = angle;
	}
	
	/**
	 * @return angle used by drone (as an int)
	 */
	protected int getAngle() { return this.angle; }
	
	/**
	 * @return number of the drone's move
	 */
	protected int getMoveNumber() { return this.moveNumber; }
	
	/**
	 * @return location from which the drone started
	 * 		   in this move
	 */
	protected Location getStartLocation() { return this.start; }
	
	/** 
	 * @return location where drone finalized the move
	 */
	protected Location getEndLocation() { return this.end; }
	
	/**
	 * @return location of the closest sensor associated with
	 * 	       the move if that sensor was in range.
	 */
	protected String getLocationOfAssociatedSensor() { return this.locationOfAssociatedSensor; }
	
	/** 
	 * @param location of the closest sensor to associate it with
	 * 		  the move of the drone if the sensor is in range.
	 */
	protected void setAssociatedSensor(String location) { this.locationOfAssociatedSensor = location; }
	
}

