package common; 

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is course class
 */
public class Course implements Serializable{
	String number;
	String name;
	Integer weeklyHours;
	AcademicUnit acUnit;
	ArrayList<String> predecessors;
	
	public String toString()
	{
		return acUnit.id+number+" " +name;
	}
	
	public String getId() {
		return acUnit.getId() + getNumber();
	}
	
	public String getNumber() {
		return number;
	}
	
	public void setNumber(String number) {
		this.number = number;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getWeeklyHours() {
		return weeklyHours;
	}
	public void setWeeklyHours(Integer weeklyHours) {
		this.weeklyHours = weeklyHours;
	}
	public AcademicUnit getAcUnit() {
		return acUnit;
	}
	public void setAcUnit(AcademicUnit acUnit) {
		this.acUnit = acUnit;
	}
	public ArrayList<String> getPredecessors() {
		return predecessors;
	}
	public void setPredecessors(ArrayList<String> predecessors) {
		this.predecessors = predecessors;
	}
	public Course(String number, String name, Integer weeklyHours, AcademicUnit acUnit, ArrayList<String> predecessors) {
		this.number = number;
		this.name = name;
		this.weeklyHours = weeklyHours;
		this.acUnit = acUnit;
		this.predecessors = predecessors;
	}
	
	public Course(String unitNumber,String number,String name,Integer weeklyHours)
	{
		this.number = number;
		this.name = name;
		this.weeklyHours = weeklyHours;
		this.acUnit = new AcademicUnit(unitNumber);
	}
	
	public Course(ArrayList<Object> arrayList) {
		this.number = (String)arrayList.get(0);
		this.name = (String)arrayList.get(2);
		this.weeklyHours = (Integer)arrayList.get(3);
		this.acUnit = new AcademicUnit((String)arrayList.get(1));
	}
	public Course(String number,String unitId, String name) {
		this.number = number;
		this.acUnit = new AcademicUnit(unitId);
		this.name = name;

	}

	public Course(String number, String unitId) {
		// TODO Auto-generated constructor stub
		this.number = number;
		this.acUnit = new AcademicUnit(unitId);
	}

}
