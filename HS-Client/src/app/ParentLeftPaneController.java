package app;

import java.util.ArrayList;

import common.Parent;
import javafx.fxml.FXML;
/**
 * 
 * @author group
 *this class is parent leftpane controller
 */
public class ParentLeftPaneController {

	@FXML
	public void onClickAbout(){
		Main.cleanBackButton();
		Main.showNewRightPane("show_parent_details.fxml",Main.user);
	}

	/**
	 * method that on click shows new right pain
	 */
	@FXML
	public void onClickCheckChildren(){	
		Main.cleanBackButton();
		if (isParentBlocked()){
			Main.showNewRightPane("blocked_parent_message_layout.fxml");
		}else{
			Main.showNewRightPane("grid_items_list_layout.fxml",new ChildrenFolderController(),null);
		}
	}
	/**
	 * method that on cchecks if parent is blocked
	 * @return true or false
	 */
	private boolean isParentBlocked(){
		Parent parent = (Parent)Main.user;
		if (SemesterController.currentSemester.getCurrentDate().after(parent.getBanUntilDate())){
			return false;
		}
		return true;
	}
}
