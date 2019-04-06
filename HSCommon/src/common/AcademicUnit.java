package common;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is academic unit class 
 */
public class AcademicUnit implements Serializable{
	
	@Override
	public String toString() {
		return getName() + " ["+ getId() +"]";
	}
	
	String id;
	String name;
	public AcademicUnit(String object, String object2) {
		super();
		this.id = object;
		this.name = object2;
	}
	public AcademicUnit(String object) {
		super();
		this.id = object;
		this.name = "unknown";
	}	
	public AcademicUnit(ArrayList<Object> arrayList) {
		this.id = (String)arrayList.get(0);
		this.name = (String)arrayList.get(1);
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
}
