package app;

import java.awt.Button;
import java.net.URL;
import java.util.ResourceBundle;

import common.CourseInClass;
import common.StudentInCourse;
import enums.UserType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/*
 * controller for showing details about course for teacher
 */
public class TeacherCourseDetailsController implements Initializable,ControllerIF {
	StudentInCourse stdInCourse;

	@FXML
	Label courseNum;
	@FXML
	Label courseName;
	@FXML
	Label weeklyHours;
	@FXML
	Label grade;
	@FXML
	Label note;
	
	@FXML
	Button editButton;
	
	
	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Course Page";
	}

	@FXML
	public void onCourseTasksClick(){
		Main.showNewRightPane("grid_items_list_layout.fxml",new TasksInCourseController(),stdInCourse);
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			stdInCourse = (StudentInCourse)bundle.getData();
			courseNum.setText(stdInCourse.getCourse().getNumber() );
			courseName.setText(stdInCourse.getCourse().getName());
			weeklyHours.setText(stdInCourse.getCourse().getWeeklyHours().toString());
			if (stdInCourse.getGrade() != null){
				grade.setText(stdInCourse.getGrade().toString());
			}
			if (stdInCourse.getNotes() != null){
				note.setText(stdInCourse.getNotes());
			}
		}
		
		if (Main.user.getType() == UserType.teacher){	
			editButton.setVisible(true);
		}
	}
	
}
