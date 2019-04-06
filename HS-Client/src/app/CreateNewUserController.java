package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import common.AcademicUnit;
import common.Packet;
import common.Parent;
import common.SchoolClass;
import common.Student;
import enums.PacketId;
import enums.PacketSub;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

import javafx.application.Application; 
import javafx.scene.Scene; 
import javafx.scene.control.Button; 
import javafx.scene.layout.*; 
import javafx.event.ActionEvent; 
import javafx.event.EventHandler; 
import javafx.scene.control.*; 
import javafx.stage.Stage; 


public class CreateNewUserController implements Initializable,ControllerIF {

	
	@FXML TextField userId;
	@FXML TextField userPass;
	@FXML TextField FirstName;
	@FXML TextField LastName;
	@FXML ComboBox<String> userTypeBox;
	@FXML Label warnLabel;
	@FXML TextField MotherID;
	@FXML ComboBox<String> StudentIdCombobox;
	@FXML TextField FatherID;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_ARRAY_LIST){
				if (pck.getPacketSub() == PacketSub.GENERIC_GET_USERS_TYPE){
					ArrayList<String> resArr = (ArrayList<String>)pck.getData();
					userTypeBox.getItems().addAll(resArr );
					}
				if (pck.getPacketSub() == PacketSub.GENERIC_GET_STUDENTS){
					@SuppressWarnings("unchecked")
					ArrayList<Student> resArr = (ArrayList<Student>)pck.getData();
					ArrayList<String> resArr2 = new ArrayList<String>();
					for(Student student: resArr)
					{
						if(student.getFather()!=null && student.getMother()!=null)
						{ System.out.println(student.toString() ); }	//resArr.remove(student);
						resArr2.add(student.toString() );
					}
					 
					StudentIdCombobox.getItems().addAll(resArr2 );
				 }
			}
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.CREATE_NEW_USER){
					boolean isAdded = (Boolean)pck.getData();
					
					if (isAdded){
						//TODO show message course added successfully.
						//System.out.println("user add success");
						Main.openPopUpWithMessage("User was added successfuly.",AlertType.INFORMATION);
					}else{
						//TODO show message course failed to be added.
						Main.openPopUpWithMessage("Adding user failed.",AlertType.ERROR);
					}
					
				}
			}
			
		}
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Define new User";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		Main.setRightPaneController(this);
		
		userTypeBox.setOnAction(e -> {
			FatherID.setDisable(true);
			MotherID.setDisable(true);
        	StudentIdCombobox.setDisable(true);
        	
			switch (userTypeBox.getValue()) {
		        case "student":
		        	FatherID.setDisable(false);
					MotherID.setDisable(false);
		          break;
		        case "parent":
		        	StudentIdCombobox.setDisable(false);
		          break;
		      }
		    });
		
		String where = "";
		Packet pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GENERIC_GET_USERS_TYPE,0,where);
		Main.sendToServer(pck);
		pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GENERIC_GET_STUDENTS,0,where);
		Main.sendToServer(pck);
	}
	
	/**
	 * method that get executed when clicking on Submit Button
	 */
	@FXML 
	public void onSubmitClick(){
		
		Integer id = 0;
		try {
			id = Integer.parseInt( userId.getText() );
		}
		catch (NumberFormatException e) {
			setWarning("User ID is not a number,You must give the user a correct id NUMBER in order to proceed!");
			return;
		}
		
		if ( id < 1 ){
			setWarning("You must give the user a correct id number in order to proceed!");
			return;
		}
		if ( userPass.getText().isEmpty()  ){
			setWarning("You must give the user a password in order to proceed!");
			return;
		}
		if ( FirstName.getText().isEmpty()  ){
			setWarning("You must give the user a first name in order to proceed!");
			return;
		}
		if ( LastName.getText().isEmpty()  ){
			setWarning("You must give the user a last name in order to proceed!");
			return;
		}
		if( userTypeBox.getSelectionModel().getSelectedItem().toString().equals("parent") )
		{
			String stdId = StudentIdCombobox.getSelectionModel().getSelectedItem(); 
		}
		if( userTypeBox.getSelectionModel().getSelectedItem().toString().equals("student")  )
		{
			String fatherID = FatherID.getText() ;
			String motherID = MotherID.getText();
		}
		
		// send to server the packet
		Packet pck2 = sendCreateUserPacket( userId.getText(),userPass.getText(), userTypeBox.getValue(),
				FirstName.getText(),LastName.getText(), StudentIdCombobox.getValue(), FatherID.getText(), MotherID.getText() );
		Main.sendToServer(pck2);
	}
	
	/**
	 * Raw packet builder method which builds the message to be sent to server with information about
	 * the user that we wish to create. 
	 * 
	 */
	
	public static Packet sendCreateUserPacket(String userId, String userPass, String userTypeBox,
			String FirstName, String LastName, String StudentIdCombobox, String FatherID, String MotherID){
		ArrayList<String> dataArr = new ArrayList<>();
		dataArr.add(userId);
		dataArr.add(userPass);
		dataArr.add(userTypeBox);
		dataArr.add(FirstName);
		dataArr.add(LastName);
		dataArr.add(StudentIdCombobox);
		dataArr.add(FatherID);
		dataArr.add(MotherID);
		
		Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.CREATE_NEW_USER,0,dataArr);
		return packet;	
	}

	/**
	 * method used to show errors or warnings in the correspondent FXML files 
	 * @param msg
	 */
	private void setWarning(String msg){
		warnLabel.setText(msg);
		warnLabel.setVisible(true);	
	}
	
}
