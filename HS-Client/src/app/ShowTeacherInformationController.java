package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import common.AcademicUnit;

import common.Teacher;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ShowTeacherInformationController implements Initializable{
	/**
	 * 
	 * @author ayal
	 *in these class we show the student information who is assigning right now
	 */
    @FXML
    private Label studentIdLabel;

    @FXML
    private Label pnameLabel;

    @FXML
    private Label fnameLabel;

    @FXML
    private Label maxWorkingLabel;

    @FXML
    private Label currentWorkingLabel;
    

    @FXML
    private TableView<AcademicUnit> academicUnitTable;

    @FXML
    private TableColumn<AcademicUnit, String> academicUnitColumn;
    
    
    /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     */
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			Teacher teacher = (Teacher)bundle.getData();
			studentIdLabel.setText(teacher.getId());
			pnameLabel.setText(teacher.getPname());
			fnameLabel.setText(teacher.getFname());
			maxWorkingLabel.setText(teacher.getMaxAllowedHours().toString() + " hours.");
			currentWorkingLabel.setText(teacher.getCurrentHours().toString() + " hours.");
			
			
			// override to toString to change the default toString of ArrayList
			ArrayList<AcademicUnit> acaList = new ArrayList<AcademicUnit>(teacher.getAcademicUnit()){
    		    @Override public String toString()
    		    {
    		        Iterator<AcademicUnit> it = iterator();
    		        if (! it.hasNext())
    		            return "";

    		        StringBuilder sb = new StringBuilder();
    		        sb.append("");
    		        for (;;) {
    		        	AcademicUnit e = it.next();
    		            sb.append(e+"\n");
    		            if (! it.hasNext())
    		                return sb.append("").toString();
    		            sb.append(',').append(' ');
    		        }
    		    }
    		};

    		if (acaList != null && acaList.size() > 0){
    			
    			academicUnitColumn.prefWidthProperty().bind(academicUnitTable.widthProperty().divide(1));
    			academicUnitColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
    					
    			ObservableList<AcademicUnit> dataForTable = FXCollections.observableArrayList(acaList);
    			academicUnitTable.setItems(dataForTable);

    		}

		}
	}

}
