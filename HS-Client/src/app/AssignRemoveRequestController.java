package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.CourseInClass;
import common.Packet;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.TextField;
/**
 * 
 * @author Amir
 *
 */
public class AssignRemoveRequestController implements Initializable,ControllerIF  {

    @FXML
    private TextField userIdTextField;

    @FXML
    private ComboBox<String> userTypeComboBox;

    @FXML
    private ComboBox<String> requestTypeComboBox;

    @FXML
    private ComboBox<CourseInClass> courseInClassComboBox;

    @FXML
    private Label warnLabel;

    /**
     * method executed when clicking on Request Button
     */
    @FXML
    void onSendRequestClick() {
    	if(courseInClassComboBox.getItems() == null || courseInClassComboBox.getItems().size() == 0){
    		warnLabel.setVisible(true);
    		warnLabel.setText("No userInCourse to choose from!");
    		return;
    	}
    	if(userIdTextField.getText().length()==0){
    		warnLabel.setVisible(true);
    		warnLabel.setText("You have to tupe user id");
    		return;
    	}
    	ArrayList <String> arr = new ArrayList<String>();
    	arr.add(userTypeComboBox.getValue());
    	arr.add(requestTypeComboBox.getValue());
    	arr.add(userIdTextField.getText());
    	
    	arr.add(courseInClassComboBox.getValue().getCourse().getNumber());
    	arr.add(courseInClassComboBox.getValue().getCourse().getAcUnit().getId());
    	arr.add(courseInClassComboBox.getValue().get_class().getId());
    	
    	Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.CREATE_NEW_REQUEST,0,arr);
		Main.sendToServer(packet);
        
    }
    
    



	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GENERIC_GET_COURSE_IN_CLASS){
					ArrayList <CourseInClass> courseList=(ArrayList<CourseInClass>)pck.getData();
					ObservableList<CourseInClass> obsList = FXCollections.observableArrayList(courseList);
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							if (obsList.size() > 0){
								courseInClassComboBox.setItems(obsList);
								courseInClassComboBox.setValue(obsList.get(0));
							}
						}
						
					});

				}
			}
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.CREATE_NEW_REQUEST){
					boolean isAdded=(boolean)pck.getData();
				    if(isAdded){
				    	Main.openPopUpWithMessage("Request was sent succesfuly", AlertType.INFORMATION);
				    	
				    }
				    else{
				    	Main.openPopUpWithMessage("Request failed to send", AlertType.ERROR);
				    }
				}
			}
		
		}
		
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Assign/Remove Request";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Main.setRightPaneController(this);

		ObservableList<String> obsList = FXCollections.observableArrayList("teacher","student");
		userTypeComboBox.setItems(obsList);
		userTypeComboBox.setValue(obsList.get(0));
		userTypeComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
            	setRequestTypes();
            }

        });
		setRequestTypes();
		
		
		
		Packet packet = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_COURSE_IN_CLASS,0,SemesterController.currentSemester.getId());
		Main.sendToServer(packet);

	}
	/**
	 * method needed to update UserType combobox to the possible request types
	 */
	private void setRequestTypes(){
		String userType = userTypeComboBox.getValue();
		if (userType.equals("teacher")){
			ObservableList<String> obsList = FXCollections.observableArrayList("assign");
			requestTypeComboBox.setItems(obsList);
			requestTypeComboBox.setValue(obsList.get(0));			
		}else{
			ObservableList<String> obsList = FXCollections.observableArrayList("assign","remove");
			requestTypeComboBox.setItems(obsList);
			requestTypeComboBox.setValue(obsList.get(0));			
		}
		
	}

}
