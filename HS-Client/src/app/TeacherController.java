package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import common.Teacher;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;


// prototype NOT USED JUST FOR LEARN
public class TeacherController implements Initializable, ControllerIF{

	@FXML
	TableView<Teacher> table;
	@FXML
	TextField searchTextField;
	
	@FXML
	Label resultSearchNote;	
	@FXML
	Label updateSearchNote;	
	@FXML
	Label resultSearchLabel;
	@FXML
	Button updateButton;	
	@FXML
	TextField newAUValueTextField;
	@FXML
	ListView<String> resultList;
	String foundId;
	
	
	public void updateTeacherData(String id,String value,Integer columnNo){
        ArrayList<String> arr = new ArrayList<String>();
        arr.add(id);
        arr.add(value);
        if (columnNo==1){
        	arr.add("name");
        }else if (columnNo==2){
        	arr.add("Unit");
        }
        Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.TEACHER_CONTROLLER_UPDATE_ENTITY,0,arr);
        Main.sendToServer(packet);	
	}
	
	
	@FXML public void onUpdateDataButtonClick(){
		updateTeacherData(foundId,newAUValueTextField.getText(),2);
	}

	
    public void onEditAcademicUnitResult(boolean result){
		if (result){
			updateSearchNote.setText("Academic Unit was updated successfuly.");
		}else{
			updateSearchNote.setText("Academic Unit value was not changed.");
		}
	}
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		
		if (table != null){
			table.getColumns().get(0).setCellValueFactory(
	                new PropertyValueFactory<>("id"));
			table.getColumns().get(1).setCellValueFactory(
	                new PropertyValueFactory<>("name"));
			table.getColumns().get(2).setCellValueFactory(
	                new PropertyValueFactory<>("academicUnit"));  
		}else{
			setResultPanelVisibility(false);
		}	
	}
	
	
	private void setResultPanelVisibility(boolean isVisible){
		resultSearchNote.setVisible(isVisible);	
		updateSearchNote.setVisible(isVisible);	
		resultSearchLabel.setVisible(isVisible);
		updateButton.setVisible(isVisible);	
		newAUValueTextField.setVisible(isVisible);	
		resultList.setVisible(isVisible);
	}
	
	
	
	
	@FXML
    public void onSearchButtonClick() {
		//System.out.println("onSearchButtonClick");
		if (!searchTextField.getText().isEmpty()){
			ArrayList<String> arr = new ArrayList<>();
			arr.add("teacher");
			arr.add("id=" + searchTextField.getText());
			arr.add("3");
			Packet packet = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.TEACHER_CONTROLLER_GET_ENTITY,0,arr);
			Main.sendToServer(packet);	
		}
    }

	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				System.out.println("Request update answer: " + (Boolean)(pck.getData()));
				
				Platform.runLater(new Runnable(){@Override public void run() {onEditAcademicUnitResult((Boolean)(pck.getData()));}});
				
			}else{
				//ArrayList<ArrayList<String>> arr = (ArrayList<ArrayList<String>>)pck.getData(); 

				if (pck.getData() instanceof ArrayList){
					ArrayList<ArrayList<String>> arr = (ArrayList<ArrayList<String>>)pck.getData();
					setFirstValueFromListOnScreen(arr);
				}	
			}
		}
	}

	private void setFirstValueFromListOnScreen(ArrayList<ArrayList<String>> arr) {
		if  (arr == null || arr.size() == 0){
			resultSearchNote.setVisible(true);
			Platform.runLater(new Runnable(){@Override public void run() {
				resultSearchNote.setText("No results found!"); 
				resultList.setVisible(false);
	        }});
			
		}else if (arr.size() == 1){
			
			Platform.runLater(new Runnable(){@Override public void run() {
				resultSearchNote.setText("Search result: ");
	        }});
			setResultPanelVisibility(true);
			foundId = arr.get(0).get(0);
			//String result = "id= " + arr.get(0).get(0) +" ,name= " + arr.get(0).get(1) +" ,AcademicUnit= " + arr.get(0).get(2);
            Platform.runLater(new Runnable(){@Override public void run() {
            	//resultSearchLabel.setText(result);
            	
            	ObservableList<String> data = FXCollections.observableArrayList();
            	data.add("Id:				"+arr.get(0).get(0));
            	data.add("Name: 			"+arr.get(0).get(1));
            	data.add("AcademicUnit: 	"+arr.get(0).get(2));
            	resultList.setItems(data);
            
            
            }});
		}else{
            Platform.runLater(new Runnable(){@Override public void run() {
            renewTable(arr);
            
        }});
	}
}
	
	private void renewTable(ArrayList<ArrayList<String>> arr){
		ObservableList<Teacher> data = FXCollections.observableArrayList();
		for(int i=0;i<arr.size();i++){
			//Teacher t = new Teacher(arr.get(i).get(0),arr.get(i).get(1),arr.get(i).get(2));
			//data.add(t);
		}
		table.setItems(data);	
	}
	
	
	@FXML
	public void onRefreshButtonClick(){
		ArrayList<String> arr = new ArrayList<>();
		arr.add("teacher");
		arr.add("");
		arr.add("3");
		Packet packet = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.TEACHER_CONTROLLER_GET_ENTITY,0,arr);
		Main.sendToServer(packet);		
		
	}


	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "teacher page";
	}
	
	
}
