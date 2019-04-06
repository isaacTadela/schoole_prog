package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import common.StudentInCourse;
import enums.PacketId;
import enums.PacketSub;
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
/**
 * controller for student folder
 * @author Amir
 *
 */
public class StudentFolderController implements Initializable,ControllerIF {

	@FXML
	Label titleLabel;
	
	@FXML
	GridPane folderGridPane;
	
	@FXML
	Label emptyLabel;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;			//(6)
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
														//(6,1)
				if (pck.getPacketSub() == PacketSub.GET_STUDENT_IN_COURSE){
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
		return "Student Folder";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		ArrayList<String> arr = new ArrayList<>();
		
		String requiredStudentId = Main.user.getId();
		if (resources != null){
			if (resources instanceof DataBundle){
				DataBundle bundle = (DataBundle)resources;
				if (bundle.getData() != null){
					requiredStudentId  = (String)bundle.getData();
				}
			}
		}
		arr.add(requiredStudentId);
		
		
		arr.add(SemesterController.chosenSemester.getId().toString());
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_STUDENT_IN_COURSE,0,arr);
		Main.sendToServer(pck);
	}
	

	/**
	 * methods sets all StudentsInCourse in grid view as folders to click on
	 * @param arr ArrayList of all StudentsInCourse that need to be displayed
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
				ImageButton b = new ImageButton(arr.get(index).getCourse().getName(),new Image(Main.class.getResourceAsStream("resources/folder_icon.png")));
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
