package common;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is student in course class
 */
public class StudentInCourse implements Serializable{
	String studentId;
	String classId;
	Course course;
	Integer grade;
	String notes;
	
	public StudentInCourse(String studentId, String classId, Course course, String unitId) {
		this.studentId = studentId;
		this.classId = classId;
		this.course = course;
	}
	
	public StudentInCourse(ArrayList<Object> arrayList) {
		this.studentId = (String)arrayList.get(0);
		this.classId = (String)arrayList.get(3);
		this.course = (Course)arrayList.get(1);
		
		if (arrayList.size() == 6){
			this.grade = (Integer)arrayList.get(4);
			this.notes = (String)arrayList.get(5);
		}
		
	}

	public String getStudentId() {
		return studentId;
	}
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public Course getCourse() {
		return course;
	}
	public void setCourse(Course course) {
		this.course = course;
	}

	public Integer getGrade() {
		return grade;
	}
	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	
}
