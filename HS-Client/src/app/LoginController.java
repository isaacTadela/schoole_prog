package app;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

import common.Packet;
import common.User;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
/**
 * controller for the login window
 * @author Amir
 *
 */
public class LoginController implements Initializable, ControllerIF {

	@FXML
	TextField usernameTextField;
	@FXML
	PasswordField passwordTextField;	
	@FXML
	Label errorMessage;
	
	@FXML
	CheckBox autoLogCheck;
	
	
	int typeId;
	User user;
	
	
	/**
	 * method executed when clicking on Sign In button.
	 */
	@FXML
	public void onSignInClick(){
		if (usernameTextField.getText().isEmpty()){
			errorMessage.setText("You must type username!");
			errorMessage.setVisible(true);
		}else if(passwordTextField.getText().isEmpty()){
			errorMessage.setText("You must type password!");
			errorMessage.setVisible(true);
		}else{
			Preferences prefs = Preferences.userNodeForPackage(Main.class);
			if (autoLogCheck.isSelected()){ 
			    prefs.put("username", usernameTextField.getText()); // remove for removal
			    prefs.put("password", passwordTextField.getText()); // remove for removal
			}else{
			    prefs.remove("username");
			    prefs.remove("password");
			}
			String where = "id = '" + usernameTextField.getText() + "' and password = '" + passwordTextField.getText()+"'";
			Packet packet = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.LOGIN_CONTROLLER_GET_DATA,0,where);
			System.out.println("login controller 62...Main.sendToServer packet" + packet );
			Main.sendToServer(packet);	
			
		}
	}
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setLeftPaneController(this);
		Preferences prefs = Preferences.userNodeForPackage(Main.class);
		String tempUser = prefs.get("username", null);
		String tempPass = prefs.get("password", null);
		
		if (tempUser != null && tempPass != null && !tempUser.isEmpty() && !tempPass.isEmpty()){
			usernameTextField.setText(tempUser);
			passwordTextField.setText(tempPass);
			autoLogCheck.setSelected(true);
		}
		if (resources != null){
			errorMessage.setText("This client was kicked out because of another client is logging with the same user.");
			errorMessage.setVisible(true);			
		}
	}


	@Override
	public void onResponse(Object msg) {
		System.out.println("login controller 90...onResponse");
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.LOGIN_CONTROLLER_GET_DATA){
					user = (User)pck.getData();
					if (user != null){
						Main.setUser(user);
						Main.showNewScene("main_connected_layout.fxml");
					}else{
						Platform.runLater(new Runnable(){
							@Override 
							public void run() {	
								errorMessage.setVisible(true);
								errorMessage.setText("Wrong Credentials.");
							}
						});
					}
				}
			}
		}
	}


	@Override
	public String getWindowTitle() {
		return "Login";
	}
}
