package Unittests;


import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;
import app.DefineNewTask;
import app.TaskDetailsController;
import app.TasksInCourseController;
import app.TeacherStudentInCourse;
import common.Packet;
import common.StudentInCourse;
import common.TasksInCourseInClass;
import enums.PacketId;
import enums.PacketSub;
import junit.framework.TestCase;
import tests_helper.TestHelperUtil;
import tests_helper.TestInterface;

/**
 * Class that tests whether student can see tasks submitted by teacher, and if teacher can see the task solutions for the student.
 * @author Amir
 *
 */
public class TasksSubmissionTest extends TestCase implements TestInterface{

	private TestHelperUtil testHelperUtil;
	private String taskNumberToBeAddedForTesting = "1234";	
	private String courseNumberToBeAddedForTesting = "666";
	private String taskAcUnitToBeAddedForTesting = "00";	
	private String classNumberToBeAddedForTesting = "222221";	
	private String fDate = "2017-8-1";
	private boolean isResponseExpectedToReturnIsAdded = false;
	private byte[] tfile = {'h','e','l','l','o'};
	private byte[] sfile = {'t','h','a','n','k','s'};	
	private String fileExtension = "txt";
	
	@Before
    public void setUp() throws Exception {
		testHelperUtil = TestHelperUtil.getInstance();
		testHelperUtil.addListener(this);
    }
	
	/**
	 * method adds precondition database data.
	 */
	private void setPreconditions(){
		// insert a two new class C for test
		testHelperUtil.insertItem("class","'TESTCLASS C','"+classNumberToBeAddedForTesting+"'"); // insert class C
		
		// insert a six new students S for test
		testHelperUtil.insertItem("user","'333331','1234','student','TESTSTUDENT','S'"); // insert student S 
		testHelperUtil.insertItem("student", "'333331',null,null,'"+classNumberToBeAddedForTesting+"'"); // insert student S 

		// insert a new academic unit [AU] for test
		testHelperUtil.insertItem("academic_unit","'" + taskAcUnitToBeAddedForTesting + "','TESTUNIT'"); // insert in table academic_unit
		
		
		// insert a new teacher [T] for test
		testHelperUtil.insertItem("user","'555551','1234','teacher','TESTTEACHER','T'"); // insert in table user
		testHelperUtil.insertItem("teacher","'555551',500,0"); // insert in table teacher
		testHelperUtil.insertItem("teacher_academic_unit","'555551','" + taskAcUnitToBeAddedForTesting + "'"); // insert in table techer_academic_unit
		
		// insert a three new courses A for test
		testHelperUtil.insertItem("course","'" + courseNumberToBeAddedForTesting + "','"+taskAcUnitToBeAddedForTesting+"','TESTCOURSE A',5"); // insert course A

		// assign class C to course A
		testHelperUtil.insertItem("course_in_class","'"+courseNumberToBeAddedForTesting+"','"+taskAcUnitToBeAddedForTesting+"','"+classNumberToBeAddedForTesting+"','555551',1");
		
		// assign student S to class in course 
		testHelperUtil.insertItem("student_in_course_in_class","'333331','"+courseNumberToBeAddedForTesting+"','"+taskAcUnitToBeAddedForTesting+"','"+classNumberToBeAddedForTesting+"','0','test course and scores'");
		
	}
	/**
	 * Method removes all data that was added during this test to clean the database from test data.
	 */
	public void resetTestData(){
		testHelperUtil.deleteItemIfExisting("submission"," course_number =  '" + courseNumberToBeAddedForTesting + "' and student_id = '333331' and unit_number = '"+ taskAcUnitToBeAddedForTesting+"' and class_number = '" + classNumberToBeAddedForTesting + "' and task_id = '" + taskNumberToBeAddedForTesting + "'"); 
		testHelperUtil.deleteItemIfExisting("task"," id =  '" + taskNumberToBeAddedForTesting + "'"); 
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id =  '333331' and course_number = '"+courseNumberToBeAddedForTesting+"' and unit_number = '"+taskAcUnitToBeAddedForTesting+"' and class_number = '"+classNumberToBeAddedForTesting+"'"); 
		testHelperUtil.deleteItemIfExisting("course_in_class"," course_number = '"+courseNumberToBeAddedForTesting+"' and unit_number = '"+taskAcUnitToBeAddedForTesting+"' and class_number = '"+classNumberToBeAddedForTesting+"' ");
		testHelperUtil.deleteItemIfExisting("course"," number=  '"+courseNumberToBeAddedForTesting+"' and unit_number = '"+taskAcUnitToBeAddedForTesting+"'"); 
		
		testHelperUtil.deleteItemIfExisting("teacher_academic_unit"," teacher_id= '555551'"); // delete from table techer_academic_unit
		testHelperUtil.deleteItemIfExisting("teacher"," id = '555551'"); // delete from table teacher
		testHelperUtil.deleteItemIfExisting("user"," id = '555551'"); // delete from table user
		testHelperUtil.deleteItemIfExisting("academic_unit"," number = '"+taskAcUnitToBeAddedForTesting+"' "); // delete academic unit	
		
		testHelperUtil.deleteItemIfExisting("student"," id = '333331'"); // delete student S
		testHelperUtil.deleteItemIfExisting("user"," id = '333331'"); // delete student S 
		
		testHelperUtil.deleteItemIfExisting("class"," number= '"+classNumberToBeAddedForTesting+"' "); // delete class C
		
	}
	
