package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

import common.AcademicUnit;
import common.Course;
import common.CourseInClass;
import common.Packet;
import common.Parent;
import common.Request;
import common.SchoolClass;
import common.Semester;
import common.Student;
import common.Teacher;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import enums.UserType;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
/**
 * 
 * @author ayal
 *this class handels ang get the requests from the data base through packets 
 */
public class RequestsController implements Initializable,ControllerIF{

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
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){	
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				
				if (pck.getPacketSub() == PacketSub.UPDATE_REQUESTS){
					Boolean data = (Boolean)pck.getData();
					if (data){
						Main.setRightPaneController(this);
						Packet pck2 = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_REQUESTS,0,Main.user.getType());
						Main.sendToServer(pck2);
					}
					else
					{
						//will not refresh the table on the screen
					}
				}

				if (pck.getPacketSub() == PacketSub.GENERIC_GET_REQUESTS){
					ArrayList<Request> data = (ArrayList<Request>)pck.getData();
					if (data.get(0) instanceof Request){

						Platform.runLater(new Runnable(){
							@Override 
							public void run() {

								ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
								table.setItems(dataForTable);
							}
						});
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
		return "Requests List";
	}
  /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     * @return 
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		lastClick = System.currentTimeMillis();

		TableColumn column1 = new TableColumn("Request #");
		TableColumn column2 = new TableColumn("User Type");
		TableColumn column3 = new TableColumn("Request Type");
		TableColumn column4 = new TableColumn("User Id");
		TableColumn column5 = new TableColumn("Course");
		TableColumn column6 = new TableColumn("Status");

		column1.setCellValueFactory(new PropertyValueFactory<>("Number"));
		column2.setCellValueFactory(new PropertyValueFactory<>("userType"));	
		column3.setCellValueFactory(new PropertyValueFactory<>("subjectType"));	
		column4.setCellValueFactory(new PropertyValueFactory<>("userId"));	
		column5.setCellValueFactory(new PropertyValueFactory<>("courseInClassIndicator"));	
		column6.setCellValueFactory(new PropertyValueFactory<>("status"));	

		column1.prefWidthProperty().bind(table.widthProperty().divide(7));
		column2.prefWidthProperty().bind(table.widthProperty().divide(7));
		column3.prefWidthProperty().bind(table.widthProperty().divide(7));
		column4.prefWidthProperty().bind(table.widthProperty().divide(7));
		column5.prefWidthProperty().bind(table.widthProperty().divide(4));
		column6.prefWidthProperty().bind(table.widthProperty().divide(7));
		
		table.getColumns().add(column1);
		table.getColumns().add(column2);
		table.getColumns().add(column3);
		table.getColumns().add(column4);
		table.getColumns().add(column5);
		table.getColumns().add(column6);
		
		if(Main.user.getType() == UserType.school_manager){
			table.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {

				if (isConfirmDoubleClick()){
					if (table.getSelectionModel().getSelectedItem() != null){
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {		
	
								Alert alert = new Alert(AlertType.CONFIRMATION);
								alert.setTitle(getWindowTitle() );
								alert.setHeaderText("Do you Approve or Reject the request?");
								alert.setContentText("You answer will send to the secretry automaicaly and will be REMOVED from the request list.");
								//alert.getButtonTypes().remove(1);
								alert.getButtonTypes().add(new ButtonType("Rejected", ButtonData.YES ) );
								alert.getButtonTypes().remove(0);
								alert.getButtonTypes().add(new ButtonType("Accepted", ButtonData.YES ) );
								Optional<ButtonType> result = alert.showAndWait();
	
								Request selectedItem = (Request)table.getSelectionModel().getSelectedItem();
								if( selectedItem != null && ( result.get().getButtonData() == ButtonType.YES.getButtonData() ) ){
									ArrayList<String> answer = new ArrayList<String>();
	
									answer.add(selectedItem.getNumber().toString() );
									System.out.println(result.get().getText() );
									answer.add(result.get().getText().toLowerCase() );
	
									Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.UPDATE_REQUESTS,0,answer);
									Main.sendToServer(pck);	
								}
	
							}
						});		
					}
				}
			});
		}

		Main.setRightPaneController(this);
		Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_REQUESTS,0,Main.user.getType());
		Main.sendToServer(pck);
	}

 /**
     * this method handlels the action when we double click on confirm 
     * @param msg
     * @return true or false if we click double click on confirm
     */
	private boolean isConfirmDoubleClick(){
		if (lastPickedSelectionIndex == table.getSelectionModel().getSelectedIndex()){
			if (lastClick+2000 > System.currentTimeMillis()){
				return true;
			}
		}
		lastClick = System.currentTimeMillis();
		lastPickedSelectionIndex = table.getSelectionModel().getSelectedIndex();
		return false;
	}

}
