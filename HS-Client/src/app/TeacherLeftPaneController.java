package app;

import javafx.fxml.FXML;
/**
 * 
 * @author group
 *this class is teacher leftpane controller
 */
public class TeacherLeftPaneController {

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickAbout(){
		Main.cleanBackButton();
		Main.showNewRightPane("show_teacher_details.fxml",Main.user);
	}
	

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickFindTeacher(){
		Main.cleanBackButton();
		Main.showNewRightPane("find_teacher_page.fxml");		
	}	

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickGetAllTeachers(){
		Main.cleanBackButton();
		Main.showNewRightPane("teachers_list.fxml");			
	}	

/**
 * method that on click shows new right pain
 */
	@FXML
	public void onClickDefineNewTask(){
		Main.cleanBackButton();
		Main.showNewRightPane("define_new_task.fxml");
		
	}

/**
 * method that on click shows new right pain
 */
	
	@FXML
	public void onClickTeacherCourse(){
		Main.cleanBackButton();
		Main.showNewRightPane("grid_items_list_layout.fxml",new TeacherEvaluationController(),null);			
	}
	

}
