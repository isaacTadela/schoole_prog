package common;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * 
 * @author saree
 *this class is semester class
 */
public class Semester implements Serializable{

	Integer id;
	Date open_date;
	Date closing_date;
	String season;
	Date currentDate;
	
	public Semester(Integer id, Date open_date, Date closing_date, String season) {
		this.id = id;
		this.open_date = open_date;
		this.closing_date = closing_date;
		this.season = season;
	}

	public Semester(ArrayList<Object> arr) {
		this.id = (Integer)arr.get(0);
		this.open_date = (Date)arr.get(1);
		this.closing_date = (Date)arr.get(2);
		this.season = (String)arr.get(3);
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}
	public void setCurrentDate(long currentDate) {
		this.currentDate = new Date(currentDate);
	}	
	
	
	
	@Override
	public String toString() {
		return getSemesterFasionableDescription();
	}
	
	public static Calendar toCalendar(Date date){ 
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return cal;
	}
	
	public String getSemesterFasionableDescription(){
		Integer year = toCalendar(getOpen_date()).get(Calendar.YEAR);
		return year.toString() + " " + getSeason();	
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getOpen_date() {
		return open_date;
	}

	public void setOpen_date(Date open_date) {
		this.open_date = open_date;
	}

	public Date getClosing_date() {
		return closing_date;
	}

	public void setClosing_date(Date closing_date) {
		this.closing_date = closing_date;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}	
}
