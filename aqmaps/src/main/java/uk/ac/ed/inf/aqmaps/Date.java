package uk.ac.ed.inf.aqmaps;

public class Date {
	
	private String year;
	private String month;
	private String day;
	
	/**
	 * Class constructor instantiating given date
	 * @param year	year of the date to create.
	 * @param month month of the date to create. Adds a 0
	 * 				in front for every month whose number is
	 * 				smaller than 10;
	 * @param day	day of the date to create. Adds a 0 
	 * 				in front for every day whose number is
	 * 				smaller than 10.
	 */
	protected Date(String year, String month, String day) {
		this.year = year;
		this.month = month.length() == 1 ? '0' + month : month;
		this.day = day.length() == 1 ? '0' + day : day;
	}
	
	/**
	 * @return year of the specific date.
	 */
	protected String getYear() {
		return this.year;
	}
	/**
	 * @return month of the specific date formatted
	 * 		   with a 0 in front if needed.
	 */
	protected String getMonth() {
		return this.month;
	}
	/**
	 * @return day of the specific date formatted
	 * 		   with a 0 in front if needed.
	 */
	protected String getDay() {
		return this.day;
	}

}
