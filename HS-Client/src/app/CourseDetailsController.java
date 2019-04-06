package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import common.StudentInCourse;
import common.Teacher;
import enums.PacketId;
import enums.PacketSub;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
/**
 * 
 * @author Amir
 *
 */
public class CourseDetailsController implements Initializable,ControllerIF {
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
	@FXML
	TextField gradeTextFild;
	@FXML
	TextField notesTextFild;
	@FXML
	Button submissionButten;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;			
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.EVALUATE_STUDENT_IN_COURSE){
				Boolean isUpdated = (Boolean)pck.getData();
				
				if(isUpdated){
					Main.openPopUpWithMessage("Evaluation Submmited Succssfuly", AlertType.INFORMATION);
					}
				else{
					Main.openPopUpWithMessage("Evaluation Submmited Failed", AlertType.ERROR);
					}
				}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Course Page";
	}
/**
 * method executed when clicking on one oCourse Tasks button
 */
	@FXML
	public void onCourseTasksClick(){
		Main.showNewRightPane("grid_items_list_layout.fxml",new TasksInCourseController(),stdInCourse);
	}
	
	/**
	 * method executed when teacher clicks evaluate button
	 * button visible only for teacher
	 */
	@FXML
	public void onEditButtonClick(){
		ArrayList<String> input = new ArrayList<String>();
		input.add(  gradeTextFild.getText() );
		input.add(  notesTextFild.getText() );
		input.add(stdInCourse.getStudentId());
		input.add(stdInCourse.getCourse().getNumber() );
		input.add(stdInCourse.getCourse().getAcUnit().getId() );
		input.add(stdInCourse.getClassId() );
		
		Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN ,PacketSub.EVALUATE_STUDENT_IN_COURSE ,0,input);
		Main.sendToServer(pck);
		
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
			//visible to submit
			if(Main.user instanceof Teacher  ){
				editButton.setVisible(true);
				gradeTextFild.setVisible(true);
				notesTextFild.setVisible(true);
				
				if (stdInCourse.getGrade() != null){
					gradeTextFild.setText(stdInCourse.getGrade().toString());
				}
				if (stdInCourse.getNotes() != null){
					notesTextFild.setText(stdInCourse.getNotes());
				}
			}
			
		}
	
	}
	
}
