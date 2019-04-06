package Unittests;


import java.io.IOException;
import java.util.ArrayList;
import org.junit.Before;
import org.junit.Test;

import app.AssignClassToCourse;
import common.Packet;
import enums.PacketId;
import enums.PacketSub;
import junit.framework.TestCase;
import tests_helper.TestHelperUtil;
import tests_helper.TestInterface;

public class NewSemesterPredTest extends TestCase implements TestInterface{

	private TestHelperUtil testHelperUtil;
	
	@Before
    public void setUp() throws Exception {
		testHelperUtil = TestHelperUtil.getInstance();
		testHelperUtil.addListener(this);
    }
	
    @Test
    public void testNewSemesterPredTest(){
    	refreshDB();
    	buildDB();
    	runPredTest();
    	testHelperUtil.waitUntilResponse(15000);
    	refreshDB();
    }
	
    /**
     * Method used to delete existing data that is going to be created in this test for testing.
     */
	private void refreshDB()
	{
		//delete all students in courses in classes
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id = '333336' "); // delete student S5
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id = '333335' "); // delete student S5
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id = '333334' "); // delete student S4
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id = '333333' "); // delete student S3
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id = '333332' "); // delete student S2
		testHelperUtil.deleteItemIfExisting("student_in_course_in_class"," student_id = '333331' "); // delete student S1 

		//delete courses in classes
		testHelperUtil.deleteItemIfExisting("course_in_class"," course_number = '666661' and unit_number = '444441' and class_number = '222221' "); // delete class C1 from course A 
		testHelperUtil.deleteItemIfExisting("course_in_class"," course_number = '666662' and unit_number = '444441' and class_number = '222221' "); // delete class C1 from course B
		testHelperUtil.deleteItemIfExisting("course_in_class"," course_number = '666661' and unit_number = '444441' and class_number = '222222' "); // delete class C2 from course A
		testHelperUtil.deleteItemIfExisting("course_in_class"," course_number = '666662' and unit_number = '444441' and class_number = '222222' "); // delete class C2 from course B 
		
		// delete A and B from being the pre courses of course C 
		testHelperUtil.deleteItemIfExisting("course_pre"," number= '666663' and unit_id= '444441' and pre_course_number= '666661' and pre_course_unit_id = '444441'"); // delete A from being the pre course of C
		testHelperUtil.deleteItemIfExisting("course_pre"," number= '666663' and unit_id= '444441' and pre_course_number= '666662' and pre_course_unit_id = '444441'"); // delete B from being the pre course of C
		
		// delete the three courses [A,B,C] of test
		testHelperUtil.deleteItemIfExisting("course"," number=  '666661' and unit_number = '444441' "); // delete course A
		testHelperUtil.deleteItemIfExisting("course"," number=  '666662' and unit_number = '444441' "); // delete course B
		testHelperUtil.deleteItemIfExisting("course"," number =  '666663' and unit_number = '444441' "); // delete course C

		// delete the teacher [T] of test
		testHelperUtil.deleteItemIfExisting("teacher_academic_unit"," teacher_id= '555551'"); // delete from table techer_academic_unit
		testHelperUtil.deleteItemIfExisting("teacher"," id = '555551'"); // delete from table teacher
		testHelperUtil.deleteItemIfExisting("user"," id = '555551'"); // delete from table user

		// delete the academic unit [AU] of test
		testHelperUtil.deleteItemIfExisting("academic_unit"," number = '444441' "); // delete academic unit
		
		// delete the six students [S1,S2,S3,S4,S5,S6] of test
		testHelperUtil.deleteItemIfExisting("user"," id = '333331'"); // delete student S1 
		testHelperUtil.deleteItemIfExisting("student"," id = '333331'"); // delete student S1 
		testHelperUtil.deleteItemIfExisting("user"," id = '333332'"); // delete student S2
		testHelperUtil.deleteItemIfExisting("student"," id = '333332'"); // delete student S2
		testHelperUtil.deleteItemIfExisting("user"," id = '333333'"); // delete student S3 
		testHelperUtil.deleteItemIfExisting("student"," id = '333333'"); // delete student S3 
		testHelperUtil.deleteItemIfExisting("user"," id = '333334'"); // delete student S4 
		testHelperUtil.deleteItemIfExisting("student"," id = '333334'"); // delete student S4
		testHelperUtil.deleteItemIfExisting("user"," id = '333335'"); // delete student S5
		testHelperUtil.deleteItemIfExisting("student"," id = '333335'"); // delete student S5
		testHelperUtil.deleteItemIfExisting("user"," id = '333336'"); // delete student S6
		testHelperUtil.deleteItemIfExisting("student"," id = '333336'"); // delete student S6
		
		// delete the two classes [C1,C2] of test
		testHelperUtil.deleteItemIfExisting("class"," number= '222221' "); // delete class C1
		testHelperUtil.deleteItemIfExisting("class"," number= '222222' "); // delete class C2
	}
	
