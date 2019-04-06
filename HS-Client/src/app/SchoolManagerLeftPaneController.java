package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import enums.UserType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
/**
 * 
 * @author amir
 *this class is school manager leftpane controller
 */
public  class SchoolManagerLeftPaneController {

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickRequests(){
		Main.cleanBackButton();
		Main.showNewRightPane("request_table.fxml");
	}

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickAbout(){
		Main.cleanBackButton();
		Main.showNewRightPane("show_user_details.fxml",Main.user);
	}

	@FXML

/**
 * method that on click shows new right pain
 */
	public void onClickGetStatistics()
	{
		Main.cleanBackButton();
		Main.showNewRightPane("statistics_layout.fxml");
	}

/**
 * method that on click shows new right pain
 */
	public void onClickObtainInformation(){
		Main.cleanBackButton();
		Main.showNewRightPane("choose_information_to_browse_layout.fxml");	
	}


}
