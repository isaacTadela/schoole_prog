package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
/**
 * 
 * @author saree
 *this class is block parent access controller 
 */
public class BlockParentController implements Initializable,ControllerIF {
	@FXML
	TextField blockParentTextField;
	@FXML
	TextField unBlockParentTextField;
	@FXML
	 DatePicker blockDate;
	
	/**
	 * this method will block the parent until chosen date in gui on click
	 */
	@FXML
	public void onClickBlockParent(){
		ArrayList<String> al=new ArrayList();
		al.add(blockParentTextField.getText());
		al.add( blockDate.getValue().toString());
		Packet pck=new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.BLOCK_PARENT_ACCESS,0,al);
		Main.sendToServer(pck);
		
	}
	/**
	 * this method will unblock the parent on click
	 */
	@FXML
	public void onClickUnBlockParent(){
		String Id;
		Id=unBlockParentTextField.getText();
		Packet pck=new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.UNBLOCK_PARENT_ACCESS,0,Id);
		Main.sendToServer(pck);

	}
/**
 * this method handles the responses
 */
	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){

				if (pck.getPacketSub() == PacketSub.BLOCK_PARENT_ACCESS){
					if((boolean)pck.getData())
						Main.openPopUpWithMessage("Parent Has Been Blocked", AlertType.INFORMATION);
					else 
						Main.openPopUpWithMessage("Parent is already blocked", AlertType.ERROR);
				}	
				else if (pck.getPacketSub() == PacketSub.UNBLOCK_PARENT_ACCESS){
					if((boolean)pck.getData())
						Main.openPopUpWithMessage("Parent Has Been Unblocked", AlertType.INFORMATION);
					else 
						Main.openPopUpWithMessage("Parent is not blocked", AlertType.ERROR);
					
				}
			}
		}
	}
			

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return 	"Block_Page";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		
	}
	
	
	
}