	/**
	 * Method sets the precondition data in database required in order to run the test.
	 */
	private void buildDB()
	{
		// insert a two new classes [C1,C2] for test
		testHelperUtil.insertItem("class","'TESTCLASS C1','222221'"); // insert class C1
		testHelperUtil.insertItem("class","'TESTCLASS C2','222222'"); // insert class C2
		
		// insert a six new students [S1,S2,S3,S4,S5,S6] for test
		// students will be in class C1
		testHelperUtil.insertItem("user","'333331','1234','student','TESTSTUDENT','S1'"); // insert student S1 
		testHelperUtil.insertItem("student", "'333331',null,null,'222221'"); // insert student S1 
		testHelperUtil.insertItem("user","'333332','1234','student','TESTSTUDENT','S2'"); // insert student S2
		testHelperUtil.insertItem("student", "'333332',null,null,'222221'"); // insert student S2
		testHelperUtil.insertItem("user","'333333','1234','student','TESTSTUDENT','S3'"); // insert student S3 
		testHelperUtil.insertItem("student", "'333333',null,null,'222221'"); // insert student S3 
		testHelperUtil.insertItem("user","'333334','1234','student','TESTSTUDENT','S4'"); // insert student S4 
		testHelperUtil.insertItem("student", "'333334',null,null,'222221'"); // insert student S4
		testHelperUtil.insertItem("user","'333335','1234','student','TESTSTUDENT','S5'"); // insert student S5
		testHelperUtil.insertItem("student", "'333335',null,null,'222221'"); // insert student S5
		testHelperUtil.insertItem("user","'333336','1234','student','TESTSTUDENT','S6'"); // insert student S6
		testHelperUtil.insertItem("student", "'333336',null,null,'222221'"); // insert student S6
		
		// insert a new academic unit [AU] for test
		testHelperUtil.insertItem("academic_unit","'444441','TESTUNIT'"); // insert in table academic_unit
		
		// insert a new teacher [T] for test
		testHelperUtil.insertItem("user","'555551','1234','teacher','TESTTEACHER','T'"); // insert in table user
		testHelperUtil.insertItem("teacher","'555551',500,0"); // insert in table teacher
		testHelperUtil.insertItem("teacher_academic_unit","'555551','444441'"); // insert in table techer_academic_unit
		
		// insert a three new courses [A,B,C] for test
		testHelperUtil.insertItem("course","'666661','444441','TESTCOURSE A',5"); // insert course A
		testHelperUtil.insertItem("course","'666662','444441','TESTCOURSE B',5"); // insert course B
		testHelperUtil.insertItem("course","'666663','444441','TESTCOURSE C',5"); // insert course C
		
		// insert A and B as the pre courses for C 
		testHelperUtil.insertItem("course_pre","'666663','444441','666661','444441'"); // insert A as the pre course of C
		testHelperUtil.insertItem("course_pre","'666663','444441','666662','444441'"); // insert B as the pre course of C
		
		// now assign some classes to courses ***without assigning students to any courses***
		// assign class C1 to course A
		testHelperUtil.insertItem("course_in_class","'666661','444441','222221','555551',1"); // insert class C1 and course A to course_in_class table
		// assign class C1 to course B
		testHelperUtil.insertItem("course_in_class","'666662','444441','222221','555551',1"); // insert class C1 and course B to course_in_class table
		// assign class C2 to course A
		testHelperUtil.insertItem("course_in_class","'666661','444441','222222','555551',1"); // insert class C2 and course A to course_in_class table
		// assign class C2 to course B
		testHelperUtil.insertItem("course_in_class","'666662','444441','222222','555551',1"); // insert class C2 and course B to course_in_class table
		
		// now assign some students to courses in classes. 
		// for the coming comments, we define "X-Y:Z" as the sentence: course X in class Y with grade Z
		testHelperUtil.insertItem("student_in_course_in_class","'333331','666661','444441','222221',70,' '"); // insert student S1 to A-C1:70
		testHelperUtil.insertItem("student_in_course_in_class","'333331','666662','444441','222221',30,' '"); // insert student S1 to B-C1:30
		testHelperUtil.insertItem("student_in_course_in_class","'333331','666662','444441','222222',60,' '"); // insert student S1 to B-C2:60
		
		testHelperUtil.insertItem("student_in_course_in_class","'333332','666661','444441','222221',54,' '"); // insert student S2 to A-C1:54
		testHelperUtil.insertItem("student_in_course_in_class","'333332','666662','444441','222222',80,' '"); // insert student S2 to B-C2:80
		
		testHelperUtil.insertItem("student_in_course_in_class","'333333','666661','444441','222222',60,' '"); // insert student S3 to A-C2:60
		testHelperUtil.insertItem("student_in_course_in_class","'333333','666662','444441','222222',55,' '"); // insert student S3 to B-C2:55
		
		testHelperUtil.insertItem("student_in_course_in_class","'333334','666661','444441','222221',50,' '"); // insert student S4 to A-C1:50
		testHelperUtil.insertItem("student_in_course_in_class","'333334','666662','444441','222221',90,' '"); // insert student S4 to B-C1:90
		testHelperUtil.insertItem("student_in_course_in_class","'333334','666661','444441','222222',90,' '"); // insert student S4 to A-C2:90
		testHelperUtil.insertItem("student_in_course_in_class","'333334','666662','444441','222222',40,' '"); // insert student S4 to B-C2:40
		
		testHelperUtil.insertItem("student_in_course_in_class","'333335','666662','444441','222221',100,' '"); // insert student S5 to B-C1:100
		
		// according to what we have in database:
		// students S2,S5,S6 does not have the pre courses of course C, while S1,S3,S4 have all the pre courses of course C.
	}
	
