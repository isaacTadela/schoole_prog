package app;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
/**
 * controller for the window that appears on disconnection in order to let you type need address and port 
 * @author Amir
 *
 */
public class DisconnectionController implements Initializable{
	
	@FXML
	TextField host;
	@FXML
	TextField port;
	/**
	 * method executed when clicking on connect button
	 */
	@FXML
    public void onConnectButtonClick() {
		setLastUsedPort(Integer.parseInt(port.getText()));
		setLastUsedHost(host.getText());
		Main.startNewConnection(host.getText(), Integer.parseInt(port.getText()));
    }


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		host.setText(Main.lastAttemptedHost);
		port.setText(Main.lastAttemptedPort.toString());
	}	


/**
 * method used to save last host used
 * @param host the host address 
 */
	public void setLastUsedHost(String host){
	    Preferences prefs = Preferences.userNodeForPackage(Main.class);
	    prefs.put("host", host); // remove for removal
	}
	/**
	 * method used to save last port used
	 * @param port is the port value that was last used
	 */
	public void setLastUsedPort(int port){
	    Preferences prefs = Preferences.userNodeForPackage(Main.class);
	    prefs.putInt("port", port); // removeInt for removal
	}  
}
