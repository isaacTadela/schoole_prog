package app;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import common.Course;
import common.CourseInClass;
import common.Packet;
import common.SchoolClass;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
/**
 * 
 * @author saree
 *this class is define ew task controller
 */
public class DefineNewTask implements Initializable,ControllerIF{

	ArrayList<CourseInClass> courseInClassList;

    @FXML
    private ComboBox<CourseInClass> courseInClassComboBox;

    @FXML
    private DatePicker finalDatePicker;

    @FXML
    private TextField taskIdLabel;

    @FXML
    private Label warningMsg;
	
    @FXML
    Button addAttachmentButton;
	
    byte[] attachment;
    String extension;
    
/**
 * method that adds new task to the DB on clicking define task in GUI section
 * 
 */
    @FXML
    void onClickDefineTask(MouseEvent event) {

    	if (courseInClassComboBox.getSelectionModel().getSelectedItem() == null){
    		setWarningMessage("You must choose course you teach!");
    		return;
    	} 
    	if (taskIdLabel.getText().length()==0){
    		setWarningMessage("You must give the task a name.");
    		return;
    	}
    	if (finalDatePicker.getValue() == null || !finalDatePicker.getValue().isAfter(LocalDate.now())){
    		setWarningMessage("You must set Date after Today!");
    		return;
    	}
    	if (attachment == null){
    		setWarningMessage("You must attach task file!");
    		return;
    	}
    	warningMsg.setVisible(false);
		Packet packet = requestCretaeNewTaskPacket(taskIdLabel.getText(),finalDatePicker.getValue().toString(),attachment,courseInClassComboBox.getSelectionModel().getSelectedItem().getCourse().getNumber(),courseInClassComboBox.getSelectionModel().getSelectedItem().getCourse().getAcUnit().getId(),courseInClassComboBox.getSelectionModel().getSelectedItem().get_class().getId(),extension);
		Main.sendToServer(packet); 
    }


    /**
     * builder method for packet that requests creating new packet to send for the server 
     * to add new task to the database.
     * @param taskId required taskId
     * @param expirationDate expiration date for the task
     * @param attachment file data in bytes[]
     * @param courseNumber course_number
     * @param unitNumber unit_number
     * @param classNumber class_number
     * @param extension extension the file has.
     */
	public static Packet requestCretaeNewTaskPacket(String taskId, String expirationDate, byte[] attachment, String courseNumber, String unitNumber, String classNumber, String extension){
    	ArrayList<Object> arr = new ArrayList<>();
    	arr.add(taskId);
    	arr.add(expirationDate);
    	arr.add(attachment);
    	arr.add(courseNumber);
    	arr.add(unitNumber);
    	arr.add(classNumber);
    	arr.add(extension);
		Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.DEFINE_TASK_SUBMIT,0,arr); 
		return packet;
    }
    
    
    
    @FXML
    void onClickedAddAttachment(MouseEvent event) {
    	setAttachment();
    }

    
    private void setWarningMessage(String msg){
    	warningMsg.setVisible(true);
    	warningMsg.setText(msg);
    }
    /**
     * this method sets the attachment of the file chooser
     */
    private void setAttachment(){
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Resource File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Text Files", "*.txt", "*.pdf", "*.doc", "*.docx"),
		         new ExtensionFilter("All Files", "*.*"));
		 File selectedFile = fileChooser.showOpenDialog(Main.getPrimaryStage());
		 if (selectedFile != null) {
			 String fileData = readFile(selectedFile);
			 attachment = fileData.getBytes();
			 extension = getFileExtension(selectedFile);
			 addAttachmentButton.setText("Chosen " + extension + " Attachement");
		 }	
	}
	/**
	 * method to read the file
	 * @param file
	 * @return content of the file as String
	 */
    public String readFile(File file)
	{
	    String content = null;
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){
	        	try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
	    return content;
	}
    /**
     * method returns the file extension
     * @param file
     * @return string that is the file extension
     */
	private String getFileExtension(File file) {
	    String name = file.getName();
	    try {
	        return name.substring(name.lastIndexOf(".") + 1);
	    } catch (Exception e) {
	        return "";
	    }
	}
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GENERIC_GET_COURSE_IN_CLASS){
					courseInClassList = (ArrayList<CourseInClass>)pck.getData();
					ObservableList<CourseInClass> obsList = FXCollections.observableArrayList(courseInClassList);
					Platform.runLater(new Runnable(){

						@Override
						public void run() {
							if (obsList.size() > 0){
								courseInClassComboBox.setItems(obsList);
								courseInClassComboBox.setValue(obsList.get(0));
							}
						}
						
					});
					
					
				}
			}
			
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.DEFINE_TASK_SUBMIT){
					Boolean isAdded = (Boolean)pck.getData();
					if (isAdded){
						Main.openPopUpWithMessage("Task has been Submitted Successfuly.", AlertType.INFORMATION);
					}else{
						Main.openPopUpWithMessage("Task failed to submit!", AlertType.ERROR);
					}

				}
			}			

		}

	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Define Task";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		courseInClassComboBox.setCellFactory(
		            new Callback<ListView<CourseInClass>, ListCell<CourseInClass>>() {
		                @Override 
		                public ListCell<CourseInClass> call(ListView<CourseInClass> param) {
		                    final ListCell<CourseInClass> cell = new ListCell<CourseInClass>() {   
		                        @Override 
		                        public void updateItem(CourseInClass item,  boolean empty) {
		                           super.updateItem(item, empty);
		                           if (item != null) {
		                              setText(item.getCourse().getName()+" "+item.get_class().getName());
		                           }else {
		                               setText(null);
		                           }
		                        }
		                };
		                return cell;
		            }
		        });
		ArrayList<Object> arr=new ArrayList<Object>();
		arr.add(SemesterController.currentSemester.getId());
		arr.add(Main.user.getId());
		Main.setRightPaneController(this);
		Packet packet = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GENERIC_GET_COURSE_IN_CLASS,0,arr);
		Main.sendToServer(packet);
	}

}
