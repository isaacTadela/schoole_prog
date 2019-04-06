package app;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Optional;
import java.util.ResourceBundle;

import common.Packet;
import common.Parent;
import common.StudentInCourse;
import common.Submission;
import common.TasksInCourseInClass;
import common.Teacher;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
/**
 * controller for task details window
 */
public class TaskDetailsController implements Initializable,ControllerIF {
	TasksInCourseInClass taskInCC;
	StudentInCourse studentInCC;
	Submission submission;
	
	@FXML
	Label taskName;
	@FXML
	Label date;
	@FXML
	Label expire;
	@FXML 
	AnchorPane submissionPane;
	@FXML
	Label submissionDate;	
	@FXML
	Label note;
	@FXML
	Label submissionGrade;
	
	@FXML
	Button submitSolutionButton;
	
	@FXML
	Button evaluationBottun;
	@FXML
	TextField gradeTextFild;
	@FXML
	TextField notesTextFild;
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
				if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
					if (pck.getPacketSub() == PacketSub.EVALUATE_STUDENT_IN_TASK){
					Boolean isUpdated = (Boolean)pck.getData();
					
					if(isUpdated){
						Main.openPopUpWithMessage("Task Evaluation Submmited Succssfuly", AlertType.INFORMATION);
						}
					else{
						Main.openPopUpWithMessage("Task Evaluation Submmited Failed", AlertType.ERROR);
						}
					}
				}
				
			if (pck.getPacketSub() == PacketSub.GET_TASK_SOLUTION){
					submission = (Submission)pck.getData();
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {
							if (submission != null){
								showSubmissionPane(true);
								submissionDate.setText(submission.getSubmitDate().toString());
								note.setText(submission.getNotes());
								if (submission.getGrade() != null){
									submissionGrade.setText(submission.getGrade().toString());
								}
								if (pck.getStage() != 4){
									Main.openPopUpWithMessage("Solution was submited successfuly",AlertType.INFORMATION);
								}
							}else{
								if (pck.getStage() != 4){
									Main.openPopUpWithMessage("Solution failed to upload!",AlertType.ERROR);
								}
							}
						}
					});
				}
		}
	}

	/**
	 * method executed when teacher clicks on evaluate submission button.
	 */
	public void onEvaluationSubmmisionClick(){
		ArrayList<String> input = new ArrayList<String>();
		input.add(gradeTextFild.getText());
		input.add(notesTextFild.getText());
		input.add(studentInCC.getStudentId());
		input.add(studentInCC.getCourse().getNumber());
		input.add(studentInCC.getCourse().getAcUnit().getId());
		input.add(studentInCC.getClassId());
		input.add(taskInCC.getTaskId());
		
		
		Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN ,PacketSub.EVALUATE_STUDENT_IN_TASK ,0,input);
		Main.sendToServer(pck);
		
	}
	/**
	 * method used to to show/hide submission part 
	 * @param isShow boolean indicating the request to show or not
	 */
	public void showSubmissionPane(boolean isShow){
		submissionPane.setVisible(isShow);	
	}
	
	
	
	@Override
	public String getWindowTitle() {
		return "Task Detail Page";
	}
	/**
	 * creating new file from data stored from sql
	 * @param extension extension of the file needs to be created
	 */
	private void setFileOnDrive(String extension){
		FileOutputStream fop = null;
		try {

			File file = new File("./tempfile."+extension);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fop.write(taskInCC.getFile());
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}
	/**
	 * creating new file from data stored from sql
	 * @param filename full file name thats need to be created
	 */
	private void setFileOnDriveByFullName(String filename){
		FileOutputStream fop = null;
		try {

			File file = new File("./"+filename);
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			fop.write(submission.getFileData());
			fop.flush();
			fop.close();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	
	}	
	
	/**
	 * method executed when clicking Watch Submission button.
	 */
	@FXML
	public void onClickWatchSubmissionFile(){
		setFileOnDriveByFullName("temp_submission." + submission.getFileExtension());
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File("./temp_submission." + submission.getFileExtension());
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}
	/**
	 * method executed clicking on Attachment button.
	 */
	@FXML
	public void onClickOpenAttachment(){
		setFileOnDrive(taskInCC.getFileExtension());
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File("./tempfile." + taskInCC.getFileExtension());
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException ex) {
		        // no application registered for PDFs
		    }
		}
	}	
	
	/**
	 * method used to read file
	 * @param file is File contains instructions about the file we need to read
	 * @return returns file content as String
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
	 * methoed executed when clicking Submit button
	 */
	@FXML
	public void onClickSubmitTask(){
		if (taskInCC.getFinal_submission().getTime() < Calendar.getInstance().getTime().getTime()){
			Platform.runLater(new Runnable(){
				@Override 
				public void run() {		
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle(getWindowTitle());
					alert.setHeaderText("Are you really sure you want to send Solution now?");
					alert.setContentText("The date assigned to this task is already expired, if you wish to add new solution it will be sent after expiration date.");
					Optional<ButtonType> result = alert.showAndWait();

					if ((result.isPresent()) && (result.get() == ButtonType.OK)) {	
						confirmSubmitSolution();
					}
				}
			});
			
		}else{
			confirmSubmitSolution();
		}

	}
	
