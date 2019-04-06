package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.CourseInClass;
import common.SchoolClass;
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
 *in these class we show the semester information who is assigning right now
 */
public class ShowClassInformationController implements Initializable{


    @FXML
    private Label classNum;

    @FXML
    private Label className;

    @FXML
    private TableColumn<String, String> studentsColumn;

    @FXML
    private TableColumn<CourseInClass, CourseInClass> courseColumn;
    @FXML
    private TableColumn<CourseInClass, Integer> semesterColumn;

    @FXML
    private TableView<String> studentsTable;

    @FXML
    private TableView<CourseInClass> courseTable;
    
     /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     */
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			SchoolClass _class = (SchoolClass)bundle.getData();
			classNum.setText(_class.getId());
			className.setText(_class.getName());
			courseColumn.prefWidthProperty().bind(courseTable.widthProperty().divide(2));
			semesterColumn.prefWidthProperty().bind(courseTable.widthProperty().divide(2));
			
			
			
			studentsColumn.prefWidthProperty().bind(studentsTable.widthProperty().divide(1));
			courseColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
			semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semesterId"));
			
			studentsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
			ObservableList<String> dataForTable1 = FXCollections.observableArrayList(_class.getStudentsInClass());
			studentsTable.setItems(dataForTable1);			
			ObservableList<CourseInClass> dataForTable2 = FXCollections.observableArrayList(_class.getCoursesInClass());
			courseTable.setItems(dataForTable2);				
		}
	}

}
