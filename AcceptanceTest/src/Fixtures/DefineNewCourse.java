package Fixtures;

import java.io.IOException;
import java.util.ArrayList;

import app.DefineCourseController;
import common.Packet;
import fit.ActionFixture;
import tests_helper.TestHelperUtil;
import tests_helper.TestInterface;
/**
 * Fit ActionFixture for Define new course Acceptance test.
 * @author Amir
 *
 */
public class DefineNewCourse extends ActionFixture implements TestInterface {
	private TestHelperUtil testHelperUtil;
	private Object response;
	
	private String course1NumberToBeAddedForTesting = "111";
	private String course1AcUnitToBeAddedForTesting = "01";
	private String course1Name = "test course1";
	private String course1WeeklyHours = "6";
	private String course1PredCourses = "[]"; // no pred course
	
	private String course2NumberToBeAddedForTesting = "222";
	private String course2AcUnitToBeAddedForTesting = "01";
	private String course2Name = "test course2";
	private String course2WeeklyHours = "5";	
	private String course2PredCourses = "[01111]";
	
	/**
	 * method connects to server and cleans data that will be recreated for conditions later.
	 */
	public void setupPreconditions(){
		testHelperUtil = TestHelperUtil.getInstance();
		testHelperUtil.addListener(this);
		deleteTestPreconditions();
	}
	
	/**
	 * method deletes data that was created with this test in database.
	 */
	public void deleteTestPreconditions(){
		testHelperUtil.deleteItemIfExisting("course", "number = '" + course1NumberToBeAddedForTesting + "' and unit_number = '" + course1AcUnitToBeAddedForTesting + "'");
		testHelperUtil.deleteItemIfExisting("course", "number = '" + course2NumberToBeAddedForTesting + "' and unit_number = '" + course2AcUnitToBeAddedForTesting + "'");
		testHelperUtil.deleteItemIfExisting("course_pre", "number = '" + course2NumberToBeAddedForTesting + "' and unit_id = '" + course2AcUnitToBeAddedForTesting + "'");
	}
	

	/**
	 * method that creates specific course without predecessors
	 * @return boolean indicating if creating new course was successful or not.
	 */
	public boolean defineCourseOne(){
		
		Packet pck = DefineCourseController.sendRequestDefineCoursePacket(course1NumberToBeAddedForTesting, course1Name, course1AcUnitToBeAddedForTesting, course1WeeklyHours, course1PredCourses);
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
	 * method that creates specific course with a predecessor course.
	 * @return boolean indicating if creating new course was successful or not.
	 */	
	public boolean defineCourseTwo(){
		
		Packet pck = DefineCourseController.sendRequestDefineCoursePacket(course2NumberToBeAddedForTesting, course2Name, course2AcUnitToBeAddedForTesting, course2WeeklyHours, course2PredCourses);
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
	 * Method checks if the second course created has in database data about the predecessor course and returns it.
	 * @return String value of the course id that is predecessor to second course created.
	 */
	public String courseTwoPred(){
		String query = "SELECT * FROM course_pre WHERE number = '" + course2NumberToBeAddedForTesting + "' and unit_id = '" + course2AcUnitToBeAddedForTesting + "'";
		ArrayList<ArrayList<Object>> arr = testHelperUtil.executeRawQuery(query);
		if(arr.size()>0){
			return (String)arr.get(0).get(3)+(String)arr.get(0).get(2);
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
