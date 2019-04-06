package app;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import common.Course;
import common.Packet;
import common.SchoolClass;
import common.Teacher;
import enums.PacketId;
import enums.PacketSub;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert.AlertType;
/**
 * controller for statistics window
 * @author Amir
 *
 */
public class StatisitcsController implements Initializable, ControllerIF{

	@FXML 
	ComboBox<String> statisticsType;
	
	@FXML
	ComboBox<String> statisticsCriterion;
	
	@FXML
	ComboBox<Object> teachersOrClassesList;
	
	@FXML
	MenuButton semestersChoiceBox;
	
	@FXML
	Label warningMsg;
	
	@FXML
	BarChart<?,?> statistics;
	
	@FXML
	CategoryAxis x;
	
	@FXML
	NumberAxis y;
	
	@Override
	public void onResponse(Object msg) {
		// TODO Auto-generated method stub
		if (msg instanceof Packet)
		{
			Packet pck = (Packet)msg;
			if (pck.getPacketId()==PacketId.REQUIRE_ARRAY_LIST)
			{
				if (pck.getPacketSub()==PacketSub.GET_SCHOOL_TEACHERS)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<Teacher> listOfTeachers = new ArrayList<Teacher>();
					for (int i=0; i<resultList.size(); i++)
						listOfTeachers.add(new Teacher((String)resultList.get(i).get(0),(String)resultList.get(i).get(1),(String)resultList.get(i).get(2),(Integer)null,(Integer)null));
					ObservableList<Object> obsList= FXCollections.observableArrayList(listOfTeachers);
					teachersOrClassesList.setItems(obsList);
				}
				if (pck.getPacketSub()==PacketSub.GET_SCHOOL_CLASSES)
				{
					ArrayList<ArrayList<Object>> resultList = (ArrayList<ArrayList<Object>>)pck.getData();
					ArrayList<SchoolClass> listOfClasses = new ArrayList<SchoolClass>();
					for (int i=0; i<resultList.size(); i++)
						 listOfClasses.add(new SchoolClass(resultList.get(i)));
					ObservableList<Object> obsList = FXCollections.observableArrayList(listOfClasses);
					teachersOrClassesList.setItems(obsList);
				}
				if (pck.getPacketSub() == PacketSub.GET_ALL_SEMESTERS){
					ArrayList<ArrayList<Object>> resArr = (ArrayList<ArrayList<Object>>)pck.getData();
					for (int i=0;i<resArr.size();i++){
						Integer semesterId = (Integer)resArr.get(i).get(0);
						CheckMenuItem temp = new CheckMenuItem(semesterId+"");
						temp.setMnemonicParsing(false);
						semestersChoiceBox.getItems().add(temp);
					}
				}
				if (pck.getPacketSub()==PacketSub.GET_SPECIFIC_TEACHER_STATISTICS)
				{
					ArrayList<ArrayList<Object>> resultArray= (ArrayList<ArrayList<Object>>)pck.getData();
					String teacherName = ((Teacher)teachersOrClassesList.getValue()).getPname()+" "+((Teacher)teachersOrClassesList.getValue()).getFname();
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							statistics.getData().clear();
							statistics.setTitle(teacherName+" Statistics");
							x.setLabel("Class Name");
							y.setLabel("Grades Average");
						}});
					XYChart.Series set= new XYChart.Series<>();
					for (int i=0; i<resultArray.size(); i++)
						if(resultArray.get(i).get(1)!=null)//if this class has received some grades with this teacher
							set.getData().add(new XYChart.Data((String)resultArray.get(i).get(0)+"",resultArray.get(i).get(1)));
						Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							statistics.getData().addAll(set); 
						}});
				}
				if (pck.getPacketSub()==PacketSub.GET_CLASS_STATISTICS_WITH_DIFFERENT_TEACHERS)
				{
					ArrayList<ArrayList<Object>> resultArray= (ArrayList<ArrayList<Object>>)pck.getData();
					String className = ((SchoolClass)teachersOrClassesList.getValue()).getName();
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							statistics.getData().clear();
							statistics.setTitle(className+" Statistics");
							x.setLabel("Teacher Name");
							y.setLabel("Grades Average");
						}});
					XYChart.Series set= new XYChart.Series<>();
					for (int i=0; i<resultArray.size(); i++)
						if(resultArray.get(i).get(2)!=null)//if the class has received some grades with this teacher
							set.getData().add(new XYChart.Data((String)resultArray.get(i).get(0)+" " +resultArray.get(i).get(1),resultArray.get(i).get(2)));
						Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							statistics.getData().addAll(set); 
						}});
				}
				if (pck.getPacketSub()==PacketSub.GET_CLASS_STATISTICS_IN_DIFFERENT_COURSES)
				{
					ArrayList<ArrayList<Object>> resultArray= (ArrayList<ArrayList<Object>>)pck.getData();
					String className = ((SchoolClass)teachersOrClassesList.getValue()).getName();
					Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							statistics.getData().clear();
							statistics.setTitle(className+" Statistics");
							x.setLabel("Course Name");
							y.setLabel("Grades Average");
						}});
					XYChart.Series set= new XYChart.Series<>();
					for (int i=0; i<resultArray.size(); i++)
						if(resultArray.get(i).get(1)!=null)//if the class has received some grades with this teacher
							set.getData().add(new XYChart.Data((String)resultArray.get(i).get(0),resultArray.get(i).get(1)));
						Platform.runLater(new Runnable(){
						@Override 
						public void run() {		
							statistics.getData().addAll(set); 
						}});
				}
			}
		}

	}

	@Override
	public String getWindowTitle() {
		// TODO Auto-generated method stub
		return "Statsitics Window";
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		Main.setRightPaneController(this);
		semestersChoiceBox.getItems().clear();
		ArrayList<String> typeList = new ArrayList<String>();
		typeList.add("Teacher statistics in different classes");
		typeList.add("Class statistics with different teachers");
		typeList.add("Class statistics in different courses");
		ObservableList<String> obsList1 = FXCollections.observableArrayList(typeList);
		statisticsType.setItems(obsList1);
		ArrayList<String> criterionList = new ArrayList<String>();
		criterionList.add("Grades");
		ObservableList<String> obsList2 = FXCollections.observableArrayList(criterionList);
		statisticsCriterion.setItems(obsList2);
		Packet pck=new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_ALL_SEMESTERS,0,"");
		Main.sendToServer(pck);
	}
	/**
	 * method requests teacher or class list
	 */
	public void onClickSetTeachersOrClassesList()
	{
		Packet pck=null;
		String type = statisticsType.getValue();
		if (type==null)
		{
			teachersOrClassesList.getItems().clear();
			return;
		}
		if(type.equals("Teacher statistics in different classes"))
		{
			pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_TEACHERS,0,"");
			teachersOrClassesList.getItems().clear();
		}
		else
		{
			pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SCHOOL_CLASSES,0,"");
			teachersOrClassesList.getItems().clear();
		}
			Main.sendToServer(pck);		
	}
	/**
	 * method executed when clicking on Get Statistics button
	 */
	public void onClickGetStatistics()
	{
		Platform.runLater(new Runnable(){
			@Override 
			public void run() {		
				String type = statisticsType.getValue();
				Object chosenObject = teachersOrClassesList.getValue();
				String criterion = statisticsCriterion.getValue();
				String semestersChosenSqlString;
				semestersChosenSqlString = getSqlSemestersString(getSemestersSelectedAsStringArray());
				if (type==null||criterion==null||chosenObject==null)
				{
					warningMsg.setText("Please choose all the required\ninformation to proceed.");
					return;
				}
				if (semestersChosenSqlString==null)
				{
					warningMsg.setText("Please choose at least one semester.");
					return;
				}
				warningMsg.setText("");
				ArrayList<String> dataArray = new ArrayList<String>(); // here save the data sent to server
				switch(criterion)
				{
				case "Grades":{
					criterion="Avg(grade)";
					break;
					}
				}
				if (chosenObject instanceof SchoolClass)
					dataArray.add(((SchoolClass) chosenObject).getId());
				else if(chosenObject instanceof Teacher)
					dataArray.add(((Teacher)chosenObject).getId());
				dataArray.add(criterion);
				dataArray.add(semestersChosenSqlString);
				Packet pck = null;
				if (type.equals("Teacher statistics in different classes"))
					pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_SPECIFIC_TEACHER_STATISTICS,0,dataArray);
				else if(type.equals("Class statistics with different teachers"))
					pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_CLASS_STATISTICS_WITH_DIFFERENT_TEACHERS,0,dataArray);
				else 
					pck = new Packet(PacketId.REQUIRE_ARRAY_LIST,PacketSub.GET_CLASS_STATISTICS_IN_DIFFERENT_COURSES,0,dataArray);
				Main.sendToServer(pck);
			}});

	}	
	/**
	 * method builds an arraylist os semester ids
	 * @return ArrayList of all semesters ids
	 */
	private ArrayList<String> getSemestersSelectedAsStringArray(){
		ObservableList<MenuItem> arr =  semestersChoiceBox.getItems();
		ArrayList<String> semesters = new ArrayList<String>();
		for (int i=0;i<arr.size();i++){
			CheckMenuItem temp = (CheckMenuItem) arr.get(i);
			if (temp.isSelected()){
				semesters.add(temp.getText()); 
			}
		}
		return semesters;
	}
	/**
	 * methods builds condition to send for sql query
	 * @param semestersChosenAsArray semester ids as string in arraylist
	 * @return the string contains the sql part of the condition ready.
	 */
	private String getSqlSemestersString(ArrayList<String> semestersChosenAsArray)
	{
		if (semestersChosenAsArray.size()==0)
			return null;
		String chosenSemestersString = " ( ";
		for (int i=0;i<semestersChosenAsArray.size(); i++)
		{
			chosenSemestersString += "semester_id = " + semestersChosenAsArray.get(i);
			if (i+1<semestersChosenAsArray.size())
				chosenSemestersString +=" OR ";
		}
		chosenSemestersString += " ) ";
		return chosenSemestersString;
	}

}
