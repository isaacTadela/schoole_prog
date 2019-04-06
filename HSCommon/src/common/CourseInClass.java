package common;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * 
 * @author saree
 *this class is course in class class
 */
public class CourseInClass implements Serializable{

	
	private Course course;
	private SchoolClass _class;
	private ArrayList<StudentInCourse> studentsInCourse;
	private String teacherId;
	private Integer semesterId;
	private ArrayList<String> tasksList;
	
	public CourseInClass(Course course,SchoolClass _class,ArrayList<StudentInCourse> studentsInCourse, String teacherId,Integer semesterId,ArrayList<String> tasksList)
	{
		this._class=_class;
		this.course=course;
		this.semesterId=semesterId;
		this.studentsInCourse=studentsInCourse;
		this.tasksList=tasksList;
		this.teacherId=teacherId;
	}
	
	public CourseInClass(ArrayList<Object> arr)
	{
		this._class = new SchoolClass((String)arr.get(2));
		this.course = (Course)arr.get(0);
		this.semesterId=(Integer)arr.get(4);
		this.teacherId=(String)arr.get(3);
	}
	
	public static CourseInClass getFullInstance(ArrayList<Object> arr){
		Course course = new Course((String)arr.get(0),(String)arr.get(1));
		course.setName((String)arr.get(5));
		SchoolClass schoolClass = new SchoolClass((String)arr.get(2),(String)arr.get(6));
		CourseInClass courseInClass = new CourseInClass(course,schoolClass,null,(String)arr.get(3),(Integer)arr.get(4),null);
		return courseInClass;
	}
	
	
	public CourseInClass(String unitNumber,String courseNumber,String courseName,Integer weeklyHours, String teacherId)
	{
		this.course=new Course(unitNumber,courseNumber,courseName,weeklyHours);
		this.teacherId=teacherId;
	}
	
	public String getCourseId() {
		return course.getId();
	}
	
	public Course getCourse() {
		return course;
	}
	
	public void setCourse(Course course) {
		this.course = course;
	}
	
	public SchoolClass get_class() {
		return _class;
	}
	
	public void set_class(SchoolClass _class) {
		this._class = _class;
	}
	
	public ArrayList<StudentInCourse> getStudentsInCourse() {
		return studentsInCourse;
	}
	
	public void setStudentsInCourse(ArrayList<StudentInCourse> studentsInCourse) {
		this.studentsInCourse = studentsInCourse;
	}
	
	public String getTeacherId() {
		return teacherId;
	}
	
	public void setTeacherId(String teacherId) {
		this.teacherId = teacherId;
	}
	
	public Integer getSemesterId() {
		return semesterId;
	}
	
	public void setSemesterId(Integer semesterId) {
		this.semesterId = semesterId;
	}
	
	public ArrayList<String> getTasksList() {
		return tasksList;
	}
	
	public void setTasksList(ArrayList<String> tasksList) {
		this.tasksList = tasksList;
	}
	
	public String toString() {
		return _class.getName() + "[" + course.getId() + " " + course.getName() + "]";
	}
	
}
