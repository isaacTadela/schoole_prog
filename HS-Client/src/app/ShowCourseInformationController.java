package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.AcademicUnit;
import common.Course;
import common.Student;
import common.Teacher;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * 
 * @author ayal
 *in these class we show the course information 
 */
public class ShowCourseInformationController implements Initializable{



    @FXML
    private Label courseNum;

    @FXML
    private Label courseName;

    @FXML
    private Label weeklyHours;

    @FXML
    private Label aUnitLabel;

    @FXML
    private TableView<String> predsTable;

    @FXML
    private TableColumn<String, String> predsColumn;
        /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			Course course = (Course)bundle.getData();
			courseNum.setText(course.getId());
			courseName.setText(course.getName());
			weeklyHours.setText(course.getWeeklyHours().toString() + " hours.");
			aUnitLabel.setText(course.getAcUnit().getId());
			
    		if (course.getPredecessors() != null && course.getPredecessors().size() > 0){
    			
    			predsColumn.prefWidthProperty().bind(predsTable.widthProperty().divide(1));
    			predsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    					
    			ObservableList<String> dataForTable = FXCollections.observableArrayList(course.getPredecessors());
    			predsTable.setItems(dataForTable);
    			
    		}
		}
	}

}
