package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import common.Packet;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import enums.UserType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;


/**
 *this class handles the deletion of users
 *get the users list from the data base through packets 
 */

public class DeleteUserController implements Initializable,ControllerIF{

	ArrayList<Object> data;

	@FXML
	TableView<Object> table;

	Long lastClick;
	
	int lastPickedSelectionIndex;
   /**
     * this method handles the packet response 
     * gets requests and sets them in the right places in the GUI  
     * @param msg
     */
	@SuppressWarnings("unchecked")
	@Override
	public void onResponse(Object msg) 
	{
		if (msg instanceof Packet)
		{	
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_ARRAY_LIST){
				if (pck.getPacketSub() == PacketSub.GENERIC_GET_USERS){
					System.out.println("Delete user controller 66");

					data = (ArrayList<Object>) pck.getData();
					ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
					
					table.setItems(dataForTable);
					System.out.println("Delete user controller 74");
				}//Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.DELETE_USER,0,usr);
			}else if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.DELETE_USER){
					boolean isAdded = (Boolean)pck.getData();
					if (isAdded){
					    // ... user deleted 
						Main.openPopUpWithMessage("User was deleted successfuly.",AlertType.INFORMATION);
					}else{
					    // ... failed to delete the user
						Main.openPopUpWithMessage("User was NOT deleted.",AlertType.INFORMATION);
					}
					
					Packet pck2 = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GENERIC_GET_USERS,0,Main.user.getType());
					Main.sendToServer(pck2);	
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
		return "Users List";
	}
  /** 
     * initialize function sets right page of GUI to the chosen controller
     * @param URL location
     * @param ResourceBundle resources
     * @return 
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		TableColumn<Object, String> column1 = new TableColumn<Object,String>("Id");
		TableColumn<Object,String> column2 = new TableColumn<Object,String>("User Type");
		TableColumn<Object,String> column3 = new TableColumn<Object,String>("First Name");
		TableColumn<Object,String> column4 = new TableColumn<Object,String>("Last Name");

		column1.setCellValueFactory(new PropertyValueFactory<>("id"));
		column2.setCellValueFactory(new PropertyValueFactory<>("type"));
		column3.setCellValueFactory(new PropertyValueFactory<>("pname"));	
		column4.setCellValueFactory(new PropertyValueFactory<>("fname"));	
				
		column1.prefWidthProperty().bind(table.widthProperty().divide(9));
		column2.prefWidthProperty().bind(table.widthProperty().divide(4));
		column3.prefWidthProperty().bind(table.widthProperty().divide(3));
		column4.prefWidthProperty().bind(table.widthProperty().divide(3));
		
		table.getColumns().add(column1);
		table.getColumns().add(column2);
		table.getColumns().add(column3);
		table.getColumns().add(column4);
		
		lastClick = System.currentTimeMillis();
		lastPickedSelectionIndex = -1;

		Main.setRightPaneController(this);		
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GENERIC_GET_USERS,0,Main.user.getType());
		Main.sendToServer(pck);		
	}
 
	/**
	 * method that get executed when clicking on Submit Button
	 */
	@FXML 
	public void onClickDeleteUser(){
		
		if(table.getSelectionModel().getSelectedIndex()  == -1  )
			return;
		
		User usr = (User) table.getSelectionModel().getSelectedItem();
				
		if(Main.user.getType() == UserType.system_manager ){
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Are you sure you want to delete the user?");
			alert.setContentText("You choice will take effect immediately and the user will be DELETE from the system.");

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK){
			    // ... user chose OK
				Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.DELETE_USER,0,usr);
				Main.sendToServer(packet);		
				
			}
	
		}
		
	}


}
