package app;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.AcademicUnit;
import common.Packet;
import common.SchoolClass;

import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;
/**
 * 
 * @author saree
 *this class is assigns student that doesnt have a class to a certain class
 */
public class AssignStudentToClass implements Initializable,ControllerIF {
   ArrayList <SchoolClass> classList;
    @FXML
     TextField studentIdText;
    @FXML
     ComboBox<SchoolClass> classesList;
    @FXML
     Label warningMsg;
    /**
     * update class column in student table in database
     * @param
     *@return 
     */
    public void OnClickedAddStudent() {
    	
    	try {
    	Integer stdId = Integer.parseInt(studentIdText.getText());
    	}
    	catch (NumberFormatException e)
    	{
    		warningMsg.setText("Please enter a valid student id");
    		return;
    	}
     	if (classesList.getValue()==null)
    	{
    		warningMsg.setText("Please choose a class");
    		return;
    	}

    	ArrayList<String> al=new ArrayList();
    	al.add(studentIdText.getText());
     	al.add(classesList.getValue().getId());
     	Packet pck=new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.UPDATE_STUDENT_CLASS,0,al);
     	Main.sendToServer(pck);
     	
    }
	
    /**
     * this method handles the packet response 
     * gets classes names and sets them in the right places in the GUI and updates the data base with the response update student
     * @param msg
     */
	@Override
	public void onResponse(Object msg) {
		
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
               if (pck.getPacketSub() == PacketSub.GET_CLASSES_NAMES){
				    classList=(ArrayList<SchoolClass>)pck.getData();
				    ObservableList obsList = FXCollections.observableArrayList(classList);
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							classesList.setItems(obsList);
				    		/*
							if(classList.size()!=0)
				    			classesList.setValue(classList.get(0));
				    		*/
						}
				    });
				}
			}
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
	               if (pck.getPacketSub() == PacketSub.UPDATE_STUDENT_CLASS){
	            	   boolean isAdded=(boolean)pck.getData();
	            	   if(isAdded){
	            		   Main.openPopUpWithMessage("student was assigned succesfully",AlertType.INFORMATION);
	            		   warningMsg.setText("");
	            	   }
	            	   else
	            	   {
	            		   Main.openPopUpWithMessage("student cannot be assigned",AlertType.ERROR);
	            		   warningMsg.setText("");
	            	   }
	            		 
	              }
			}
		}
	}
	
	/**
	 * get title window
	 */
	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "assign student to class";
	}
/** 
 * initialize function sets right page of GUI to the choosen controller
 * @param URL location
 * @param ResourceBundle resourses
 */
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		Packet packet = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_CLASSES_NAMES,0,SemesterController.currentSemester.getId());
		Main.sendToServer(packet);	
	}
}


