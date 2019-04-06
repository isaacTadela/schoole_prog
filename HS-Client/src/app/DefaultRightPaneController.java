package app;

import java.net.URL;
import java.util.ResourceBundle;

import common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
/**
 * default right pane window
 * @author Amir
 *
 */
public class DefaultRightPaneController implements Initializable,ControllerIF {
	
	@FXML
	TableView<User> table;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Main.setRightPaneController(this);
		restoreTableData();
	}

	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
		
	}
/**
 * builds small table with the information about the user who logged in	
 */
	private void restoreTableData(){
		if (table != null){
			table.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("pname"));
			table.getColumns().get(0).setResizable(false);
			table.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("fname"));
			table.getColumns().get(1).setResizable(false);
			table.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("id")); 
			table.getColumns().get(2).setResizable(false);
			table.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("type"));
			table.getColumns().get(3).setResizable(false);
			
			ObservableList<User> data = FXCollections.observableArrayList(Main.user);
			table.setItems(data);	
			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			table.setVisible(true);
		}
		
	}
	
	@Override
	public String getWindowTitle() {
		return "Main Page";
	}
	
}
