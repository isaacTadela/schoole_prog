package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import common.Course;
import common.CourseInClass;
import common.Packet;
import common.SchoolClass;
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
import javafx.util.StringConverter;
import javafx.scene.control.Alert.AlertType;


/**
 * 
 * @author Amir
 *
 */
public class AssignTeacherToCourseInClass implements Initializable, ControllerIF{

	@FXML
	ComboBox<SchoolClass> classesList;
	
	@FXML 
	ComboBox<CourseInClass> coursesList;
	
	@FXML
	ComboBox<Teacher> teachersList;
	
	@FXML
	Label warningMsg;
	
	@FXML
	Label currentTeacherLabel;
	
	Teacher currentTeacherInCourse;

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
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							currentTeacherLabel.setText("");
						}
					});
				}
				if (pck.getPacketSub() == PacketSub.GET_COURSES_IN_SPECIFIC_CLASS_IN_CURRENT_SEMESTER)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<CourseInClass> listOfCourses= new ArrayList<CourseInClass>();
					for (int i=0; i<resultList.size(); i++)
					listOfCourses.add(new CourseInClass((String)resultList.get(i).get(0),(String)resultList.get(i).get(1),(String)resultList.get(i).get(2),(Integer)resultList.get(i).get(3),(String)resultList.get(i).get(4)));
					ObservableList<CourseInClass> obsList = FXCollections.observableArrayList(listOfCourses);
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
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							currentTeacherLabel.setText("");
						}
					});
				}
				if (pck.getPacketSub()==PacketSub.GET_SCHOOL_TEACHERS_IN_SPECIFIC_UNIT)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<Teacher> listOfTeachers = new ArrayList<Teacher>();
					for (int i=0; i<resultList.size(); i++)
						if (!coursesList.getValue().getTeacherId().equals((String)resultList.get(i).get(0)))
							listOfTeachers.add(new Teacher(resultList.get(i)));
						else
						{
							currentTeacherInCourse=new Teacher(resultList.get(i));
							Platform.runLater(new Runnable(){
								@Override 
								public void run() {		
									currentTeacherLabel.setText("Current teacher: " + currentTeacherInCourse);
								}
							});
							
						}
							
					ObservableList<Teacher> obsList= FXCollections.observableArrayList(listOfTeachers);
					teachersList.setItems(obsList);
				}
			}
			if (pck.getPacketId()==PacketId.REQUIRE_BOOLEAN)
			{
				if(pck.getPacketSub()==PacketSub.ASSIGN_TEACHER_TO_COURSE_IN_CLASS)
				{
					if ((Boolean)pck.getData()==false)
					{
						Main.openPopUpWithMessage("Assigning teacher has failed for some reasen!",AlertType.ERROR);
						return;
					}
					SchoolClass _class = classesList.getValue();
					Course course=coursesList.getValue().getCourse();
					Teacher newTeacher =teachersList.getValue();
					Integer newWorkingHours = newTeacher.getCurrentHours()+course.getWeeklyHours();
					String msg1 = newTeacher.getPname()+" "+newTeacher.getFname() + " ("+newTeacher.getId()+") has been appointed as the teacher of course "+course.getName()+ " in class " + _class.getName()+".\nThe new number of working hours for "+ newTeacher.getPname()+" "+newTeacher.getFname()+" is "+newWorkingHours+".\n";
					String msg2 = currentTeacherInCourse.getPname()+" "+currentTeacherInCourse.getFname() + " ("+currentTeacherInCourse.getId()+") has been removed from teaching the course "+course.getName()+ " in class " + _class.getName()+".";
					Main.openPopUpWithMessage(msg1+msg2,AlertType.INFORMATION);
					currentTeacherInCourse=null;
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							teachersList.getItems().clear();
							coursesList.getItems().clear();
							classesList.getItems().clear();
							Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_CLASSES,0,"");
							Main.sendToServer(pck);
						}});

					}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Assign teacher to course in class";
	}
	/*
	 * method for requesting teachers list from server
	 */
	public void updateTeachersList()
	{
		CourseInClass chosenCourseInClass;
		if ((chosenCourseInClass= coursesList.getValue())==null)
			return;
		String unitNumber=chosenCourseInClass.getCourse().getAcUnit().getId();// get unit number
		teachersList.getItems().clear();
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_TEACHERS_IN_SPECIFIC_UNIT,0,unitNumber);
		Main.sendToServer(pck);
	}
	/**
	 * method used to request course list from server
	 */
	public void updateCoursesList()
	{
		SchoolClass chosenClass=classesList.getValue();
		if (chosenClass==null)
			return;
		String classId=chosenClass.getId();
		teachersList.getItems().clear();
		coursesList.getItems().clear();
		ArrayList<String> dataArr = new ArrayList<String>();
		String currentSemesterId = SemesterController.currentSemester.getId()+"";
		dataArr.add(currentSemesterId);
		dataArr.add(classId);
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_COURSES_IN_SPECIFIC_CLASS_IN_CURRENT_SEMESTER,0,dataArr);
		Main.sendToServer(pck);
	}
	/**
	 * method gets executed when clicking on assign button.
	 */
	public void onClickAssignTeacherToCourse()
	{
		SchoolClass chosenClass=classesList.getValue();
		Teacher chosenTeacher=teachersList.getValue();
		CourseInClass chosenCourse=coursesList.getValue();
		if (chosenCourse==null||chosenClass==null||chosenTeacher==null)
		{
			warningMsg.setText("Please enter all the needed details.");
			return;
		}
		if(chosenTeacher.getCurrentHours()+chosenCourse.getCourse().getWeeklyHours()>chosenTeacher.getMaxAllowedHours())
		{
			warningMsg.setText("The chosen teacher cannot teach this course due to his maximum hours limit.");
			return;
		}
		Integer newWorkingHoursForNewTeacher = chosenTeacher.getCurrentHours()+chosenCourse.getCourse().getWeeklyHours();
		Integer newWorkingHoursForOldTeacher = currentTeacherInCourse.getCurrentHours()-chosenCourse.getCourse().getWeeklyHours();
		ArrayList<String> dataArray = new ArrayList<String>(
				Arrays.asList(
						chosenCourse.getCourse().getNumber(),
						chosenCourse.getCourse().getAcUnit().getId(),
						chosenClass.getId(),
						chosenTeacher.getId(),
						newWorkingHoursForNewTeacher+"",
						currentTeacherInCourse.getId(),
						newWorkingHoursForOldTeacher+"")
				);
		Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.ASSIGN_TEACHER_TO_COURSE_IN_CLASS,0,dataArray);
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
