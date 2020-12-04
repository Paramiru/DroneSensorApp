package uk.ac.ed.inf.aqmaps;

public class Date {
	
	private String year;
	private String month;
	private String day;
	
	protected Date(String year, String month, String day) {
		this.year = year;
		this.month = month.length() == 1 ? '0' + month : month;
		this.day = day.length() == 1 ? '0' + day : day;
	}
	
	protected String getYear() {
		return this.year;
	}
	
	protected String getMonth() {
		return this.month;
	}
	
	protected String getDay() {
		return this.day;
	}

}