	/**
	 * method that uses client packet generating method in order to send request to build new task for testing.
	 */
	
	private void submitNewTask(){
    	Packet pck = DefineNewTask.requestCretaeNewTaskPacket(taskNumberToBeAddedForTesting,fDate,tfile, courseNumberToBeAddedForTesting, taskAcUnitToBeAddedForTesting,  classNumberToBeAddedForTesting, fileExtension);
		try {
			isResponseExpectedToReturnIsAdded = true;
			testHelperUtil.getClient().sendToServer(pck);
		} catch (IOException e) {
			fail("failed to send packet to server!");
		}	
	}
	
	/**
	 * method that sends same packet requests as the client to see what tasks a student can see in order to
	 * look for the new task we are testing if we can find.
	 */
	private void doesStudentSeeNewTask(){
		Packet pck = TasksInCourseController.requestGetStudentInCoursePacket(courseNumberToBeAddedForTesting,taskAcUnitToBeAddedForTesting,classNumberToBeAddedForTesting,"1");
		try {
			testHelperUtil.getClient().sendToServer(pck);
		} catch (IOException e) {
			fail("failed to send packet to server!");
		}
	}	
	
	/**
	 * method that uses client packet that requests sending solution for specific task.
	 */
    private void submitSolution() {
		Packet pck = TaskDetailsController.submitSolutionPacketRequest(courseNumberToBeAddedForTesting,"333331",taskAcUnitToBeAddedForTesting,classNumberToBeAddedForTesting,taskNumberToBeAddedForTesting,sfile,"txt");
		try {
			testHelperUtil.getClient().sendToServer(pck);
		} catch (IOException e) {
			fail("failed to send packet to server!");
		}
    }
	
    /**
     * method requests to get information about students that a teacher teaches in specific course.
     */
	private void doesTeacherSeeStudentSubmission() {
		// course_in_class
		Packet pck = TeacherStudentInCourse.getStudentsInTeachersCoursePacket(courseNumberToBeAddedForTesting,taskAcUnitToBeAddedForTesting,classNumberToBeAddedForTesting,"1");
		try {
			testHelperUtil.getClient().sendToServer(pck);
		} catch (IOException e) {
			fail("failed to send packet to server!");
		}
	}

	
	@Test
    public void testDefineNewTask(){
    	resetTestData();
    	setPreconditions();   	
    	submitNewTask();
    	//submitting task again should allow it (considers it editing for the task).
    	submitNewTask();
    	testHelperUtil.waitUntilResponse(15000);
    	doesStudentSeeNewTask();
    	testHelperUtil.waitUntilResponse(15000);
    	submitSolution();
    	testHelperUtil.waitUntilResponse(15000);
    	//submitting solution again should allow it (considers it editing for the solution).
    	submitSolution();
    	testHelperUtil.waitUntilResponse(15000);
    	
    	//if the teacher can see the student as of his students in specific CourseInClass
    	//then he can log into his folders as teacher for that course.
    	//so missing only to check if that student can really be seen as of the teachers students for the course that has the task.
    	doesTeacherSeeStudentSubmission();
    	testHelperUtil.waitUntilResponse(15000);
    	
    	resetTestData();
    }
    


	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_BOOLEAN){
				if (pck.getPacketSub() == PacketSub.DEFINE_TASK_SUBMIT){
					boolean isAdded = (Boolean)pck.getData();
					if (isAdded){
						assertTrue(isResponseExpectedToReturnIsAdded);
					}else{
						assertFalse(isResponseExpectedToReturnIsAdded);
					}
					
				}
			}
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GET_STUDENT_IN_COURSE_TASKS){
					ArrayList<TasksInCourseInClass> taskResultList = (ArrayList<TasksInCourseInClass>)pck.getData();
					boolean isTaskAvailable = checkIfTaskAvailable(taskResultList);
					assertTrue(isTaskAvailable);
				}
			}
			if (pck.getPacketId() == PacketId.REQUIRE_USER_ENTITY){
				if (pck.getPacketSub() == PacketSub.GET_STUDENT_IN_TEACHER_COURSE){

					ArrayList<StudentInCourse> studentInCourseResultList = (ArrayList<StudentInCourse>)pck.getData();
					boolean isStudentAvailable = checkIfStudentAvailable(studentInCourseResultList);
					assertTrue(isStudentAvailable);
				}
			}
		}
	}


	private boolean checkIfTaskAvailable(ArrayList<TasksInCourseInClass> taskResultList) {
		for (int i =0;i<taskResultList.size();i++){
			String taskId = taskResultList.get(i).getTaskId();
			if (taskId.equals(taskNumberToBeAddedForTesting)){
				return true;
			}
		}
		return false;
	}
	
	private boolean checkIfStudentAvailable(ArrayList<StudentInCourse> taskResultList) {
		for (int i =0;i<taskResultList.size();i++){
			String studentId = taskResultList.get(i).getStudentId();
			if (studentId.equals("333331")){
				return true;
			}
		}
		return false;
	}
}
