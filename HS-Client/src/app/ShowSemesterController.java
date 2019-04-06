package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.Semester;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/**
 * 
 * @author ayal
 *in these class we show the semester information 
 */
public class ShowSemesterController implements Initializable{
	
    @FXML
    private Label idLabel;

    @FXML
    private Label seasonLabel;

    @FXML
    private Label openingDateLabel;

    @FXML
    private Label closingDateLabel;
    /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			Semester semester = (Semester)bundle.getData();
			idLabel.setText(semester.getId().toString());
			seasonLabel.setText(semester.getSeason());
			openingDateLabel.setText(semester.getOpen_date().toString());
			try{
				closingDateLabel.setText(semester.getClosing_date().toString());
			}catch(NullPointerException e){
				closingDateLabel.setText("Its Current Semester and havent been closed yet.");
			}
		}
		
	}

}