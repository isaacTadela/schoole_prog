package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import common.Packet;
import common.SchoolClass;
import common.Semester;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
/*
 * controller for open new semester window
 */
public class SemesterController implements Initializable,ControllerIF{

	
	@FXML
    ComboBox<String> seasonsbox;
	@FXML
	Label currentSemesterLabel;
	
	public static Semester currentSemester;
	public static Semester chosenSemester;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GET_LATEST_SEMESTER){
					currentSemester = (Semester)pck.getData();
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							currentSemesterLabel.setText("Current semester is #" + currentSemester.getId() + ", its " + currentSemester.getSeason() + " season and it was opened in " + currentSemester.getOpen_date());
						}
					});
				}
			}
			
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.OPEN_NEW_SEMESTER){
					Boolean isAdded = (Boolean)pck.getData();
					if (isAdded){
						Main.openPopUpWithMessage("New Semester was created!", AlertType.INFORMATION);
					}else{
						Main.openPopUpWithMessage("New Semester Creation failed!", AlertType.ERROR);
					}
				}
			}			
			
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Semester Page";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		requestCurrentSemesterData(0);
		seasonsbox.getItems().addAll("Winter","Spring","Summer");
		seasonsbox.getSelectionModel().select(0);
	}
	/**
	 * method executed when clicking on confirm opening new semester.
	 */
	@FXML
	public void onConfirmClick(){
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {		
				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle(getWindowTitle());
				alert.setHeaderText("Are you really sure you want to open new semester?");
				alert.setContentText("click OK only if you really plan to create new semester.");
				Optional<ButtonType> result = alert.showAndWait();

				if ((result.isPresent()) && (result.get() == ButtonType.OK)) {	
					ArrayList<String> details = new ArrayList<>();
					String season = seasonsbox.getValue();
					details.add(season);
					details.add(currentSemester.getId().toString());
					Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.OPEN_NEW_SEMESTER,0,details);
					Main.sendToServer(pck);
				}
			}
		});
	}
/**
 * method checks if the chosen semester is indeed the latest/current semester.
 * @return
 */
	public static boolean isChosenSemesterLatest(){
		if (chosenSemester.getId().intValue() == currentSemester.getId().intValue()){
			return true;
		}
		return false;
	}	
/**
 * static method requests obtaining the latets semester class	
 * @param stage because method is static it requires an available stage to update on
 */
	public static void requestCurrentSemesterData(int stage){
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_LATEST_SEMESTER,stage,"");
		Main.sendToServer(pck);
	}
	
}