/**
 * method executed which clicking upload button which ipens file chosser to pick file from.
 */
	private void confirmSubmitSolution(){
		 FileChooser fileChooser = new FileChooser();
		 fileChooser.setTitle("Open Resource File");
		 fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("Text Files", "*.txt", "*.pdf", "*.doc", "*.docx"),
		         new ExtensionFilter("All Files", "*.*"));
		 File selectedFile = fileChooser.showOpenDialog(Main.getPrimaryStage());
		 if (selectedFile != null) {
			 
			 
			 String fileData = readFile(selectedFile);
			 byte[] fileDataInBytes = fileData.getBytes();
			 Packet pck = submitSolutionPacketRequest(studentInCC.getCourse().getNumber(),studentInCC.getStudentId(),studentInCC.getCourse().getAcUnit().getId(),studentInCC.getClassId(),taskInCC.getTaskId(),fileDataInBytes,getFileExtension(selectedFile));
			 Main.sendToServer(pck);
		 }	
		
	}
	
	/**
	 * Method that build packet that can be sent to server in order to request send submission to a task.
	 * @param number course_number
	 * @param studentId student_id
	 * @param id unit_number
	 * @param classId class_number
	 * @param taskId task_id
	 * @param fileDataInBytes file data in byte[]
	 * @param fileExtension file extension
	 * @return packet that can be sent to request submission
	 */
	public static Packet submitSolutionPacketRequest(String number, String studentId, String id, String classId, String taskId, byte[] fileDataInBytes, String fileExtension){
		 ArrayList<Object> arr = new ArrayList<>();
		 arr.add(number);
		 arr.add(studentId);
		 arr.add(id);
		 arr.add(classId);
		 arr.add(taskId);
		 arr.add(fileDataInBytes);
		 arr.add(fileExtension);
		 Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.SUBMIT_TASK_SOLUTION,0,arr);
		 return pck;	
		
	}
	
	
/**
 * method obtains suffix of file after . in order to give us its extension.
 * @param file is the file in question
 * @return the extension as string
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
	public void initialize(URL location, ResourceBundle resources) {
		if (resources != null){
			if (resources instanceof DataBundle){
				Main.setRightPaneController(this);
				DataBundle bundle = (DataBundle)resources;
				@SuppressWarnings("unchecked")
				ArrayList<Object> data = (ArrayList<Object>)bundle.getData();
				taskInCC = (TasksInCourseInClass)data.get(0);
				studentInCC = (StudentInCourse)data.get(1);
				taskName.setText(taskInCC.getTaskId());
				date.setText(taskInCC.getDate().toString());
				expire.setText(taskInCC.getFinal_submission().toString());
				showSubmissionPane(false);

				
				if (Main.user instanceof Parent || !SemesterController.isChosenSemesterLatest())
				{
					submitSolutionButton.setVisible(false);
				}
				
				if(Main.user instanceof Teacher  ){
					evaluationBottun.setVisible(true);
					gradeTextFild.setVisible(true);
					notesTextFild.setVisible(true);
					
					if (studentInCC.getGrade() != null){
						gradeTextFild.setText(studentInCC.getGrade().toString() );
					}
					if (studentInCC.getNotes() != null){
						notesTextFild.setText(studentInCC.getNotes() );
					}
				}
				
				ArrayList<Object> arr = new ArrayList<>();
				arr.add(studentInCC.getCourse().getNumber());
				arr.add(studentInCC.getStudentId());
				arr.add(studentInCC.getCourse().getAcUnit().getId());
				arr.add(studentInCC.getClassId());
				arr.add(taskInCC.getTaskId()); 
				Packet pck = new Packet(PacketId.REQUIRE_USER_ENTITY,PacketSub.GET_TASK_SOLUTION,3,arr);
				Main.sendToServer(pck);
			}
		}
	}

}
