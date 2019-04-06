package Fixtures;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import app.DefineNewTask;

import common.Packet;
import fit.ActionFixture;
import tests_helper.TestHelperUtil;
import tests_helper.TestInterface;

/**
 * Fit Action Fixture for Create New Assignment acceptance test.
 * @author Amir
 *
 */
public class CreateNewAssignment extends ActionFixture implements TestInterface {
	private TestHelperUtil testHelperUtil;
	private Object response;

	private String taskIdToBeAddedForTesting = "1234";

	private String finalSubmmisionDateToBeAddedForTesting = "2017-10-12";
	private String finalSubmmisionDate2ToBeAddedForTesting = "2017-11-12";

	private String courseNumToBeAddedForTesting = "666";
	private String unitNumToBeAddedForTesting = "00";
	private String classNumToBeAddedForTesting = "222221";
	private String fileExtensionToBeAddedForTesting = "txt";
	
	byte[] file = {'h','e','l','l','o'};
	
	/**
	 * method that starts connection with server and database
	 */
	public void setupConnection(){
		testHelperUtil = TestHelperUtil.getInstance();
		testHelperUtil.addListener(this);
		resetTestData();
	}
	
	/**
	 * the test need preconditions, this method provides database with preconditions needed for the test.
	 */
	public void setPreconditions(){
		// insert a two new class C for test
		testHelperUtil.insertItem("class","'TESTCLASS C','"+classNumToBeAddedForTesting+"'"); // insert class C
		
		// insert a six new students S for test
		testHelperUtil.insertItem("user","'333331','1234','student','TESTSTUDENT','S'"); // insert student S 
		testHelperUtil.insertItem("student", "'333331',null,null,'"+classNumToBeAddedForTesting+"'"); // insert student S 

		// insert a new academic unit [AU] for test
		testHelperUtil.insertItem("academic_unit","'" + unitNumToBeAddedForTesting + "','TESTUNIT'"); // insert in table academic_unit
		
		
		// insert a new teacher [T] for test
		testHelperUtil.insertItem("user","'555551','1234','teacher','TESTTEACHER','T'"); // insert in table user
		testHelperUtil.insertItem("teacher","'555551',500,0"); // insert in table teacher
		testHelperUtil.insertItem("teacher_academic_unit","'555551','" + unitNumToBeAddedForTesting + "'"); // insert in table techer_academic_unit
		
		// insert a three new courses A for test
		testHelperUtil.insertItem("course","'" + courseNumToBeAddedForTesting + "','"+unitNumToBeAddedForTesting+"','TESTCOURSE A',5"); // insert course A

		// assign class C to course A
		testHelperUtil.insertItem("course_in_class","'"+courseNumToBeAddedForTesting+"','"+unitNumToBeAddedForTesting+"','"+classNumToBeAddedForTesting+"','555551',1");
		
		// assign student S to class in course 
		testHelperUtil.insertItem("student_in_course_in_class","'333331','"+courseNumToBeAddedForTesting+"','"+unitNumToBeAddedForTesting+"','"+classNumToBeAddedForTesting+"','0','test course and scores'");
		
	}
	
