package app;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.CourseInClass;
import common.Packet;
import common.StudentInCourse;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
/**
 * controller for teacher gird window when he need to see studenst he has for specific course.
 * @author Amir
 *
 */
public class TeacherStudentInCourse implements ControllerIF,Initializable {
	
	@FXML
	Label titleLabel;
	
	@FXML
	GridPane folderGridPane;
	
	@FXML
	Label emptyLabel;
	
	ArrayList<User> parentChildren;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GET_STUDENT_IN_TEACHER_COURSE){
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							setGridButtons((ArrayList<StudentInCourse>)pck.getData());
						}
					});
				}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Student in course Page";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources != null){
			titleLabel.setText("Student in Course");
			DataBundle resources2= (DataBundle)resources;
			
			Main.setRightPaneController(this);
			CourseInClass thisCourse = (CourseInClass)resources2.getData();
			Packet pck = getStudentsInTeachersCoursePacket(thisCourse.getCourse().getNumber(),thisCourse.getCourse().getAcUnit().getId(),thisCourse.get_class().getId(),SemesterController.chosenSemester.getId().toString() );
			Main.sendToServer(pck);
		}
	}
	
	public static Packet getStudentsInTeachersCoursePacket(String courseNumber,String academicUnit,String classId,String semesterId){
		ArrayList<String> arr = new ArrayList();
		arr.add(courseNumber);
		arr.add(academicUnit);
		arr.add(classId);
		arr.add(semesterId);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_STUDENT_IN_TEACHER_COURSE,0,arr);
		return pck;
	}
	
	
	
	/**
	 * method displays all the students for a course the teacher teaches in chosen semester
	 * @param ArrayList of StudentInCourse to show
	 */
	private void setGridButtons(ArrayList<StudentInCourse> arr){
		if (arr.size()>0){
			emptyLabel.setVisible(false);
		}else{
			emptyLabel.setVisible(true);
		}
		for (int j=0;j<arr.size()/3+1;j++){
			folderGridPane.getRowConstraints().add(j, new RowConstraints(120));
			for (int i=0;i<3 && new Integer(j*3+i) < arr.size();i++){

				Integer index = new Integer(j*3+i);
				ImageButton b = new ImageButton(arr.get(index).getStudentId(),new Image(Main.class.getResourceAsStream("resources/folder_icon.png")));
				b.setAlignment(Pos.CENTER);
				b.setOnMouseClicked(new EventHandler<MouseEvent>(){
					 
			          @Override
			          public void handle(MouseEvent arg0) {
			        	  Main.showNewRightPane("course_details_layout.fxml",arr.get(index));
			          }
			 
			      });
				folderGridPane.add(b, i, j);
				folderGridPane.setHalignment(b, HPos.CENTER);
			}
		}		
	}

}
