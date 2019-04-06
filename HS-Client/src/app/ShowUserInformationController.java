package app;

import java.net.URL;
import java.util.ResourceBundle;

import common.Parent;
import common.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/** 
 * @author ayal
 * in these class we show the user information who is assigning right now
 */
public class ShowUserInformationController implements Initializable {
	 @FXML
	    private Label userLabel;

	    @FXML
	    private Label pnameLabel;

	    @FXML
	    private Label fnameLabel;
	    @FXML
	    private Label typeLabel ;
	    /** 
	     * initialize function sets right page of GUI to the choosen controller
	     * @param URL location
	     * @param ResourceBundle resourses
	     */
	   
	    @Override
	    public void initialize(URL location, ResourceBundle resources) {
			if (resources instanceof DataBundle){
				DataBundle bundle = (DataBundle)resources;
				User user= (User)bundle.getData();
				userLabel.setText(user.getId());
				pnameLabel.setText(user.getPname());
				fnameLabel.setText(user.getFname());
				typeLabel.setText(user.getType().toString());
				
			}		
	    }
}
