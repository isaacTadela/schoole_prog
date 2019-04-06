package app;

import javafx.fxml.FXML;
/**
 * 
 * @author group
 *this class is secretary leftpane controller
 */
public class SecretaryLeftPaneController {
	/**
	 * method that on click shows new right pain
	 */
	@FXML
	public void onClickAbout(){
		Main.cleanBackButton();
		Main.showNewRightPane("show_user_details.fxml",Main.user);
	}/**
	 * method that on click shows new right pain
	 */
	@FXML
	public void onClickOpenNewSemester(){
		Main.cleanBackButton();
		Main.showNewRightPane("open_new_semester_layout.fxml");
	}
	/**
	 * method that on click shows new right pain
	 */
	@FXML
	public void onClickOpenBlockParent(){
		Main.cleanBackButton();
		Main.showNewRightPane("block_parent_layout.fxml");

	}
	/**
	 * method that on click shows new right pain
	 */
	@FXML
	public void onClickDefineNewClass(){
		Main.cleanBackButton();
		Main.showNewRightPane("define_new_class.fxml");

	}
	@FXML
	public void onClickAssignNewStudent(){
		Main.cleanBackButton();
		Main.showNewRightPane("assign_student_to_class.fxml");

	}
	/**
	 * method that on click shows new right pain
	 */
	public void onClickAssignStudentToCourse(){
		Main.cleanBackButton();
		Main.showNewRightPane("assignOrRemove_student_in_course_in_class_layout.fxml");
	}
	/**
	 * method that on click shows new right pain
	 */
	public void onClickAssignClassToCourse(){
		Main.cleanBackButton();
		Main.showNewRightPane("assign_class_to_course_layout.fxml");
	}
	/**
	 * method that on click shows new right pain
	 */
	public void onClickAssignTeacherToCourseInClass(){
		Main.cleanBackButton();
		Main.showNewRightPane("assign_teacher_to_course_in_class_layout.fxml");
	}
	/**
	 * method that on click shows new right pain
	 */
	public void onClickGetStatistics(){
		Main.cleanBackButton();
		Main.showNewRightPane("get_statistics_options_layout.fxml");
	}
	@FXML
	public void onClickAssignRemove(){
		Main.cleanBackButton();
		Main.showNewRightPane("assign_remove__requests_layout.fxml");
	}/**
	 * method that on click shows new right pain
	 */
    @FXML
    void onClickReseveRequests() {
    	Main.cleanBackButton();
    	Main.showNewRightPane("request_table.fxml");
    }
	

}
