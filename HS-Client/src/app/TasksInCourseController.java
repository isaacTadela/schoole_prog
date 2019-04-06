package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import common.StudentInCourse;
import common.TasksInCourseInClass;
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
 * controller for tasks in course grid view
 * @author Amir
 *
 */
public class TasksInCourseController implements ControllerIF,Initializable {
	
	@FXML
	Label titleLabel;
	
	@FXML
	GridPane folderGridPane;
	
	@FXML
	Label emptyLabel;
	
	StudentInCourse stdInCourse;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GET_STUDENT_IN_COURSE_TASKS){
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							setGridButtons((ArrayList<TasksInCourseInClass>)pck.getData());
						}
					});
				}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Tasks Page";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources != null){
			if (resources instanceof DataBundle){
				DataBundle bundle = (DataBundle)resources;
				stdInCourse = (StudentInCourse)bundle.getData();
				titleLabel.setText("Tasks Page for course " + stdInCourse.getCourse().getName());
				
				Main.setRightPaneController(this);
				Packet pck = requestGetStudentInCoursePacket(stdInCourse.getCourse().getNumber(),stdInCourse.getCourse().getAcUnit().getId(),stdInCourse.getClassId(),SemesterController.chosenSemester.getId().toString());
				Main.sendToServer(pck);
			}
		}
	}
	
	public static Packet requestGetStudentInCoursePacket(String courseNumber,String acUnit,String classId,String semesterId){
		ArrayList<String> arr = new ArrayList<>();
		arr.add(courseNumber);
		arr.add(acUnit);
		arr.add(classId);
		arr.add(semesterId);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_STUDENT_IN_COURSE_TASKS,0,arr);		
		return pck;
	}
	
	
	
	/**
	 * method used to display all the TaskInCourseInClass items
	 * @param arr ArrayList of TasksInCourseInClass items
	 */
	private void setGridButtons(ArrayList<TasksInCourseInClass> arr){
		if (arr.size()>0){
			emptyLabel.setVisible(false);
		}else{
			emptyLabel.setVisible(true);
		}
		for (int j=0;j<arr.size()/3+1;j++){
			folderGridPane.getRowConstraints().add(j, new RowConstraints(120));
			for (int i=0;i<3 && new Integer(j*3+i) < arr.size();i++){

				Integer index = new Integer(j*3+i);
				ImageButton b = new ImageButton(arr.get(index).getTaskId(),new Image(Main.class.getResourceAsStream("resources/folder_icon.png")));
				b.setAlignment(Pos.CENTER);
				b.setOnMouseClicked(new EventHandler<MouseEvent>(){
					 
			          @Override
			          public void handle(MouseEvent arg0) {
			        	 ArrayList<Object>data = new ArrayList<Object>();
			        	 data.add(arr.get(index));
			        	 data.add(stdInCourse);
			             Main.showNewRightPane("task_details_layout.fxml",data);
			          }
			 
			      });
				folderGridPane.add(b, i, j);
				folderGridPane.setHalignment(b, HPos.CENTER);
			}
		}		
	}

}
