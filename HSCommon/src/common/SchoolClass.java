package common;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is school class class
 */
public class SchoolClass implements Serializable{
	
	private String id;
	private String name;
	private ArrayList<String> studentsInClass;
	private ArrayList<CourseInClass> coursesInClass;
	
	public SchoolClass(String id,String name, ArrayList<String> studentsInClass,ArrayList<CourseInClass> coursesInClass)
	{
		this.id=id;
		this.name=name;
		this.studentsInClass=studentsInClass;
		this.coursesInClass=coursesInClass;
	}
	
	public SchoolClass(String id)
	{
		this.id = id;
		this.name = "";
	}	
	
	public SchoolClass(String id,String name)
	{
		this.id = id;
		this.name = name;
	}	
	
	public String toString()
	{
	return id+" "+name;	
	}
	
	public SchoolClass(ArrayList<Object> arrayList) {
		this.id=(String)arrayList.get(1);
		this.name=(String)arrayList.get(0);	
	}

	public String getName(){
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}

	public ArrayList<String> getStudentsInClass() {
		return studentsInClass;
	}

	public void setStudentsInClass(ArrayList<String> studentsInClass) {
		this.studentsInClass = studentsInClass;
	}

	public ArrayList<CourseInClass> getCoursesInClass() {
		return coursesInClass;
	}

	public void setCoursesInClass(ArrayList<CourseInClass> coursesInClass) {
		this.coursesInClass = coursesInClass;
	}
	

	

}
