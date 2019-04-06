package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Student;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/**
 * 
 * @author ayal
 *in these class we show the student information who is assigning right now
 */
public class ShowStudentInformationController implements Initializable{

    @FXML
    private Label studentIdLabel;

    @FXML
    private Label pnameLabel;

    @FXML
    private Label fnameLabel;

    @FXML
    private Label classIdLabel;

    @FXML
    private Label fatherIdLabel;

    @FXML
    private Label motherIdLabel;
    /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			Student student = (Student)bundle.getData();
			studentIdLabel.setText(student.getId());
			pnameLabel.setText(student.getPname());
			fnameLabel.setText(student.getFname());
			classIdLabel.setText(student.get_class());
			fatherIdLabel.setText(student.getFather());
			motherIdLabel.setText(student.getMother());
		}
	}

}
