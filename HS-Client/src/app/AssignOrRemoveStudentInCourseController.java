package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import common.Course;
import common.CourseInClass;
import common.Packet;
import common.SchoolClass;
import common.Student;
import common.StudentInCourse;
import common.Teacher;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.util.StringConverter;
/**
 * 
 * @author Amir
 *
 */
public class AssignOrRemoveStudentInCourseController implements Initializable, ControllerIF{

	@FXML
	ComboBox<SchoolClass> classesList;
	
	@FXML 
	ComboBox<CourseInClass> coursesList;
	
	@FXML
	TextField studentIdField;
	
	@FXML
	Label warningMsg;
	
	@FXML 
	ListView<Student> studentsInCourseInClass;

	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
		
		if (msg instanceof Packet)
		{
			Packet pck = (Packet)msg;
			if (pck.getPacketId()==PacketId.REQUIRE_ARRAY_LIST)
			{
				if (pck.getPacketSub()==PacketSub.GET_SCHOOL_CLASSES)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<SchoolClass> listOfClasses = new ArrayList<SchoolClass>();
					for (int i=0; i<resultList.size(); i++)
						 listOfClasses.add(new SchoolClass(resultList.get(i)));
					ObservableList<SchoolClass> obsList = FXCollections.observableArrayList(listOfClasses);
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							classesList.setItems(obsList);
						}});
				}
				if (pck.getPacketSub() == PacketSub.GET_COURSES_IN_SPECIFIC_CLASS_IN_CURRENT_SEMESTER)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<CourseInClass> listOfCourses= new ArrayList<CourseInClass>();
					for (int i=0; i<resultList.size(); i++)
					listOfCourses.add(new CourseInClass((String)resultList.get(i).get(0),(String)resultList.get(i).get(1),(String)resultList.get(i).get(2),(Integer)resultList.get(i).get(3),(String)resultList.get(i).get(4)));
					ObservableList<CourseInClass> obsList = FXCollections.observableArrayList(listOfCourses);
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							coursesList.setConverter(new StringConverter<CourseInClass>() {
					              @Override
					              public String toString(CourseInClass cc) {
					                if (cc== null){
					                  return null;
					                } else {
					                  return cc.getCourse().getId()+" "+cc.getCourse().getName();
					                }
					              }

					            @Override
					            public CourseInClass fromString(String id) {
					                return null;
					            }
					        });
							coursesList.setItems(obsList);
						}});

				}
				if (pck.getPacketSub() == PacketSub.GET_STUDENTS_IN_COURSE_IN_CLASS)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<Student> listOfStudents= new ArrayList<Student>();
					for (int i=0; i<resultList.size(); i++)
					listOfStudents.add(new Student((String)resultList.get(i).get(0),(String)resultList.get(i).get(1),(String)resultList.get(i).get(2)));
					ObservableList<Student> obsList = FXCollections.observableArrayList(listOfStudents);
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							studentsInCourseInClass.setItems(obsList);
						}});
				
				}
			}
			if (pck.getPacketId()==PacketId.REQUIRE_BOOLEAN) 
			{
				if (pck.getPacketSub()==PacketSub.ASSIGN_STUDENT_TO_COURSE_IN_CLASS)
				{
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							if((Boolean)pck.getData()==false)
								warningMsg.setText("There is no such student in database.");
							else
							{
								warningMsg.setText("");
								updateStudentsList();
							}				
							}});
				}
			}
			if (pck.getPacketId()==PacketId.REQUIRE_BOOLEAN) 
			{
				if (pck.getPacketSub()==PacketSub.REMOVE_STUDENT_FROM_COURSE_IN_CLASS)
				{
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							if ((Boolean)pck.getData()==false)
								Main.openPopUpWithMessage("Removing failed for some reason!",AlertType.ERROR);
							else
							{
								warningMsg.setText("");
								updateStudentsList();
							}
								
						}});
				}

			}
		}
		
		
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "AssignOrRemove Student in course in class";
	}
	/**
	 * method intended for updating the course list
	 */
	public void updateCoursesList()
	{
		SchoolClass chosenClass=classesList.getValue();
		if (chosenClass==null)
			return;
		String classId=chosenClass.getId();
		studentsInCourseInClass.getItems().clear();
		coursesList.getItems().clear();
		ArrayList<String> dataArr = new ArrayList<String>();
		String currentSemesterId = SemesterController.currentSemester.getId()+"";
		dataArr.add(currentSemesterId);
		dataArr.add(classId);
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_COURSES_IN_SPECIFIC_CLASS_IN_CURRENT_SEMESTER,0,dataArr);
		Main.sendToServer(pck);
	}
	/**
	 * method intended to update student list
	 * 
	 */
	public void updateStudentsList()
	{
		SchoolClass chosenClass = classesList.getValue();
		CourseInClass chosenCourse = coursesList.getValue();
		studentsInCourseInClass.getItems().clear();
		ArrayList<String> dataArray = new ArrayList<String>(
				Arrays.asList(chosenClass.getId(),chosenCourse.getCourse().getAcUnit().getId(),chosenCourse.getCourse().getNumber()));
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_STUDENTS_IN_COURSE_IN_CLASS,0,dataArray);
		Main.sendToServer(pck);
	}
	/**
	 * method gets executed when clicking on Submit
	 */
	@FXML
	public void onClickSubmitStudent()
	{
		SchoolClass chosenClass = classesList.getValue();
		CourseInClass chosenCourse = coursesList.getValue();
		String studentId = studentIdField.getText();
		if ( chosenClass==null||chosenCourse==null)
			{
				warningMsg.setText("Please choose a course and class.");
				return;
			}
		if (studentId==null)
			{
				warningMsg.setText("Please enter student id.");
				return;
			}
		try {
				int stdId = Integer.parseUnsignedInt(studentId);
			}
					catch(NumberFormatException e)
			{
				warningMsg.setText("Please enter a valid student id.");
				return;
			}
				if (studentId.charAt(0) == '+')
			{
				warningMsg.setText("Please enter a valid student id.");
				return;
			}
				ObservableList<Student> obsList =studentsInCourseInClass.getItems();
		for (int i=0; i<obsList.size();i++)
			{
				if(obsList.get(i).getId().equals(studentId))
				{
					warningMsg.setText("Student is already in this course in class!");
					return;
				}
			}
		ArrayList<String> dataArray = new ArrayList<String>(
				Arrays.asList(studentId,chosenClass.getId(),chosenCourse.getCourse().getAcUnit().getId(),chosenCourse.getCourse().getNumber()));
		Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.ASSIGN_STUDENT_TO_COURSE_IN_CLASS,0,dataArray);
		Main.sendToServer(pck);
	}
	/**
	 * method gets executed when clicking on Remove Button
	 */
	public void onClickRemoveStudent()
	{
		SchoolClass chosenClass = classesList.getValue();
		CourseInClass chosenCourse = coursesList.getValue();
		Student chosenStudent = (Student) studentsInCourseInClass.getSelectionModel().getSelectedItem();
		if(chosenStudent==null)
			{
				warningMsg.setText("Please choose a student to remove first.");
					return;
			}
		String studentId = chosenStudent.getId();
		ArrayList<String> dataArray = new ArrayList<String>(
				Arrays.asList(studentId,chosenClass.getId(),chosenCourse.getCourse().getAcUnit().getId(),chosenCourse.getCourse().getNumber()));
		Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.REMOVE_STUDENT_FROM_COURSE_IN_CLASS,0,dataArray);
		Main.sendToServer(pck);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_CLASSES,0,"");
		Main.sendToServer(pck);
		
	}

	
	
	
}
