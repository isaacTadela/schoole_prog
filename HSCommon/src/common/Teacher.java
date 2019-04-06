package common;

import java.io.Serializable;
import java.util.ArrayList;

import enums.UserType;

// TODO need update
/**
 * 
 * @author saree
 *this class is teacher class
 */
public class Teacher extends User implements Serializable{

	private ArrayList<AcademicUnit> academicUnit;
	private Integer maxAllowedHours;
	private Integer currentHours;


	
	public String toString()
	{
		return super.getId()+" "+super.getPname()+" "+super.getFname();
	}
	
	public Teacher (String id,String pname,String fname,Integer currHours,Integer maxHours)
	{
		super(id,pname,fname);
		maxAllowedHours=maxHours;
		currentHours=currHours;
	}

	
	public Teacher (String id,Integer currHours,Integer maxHours)
	{
		super(id,null,null);
		maxAllowedHours=maxHours;
		currentHours=currHours;
	}
	
	public Teacher(ArrayList<Object> arr)
	{
		super((String)arr.get(0),(String)arr.get(1),(String)arr.get(2));
		currentHours=(Integer)arr.get(3);
		maxAllowedHours=(Integer)arr.get(4);
	}

	public Integer getMaxAllowedHours() {
		return maxAllowedHours;
	}
	public void setMaxAllowedHours(int maxAllowedHours) {
		this.maxAllowedHours = maxAllowedHours;
	}
	public Integer getCurrentHours() {
		return currentHours;
	}
	public void setCurrentHours(int currentHours) {
		this.currentHours = currentHours;
	}
	public Teacher(String id, String pname, String fname, String type, ArrayList<AcademicUnit> academicUnit) {
		super(id, pname,fname, type);
		this.academicUnit = academicUnit;
	}
	
	public Teacher(String id, String pname, String fname, String type,Integer maxWorkingHours, Integer currentWorkingHours) {
		super(id, pname, fname, type);
		this.maxAllowedHours = maxWorkingHours;
		this.currentHours = currentWorkingHours;
	}
	
	public ArrayList<AcademicUnit> getAcademicUnit() {
		return academicUnit;
	}
	public void setAcademicUnit(ArrayList<AcademicUnit> academicUnit) {
		this.academicUnit = academicUnit;
	}
}
