package app;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


import common.Packet;
import common.Semester;
import enums.PacketId;
import enums.PacketSub;
import enums.UserType;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.util.Callback;
/*
 * main left pane window controller
 */
public class LeftPaneController implements Initializable,ControllerIF{
	
	@FXML
	Label menuLabel;
	
	
	@FXML
	ComboBox<Semester> chosenSemesterComboBox;
	
	@FXML
	Button backButton;
	
	@FXML
	public void onExitClick(){
		System.exit(0);		
	}
/**
 * method executed on click Logout button
 */
	@FXML
	public void onLogoutClick(){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {
				Main.setUser(null);
				Main.showNewScene("login_layout.fxml");
			}
		});
	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (Main.user != null){
			UserType userType = Main.user.getType();
			Main.setLeftPaneController(this);
			Main.setBackButton(backButton);
			switch (userType.getTypeId()){
				case 1:{
					menuLabel.setText("System Manager Menu");
					Main.showNewLeftPane("leftpane_system_manager.fxml");
					break;
				}
				case 2:{
					menuLabel.setText("Secretary Menu");
					Main.showNewLeftPane("leftpane_secretary.fxml");
					break;
				}
				case 3:{
					menuLabel.setText("School Manager Menu");
					Main.showNewLeftPane("leftpane_school_manager.fxml");
					break;
				}
				case 4:{
					menuLabel.setText("Teacher Menu");
					Main.showNewLeftPane("leftpane_teacher.fxml");
					break;
				}
				case 5:{
					menuLabel.setText("Student Menu");
					Main.showNewLeftPane("leftpane_student.fxml");
					break;
				}
				case 6:{
					menuLabel.setText("Parent Menu");
					Main.showNewLeftPane("leftpane_parent.fxml");
					break;
				}		
			}
			Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_SEMESTERS,0,"");
			Main.sendToServer(pck);
		}	
        chosenSemesterComboBox.valueProperty().addListener(new ChangeListener<Semester>(){

			@Override
			public void changed(ObservableValue observable, Semester oldValue, Semester newValue) {
				if (newValue != null){
					Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_CHOSEN_SEMESTER,0,newValue.getId().toString());
					Main.sendToServer(pck);
				}
			}

        });	
        
        chosenSemesterComboBox.setCellFactory(
                new Callback<ListView<Semester>, ListCell<Semester>>() {
                    @Override public ListCell<Semester> call(ListView<Semester> param) {
                        final ListCell<Semester> cell = new ListCell<Semester>() {
                               @Override public void updateItem(Semester item, 
                                boolean empty) {
                                    super.updateItem(item, empty);
                                    if (item != null) {
                                        setText(item.getSemesterFasionableDescription());    
                                    }
                                    else {
                                        setText(null);
                                    }
                                }
                    };
                    return cell;
                }
            });
	}

	@FXML
	public void onBackClick(){
		Main.onBackClick();
	}

	
	
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GENERIC_GET_SEMESTERS && pck.getStage()==1){
					ArrayList<Semester> semestersList = (ArrayList<Semester>)pck.getData();
					
					SemesterController.currentSemester = (Semester)semestersList.get(semestersList.size()-1);
					SemesterController.chosenSemester = SemesterController.currentSemester;

					Main.cleanRightPane();
					ObservableList<Semester> semesterObsList = FXCollections.observableArrayList(semestersList);
			        
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							System.out.println("1   : "+semesterObsList);
							chosenSemesterComboBox.setItems(semesterObsList);
							System.out.println("2   : "+SemesterController.currentSemester);
							chosenSemesterComboBox.setValue(SemesterController.currentSemester);
						}
					});			        
				}else if (pck.getPacketSub() == PacketSub.GET_CHOSEN_SEMESTER){
					SemesterController.chosenSemester = (Semester)pck.getData();
					System.out.println("chosen semester id: " + SemesterController.chosenSemester.getId());
					Main.cleanRightPane();
				}
			}
		}	
	}

	@Override
	public String getWindowTitle() {
		return "Main Menu";
	}
}
