package app;

import javafx.fxml.FXML;
/**
 * 
 * @author group
 *this class is student leftpane controller
 */
public class StudentLeftPaneController {

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickAbout(){
		Main.cleanBackButton();
		Main.showNewRightPane("show_student_details.fxml",Main.user);
	}

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickStudentFolder(){
		Main.cleanBackButton();
		Main.showNewRightPane("grid_items_list_layout.fxml",new StudentFolderController(),null);
	}	
}
