package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.AcademicUnit;
import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
/**
 * class used as controller for school manager data watch requests window
 *
 */
public class ObtainInformationController implements Initializable, ControllerIF{
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				switch (pck.getPacketSub()){
					case DEFINE_COURSE_GET_ACADEMIC_UNITS: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}
					case GENERIC_GET_SEMESTERS: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}					
					case GENERIC_GET_PARENTS: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}					
					case GENERIC_GET_STUDENTS: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}	
					case GENERIC_GET_TEACHERS: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}					
					case GENERIC_GET_COURSES: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}
					case GENERIC_GET_CLASSES: {
						ArrayList<Object> data = (ArrayList<Object>)pck.getData();
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {
								Main.showNewRightPane("selectionable_table_data_layout.fxml", data);
							}
						});
						break;
					}
					
				}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		return "Obtain Information";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		
	}
	/**
	 * method executed when clicking on teachers button
	 */
	@FXML
	public void onClickGetTeachers(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_TEACHERS,0,"");
		Main.sendToServer(pck);	
		
	}
	/**
	 * method executed when clicking on courses button
	 */	
	@FXML
	public void onClickGetCourses(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_COURSES ,0,"");
		Main.sendToServer(pck);			
	}
	/**
	 * method executed when clicking on classes button
	 */
	@FXML
	public void onClickGetClasses(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_CLASSES,0,"");
		Main.sendToServer(pck);		
	}
	/**
	 * method executed when clicking on semesters button
	 */
	@FXML
	public void onClickGetSemesters(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_SEMESTERS,2,"");
		Main.sendToServer(pck);		
	}
	/**
	 * method executed when clicking on students button
	 */
	@FXML
	public void onClickGetStudents(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_STUDENTS,0,"");
		Main.sendToServer(pck);		
	}
	/**
	 * method executed when clicking on parents button
	 */
	@FXML
	public void onClickGetParents(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_PARENTS,0,"");
		Main.sendToServer(pck);
	}
	/**
	 * method executed when clicking on academic units button
	 */
	@FXML
	public void onClickGetAcademicUnits(){
		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.DEFINE_COURSE_GET_ACADEMIC_UNITS,0,"");
		Main.sendToServer(pck);
	}
}
