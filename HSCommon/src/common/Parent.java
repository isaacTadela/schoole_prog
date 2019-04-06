package common;

import java.io.Serializable;
import java.sql.Date;

/**
 * 
 * @author saree
 *this class is parent class
 */
public class Parent extends User implements Serializable {
	Date banUntilDate;
	public Date getBanUntilDate() {
		return banUntilDate;
	}
	public void setBanUntilDate(Date banUntilDate) {
		this.banUntilDate = banUntilDate;
	}
	public String isBannedOrUntil(Date currentDate) {
		if (currentDate == null){
			return "Not banned.";
		}
		if (currentDate != null && !currentDate.after(getBanUntilDate())){
			return getBanUntilDate().toString();
		}
		return "Not banned.";
	}
	
	public Parent(String id, String pname, String fname, String type,Date banUntilDate) {
		super(id, pname, fname, type);
		this.banUntilDate = banUntilDate;
		
		// TODO Auto-generated constructor stub
	}
}
