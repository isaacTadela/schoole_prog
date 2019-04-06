package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import common.Course;
import common.Packet;
import common.SchoolClass;
import common.Teacher;
import enums.PacketId;
import enums.PacketSub;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;


/**
 * 
 * @author Kareem
 *
 */

public class AssignClassToCourse implements Initializable, ControllerIF {
	
	@FXML
	ComboBox<SchoolClass> classesList;
	
	@FXML 
	ComboBox<Course> coursesList;
	
	@FXML
	ComboBox<Teacher> teachersList;
	
	@FXML
	Label warningMsg;
	
	String lastChosenAcademicUnit="";

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
					classesList.setItems(obsList);
				}
				if (pck.getPacketSub() == PacketSub.DEFINE_COURSE_GET_COURSES)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<Course> listOfCourses = new ArrayList<Course>();
					for (int i=0; i<resultList.size(); i++)
						 listOfCourses.add(new Course(resultList.get(i)));
					ObservableList<Course> obsList = FXCollections.observableArrayList(listOfCourses);
					coursesList.setItems(obsList);
				}
				if (pck.getPacketSub()==PacketSub.GET_SCHOOL_TEACHERS_IN_SPECIFIC_UNIT)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<Teacher> listOfTeachers = new ArrayList<Teacher>();
					for (int i=0; i<resultList.size(); i++)
						listOfTeachers.add(new Teacher(resultList.get(i)));
					ObservableList<Teacher> obsList= FXCollections.observableArrayList(listOfTeachers);
					teachersList.setItems(obsList);
				}
				if (pck.getPacketSub()==PacketSub. ASSIGN_STUDENTS_IN_CLASS_TO_COURSE_ONLY_THOSE_WHO_HAVE_ALL_PRECOURSES_AND_GET_THEM)
				{
					ArrayList<ArrayList<ArrayList<Object>>> resultList = (ArrayList<ArrayList<ArrayList<Object>>>)pck.getData();
					ArrayList<ArrayList<Object>> allStudentsInClass = resultList.get(0);
					ArrayList<ArrayList<Object>> studentsInClassThatHasAllPreCourses = resultList.get(1);
					ArrayList<String> allStudentsInClassStringList = new ArrayList<String>();
					ArrayList<String> studentsInClassThatHasAllPreCoursesStringList = new ArrayList<String>();
					for (int i=0; i<allStudentsInClass.size(); i++)
						allStudentsInClassStringList.add((String)allStudentsInClass.get(i).get(0));
					for (int i=0; i<studentsInClassThatHasAllPreCourses.size(); i++)
						studentsInClassThatHasAllPreCoursesStringList.add((String)studentsInClassThatHasAllPreCourses.get(i).get(0));
					allStudentsInClassStringList.removeAll(studentsInClassThatHasAllPreCoursesStringList);
					ArrayList<String> studentsThatAreNotAssigned=allStudentsInClassStringList;
					SchoolClass chosenClass=classesList.getValue();
					Teacher chosenTeacher=teachersList.getValue();
					Course chosenCourse=coursesList.getValue();	
					String assignMsg = chosenClass.getName()+" class has been assigned to course "+chosenCourse.getName()+" successfully!\nTeacher in course is "+chosenTeacher.getPname()+" "+chosenTeacher.getFname()+".\n";
					if (studentsThatAreNotAssigned.size()>0)
					{
						String msg1="Students in class have been assigned to course.\nThe following students have not done the needed pre courses\nand have not been assigned to the course.";
						for (int i=0; i<studentsThatAreNotAssigned.size(); i++)
							msg1+="\n"+studentsThatAreNotAssigned.get(i);
						Main.openPopUpWithMessage(assignMsg+msg1,AlertType.INFORMATION);
					}
					else
						Main.openPopUpWithMessage(assignMsg+"\nAll students in class have been assigned to course.",AlertType.INFORMATION);
				}
			}
			if (pck.getPacketId()==PacketId.REQUIRE_BOOLEAN)
			{
				if(pck.getPacketSub()==PacketSub.ASSIGN_COURSE_TO_CLASS)
				{
					boolean isAssigned = (Boolean)pck.getData();
					if (isAssigned==false)
					{
						Main.openPopUpWithMessage("Assigning class failed.\nThis class has already learned this course.",AlertType.ERROR);
						return;
					}
					//the class has been assigned to the course. 
					//now assign all the students in class to the course in class.
						SchoolClass chosenClass=classesList.getValue();
						Course chosenCourse=coursesList.getValue();	
						Main.sendToServer(getPacketAssignStudentsInClassToCourse(chosenClass.getId(),chosenCourse.getAcUnit().getId(),chosenCourse.getNumber()));
				}
				}
			}
	}
	
	public static Packet getPacketAssignStudentsInClassToCourse(String classId,String courseAcUnit,String courseNumber)
	{
		ArrayList<String> dataArray = new ArrayList<String>(Arrays.asList(classId, courseAcUnit,courseNumber));
		return new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.ASSIGN_STUDENTS_IN_CLASS_TO_COURSE_ONLY_THOSE_WHO_HAVE_ALL_PRECOURSES_AND_GET_THEM,0,dataArray);
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Assign class to course";
	}
	
	/**
	 * kariem
	 * takes the class and course and teacher that are chosen by the secretary 
	 * and add that course in class in course_in_class table in database
	 * if course or class or teacher are not chosen then show an error message
	 */
	public void onClickAssignClassToCourse()
	{
		SchoolClass chosenClass=classesList.getValue();
		Teacher chosenTeacher=teachersList.getValue();
		Course chosenCourse=coursesList.getValue();
		if (chosenCourse==null||chosenClass==null||chosenTeacher==null)
		{
			warningMsg.setText("Please enter all the needed details.");
			return;
		}
		if(chosenTeacher.getCurrentHours()+chosenCourse.getWeeklyHours()>chosenTeacher.getMaxAllowedHours())
		{
			warningMsg.setText("The chosen teacher cannot teach this course due to his maximum hours limit.\nPlease choose another teacher.");
			return;
		}
		Integer newWorkingHours = chosenTeacher.getCurrentHours()+chosenCourse.getWeeklyHours();
		ArrayList<String> dataArray = new ArrayList<String>(
				Arrays.asList(
						chosenCourse.getNumber(),
						chosenCourse.getAcUnit().getId(),
						chosenClass.getId(),
						chosenTeacher.getId(),
						SemesterController.currentSemester.getId()+"",
						newWorkingHours+"")
				);
		Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.ASSIGN_COURSE_TO_CLASS,0,dataArray);
		Main.sendToServer(pck);
	}
	
	/**
	 * a method that updates the teachers
	 */
	public void updateTeachersList()
	{
		Course chosenCourse;
		if ((chosenCourse= coursesList.getValue())==null)
			return;
		String unitNumber=chosenCourse.getAcUnit().getId();// get unit number
		if (lastChosenAcademicUnit.equals(unitNumber)) // if its the same unit number then there is no need to look for an update
			return;
		teachersList.getItems().clear();
		lastChosenAcademicUnit=unitNumber;
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_TEACHERS_IN_SPECIFIC_UNIT,0,unitNumber);
		Main.sendToServer(pck);
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_CLASSES,0,"");
		Main.sendToServer(pck);
	    pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.DEFINE_COURSE_GET_COURSES,0,"");
		Main.sendToServer(pck);
	}

}
