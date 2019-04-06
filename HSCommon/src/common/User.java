package common;

import java.io.Serializable;
import java.util.ArrayList;

import enums.UserType;
/**
 * 
 * @author saree
 *this class is user class
 */
public class User implements Serializable {

	private String id;
	private String pname;
	private String fname;
	private UserType type;
	
	public User(String id, String pname, String fname, String type){
		this.id = id;
		this.pname = pname;
		this.fname = fname;
		this.type = UserType.valueOf(type);
	}	
	
	public User(String id, String pname, String fname){
		this.id = id;
		this.pname = pname;
		this.fname = fname;
	}	
	
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public User() {

	}
		
	public User(ArrayList<Object> arrayList) {
		this.id = (String)arrayList.get(0);
		this.pname = (String)arrayList.get(3);
		this.fname = (String)arrayList.get(4);
		this.type = UserType.valueOf((String)arrayList.get(2));		
	}
	
	public static User InstantiateByArray(ArrayList<Object> arr){
		return new User((String)arr.get(0),(String)arr.get(3),(String)arr.get(4),(String)arr.get(2));		
	}
	
	public String getId() {
		return id;
	}
	public String getPname() {
		return pname;
	}
	public void setId(String id) {
		this.id = id;
	}

	public UserType getType() {
		return type;
	}
	public void setType(UserType type) {
		this.type = type;
	}
	
	public String getUser() {
		return this.getId()+" "+this.getType()+" "+this.getPname()+" "+this.getFname() ;
	}
	
}
