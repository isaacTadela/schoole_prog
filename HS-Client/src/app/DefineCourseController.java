package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
/**
 * controller for define course window
 */

public class DefineCourseController implements Initializable,ControllerIF {

	@FXML
	Label fullCourseNameLabel;
	
	@FXML
    ComboBox<String> acaUnit;
	
	@FXML
	MenuButton courseChoiceBox;
	
	@FXML
	TextField courseNum;
	@FXML
	TextField courseName;	
	@FXML
	TextField weeklyHours;		
	@FXML
	Label warnLabel;
	
	/**
	 * method that receive response from server while this controller is active
	 */
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_ARRAY_LIST){
				
				if (pck.getPacketSub() == PacketSub.DEFINE_COURSE_GET_ACADEMIC_UNITS){
					ArrayList<ArrayList<String>> resArr = (ArrayList<ArrayList<String>>)pck.getData();					
					for (int i=0;i<resArr.size();i++){
						acaUnit.getItems().addAll( (String)resArr.get(i).get(0)+" " +(String)resArr.get(i).get(1));
					}
					acaUnit.getSelectionModel().select(0);
				}
				if (pck.getPacketSub() == PacketSub.DEFINE_COURSE_GET_COURSES){
					ArrayList<ArrayList<Object>> resArr = (ArrayList<ArrayList<Object>>)pck.getData();
					for (int i=0;i<resArr.size();i++){
						CheckMenuItem temp = new CheckMenuItem(resArr.get(i).get(1)+(String)resArr.get(i).get(0));
						temp.setMnemonicParsing(false);
						courseChoiceBox.getItems().add(temp);
					}
				}
				
			}else if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.DEFINE_COURSE_SUBMIT){
					boolean isAdded = (Boolean)pck.getData();
					if (isAdded){
						//TODO show message course added successfuly.
						//System.out.println("course add success");
						Main.openPopUpWithMessage("Course was added successfuly.",AlertType.INFORMATION);
					}else{
						//TODO show message course failed to be added.
						Main.openPopUpWithMessage("Adding course failed.",AlertType.ERROR);
					}
					
					courseChoiceBox.getItems().clear();
					String where = "";
					Packet packet  = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.DEFINE_COURSE_GET_COURSES,0,where);
					Main.sendToServer(packet);
				}
			
			}
		}
	}

	/**
	 * method that get executed when clicking on Submit Button
	 */
	@FXML 
	public void onSubmitClick(){
		Float hours;
		try{
			hours = Float.parseFloat(weeklyHours.getText());
		}catch(NumberFormatException e){
			hours = null;
		}
		
		if (courseNum.getText().length() == 0){
			setWarning("You must give course a number in order to proceed!");
			return;
		}
		if (courseName.getText().length() == 0){
			setWarning("You must give course a name in order to proceed!");
			return;
		}
		if ( acaUnit.getItems().isEmpty() ){
			setWarning("You must choose an acadmic unit in order to proceed!");
			return;
		}
		if (hours != null){
			Packet pck = sendRequestDefineCoursePacket(courseNum.getText(),courseName.getText(),
					getUnitSelectionId() ,weeklyHours.getText(),getPredCoursesSelectedAsString().toString());
			Main.sendToServer(pck);	
		}else{
			setWarning("Weekly hours must be a float in order to proceed!");
		}
		
	}
	
	/**
	 * Raw packet builder method which builds the message to be sent to server with information about
	 * the course that we wish to create. 
	 * @param courseNum course number
	 * @param courseName course name
	 * @param unitNumber academic unit number
	 * @param weeklyHours course weekly hours of teaching
	 * @param predStringArray string of array that holds all the preds courses for the course, example: "["02001","02002"]"
	 */
	
	public static Packet sendRequestDefineCoursePacket(String courseNum, String courseName, String unitNumber, String weeklyHours, String predStringArray){
		ArrayList<String> dataArr = new ArrayList<>();
		dataArr.add(courseNum);
		dataArr.add(courseName);
		dataArr.add(unitNumber);
		dataArr.add(weeklyHours);
		dataArr.add(predStringArray);
		Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.DEFINE_COURSE_SUBMIT,0,dataArr);
		return packet;	
	}
	
	/**
	 * method that gets run whenever this controller enters in action for its FXML window
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
	
		String where = "";
		Packet packet = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.DEFINE_COURSE_GET_ACADEMIC_UNITS,0,where);
		Main.sendToServer(packet);	
		
		packet = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.DEFINE_COURSE_GET_COURSES,0,where);
		Main.sendToServer(packet);	
		
	}
	/**
	 * method used to show errors or warnings in the correspondent FXML files 
	 * @param msg
	 */
	private void setWarning(String msg){
		warnLabel.setText(msg);
		warnLabel.setVisible(true);	
	}
	
	/**
	 * method runs whenever we choose academic unit in define course page
	 */
	@FXML
	public void onSelectUnitClick(){
		updateCourseFullName();	
	}
	
	/**
	 * method that run whenever change in course number or academic unit chosen
	 * in order to update the full course name that will be created.
	 */
	private void updateCourseFullName(){
		String out = getUnitSelectionId();	
		fullCourseNameLabel.setText(out + courseNum.getText());
	}
	
	/**
	 * method obtains the academic unit chosen for the course.
	 * @return academic unit id
	 */
	private String getUnitSelectionId(){
		String textUnit =  acaUnit.getValue().substring(0, 2);
		return textUnit;	
	}
	
	/**
	 * method that transforms chosen predecessor courses into string that will later be reparsed in server.
	 * @return string of the chosen predecessors
	 */
	private ArrayList<String> getPredCoursesSelectedAsString(){
		ObservableList<MenuItem> arr =  courseChoiceBox.getItems();
		ArrayList<String> preds = new ArrayList<>();
		for (int i=0;i<arr.size();i++){
			CheckMenuItem temp = (CheckMenuItem) arr.get(i);
			if (temp.isSelected()){
				preds.add(temp.getText()); // .substring(0,temp.getText().length()-2)
			}
		}
		return preds;
	}

	/**
	 * method contains title of page name, in case of popups, the popup will take this title.
	 */
	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Define Course";
	}
	
	

}
