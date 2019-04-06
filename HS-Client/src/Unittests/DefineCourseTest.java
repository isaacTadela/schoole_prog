package Unittests;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import junit.framework.TestCase;
import tests_helper.TestHelperUtil;
import tests_helper.TestInterface;

public class DefineCourseTest extends TestCase implements TestInterface{
	
	private TestHelperUtil testHelperUtil;
	
	private boolean isResponseExpectedToReturnIsAdded = false;
	private String courseNumberToBeAddedForTesting = "111";
	private String courseAcUnitToBeAddedForTesting = "01";

	// method mimicking same method that sends the request to define new course to the server.
	private void defineNewCourseRequest(String courseNum,String courseName,String academicUnit, String weekyHours,String preds){
		ArrayList<String> dataArr = new ArrayList<>();
		dataArr.add(courseNum);
		dataArr.add(courseName);
		dataArr.add(academicUnit);
		dataArr.add(weekyHours);
		dataArr.add(preds);
		Packet packet = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.DEFINE_COURSE_SUBMIT,0,dataArr);
		try {
			testHelperUtil.getClient().sendToServer(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	/**
	 * method checks if course with specific number and unit_number exists in database
	 * @param courseNumber couse_number
	 * @param acUnitNumber unit_number
	 * @return returns if item was found or not
	 */
	private boolean isCourseExist(String courseNumber,String acUnitNumber){
		String query = "Select * From course Where number = '" + courseNumberToBeAddedForTesting +"' and unit_number = '" + courseAcUnitToBeAddedForTesting + "'";
		ArrayList<ArrayList<Object>> resArr = testHelperUtil.executeRawQuery(query);
		if (resArr == null || resArr.size() == 0){
			return false;
		}
		return true;
	}
	
	@Before
    public void setUp() throws Exception {
		testHelperUtil = TestHelperUtil.getInstance();
		testHelperUtil.addListener(this);
    }
    
	
	
	
	
    @Test
    public void testDefineNewCourse(){
    	// first of all we need to delete the test course if it already exist, in order to test the add new course functionality.
    	String where = "number = '" + courseNumberToBeAddedForTesting + "' and unit_number = '" + courseAcUnitToBeAddedForTesting + "'";
    	testHelperUtil.deleteItemIfExisting("course",where);
    	// verifying that the course we are about to create isn't available yet.
    	assertFalse(isCourseExist(courseNumberToBeAddedForTesting,courseAcUnitToBeAddedForTesting));
    	// now that the course we want to test creating does not exist then its time to attempt to create it.
    	isResponseExpectedToReturnIsAdded = true;
    	defineNewCourseRequest(courseNumberToBeAddedForTesting,"test course",courseAcUnitToBeAddedForTesting, "6","[]");
    	testHelperUtil.waitUntilResponse(15000);
    	// verifying that the course we attempted to create is actually available now.
    	boolean isExist = isCourseExist(courseNumberToBeAddedForTesting,courseAcUnitToBeAddedForTesting);
    	//isExist expected to be true
    	assertTrue(isExist);
    	isResponseExpectedToReturnIsAdded = false;
    	defineNewCourseRequest(courseNumberToBeAddedForTesting,"test course",courseAcUnitToBeAddedForTesting, "6","[]");
    	// waiting on response to arrive on AbstractClient Thread for assertion on response to be performed.
    	testHelperUtil.waitUntilResponse(15000);
    }

    
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.DEFINE_COURSE_SUBMIT){
					boolean isAdded = (Boolean)pck.getData();
					if (isAdded){
						assertTrue(isResponseExpectedToReturnIsAdded);
					}else{
						assertFalse(isResponseExpectedToReturnIsAdded);
					}
					
				}
			}
		}
	}

}
