package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import common.AcademicUnit;
import common.Course;
import common.Parent;
import common.SchoolClass;
import common.Semester;
import common.Student;
import common.Teacher;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ChoiceBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
/**
 * controller for default double clickable table view window
 * @author Amir
 *
 */
public class SelectableTableInformationController implements Initializable,ControllerIF{

	ArrayList<Object> data;
	
	@FXML
	TableView table;
	@FXML
	Label title;
	
	Long lastClick;
	int lastPickedSelectionIndex;
	
	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Information Window";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Main.setRightPaneController(this);
		lastClick = System.currentTimeMillis();
		lastPickedSelectionIndex = -1;
		if (resources instanceof DataBundle){
			DataBundle bundle = (DataBundle)resources;
			 data = (ArrayList<Object>)bundle.getData();
			 if (data.size() > 0){
				 if (data.get(0) instanceof Semester){
					 	title.setText("Semester Information: ");
					 	TableColumn column1 = new TableColumn("Semester Id");
					 	TableColumn column2 = new TableColumn("Semester Season");
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("season"));	
					 	column1.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
						table.setItems(dataForTable);
						table.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
							if (isConfirmDoubleClick()){
								Semester selectedItem = (Semester)table.getSelectionModel().getSelectedItem();
								Main.showNewRightPane("show_semester_details.fxml",selectedItem);
							}
				        });
				 }
				 if (data.get(0) instanceof AcademicUnit){
					 title.setText("Academic Units Information: ");
					 	TableColumn column1 = new TableColumn("Unit Number");
					 	TableColumn column2 = new TableColumn("Unit Name");
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("name"));	
					 	column1.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
						table.setItems(dataForTable);
				 }
				 if (data.get(0) instanceof Parent){
					 title.setText("Parents Information: ");
					 	TableColumn column1 = new TableColumn("Id");
					 	TableColumn column2 = new TableColumn("First name");
					 	TableColumn column3 = new TableColumn("Last name");
					 	TableColumn<Parent, String> column4 = new TableColumn<>("Ban Expiration");
					 	
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("pname"));	
					 	column3.setCellValueFactory(new PropertyValueFactory<>("fname"));	
					 	
					 	column4.setCellValueFactory(
								new Callback<CellDataFeatures<Parent, String>, ObservableValue<String>>() {

									@Override
									public ObservableValue<String> call(CellDataFeatures<Parent, String> param) {
										// TODO Auto-generated method stub
										System.out.println("current: "+SemesterController.currentSemester.toString());
										System.out.println("current: "+SemesterController.currentSemester.toString());
										if (param !=null){
											return new SimpleStringProperty(param.getValue().isBannedOrUntil(SemesterController.currentSemester.getCurrentDate()));
										}else{
											return new SimpleStringProperty("null");
										}
									}
								}
						);
					 	 

					 	column1.prefWidthProperty().bind(table.widthProperty().divide(4));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(4));
					 	column3.prefWidthProperty().bind(table.widthProperty().divide(4));
					 	column4.prefWidthProperty().bind(table.widthProperty().divide(4));
					 	
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
					 	table.getColumns().add(column3);
					 	table.getColumns().add(column4);
					 	
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
						table.setItems(dataForTable);
				 }
				 if (data.get(0) instanceof Student){
					 title.setText("Students Information: ");
					 	TableColumn column1 = new TableColumn("Id");
					 	TableColumn column2 = new TableColumn("First name");
					 	TableColumn column3 = new TableColumn("Last name");
					 	
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("pname"));	
					 	column3.setCellValueFactory(new PropertyValueFactory<>("fname"));	
					 	
					 	column1.prefWidthProperty().bind(table.widthProperty().divide(3));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(3));
					 	column3.prefWidthProperty().bind(table.widthProperty().divide(3));
					 	
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
					 	table.getColumns().add(column3);

					 	
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data );
						table.setItems(dataForTable);
						table.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
							if (isConfirmDoubleClick()){
								Student selectedItem = (Student)table.getSelectionModel().getSelectedItem();
								Main.showNewRightPane("show_student_details.fxml",selectedItem);
							}
				        });
				 }	
				 if (data.get(0) instanceof Teacher){
					 	title.setText("Teachers Information: ");
					 	TableColumn column1 = new TableColumn("Id");
					 	TableColumn column2 = new TableColumn("First name");
					 	TableColumn column3 = new TableColumn("Last name");
					 	
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("pname"));	
					 	column3.setCellValueFactory(new PropertyValueFactory<>("fname"));	
					 	
					 	column1.prefWidthProperty().bind(table.widthProperty().divide(3));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(3));
					 	column3.prefWidthProperty().bind(table.widthProperty().divide(3));
					 	
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
					 	table.getColumns().add(column3);

					 	
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
						table.setItems(dataForTable);
						table.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
							if (isConfirmDoubleClick()){
								Teacher selectedItem = (Teacher)table.getSelectionModel().getSelectedItem();
								Main.showNewRightPane("show_teacher_details.fxml",selectedItem);
							}
				        });
				 }
				  if (data.get(0) instanceof Course){
					 	title.setText("Course Information: ");
					 	TableColumn column1 = new TableColumn("Course Id");
					 	TableColumn column2 = new TableColumn("Course Name");
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("name"));	
					 	column1.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
						table.setItems(dataForTable);
						table.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
							if (isConfirmDoubleClick()){
								Course selectedItem = (Course)table.getSelectionModel().getSelectedItem();
								Main.showNewRightPane("show_course_information_layout.fxml",selectedItem);
							}
				        });
				 } 
				 
				 if (data.get(0) instanceof SchoolClass){
					 	title.setText("Course Information: ");
					 	TableColumn column1 = new TableColumn("Class Id");
					 	TableColumn column2 = new TableColumn("Class Name");
					 	column1.setCellValueFactory(new PropertyValueFactory<>("id"));
					 	column2.setCellValueFactory(new PropertyValueFactory<>("name"));	
					 	column1.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	column2.prefWidthProperty().bind(table.widthProperty().divide(2));
					 	table.getColumns().add(column1);
					 	table.getColumns().add(column2);
						ObservableList<Object> dataForTable = FXCollections.observableArrayList(data);
						table.setItems(dataForTable);
						table.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
							if (isConfirmDoubleClick()){
								SchoolClass selectedItem = (SchoolClass)table.getSelectionModel().getSelectedItem();
								Main.showNewRightPane("show_class_information_layout.fxml",selectedItem);
							}
				        });
				 } 				 
				 
			 }
		}
	}
	/**
	 * method checks if there was double click
	 * @return boolean if it was double click or not
	 */
	private boolean isConfirmDoubleClick(){
		if (lastPickedSelectionIndex == table.getSelectionModel().getSelectedIndex()){
			if (lastClick+2000 > System.currentTimeMillis()){
				return true;
			}
		}
		lastClick = System.currentTimeMillis();
		lastPickedSelectionIndex = table.getSelectionModel().getSelectedIndex();
		return false;
	}

}
