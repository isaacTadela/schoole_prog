package app;

import java.net.URL;
import java.util.ResourceBundle;

import common.Parent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/**
 * 
 * @author ayal
 *in these class we show the parent information who is assigning right now
 */
public class ShowParentInformationController implements Initializable {
    @FXML
    private Label parentIdLabel;

    @FXML
    private Label pnameLabel;

    @FXML
    private Label fnameLabel;

    @FXML
    private Label banDateLable;
       /** 
     * initialize function sets right page of GUI to the choosen controller
     * @param URL location
     * @param ResourceBundle resourses
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			Parent parent= (Parent)bundle.getData();
			parentIdLabel.setText(parent.getId());
			pnameLabel.setText(parent.getPname());
			fnameLabel.setText(parent.getFname());
			banDateLable.setText(parent.getBanUntilDate().toString());
		}		
		}
}
