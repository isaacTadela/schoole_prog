package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.CourseInClass;
import common.Packet;
import common.StudentInCourse;
import common.TasksInCourseInClass;
import enums.PacketId;
import enums.PacketSub;
import enums.UserType;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
/*
 * controller for gird view window for teacher for the courses he teaches.
 */
public class TeacherEvaluationController implements Initializable,ControllerIF {

	@FXML
	Label titleLabel;
	
	@FXML
	GridPane folderGridPane;
	
	@FXML
	Label emptyLabel;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){	
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.TEACHER_CONTROLLER_MY_COURSES){
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							setGridButtons((ArrayList<CourseInClass>)pck.getData());
						}
					});
				}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		return "Teacher Courses";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		ArrayList<String> arr = new ArrayList<>();
		String requiredTeacherId = Main.user.getId();
		
		arr.add(requiredTeacherId);
		
		titleLabel.setText("My Courses");
		
		arr.add(SemesterController.chosenSemester.getId().toString());
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.TEACHER_CONTROLLER_MY_COURSES,0,arr);
		Main.sendToServer(pck);
	}

	/**
	 * method displays all the courses a teacher teaches in chosen semester
	 * @param ArrayList of CoursesInClass to show
	 */
	private void setGridButtons(ArrayList<CourseInClass> arrayList){
	
		if (arrayList.size()>0){
			emptyLabel.setVisible(false);
		}else{
			emptyLabel.setVisible(true);
		}
		for (int j=0;j<arrayList.size()/3+1;j++){
			folderGridPane.getRowConstraints().add(j, new RowConstraints(120));
			for (int i=0;i<3 && new Integer(j*3+i) < arrayList.size();i++){

				Integer index = new Integer(j*3+i);
				ImageButton b = new ImageButton(arrayList.get(index).getCourse().getName()  ,new Image(Main.class.getResourceAsStream("resources/folder_icon.png")));
				b.setAlignment(Pos.CENTER);
				b.setOnMouseClicked(new EventHandler<MouseEvent>(){
					 
			          @Override
			          public void handle(MouseEvent arg0) {
			             Main.showNewRightPane("grid_items_list_layout.fxml",new TeacherStudentInCourse() ,arrayList.get(index)  );
			          }
			          
			      });
				folderGridPane.add(b, i, j);
				folderGridPane.setHalignment(b, HPos.CENTER);
			}
		}		
	}
	
	
}
