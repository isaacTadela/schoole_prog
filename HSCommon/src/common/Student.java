package common;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;

import enums.UserType;
/**
 * 
 * @author saree
 *this class is student class
 */
public class Student extends User implements Serializable{
	String _class;
	String father;
	String mother;
	
	public Student(String id, String pname, String fname, String type,String _class, String father,String mother) {
		super(id, pname, fname, type);
		this._class = _class;
		this.father = father;
		this.mother = mother;
	}
	
	public Student(String id,String pname,String fname)
	{
		super(id,pname,fname);
	}

	public String toString()
	{
		return super.getId()+" "+super.getPname()+" "+super.getFname();
	}

	public String get_class() {
		return _class;
	}

	public void set_class(String _class) {
		this._class = _class;
	}

	public String getFather() {
		return father;
	}

	public void setFather(String father) {
		this.father = father;
	}

	public String getMother() {
		return mother;
	}

	public void setMother(String mother) {
		this.mother = mother;
	}
	
}
