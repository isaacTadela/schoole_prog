package app;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
/**
 * 
 * @author saree
 *define a new class controller 
 */
public class DefineNewClass  implements Initializable,ControllerIF{
	   @FXML
	    private TextField classNameText;

	    @FXML
	    private TextField classNumberText;
/**
 * this method adds new class to DB on clicking Define Class in the GUI section
 * no @param 
 */
	    @FXML
	public    void onClickedDefineClass() {
	    	ArrayList<String> al=new ArrayList();
	    	al.add(classNameText.getText());
	     	al.add(classNumberText.getText());
	     	Packet pck=new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.DEFINE_NEW_CLASS,0,al);
	     	Main.sendToServer(pck);
	    }

		@Override
		public void onResponse(Object msg) {
			if (msg instanceof Packet){
				Packet pck = (Packet)msg;
				if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){

					if (pck.getPacketSub() == PacketSub.DEFINE_NEW_CLASS){
						if((boolean)pck.getData())
							Main.openPopUpWithMessage("A class has been added", AlertType.INFORMATION);
						else 
							Main.openPopUpWithMessage("Failed to add class", AlertType.ERROR);
					}	
				}
			}
		}

		@Override
		public String getWindowTitle() {
			// TODO Auto-generated method stub
			return null;
		}
/**
 * set right pane method
 */
		@Override
		public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
			
		}
	
}