	/**
	 * method resets and returns database as it was before the test execution.
	 */
	public void resetTestData(){
		testHelperUtil.deleteItemIfExisting("task"," id =  '" + taskIdToBeAddedForTesting + "'"); 
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id =  '333331' and course_number = '"+courseNumToBeAddedForTesting+"' and unit_number = '"+unitNumToBeAddedForTesting+"' and class_number = '"+classNumToBeAddedForTesting+"'"); 
		testHelperUtil.deleteItemIfExisting("course_in_class"," course_number = '"+courseNumToBeAddedForTesting+"' and unit_number = '"+unitNumToBeAddedForTesting+"' and class_number = '"+classNumToBeAddedForTesting+"' ");
		testHelperUtil.deleteItemIfExisting("course"," number=  '"+courseNumToBeAddedForTesting+"' and unit_number = '"+unitNumToBeAddedForTesting+"'"); 
		
		testHelperUtil.deleteItemIfExisting("teacher_academic_unit"," teacher_id= '555551'"); // delete from table techer_academic_unit
		testHelperUtil.deleteItemIfExisting("teacher"," id = '555551'"); // delete from table teacher
		testHelperUtil.deleteItemIfExisting("user"," id = '555551'"); // delete from table user
		testHelperUtil.deleteItemIfExisting("academic_unit"," number = '"+unitNumToBeAddedForTesting+"' "); // delete academic unit	
		
		testHelperUtil.deleteItemIfExisting("student"," id = '333331'"); // delete student S
		testHelperUtil.deleteItemIfExisting("user"," id = '333331'"); // delete student S 
		
		testHelperUtil.deleteItemIfExisting("class"," number= '"+classNumToBeAddedForTesting+"' "); // delete class C
		
	}

	/**
	 * method that will create new task based on method that generates packet for new task request.
	 * @return boolean indicating if the task was added into database or not.
	 */
	public boolean createNewTask(){

		Packet pck = DefineNewTask.requestCretaeNewTaskPacket(taskIdToBeAddedForTesting, finalSubmmisionDateToBeAddedForTesting, file, courseNumToBeAddedForTesting
				, unitNumToBeAddedForTesting, classNumToBeAddedForTesting, fileExtensionToBeAddedForTesting);
		try {
			testHelperUtil.getClient().sendToServer(pck);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testHelperUtil.waitUntilResponse(15000);
		if (response instanceof Boolean){
			return (Boolean)response;
		}
		
		return false;
	}	

	/**
	 * method sends same task again with only 1 difference which is the date, in order to be able to check it later.
	 * @return if the task was modified.
	 */
	public boolean editTask(){
		Packet pck = DefineNewTask.requestCretaeNewTaskPacket(taskIdToBeAddedForTesting,finalSubmmisionDate2ToBeAddedForTesting , file, courseNumToBeAddedForTesting
				, unitNumToBeAddedForTesting, classNumToBeAddedForTesting, fileExtensionToBeAddedForTesting);
		try {
			testHelperUtil.getClient().sendToServer(pck);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		testHelperUtil.waitUntilResponse(15000);
		if (response instanceof Boolean){
			return (Boolean)response;
		}
		
		return false;
		
	}
	
	
	/**
	 * method checks database looking for the task and picks the date from it and returns it.
	 * @return String with the expiration date of the task in question.
	 */
	public String getExpirationDate(){
		String query = "SELECT * FROM task WHERE id = '" + taskIdToBeAddedForTesting + "' and unit_number = '" + unitNumToBeAddedForTesting + 
					   "' and class_number = '" + classNumToBeAddedForTesting + "' and course_number = '" + courseNumToBeAddedForTesting + "'";
		ArrayList<ArrayList<Object>> arr = testHelperUtil.executeRawQuery(query);
		if(arr.size()>0){
			return ((Date)arr.get(0).get(2)).toString();
		}
		return null;
	}
	
	/**
	 * method checks database looking for the task and picks the file contents from it and returns it.
	 * @return String with the expiration date of the task in question.
	 */
	public String getFileData(){
		String query = "SELECT * FROM task WHERE id = '" + taskIdToBeAddedForTesting + "' and unit_number = '" + unitNumToBeAddedForTesting + 
					   "' and class_number = '" + classNumToBeAddedForTesting + "' and course_number = '" + courseNumToBeAddedForTesting + "'";
		ArrayList<ArrayList<Object>> arr = testHelperUtil.executeRawQuery(query);
		if(arr.size()>0){
			return new String((byte[])arr.get(0).get(3));
		}
		return null;
	}
	
	
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			response = ((Packet) msg).getData();
		}
	}
	
}
