package common;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is request class
 */

public class Request implements Serializable {
	Integer number;
	String userType;
	String requestType;
	String userId;
	String courseNumber;
	String unitNumber;
	String classNumber;
    String status;

	public Request(ArrayList<Object> arr) {
		this.number = (Integer)arr.get(0);
		this.userType = (String)arr.get(1);
		this.requestType = (String)arr.get(2);
		this.userId = (String)arr.get(3);
		this.courseNumber=(String)arr.get(4);
		this.unitNumber=(String)arr.get(5);
	    this.classNumber=(String)arr.get(6);
	    this.status=(String)arr.get(7);
	} 
	
	public Request(int i, String userType, String subject, String userId, String courseNumber, String unitNumber, String classNumber, String status) {
		// TODO Auto-generated constructor stub
		this.number=i;
		this.userType = userType;
		this.requestType = subject;
		this.userId = userId;
		this.courseNumber=courseNumber;
		this.unitNumber=unitNumber;
	    this.classNumber=classNumber;
	    this.status=status;
	}

	
	
	public Request(String userType) {
		// TODO Auto-generated constructor stub
		this.userType=userType;
	}

	public Integer getNumber() {
		return number;
	}
	public void setNumber(Integer number) {
		this.number = number;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String type) {
		this.userType = type;
	}
	public String getSubjectType() {
		return requestType;
	}
	public void setSubjectType(String SubjectType) {
		this.requestType = SubjectType;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getUnitNumber() {
		return unitNumber;
	}
	public void setUnitNumber(String unitNumber) {
		this.unitNumber = unitNumber;
	}
	public String getClassNumber() {
		return classNumber;
	}
	public void setClassNumber(String classNumber) {
		this.classNumber = classNumber;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getCourseInClassId() {
		return ( getCourseNumber()+getUnitNumber() );
	}

	public String getCourseInClassIndicator(){
		return getCourseInClassId() + " in class: " + classNumber;
	}
}
