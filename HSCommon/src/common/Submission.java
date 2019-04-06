package common;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
/**
 * 
 * @author saree
 *this class is submission class
 */
public class Submission implements Serializable{
	String taskId;
	String studentId;
	String courseNumber;
	String unitId;
	String classId;
	String fileExtension;
	byte[] fileData;
	Date submitDate;
	String notes;
	Integer grade;
	
	public Submission(String taskId, String studentId, String courseNumber, String unitId, String classId,
			String fileExtension, byte[] fileData, Date submitDate, String notes) {
		super();
		this.taskId = taskId;
		this.studentId = studentId;
		this.courseNumber = courseNumber;
		this.unitId = unitId;
		this.classId = classId;
		this.fileExtension = fileExtension;
		this.fileData = fileData;
		this.submitDate = submitDate;
		this.notes = notes;
		
	}
	
	public Submission(ArrayList<Object> resArr) {
		if (resArr.size() == 7){
			this.courseNumber = (String)resArr.get(0);
			this.studentId = (String)resArr.get(1);
			this.unitId = (String)resArr.get(2);
			this.classId = (String)resArr.get(3);
			this.taskId = (String)resArr.get(4);
			this.submitDate = new Date(Calendar.getInstance().getTime().getTime());
			this.fileData = (byte[])resArr.get(5);
			this.fileExtension = (String)resArr.get(6);			
		}else{
			this.courseNumber = (String)resArr.get(0);
			this.studentId = (String)resArr.get(1);
			this.unitId = (String)resArr.get(2);
			this.classId = (String)resArr.get(3);
			this.taskId = (String)resArr.get(4);
			this.grade = (Integer)resArr.get(5);
			this.submitDate = (Date)resArr.get(6);
			this.fileData = (byte[])resArr.get(7);
			this.notes = (String)resArr.get(8);
			this.fileExtension = (String)resArr.get(9);
		}
		
		
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getStudentId() {
		return studentId;
	}

	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public String getCourseNumber() {
		return courseNumber;
	}

	public void setCourseNumber(String courseNumber) {
		this.courseNumber = courseNumber;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Date getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Date submitDate) {
		this.submitDate = submitDate;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Integer getGrade() {
		return grade;
	}

	public void setGrade(Integer grade) {
		this.grade = grade;
	}
	
}
