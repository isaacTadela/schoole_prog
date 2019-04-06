package app;

import java.net.URL;
import java.util.ResourceBundle;

import common.Parent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
/**
 * 
 * @author amir
 *this class implements the view that a banned parent can see
 */
public class BlockedAccessController implements Initializable {
	@FXML
	Label banDate;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Parent parent = (Parent)Main.user;
		banDate.setText(parent.getBanUntilDate().toString());		
	}	
}