	/**
	 * The method that is actually responsible for the test itself.
	 */
	private void runPredTest()
	{
		Packet packet = AssignClassToCourse.getPacketAssignStudentsInClassToCourse("222221","444441","666663");
		try {
			testHelperUtil.getClient().sendToServer(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	/**
	 * Method get called upon receiving data from server.
	 */
	@Override
	public void onResponse(Object msg) {
		if (msg instanceof Packet){
			Packet pck = (Packet)msg;
			if (pck.getPacketId() == PacketId.REQUIRE_ARRAY_LIST){
				if (pck.getPacketSub() == PacketSub.ASSIGN_STUDENTS_IN_CLASS_TO_COURSE_ONLY_THOSE_WHO_HAVE_ALL_PRECOURSES_AND_GET_THEM)
				{
					checkPacketDataIfCorrect(pck);
					assertFalse(isStudentInCourseInClassExist("333332","222221","444441","666663"));
					assertFalse(isStudentInCourseInClassExist("333335","222221","444441","666663"));
					assertFalse(isStudentInCourseInClassExist("333336","222221","444441","666663"));
					assertTrue(isStudentInCourseInClassExist("333331","222221","444441","666663"));
					assertTrue(isStudentInCourseInClassExist("333333","222221","444441","666663"));
					assertTrue(isStudentInCourseInClassExist("333334","222221","444441","666663"));
				}
			}
		}
	}
	
	/**
	 * Method that can check the affiliation of student to course in class.
	 * @param studentId student id
	 * @param classNumber class_number
	 * @param acUnitNumber unit_number
	 * @param courseNumber course_number
	 * @return true if the student has the course in class.
	 */
	private boolean isStudentInCourseInClassExist(String studentId,String classNumber,String acUnitNumber,String courseNumber)
	{
		
		String where= " student_id='"+studentId+"' and course_number='"+courseNumber+"' and unit_number='"+acUnitNumber+"' and class_number= '"+classNumber +"' ";
		String query = "Select student_id From student_in_course_in_class Where " + where;
		ArrayList<ArrayList<Object>> resArr = testHelperUtil.executeRawQuery(query);
		if (resArr == null || resArr.size() == 0){
			return false;
		}
		return true;
	}
	
	/**
	 * Method analyzes data received from server response to check which students were accepted for
	 * the new course and which not.
	 * @param pck
	 */
	private void checkPacketDataIfCorrect(Packet pck)
	{
		ArrayList<ArrayList<ArrayList<Object>>> resultList = (ArrayList<ArrayList<ArrayList<Object>>>)pck.getData();
		ArrayList<ArrayList<Object>> allStudentsArr = resultList.get(0);
		ArrayList<ArrayList<Object>> qualifiedStudentsArr = resultList.get(1);
		ArrayList<String> allStudentsId = new ArrayList<String>();
		ArrayList<String> qualifiedStudentsId = new ArrayList<String>();
		ArrayList<String> notQualifiedStudentsId;
		for (int i=0; i<allStudentsArr.size(); i++)
			allStudentsId.add((String)allStudentsArr.get(i).get(0));
		for (int i=0; i<qualifiedStudentsArr.size(); i++)
			qualifiedStudentsId.add((String)qualifiedStudentsArr.get(i).get(0));
		allStudentsId.removeAll(qualifiedStudentsId);
		notQualifiedStudentsId=allStudentsId;
		assertTrue(notQualifiedStudentsId.contains("333332"));
		assertTrue(notQualifiedStudentsId.contains("333335"));
		assertTrue(notQualifiedStudentsId.contains("333336"));
		assertTrue(qualifiedStudentsId.contains("333331"));
		assertTrue(qualifiedStudentsId.contains("333333"));
		assertTrue(qualifiedStudentsId.contains("333334"));
		assertFalse(qualifiedStudentsId.contains("333332"));
		assertFalse(qualifiedStudentsId.contains("333335"));
		assertFalse(qualifiedStudentsId.contains("333336"));
		assertFalse(notQualifiedStudentsId.contains("333331"));
		assertFalse(notQualifiedStudentsId.contains("333333"));
		assertFalse(notQualifiedStudentsId.contains("333334"));
	}
	

}
