package common;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is task in course in class class
 */
public class TasksInCourseInClass implements Serializable{
	String taskId;
	String courseNumber;
	String unitId;
	String classId;
	byte[] file;
	String fileExtension;
	Date date;
	Date final_submission;
	
	public TasksInCourseInClass(String taskId, String courseNumber, String unitId, String classId,
			byte[] file, Date date,Date final_submission,String fileExtension) {
		super();
		this.taskId = taskId;
		this.courseNumber = courseNumber;
		this.unitId = unitId;
		this.classId = classId;
		this.file = file;
		this.date = date;
		this.final_submission = final_submission;
		this.fileExtension = fileExtension;
	}
	
	public TasksInCourseInClass(ArrayList<Object> arrayList) {
		this.taskId = (String)arrayList.get(0);
		this.date = (Date)arrayList.get(1);
		this.final_submission = (Date)arrayList.get(2);
		this.file = (byte[])arrayList.get(3);
		this.courseNumber = (String)arrayList.get(4);
		this.unitId = (String)arrayList.get(5);
		this.classId = (String)arrayList.get(6);
		this.fileExtension = (String)arrayList.get(7);
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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

	public byte[] getFile() {
		return file;
	}

	public void setFile(byte[] file) {
		this.file = file;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getFinal_submission() {
		return final_submission;
	}

	public void setFinal_submission(Date final_submission) {
		this.final_submission = final_submission;
	}
	
}
