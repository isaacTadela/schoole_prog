package app;

import javafx.fxml.FXML;

public class SystemManagerLeftPaneController {
/**
 * method executed when clicking About on system manager left pane window
 */
	@FXML
	public void onClickAbout(){
		Main.cleanBackButton();
		Main.showNewRightPane("show_user_details.fxml",Main.user);
	}
/**
 * method executed when clicking on Define Course button in left pane for system manager.
 */
	@FXML
	public void onClickDefineCourse(){
		Main.cleanBackButton();
		Main.showNewRightPane("define_course_layout.fxml");
	}	
	
	/**
	 * method executed when clicking on Create user button in left pane for system manager.
	 */
	@FXML
	public void onClickCreateUser(){
		Main.cleanBackButton();
		Main.showNewRightPane("create_new_user.fxml");
	}


	/**
	 * method executed when clicking on Create user button in left pane for system manager.
	 */
	@FXML
	public void onClickDeleteUser(){
		Main.cleanBackButton();
		Main.showNewRightPane("delete_user_table.fxml");
	}



}
