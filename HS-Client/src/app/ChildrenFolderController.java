package app;


import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
/**
 * 
 * @author Amir
 *
 */
public class ChildrenFolderController implements ControllerIF,Initializable {
	
	@FXML
	Label titleLabel;
	
	@FXML
	GridPane folderGridPane;
	
	@FXML
	Label emptyLabel;
	
	ArrayList<User> parentChildren;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GET_STUDENTS_FOR_PARENT){
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							setGridButtons((ArrayList<User>)pck.getData());
						}
					});
				}
			}
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Childrens' Page";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources != null){
			titleLabel.setText("Children in school for you");
			Main.setRightPaneController(this);

			Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_STUDENTS_FOR_PARENT,0,Main.user.getId());
			Main.sendToServer(pck);
		}
	}
	
	/**
	 * method used to update the visible gird with new data
	 * @param arr arraylist contains all the User data that need to be shown in the grid view
	 */
	private void setGridButtons(ArrayList<User> arr){
		if (arr.size()>0){
			emptyLabel.setVisible(false);
		}else{
			emptyLabel.setVisible(true);
		}
		for (int j=0;j<arr.size()/3+1;j++){
			folderGridPane.getRowConstraints().add(j, new RowConstraints(120));
			for (int i=0;i<3 && new Integer(j*3+i) < arr.size();i++){

				Integer index = new Integer(j*3+i);
				ImageButton b = new ImageButton(arr.get(index).getPname()+"\n"+arr.get(index).getFname(),new Image(Main.class.getResourceAsStream("resources/folder_icon.png")));
				b.setAlignment(Pos.CENTER);
				b.setOnMouseClicked(new EventHandler<MouseEvent>(){
					 
			          @Override
			          public void handle(MouseEvent arg0) {
			        	  Main.showNewRightPane("grid_items_list_layout.fxml",new StudentFolderController(),arr.get(index).getId());
			          }
			 
			      });
				folderGridPane.add(b, i, j);
				folderGridPane.setHalignment(b, HPos.CENTER);
			}
		}		
	}

}
