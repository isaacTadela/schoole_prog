package hs_server;


import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.JFrame;

import org.ini4j.Ini;
import org.ini4j.InvalidFileFormatException;

import common.AcademicUnit;
import common.Course;
import common.CourseInClass;
import common.Packet;
import common.Parent;
import common.Request;
import common.SchoolClass;
import common.Semester;
import common.Student;
import common.StudentInCourse;
import common.Submission;
import common.TasksInCourseInClass;
import common.Teacher;
import common.User;
import enums.DatabaseTables;
import enums.PacketId;
import enums.PacketSub;
import enums.UserType;
import ocsf.server.*;



public class ServerApp extends AbstractServer  implements WindowListener
{
	//Class variables *************************************************
	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	final public static String INI_PATH = ".\\serverConfig.ini";

	public static String DB_HOST = "jdbc:mysql://localhost/schooldb";
	public static String DB_USER = "root";
	public static String DB_PASSWORD = "123456";

	/*
	public static String DB_HOST = "sql11.freemysqlhosting.net:3306/sql11175785";
	public static String DB_USER = "sql11175785";
	public static String DB_PASSWORD = "QHUUxthJB2";
	*/
	private HSServerFrameGUI frame;
	private DBController dbController;
	private Ini configIni;



	//Constructors ****************************************************

	/**
	 * 
	 * @param port server port number required to connect with
	 * @param db_host database host
	 * @param db_user database username
	 * @param db_pass database password
	 */

	public ServerApp(int port, String db_host, String db_user, String db_pass) {
		super(port);
		DB_HOST = db_host;
		DB_USER = db_user;
		DB_PASSWORD = db_pass;
		configIni = loadIniFile(INI_PATH);  
	}

	/**
	 * method contains in it the tools to connect to database
	 * @param host database host
	 * @param user database username
	 * @param pass database password
	 */
	public void connectWithDatabase(String host,String user,String pass){
		DB_HOST = host;
		DB_USER = user;
		DB_PASSWORD = pass;
		try 
		{
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (Exception ex) 
		{/* handle the error*/}

		try 
		{
			ArrayList<String> link = new ArrayList<>();
			link.add("jdbc:mysql://" + DB_HOST + "?zeroDateTimeBehavior=convertToNull&serverTimezone=UTC");
			link.add(DB_USER);
			link.add(DB_PASSWORD);
			Connection conn = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
			System.out.println("SQL connection succeed");
			dbController = new DBController(conn,link);
			if (frame != null){
				frame.updateDBConnectionUi(true);
			}
		} catch (SQLException ex) 
		{/* handle any errors*/
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			if (frame != null){
				frame.updateDBConnectionUi(false);
			}
		}    
	}




	//Instance methods ************************************************


	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted()
	{
		System.out.println
		("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	protected void serverStopped()
	{
		System.out.println
		("Server has stopped listening for connections.");
	}


	/**
	 * method which loads the gui class for the server
	 * @param isListeningEstablished boolean indicating if the server listening is opened or not
	 * 
	 */

	void loadUI(boolean isListeningEstablished){

		frame = new HSServerFrameGUI(this,isListeningEstablished);
		frame.setSize(600,260);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		frame.addWindowListener(this);

		frame.setVisible(true);  
	}



	/**
	 * This method is responsible for the creation of 
	 * the server instance.
	 *
	 * @param args[0] isn't needed.
	 */
	public static void main(String[] args) 
	{
		Ini configIni = loadIniFile(INI_PATH);
		int port = new Integer(configIni.get("server", "port"));

		String db_host = configIni.get("db-server", "host");
		String db_user = configIni.get("db-server", "user");
		String db_pass = configIni.get("db-server", "pass");

		ServerApp sv = new ServerApp(port,db_host,db_user,db_pass);

		try 
		{
			sv.listen(); //Start listening for connections
			sv.loadUI(true);
		} 
		catch (Exception ex) 
		{
			System.out.println("ERROR - Could not listen for clients!");
			sv.loadUI(false);
		}
		sv.connectWithDatabase(DB_HOST,DB_USER,DB_PASSWORD);
	}


	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * method is from WindowListener interface in order to perform some actions like closing the server listening port 
	 * before closing the gui window to free the port for next use
	 */
	@Override
	public void windowClosing(WindowEvent e) {
		try {

			this.stopListening();
			this.close();
			System.exit(0);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}


	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}


	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * method to change the server port required to listen to
	 * @param port port number required
	 */
	public void setServerPort(int port) {
		setPort(port);
	}

	/**
	 * method used to update some keys in the ini file using ini4j
	 * @param section ini section
	 * @param key ini key
	 * @param value ini key value
	 */
	public void updateValueInIni(String section,String key,String value){
		configIni.put(section, key, value);	
		saveIniFile(configIni);
	}
	/**
	 * method to load ini file
	 * @param path ini file path
	 * @return Ini class with loaded keys
	 */
	static Ini loadIniFile(String path)
	{
		Ini ini = new Ini();
		try {
			ini.load(new FileReader(new File(path)));
		} catch (InvalidFileFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			ini.put("server", "port", DEFAULT_PORT);
			ini.put("db-server", "host", DB_HOST);
			ini.put("db-server", "user", DB_USER);
			ini.put("db-server", "pass", DB_PASSWORD);
			try {
				ini.store(new File(path));
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ini;
	}
	/**
	 * method saves ini file
	 * @param ini indicates ini class that contains the keys for out program
	 */
	void saveIniFile(Ini ini){
		try {
			ini.store(new File(INI_PATH));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client)
	{
		//this.getClientConnections()
		
		if (msg instanceof Packet){
			frame.setLastMessageLog("Server Recieved Packet: " + msg.toString());
			Packet pck = (Packet)msg;

			switch (pck.getPacketId() ){
			case REQUIRE_ARRAY_LIST:{
				switch (pck.getPacketSub()){
				case GENERIC_GET_USERS_TYPE:{
					
					System.out.println("GENERIC_GET_USERS_TYPE in server line 1137");
					ArrayList<String> arr = new ArrayList<String>(
							Arrays.asList( "system_manager","school_manager","secretary","teacher","student","parent") );
					pck.setNextStage();
					pck.setData(arr);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case TEACHER_CONTROLLER_GET_ENTITY:{	
					if (pck.getData() instanceof ArrayList){
						ArrayList<String> arr = (ArrayList<String>)pck.getData();
						int count = Integer.parseInt(arr.get(2));
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(arr.get(0), arr.get(1));

						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case DEFINE_COURSE_GET_ACADEMIC_UNITS:{
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.ACADEMIC_UNIT.getTable(), where);

						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}	

				case DEFINE_COURSE_GET_COURSES:{
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.COURSE.getTable(), where);

						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_SCHOOL_CLASSES:{// get all school classes
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.CLASS.getTable(), where);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_SCHOOL_TEACHERS:{// get all school teachers
					if (pck.getStage() == 0){
						String query=
								"SELECT U.id,U.first_name,U.last_name"
										+" FROM user as U"
										+" WHERE U.type='teacher'";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_ALL_SEMESTERS:{// get all semesters ids till now
					if (pck.getStage() == 0){
						String query=
								"SELECT semester.id"
										+" FROM semester";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_SPECIFIC_TEACHER_STATISTICS:{
					if (pck.getStage() == 0){
						ArrayList<String> dataArray = (ArrayList<String>)pck.getData();
						String teacherId = dataArray.get(0);
						String criterion = dataArray.get(1);
						String chosenSemesters = dataArray.get(2);
						String query=
								" SELECT c.name," + criterion 
								+" FROM student_in_course_in_class as scc,course_in_class as cc,class as c"
								+" WHERE scc.class_number=cc.class_number AND cc.class_number=c.number AND scc.course_number=cc.course_number AND scc.unit_number=cc.unit_number"
								+" AND cc.teacher_id='"+teacherId+"'" + " AND " + chosenSemesters
								+" GROUP BY scc.class_number";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_CLASS_STATISTICS_WITH_DIFFERENT_TEACHERS:{
					if (pck.getStage() == 0){
						ArrayList<String> dataArray = (ArrayList<String>)pck.getData();
						String classId = dataArray.get(0);
						String criterion = dataArray.get(1);
						String chosenSemesters = dataArray.get(2);
						String query=
								" SELECT u.first_name,u.last_name," + criterion 
								+" FROM student_in_course_in_class as scc,course_in_class as cc,user as u"
								+" WHERE scc.class_number=cc.class_number AND scc.course_number=cc.course_number AND scc.unit_number=cc.unit_number AND u.id=cc.teacher_id"
								+" AND cc.class_number='"+classId+"'" + " AND " + chosenSemesters
								+" GROUP BY u.id";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_CLASS_STATISTICS_IN_DIFFERENT_COURSES:{
					if (pck.getStage() == 0){
						ArrayList<String> dataArray = (ArrayList<String>)pck.getData();
						String classId = dataArray.get(0);
						String criterion = dataArray.get(1);
						String chosenSemesters = dataArray.get(2);
						String query=
								" SELECT c.name," + criterion 
								+" FROM student_in_course_in_class as scc,course_in_class as cc,course as c"
								+" WHERE scc.class_number=cc.class_number AND scc.course_number=cc.course_number AND scc.unit_number=cc.unit_number AND c.number=cc.course_number AND c.unit_number=cc.unit_number"
								+" AND cc.class_number='"+classId+"'" + " AND " + chosenSemesters
								+" GROUP BY c.number,c.unit_number";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_STUDENTS_IN_COURSE_IN_CLASS:{
					if (pck.getStage() == 0){
						ArrayList<String> arr = (ArrayList<String>) pck.getData();
						String X=arr.get(0); // X = class number
						String Y=arr.get(1); // Y = course academic unit id
						String Z=arr.get(2); // Z = course number
						String where = "SELECT u.id,u.first_name,u.last_name "
								+"FROM user as u,student_in_course_in_class as scc "
								+ "WHERE u.id=scc.student_id AND scc.class_number='"+X+"' AND scc.unit_number='"+Y+"' AND scc.course_number='"+Z+"'";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(where);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_COURSES_IN_SPECIFIC_CLASS_IN_CURRENT_SEMESTER:{ // get all the courses that are teached in a given class in the current semester
					if (pck.getStage() == 0){
						ArrayList<String> pckList  = (ArrayList<String>)pck.getData();
						String query = "SELECT c.unit_number,c.number,c.name,c.number_of_hours,cc.teacher_id"
								+ " FROM course_in_class as cc,course as c"
								+ " WHERE cc.class_number= "+"'"+pckList.get(1)+"'  "
								+ "AND cc.semester_id = "+pckList.get(0)+" "
								+ "AND cc.unit_number=c.unit_number AND cc.course_number=c.number";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case GET_SCHOOL_TEACHERS_IN_SPECIFIC_UNIT:{	// get all the teachers in a given academic unit
					if (pck.getStage() == 0){
						String unitNumber = (String) pck.getData();
						String query=
								"SELECT U.id,U.first_name,U.last_name,T.working_hours,T.max_working_hours "
										+" FROM user as U,teacher as T,teacher_academic_unit as TAC "
										+" WHERE U.type='teacher' AND U.id=TAC.teacher_id AND U.id=T.id AND TAC.unit_number='"+ unitNumber+"'";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case   ASSIGN_STUDENTS_IN_CLASS_TO_COURSE_ONLY_THOSE_WHO_HAVE_ALL_PRECOURSES_AND_GET_THEM:{	
					if (pck.getStage() == 0){
						ArrayList<String> arr = (ArrayList<String>) pck.getData();
						String X=arr.get(0); // X = class number
						String Y=arr.get(1); // Y = course academic unit id
						String Z=arr.get(2); // Z = course number
						String firstQuery= " SELECT student.id "
								+ " FROM student "
								+ " WHERE student.class_number= '" + X +"'" ;
						ArrayList<ArrayList<Object>> allStudentsInClass = dbController.executeRawQuery(firstQuery);
						String secondQuery=
								"		SELECT student.id "
										+"		FROM student"
										+"		WHERE student.class_number='"+X+"' AND NOT EXISTS ( "
										+"		(SELECT pre_course_unit_id,pre_course_number"
										+"		FROM course_pre"
										+"		WHERE unit_id='"+Y+"'"+" AND number='"+Z+"'"+" AND pre_course_unit_id+pre_course_number NOT IN"
										+"		(SELECT unit_number+course_number as res"
										+"		FROM student_in_course_in_class	"
										+"		WHERE student.id=student_in_course_in_class.student_id AND student_in_course_in_class.grade>54)))";			   
						ArrayList<ArrayList<Object>> studentsInClassThatHasAllPreCourses = dbController.executeRawQuery(secondQuery);
						for (int i=0; i<studentsInClassThatHasAllPreCourses.size();i++)
						{
							String studentId = (String)studentsInClassThatHasAllPreCourses.get(i).get(0);
							String data = "'"+studentId+"' , '"+Z+ "','"+Y+ "','"+X+ "' ,null,' ' ";
							if(dbController.insertItem(DatabaseTables.STUDENT_COURSES.getTable(),data)==false)
								System.out.println("\nERROR,CANNOT INSERT STUDENT TO COURSE IN CLASS.");
						}
						ArrayList<ArrayList<ArrayList<Object>>> resultArray = new ArrayList<ArrayList<ArrayList<Object>>>();
						resultArray.add(allStudentsInClass);
						resultArray.add(studentsInClassThatHasAllPreCourses);
						pck.setNextStage();
						pck.setData(resultArray);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					break;
				}
				

				case GENERIC_GET_USERS:{
					if (pck.getStage() == 0){
						String where = "SELECT id ,type ,first_name ,last_name FROM user";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(where);
						ArrayList<User> usersList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){
						usersList.add(new User( (String)arrResult.get(i).get(0),(String)arrResult.get(i).get(2),(String)arrResult.get(i).get(3),(String)arrResult.get(i).get(1)  ) );
						}
						pck.setNextStage();
						pck.setData(usersList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
					/*
					System.out.println("GENERIC_GET_USERS in server line 1302");
					if (pck.getStage() == 0){
						String where = "SELECT id ,type ,first_name ,last_name FROM user";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(where);
						
						pck.setNextStage();
						pck.setData(arrResult);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break; */
				}
				
				}
			}
			case REQUIRE_BOOLEAN: {
				switch (pck.getPacketSub()){
				case TEACHER_CONTROLLER_UPDATE_ENTITY: {// OLD
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					Boolean arrResult = dbController.updateQuery("Teacher",arr.get(0), arr.get(1),arr.get(2));
					pck.setNextStage();
					pck.setData(arrResult);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case ADD_NEW_STUDENT:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					ArrayList<Object> studentRow= dbController.updateQueryAndReturnAChangedRow("student", "class_number= '"+arr.get(1)+"'","id='"+arr.get(0)+"'" );
					pck.setData(studentRow);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case CREATE_NEW_USER:{
					Boolean isAdded = false;
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					
					String where1 ="'"+arr.get(0)+"','"+arr.get(1)+"','"+arr.get(2)+"','"+arr.get(3)
					+"','"+arr.get(4)+"'";
					isAdded = dbController.insertItem(DatabaseTables.USER.getTable() ,where1 );
					
					if(!isAdded) {System.out.println("failed to create user"); }
					else {System.out.println("create user"); }
					
					String type = arr.get(2);
					if(type == "system_manager" || type == "school_manager" || type == "secretary") 
					{
						pck.setNextStage();
						pck.setData(isAdded);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
					isAdded = false;
					switch (arr.get(2) ){
						case "parent": {
							System.out.println("create parent");
							
							String where ="'"+arr.get(0)+"',NOW(),'"+arr.get(3)+"','"+arr.get(4)+"'";
							isAdded = dbController.insertItem(DatabaseTables.PARENT.getTable() ,where );
							if(!isAdded) {System.out.println("failed to create user");}
							pck.setNextStage();
							pck.setData(isAdded);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
						case "student": {
							System.out.println("create student");
							
							String where ="'"+arr.get(0)+"','','"+arr.get(6)+"','"+arr.get(7)+"'";
							isAdded = dbController.insertItem(DatabaseTables.STUDENT.getTable() ,where );
							if(!isAdded) {System.out.println("failed to create user");}
							pck.setNextStage();
							pck.setData(isAdded);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
						case "teacher": {
							System.out.println("create teacher");
							
							String where ="'"+arr.get(0)+"','100','0','10','0'";
							isAdded = dbController.insertItem(DatabaseTables.TEACHER.getTable() ,where );
							if(!isAdded) {System.out.println("failed to create user");}
							pck.setNextStage();
							pck.setData(isAdded);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								e.printStackTrace();
							}
							break;
						}
					}
					break;
				}
				case DEFINE_NEW_CLASS:{

					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					String data="'"+arr.get(0)+"'"+","+"'"+arr.get(1)+"'";
					boolean isAdded=	dbController.insertItem(DatabaseTables.CLASS.getTable(),data );
					pck.setNextStage();
					pck.setData(isAdded);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}

					break;
				}
				case DEFINE_TASK_SUBMIT:{
					ArrayList<Object> arr = (ArrayList<Object>)pck.getData();

					String where = DatabaseTables.TASK.getTable()+".course_number = '" + arr.get(3) + "' and " + DatabaseTables.TASK.getTable() + ".unit_number = '" + arr.get(4) + "' and " + DatabaseTables.TASK.getTable() + ".class_number = '" + arr.get(5) + "' and " + DatabaseTables.TASK.getTable() + ".id = '" + arr.get(0) + "'";
					Boolean isRemoved = dbController.deleteItem(DatabaseTables.TASK.getTable(), where);
					Boolean isAdded = dbController.insertItem("INSERT INTO " + DatabaseTables.TASK.getTable()+" VALUES(?,CURDATE(),?,?,?,?,?,?)", arr);
					pck.setData(null);
					if (isAdded){
						pck.setData(isAdded);
					}
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case  UNBLOCK_PARENT_ACCESS:{
					ArrayList<ArrayList<Object>> arr;
					Date d,a;
					a=new Date(0);
					boolean isUpdated=false;
					arr=dbController.executeQuery(DatabaseTables.PARENT.getTable(),"id='"+(String)pck.getData()+"'" );
					d=(Date)arr.get(0).get(1);
					if(d.after(Calendar.getInstance().getTime())){
						isUpdated=dbController.updateQueryById(DatabaseTables.PARENT.getTable(), "id", (String)pck.getData()," block_date='"+a.toString()+"'");		
					}
					
					pck.setData(isUpdated);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case  BLOCK_PARENT_ACCESS:{
					boolean isChange;
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					isChange=dbController.updateQueryById(DatabaseTables.PARENT.getTable(), "id", arr.get(0)," block_date='"+arr.get(1)+"'");
					pck.setData(isChange);
					pck.setNextStage();
					try {

						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case UPDATE_STUDENT_CLASS:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					ArrayList<ArrayList<Object>> arrayList = dbController.executeRawQuery("Select * FROM student WHERE id = '"+ arr.get(0)+"'");
					Boolean arrResult ;
					System.out.println((String)arrayList.get(0).get(3)+"!!!!!!!!\n");
					if (arrayList.size()==0)
						arrResult=false;
					else if ((String)arrayList.get(0).get(3)==null)
						arrResult = dbController.updateQuery("student", arr.get(0),arr.get(1), "class_number");
					else arrResult=false;
					pck.setNextStage();
					pck.setData(arrResult);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;	
				}

				case CREATE_NEW_REQUEST:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					String where ="null,'"+arr.get(0)+"','"+arr.get(1)+"','"+arr.get(2)+"','"+arr.get(3)
					+"','"+arr.get(4)+"','"+arr.get(5)+"','"+"pending'";
					Boolean isAdded = dbController.insertItem(DatabaseTables.REQUEST.getTable(),where );
					pck.setData(isAdded);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case OPEN_NEW_SEMESTER:{
					//in case you need to reset increment use: ALTER TABLE semester AUTO_INCREMENT = 1
					ArrayList<String> arr = (ArrayList<String>)pck.getData();
					String season = (String)arr.get(0);
					String curId = (String)arr.get(1);
					String data = "null,CURDATE(),null,'" + season + "'";
					Boolean isAdded = dbController.insertItem(DatabaseTables.SEMESTER.getTable(), data);
					if(isAdded){
						dbController.updateQuery(DatabaseTables.SEMESTER.getTable(), "closing_date = CURDATE()", "id = '" + curId + "'");
						dbController.updateQuery(DatabaseTables.TEACHER.getTable(), "working_hours = 0", "working_hours > 0");
					}
					pck.setNextStage();
					pck.setData(isAdded);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case EVALUATE_STUDENT_IN_COURSE:{

					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String where = "student_id = '"+arr.get(2)+"' and course_number = '"+
							arr.get(3)+"' and unit_number = '"+arr.get(4)+"' and class_number = '"
							+arr.get(5)+"'";

					String set = "grade = '"+arr.get(0)+"' ,notes = '"+arr.get(1)+"'";
					Boolean isUpdated = dbController.updateQuery(DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()  , set, where);
					pck.setData(isUpdated);

					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case EVALUATE_STUDENT_IN_TASK:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String where ="course_number = '"+arr.get(3)+ "' and student_id = '"+arr.get(2)+"' and unit_number = '"+arr.get(4)+"' and class_number = '"+arr.get(5)+"' and task_id = '"+arr.get(6)+"'";

					String set = "grade = '"+arr.get(0)+"' ,notes = '"+arr.get(1)+"'";
					Boolean isUpdated = dbController.updateQuery(DatabaseTables.SUBMISSION.getTable()  , set, where);
					pck.setData(isUpdated);

					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case DEFINE_COURSE_SUBMIT:{

					String data;
					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					ArrayList<String> preds = new ArrayList<String>();
					if (arr.get(4).length()>2){
						try{
							preds = new ArrayList(Arrays.asList(arr.get(4).substring(1, arr.get(4).length() - 1).replaceAll("\\s", "").split(",")));
						}catch(Exception e){
							e.printStackTrace();
						}
					}

					data = "'" + arr.get(0)+ "', '" + arr.get(2) + "', '" + arr.get(1) + "', '" + arr.get(3) + "'";
					Boolean isAdded = dbController.insertItem(DatabaseTables.COURSE.getTable(),data);
					if (isAdded){
						for (int i=0;i<preds.size();i++){
							String predCourseUnitId = preds.get(i).substring(0,2);
							String predCourseNumber = preds.get(i).substring(2);
							isAdded = dbController.insertItem(DatabaseTables.COURSE_PREDESESSORS.getTable(),"'" + arr.get(0) + "', '" + arr.get(2) + "', '" + predCourseNumber + "', '" + predCourseUnitId+"'");
						}
					}
					pck.setData(isAdded);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case ASSIGN_TEACHER_TO_COURSE_IN_CLASS:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();	
					String where = " course_number='"+arr.get(0)+"' AND unit_number='"+arr.get(1)+"' AND class_number='"+arr.get(2)+"' ";
					String newTeacherId = arr.get(3);
					String newWorkingHoursForNewTeacher = arr.get(4);
					String oldTeacherId = arr.get(5);
					String newWorkingHoursForOldTeacher = arr.get(6);
					Boolean isUpdated = dbController.updateQuery(" course_in_class"," teacher_id='"+newTeacherId+"'",where);
					if (isUpdated)
					{
						dbController.updateQuery("teacher",oldTeacherId,newWorkingHoursForOldTeacher,"working_hours");
						dbController.updateQuery("teacher",newTeacherId,newWorkingHoursForNewTeacher,"working_hours");
					}
					pck.setNextStage();
					pck.setData(isUpdated);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case ASSIGN_COURSE_TO_CLASS:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();	
					String data="'"+arr.get(0)+"', '"+arr.get(1)+"', '"+arr.get(2)+"', '"+arr.get(3)+"', '"+arr.get(4)+"'";
					Boolean isAdded = dbController.insertItem(DatabaseTables.COURSE_IN_CLASS.getTable(), data);
					String teacherId = arr.get(3);
					String newWorkingHours = arr.get(5);
					if (isAdded)
						dbController.updateQuery("teacher",teacherId,newWorkingHours,"working_hours");
					pck.setNextStage();
					pck.setData(isAdded);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case   ASSIGN_STUDENT_TO_COURSE_IN_CLASS:{	
					if (pck.getStage() == 0){
						ArrayList<String> arr = (ArrayList<String>) pck.getData();
						String S=arr.get(0); // S = student id
						String X=arr.get(1); // X = class number
						String Y=arr.get(2); // Y = course academic unit id
						String Z=arr.get(3); // Z = course number
						String data = "'"+S+"' , '"+Z+ "','"+Y+ "','"+X+ "' ,null,' ' ";
						Boolean isAssigned = dbController.insertItem(DatabaseTables.STUDENT_COURSES.getTable(),data);
						pck.setNextStage();
						pck.setData(isAssigned);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case   REMOVE_STUDENT_FROM_COURSE_IN_CLASS:{	
					if (pck.getStage() == 0){
						ArrayList<String> arr = (ArrayList<String>) pck.getData();
						String S=arr.get(0); // S = student id
						String X=arr.get(1); // X = class number
						String Y=arr.get(2); // Y = course academic unit id
						String Z=arr.get(3); // Z = course number
						String data = "student_id='"+S+"' AND course_number='"+Z+"' AND unit_number='"+Y+ "' AND class_number='"+X+ "'";
						Boolean isRemoved = dbController.deleteItem(DatabaseTables.STUDENT_COURSES.getTable(),data);
						pck.setNextStage();
						pck.setData(isRemoved);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case   DELETE_USER:{	
					Boolean isRemoved = false ;
					User user = (User) pck.getData();
					String where1 = "userId = '" + user.getId() + "'";
					String where2 = "id = '" + user.getId() + "'";
					
					if( Integer.parseInt(user.getId()) == 0 )
					{
						pck.setNextStage();
						pck.setData(isRemoved);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						break;
					}
					else {	
						switch (user.getType().toString() ){
						case "teacher": {
							System.out.println("delete teacher");
							
							String where3 = "teacher_id = '" + user.getId() + "'";
							
							Boolean isRemoved1 = dbController.deleteItem(DatabaseTables.REQUEST.getTable() ,where1);
							Boolean isRemoved2 = dbController.deleteItem(DatabaseTables.USER.getTable(),where2 );
							Boolean isRemoved3 = dbController.deleteItem(DatabaseTables.COURSE_IN_CLASS.getTable() ,where3);
							Boolean isRemoved4 = dbController.deleteItem(DatabaseTables.TEACHER_ACADEMIC_UNIT.getTable(),where3);
							Boolean isRemoved5 = dbController.deleteItem(DatabaseTables.TEACHER.getTable(),where3);
							
							int count = 0;
							if(isRemoved1)
								count++;
							if(isRemoved2)
								count++;
							if(isRemoved3)
								count++;
							if(isRemoved4)
								count++;
							if(isRemoved5)
								count++;
							if(count == 5) isRemoved = true;
							
							pck.setNextStage();
							pck.setData(isRemoved);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							break;
						}			
						case "parent": {
							System.out.println("delete parent");
							
							Boolean isRemoved1 = dbController.deleteItem(DatabaseTables.REQUEST.getTable() ,where1);
							Boolean isRemoved2 = dbController.deleteItem(DatabaseTables.USER.getTable(),where2 );
							Boolean isRemoved3 = dbController.deleteItem(DatabaseTables.PARENT.getTable(), where2);
							
							int count = 0;
							if(isRemoved1)
								count++;
							if(isRemoved2)
								count++;
							if(isRemoved3)
								count++;
							if(count == 3) isRemoved = true;
							
							pck.setNextStage();
							pck.setData(isRemoved);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}
						case "student": {
							System.out.println("delete student");

							String where3 = "studentsInClass = '" + user.getId() + "'";
							String where4 = "student_id = '" + user.getId() + "'";
							
							Boolean isRemoved1 = dbController.deleteItem(DatabaseTables.REQUEST.getTable() ,where1);
							Boolean isRemoved2 = dbController.deleteItem(DatabaseTables.USER.getTable(),where2 );
							Boolean isRemoved3 = dbController.deleteItem(DatabaseTables.STUDENT.getTable(),where2 );
							Boolean isRemoved4 = dbController.deleteItem(DatabaseTables.CLASS.getTable(), where3);
							Boolean isRemoved5 = dbController.deleteItem(DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable(),where4);
							Boolean isRemoved6 = dbController.deleteItem(DatabaseTables.SUBMISSION.getTable(),where4);
							
							int count = 0;
							if(isRemoved1)
								count++;
							if(isRemoved2)
								count++;
							if(isRemoved3)
								count++;
							if(isRemoved4)
								count++;
							if(isRemoved5)
								count++;
							if(isRemoved6)
								count++;
							
							if(count == 6) isRemoved = true;
							
							pck.setNextStage();
							pck.setData(isRemoved);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}

						default:{
							System.out.println("defaulte delete case");
							
							Boolean isRemoved1 = dbController.deleteItem(DatabaseTables.REQUEST.getTable() ,where1);
							Boolean isRemoved2 = dbController.deleteItem(DatabaseTables.USER.getTable(),where2 );
							
							int count = 0;
							if(isRemoved1)
								count++;
							if(isRemoved2)
								count++;
							if(count == 2) isRemoved = true;
							
							pck.setNextStage();
							pck.setData(isRemoved);
							try {
								client.sendToClient(pck);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							break;
						}						
							
					 }//end of switch	
					}//end of else
				}//end of case	DELETE_USER
				
			}//end of switch	
		}//end of REQUIRE_BOOLEAN
			
			case REQUIRE_USER_ENTITY: {
				switch (pck.getPacketSub()){
				case 	GET_CLASSES_NAMES:{
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.CLASS.getTable(), where);
						ArrayList <SchoolClass> classList=new ArrayList<>();
						for(int j=0 ; j<arrResult.size();j++)
						{
							classList.add(new SchoolClass( arrResult.get(j)));
						}

						pck.setNextStage();
						pck.setData(classList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					break;

				}
				case LOGIN_CONTROLLER_GET_DATA: {
					String where = (String)pck.getData();
					ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.USER.getTable(), where);
					pck.setData(null);
					if (arrResult.size() == 1){
						// obtain more details
						ArrayList<Object> userArr = arrResult.get(0);
						switch ((String)userArr.get(2)){
						case "teacher": {
							String where2 = "teacher_id = '" + (String)userArr.get(0) + "'";
							String where3 = "id = '" + (String)userArr.get(0) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.TEACHER_ACADEMIC_UNIT.getTable(), where2);
							ArrayList<ArrayList<Object>> arrResult3 = dbController.executeQuery(DatabaseTables.TEACHER.getTable(), where3);


							ArrayList<AcademicUnit> academicUnitList = new ArrayList<>();				    			

							for (int j = 0 ; j < arrResult2.size() ; j++){
								String where4 = "number = '" + (String)arrResult2.get(j).get(1) + "'";
								ArrayList<ArrayList<Object>> arrResult4 = dbController.executeQuery(DatabaseTables.ACADEMIC_UNIT.getTable(), where4);

								academicUnitList.add(new AcademicUnit((String)arrResult4.get(0).get(0),(String)arrResult4.get(0).get(1)));
							}

							if (arrResult3.size() == 1){
								Teacher usr = new Teacher((String)userArr.get(0),(String)userArr.get(3),(String)userArr.get(4),(String)userArr.get(2),academicUnitList);
								Object i = arrResult3.get(0).get(1);
								usr.setMaxAllowedHours((Integer)arrResult3.get(0).get(1));
								usr.setCurrentHours((Integer)arrResult3.get(0).get(2));
								checkExistanceForDismissal(usr.getId());
								client.setInfo(usr.getId(), Calendar.getInstance().getTimeInMillis());
								pck.setData(usr);
							}
							break;
						}			
						case "parent": {
							String where2 = "id = '" + (String)userArr.get(0) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.PARENT.getTable(), where2);
							if (arrResult2.size() == 1){
								Date t;
								if (arrResult2.get(0).get(1) == null){
									t = null;
								}else{
									t = (Date)arrResult2.get(0).get(1); //Timestamp.valueOf(
								}
								Parent usr = new Parent((String)userArr.get(0),(String)userArr.get(3),(String)userArr.get(4),(String)userArr.get(2),t);
								checkExistanceForDismissal(usr.getId());
								client.setInfo(usr.getId(), Calendar.getInstance().getTimeInMillis());
								pck.setData(usr);
							}
							break;
						}
						case "student": {
							String where2 = "id = '" + (String)userArr.get(0) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.STUDENT.getTable(), where2);
							if (arrResult2.size() == 1){
								Student usr = new Student((String)userArr.get(0),(String)userArr.get(3),(String)userArr.get(4),(String)userArr.get(2),(String)arrResult2.get(0).get(1),(String)arrResult2.get(0).get(2),(String)arrResult2.get(0).get(3));
								checkExistanceForDismissal(usr.getId());
								client.setInfo(usr.getId(), Calendar.getInstance().getTimeInMillis());
								pck.setData(usr);
							}
							break;
						}

						default:{
							User usr = User.InstantiateByArray(userArr);
							checkExistanceForDismissal(usr.getId());
							client.setInfo(usr.getId(), Calendar.getInstance().getTimeInMillis());
							pck.setData(usr);	
						}								
						}	
					}	
					pck.setNextStage();
					try {
						System.out.println("server 1016...");
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}

				case GENERIC_GET_REQUESTS:{

					if (pck.getStage() == 0){
						UserType userType = (UserType) pck.getData();
						String where = "";

						if(userType == UserType.school_manager){
							where = "status = 'pending'";
						}

						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.REQUEST.getTable(), where);
						ArrayList<Request> requestList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){
							requestList.add( new Request( (int)arrResult.get(i).get(0),(String)arrResult.get(i).get(1)
									,(String)arrResult.get(i).get(2),(String)arrResult.get(i).get(3),(String)arrResult.get(i).get(4),
									(String)arrResult.get(i).get(5),(String)arrResult.get(i).get(6),(String)arrResult.get(i).get(7) ));
						} 

						pck.setNextStage();
						pck.setData(requestList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				case UPDATE_REQUESTS:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String where ="id = '"+arr.get(0)+"'";
					String set = "status = '"+arr.get(1)+"'";

					Boolean isUpdated = dbController.updateQuery(DatabaseTables.REQUEST.getTable()  , set, where);

					pck.setNextStage();
					pck.setData(isUpdated);

					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
				case GENERIC_GET_COURSE_IN_CLASS:{
					String lastWherePart;
					if (pck.getData() instanceof Integer){
						Integer semesterId = (Integer)pck.getData();
						lastWherePart = " and " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".semester_id = " + semesterId.toString();
					}else{
						ArrayList<Object> arr = (ArrayList<Object>)pck.getData();
						Integer semesterId = (Integer)arr.get(0);
						String teacherId = (String)arr.get(1);
						lastWherePart = " and " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".semester_id = " + semesterId.toString()+" and " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".teacher_id = '" + teacherId + "'";	
					}
					//DatabaseTables.COURSE_IN_CLASS.getTable()
					String query = "Select " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".*, "+DatabaseTables.COURSE.getTable()+".name, "+DatabaseTables.CLASS.getTable()+".name"+
							" From "+DatabaseTables.COURSE_IN_CLASS.getTable()+", "+DatabaseTables.COURSE.getTable()+", "+DatabaseTables.CLASS.getTable()+
							" Where " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".course_number = "+DatabaseTables.COURSE.getTable()+".number and " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".unit_number = " + DatabaseTables.COURSE.getTable() + ".unit_number and " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".class_number = " + DatabaseTables.CLASS.getTable() + ".number" +
							lastWherePart;
					ArrayList<ArrayList<Object>> arrResult = dbController.executeRawQuery(query);

					ArrayList<CourseInClass> courseInClassList = new ArrayList<>();
					for (int j = 0 ; j < arrResult.size() ; j++){
						CourseInClass courseInClass = CourseInClass.getFullInstance(arrResult.get(j));
						courseInClassList.add(courseInClass);
					}
					pck.setNextStage();
					pck.setData(courseInClassList);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case DEFINE_COURSE_GET_ACADEMIC_UNITS:{
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.ACADEMIC_UNIT.getTable(), where);
						ArrayList<AcademicUnit> academicUnitList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){
							academicUnitList.add(new AcademicUnit(arrResult.get(i)));
						}
						pck.setNextStage();
						pck.setData(academicUnitList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}	
				
				case GENERIC_GET_SEMESTERS:{
					String where = "";
					ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.SEMESTER.getTable(), where);
					ArrayList<Semester> semesterList = new ArrayList<>();
					for (int i = 0 ; i < arrResult.size() ; i++){
						semesterList.add(new Semester(arrResult.get(i)));	
					}
					if (semesterList.size() > 0){
						semesterList.get(semesterList.size()-1).setCurrentDate(Calendar.getInstance().getTime().getTime());
					}
					pck.setNextStage();
					pck.setData(semesterList);
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case GENERIC_GET_PARENTS:{
					if (pck.getStage() == 0){
						String where = "type = 'parent'";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.USER.getTable(), where);
						ArrayList<Parent> parentList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){
							String where2 = "id = '" + (String)arrResult.get(i).get(0) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.PARENT.getTable(), where2);
							if (arrResult2.size() == 1){
								parentList.add(new Parent((String)arrResult.get(i).get(0),(String)arrResult.get(i).get(3),
										(String)arrResult.get(i).get(4),(String)arrResult.get(i).get(2),(Date)arrResult2.get(0).get(1)));
							}	
						}
						pck.setNextStage();
						pck.setData(parentList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}	

/*							//public User(String id, String pname, String fname, String type){
 * 					
 */
				case GENERIC_GET_STUDENTS:{
					if (pck.getStage() == 0){
						String where = "type = 'student'";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.USER.getTable(), where);
						ArrayList<Student> studentList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){
							String where2 = "id = '" + (String)arrResult.get(i).get(0) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.STUDENT.getTable(), where2);
							if (arrResult2.size() == 1){
								studentList.add(new Student((String)arrResult.get(i).get(0),(String)arrResult.get(i).get(3),(String)arrResult.get(i).get(4),
							(String)arrResult.get(i).get(2),(String)arrResult2.get(0).get(1),(String)arrResult2.get(0).get(2),(String)arrResult2.get(0).get(3)));
							}
						}
						pck.setNextStage();
						pck.setData(studentList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}			

				case GENERIC_GET_TEACHERS:{
					if (pck.getStage() == 0){
						String where = "type = 'teacher'";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.USER.getTable(), where);
						ArrayList<Teacher> teacherList = new ArrayList<Teacher>();
						for (int i = 0 ; i < arrResult.size() ; i++){
							String where2 = "id = '" + (String)arrResult.get(i).get(0) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.TEACHER.getTable(), where2);
							String where3 = "SELECT " + DatabaseTables.TEACHER_ACADEMIC_UNIT.getTable() + ".*, " + DatabaseTables.ACADEMIC_UNIT.getTable() + ".name FROM " + DatabaseTables.ACADEMIC_UNIT.getTable()+"," +DatabaseTables.TEACHER_ACADEMIC_UNIT.getTable()+ " WHERE " + DatabaseTables.TEACHER_ACADEMIC_UNIT.getTable() + ".teacher_id = '" + (String)arrResult.get(i).get(0) + "' and "+ DatabaseTables.TEACHER_ACADEMIC_UNIT.getTable() + ".unit_number = "+ DatabaseTables.ACADEMIC_UNIT.getTable() + ".number" ;
							ArrayList<ArrayList<Object>> arrResult3 = dbController.executeRawQuery(where3);
							ArrayList<AcademicUnit> academicUnitList = new ArrayList<>();				    			

							for (int j = 0 ; j < arrResult3.size() ; j++){
								AcademicUnit tempAcad = new AcademicUnit((String)arrResult3.get(j).get(1),(String)arrResult3.get(j).get(2));
								academicUnitList.add(tempAcad);
							}
							if (arrResult2.size() !=0){
								Teacher teacher = new Teacher((String)arrResult.get(i).get(0),(String)arrResult.get(i).get(3),(String)arrResult.get(i).get(4),(String)arrResult.get(i).get(2),(Integer)arrResult2.get(0).get(1),(Integer)arrResult2.get(0).get(2));
								teacher.setAcademicUnit(academicUnitList);
								if (arrResult2.size() == 1){
									teacherList.add(teacher);
								}
							}
						}
						pck.setNextStage();
						pck.setData(teacherList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}				

				case GENERIC_GET_CLASSES:{
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.CLASS.getTable(), where);
						ArrayList<SchoolClass> classList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){

							String where2 = "class_number = '" + (String)arrResult.get(i).get(1) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.STUDENT.getTable(), where2);
							ArrayList<String> studentList = new ArrayList<>();
							for (int j = 0 ; j < arrResult2.size() ; j++){
								studentList.add((String)arrResult2.get(j).get(0));
							}				    			

							//String where3 = "class_number = '" + (String)arrResult.get(i).get(1) + "'";
							//ArrayList<ArrayList<Object>> arrResult3 = dbController.executeQuery(DatabaseTables.COURSE_IN_CLASS.getTable(), where3);
							String where3 = "SELECT CIC.*, C.name " +
									"FROM " + DatabaseTables.COURSE_IN_CLASS.getTable() + " AS CIC,"+DatabaseTables.COURSE.getTable() + " AS C " +
									"WHERE CIC.class_number = '" + (String)arrResult.get(i).get(1) + "' and CIC.course_number = C.number and CIC.unit_number = C.unit_number";
							ArrayList<ArrayList<Object>> arrResult3 = dbController.executeRawQuery(where3);


							ArrayList<CourseInClass> courseInClassList = new ArrayList<>();
							for (int j = 0 ; j < arrResult3.size() ; j++){

								arrResult3.get(j).set(0, new Course((String)arrResult3.get(j).get(0),(String)arrResult3.get(j).get(1),(String)arrResult3.get(j).get(5)));
								CourseInClass courseInClass = new CourseInClass(arrResult3.get(j));
								courseInClassList.add(courseInClass);
							}

							SchoolClass schoolClass = new SchoolClass(arrResult.get(i));
							schoolClass.setStudentsInClass(studentList);
							schoolClass.setCoursesInClass(courseInClassList);
							classList.add(schoolClass);	
						}
						pck.setNextStage();
						pck.setData(classList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}
				
				case GENERIC_GET_COURSES:{
					if (pck.getStage() == 0){
						String where = "";
						ArrayList<ArrayList<Object>> arrResult = dbController.executeQuery(DatabaseTables.COURSE.getTable(), where);
						ArrayList<Course> courseList = new ArrayList<>();
						for (int i = 0 ; i < arrResult.size() ; i++){
							String where2 = "number = '" + (String)arrResult.get(i).get(0) + "' and unit_id = '" + (String)arrResult.get(i).get(1) + "'";
							ArrayList<ArrayList<Object>> arrResult2 = dbController.executeQuery(DatabaseTables.COURSE_PREDESESSORS.getTable(), where2);
							ArrayList<String> predList = new ArrayList<>();
							for (int j = 0 ; j < arrResult2.size() ; j++){
								predList.add((String)arrResult2.get(j).get(3)+(String)arrResult2.get(j).get(2));
							}
							Course course = new Course(arrResult.get(i));
							course.setPredecessors(predList);
							courseList.add(course);	
						}
						pck.setNextStage();
						pck.setData(courseList);
						try {
							client.sendToClient(pck);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;
				}						



				case GET_LATEST_SEMESTER:{
					String where = "id = (SELECT MAX(id) FROM semester)";
					ArrayList<ArrayList<Object>> resArr = dbController.executeQuery(DatabaseTables.SEMESTER.getTable(), where);
					Semester semester;
					pck.setData(null);
					if (resArr.size() == 1){
						semester = new Semester(resArr.get(0));
						semester.setCurrentDate(Calendar.getInstance().getTime().getTime());
						pck.setData(semester);
					}
					pck.setNextStage();
					try {
						client.sendToClient(pck);
						System.out.println("sent semester current packet");
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				}
				case GET_STUDENT_IN_COURSE:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String query = "SELECT " + DatabaseTables.STUDENT_COURSES.getTable() + ".* FROM " + DatabaseTables.STUDENT_COURSES.getTable() +"," + DatabaseTables.COURSE_IN_CLASS.getTable() + " WHERE " +
							DatabaseTables.COURSE_IN_CLASS.getTable()+".course_number = " + DatabaseTables.STUDENT_COURSES.getTable()+".course_number and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".unit_number = " + DatabaseTables.STUDENT_COURSES.getTable()+".unit_number and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".class_number = " + DatabaseTables.STUDENT_COURSES.getTable()+".class_number and "+
							"student_id = '" + (String)arr.get(0)+ "' and " + DatabaseTables.COURSE_IN_CLASS.getTable() + ".semester_id = '" + (String)arr.get(1) +"'";


					ArrayList<ArrayList<Object>> resArr = dbController.executeRawQuery(query);
					ArrayList<StudentInCourse> stInCourseList = new ArrayList<>();


					for(int i=0;i<resArr.size();i++){
						String where2 = "number = '" + resArr.get(i).get(1) + "' and unit_number = '" + resArr.get(i).get(2) + "'";
						ArrayList<ArrayList<Object>> resArr2 = dbController.executeQuery(DatabaseTables.COURSE.getTable(), where2);
						if (resArr2.size() == 1){
							resArr.get(i).remove(1);
							resArr.get(i).add(1,new Course(resArr2.get(0)));
							stInCourseList.add(new StudentInCourse(resArr.get(i)));
						}
					}


					pck.setData(stInCourseList);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				}

				case TEACHER_CONTROLLER_MY_COURSES:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String where = "teacher_id = '"+ arr.get(0)+"' and semester_id = '"+arr.get(1)+"'"; 
					ArrayList<ArrayList<Object> > resArr = dbController.executeQuery(DatabaseTables.COURSE_IN_CLASS.getTable(), where );
					ArrayList<CourseInClass> courseInClassList = new ArrayList<CourseInClass>();

					for(int i =0;i<resArr.size();i++)
					{	
						where = "unit_number = '"+ resArr.get(i).get(1)+"' and number = '"+resArr.get(i).get(0)+"'";

						ArrayList<ArrayList<Object> > resArr2 = dbController.executeQuery(DatabaseTables.COURSE.getTable(), where );
						if(resArr2.size() == 1 ){

							Course tempCourse = new Course(resArr2.get(0));
							resArr.get(i).set(0,tempCourse);
							courseInClassList.add( new  CourseInClass(resArr.get(i)));
						}
					}
					pck.setData( courseInClassList );
					pck.setNextStage();

					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				}
				case GET_STUDENT_IN_TEACHER_COURSE:{

					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String query = "SELECT " +DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+ ".* FROM "+DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+","+
							DatabaseTables.COURSE_IN_CLASS.getTable()+" WHERE "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".semester_id = '"+arr.get(3)+"' and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".course_number = "+DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+".course_number" + " and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".class_number = "+DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+".class_number" + " and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".unit_number = "+DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+".unit_number" + " and "+
							DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+".course_number = '"+arr.get(0)+"' and "+
							DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+".unit_number = '"+arr.get(1)+"' and "+
							DatabaseTables.STUDENT_IN_COURSE_IN_CLASS.getTable()+".class_number = '"+arr.get(2)+"'";

					ArrayList<ArrayList<Object>> resArr = dbController.executeRawQuery(query);
					ArrayList<StudentInCourse> studentsInCourse = new ArrayList<>();

					for(int i=0;i<resArr.size();i++){
						String where2 = "number = '" + resArr.get(i).get(1) + "' and unit_number = '" + resArr.get(i).get(2) + "'";
						ArrayList<ArrayList<Object>> resArr2 = dbController.executeQuery(DatabaseTables.COURSE.getTable(), where2);
						if (resArr2.size() == 1){
							resArr.get(i).remove(1);
							resArr.get(i).add(1,new Course(resArr2.get(0)));
							studentsInCourse.add(new StudentInCourse(resArr.get(i)));
						}
					}

					pck.setData(studentsInCourse);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case GET_STUDENT_IN_COURSE_TASKS:{
					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String where = DatabaseTables.TASK.getTable()+".course_number = '" + arr.get(0) + "' and " + DatabaseTables.TASK.getTable() + ".unit_number = '" + arr.get(1) + "' and " + DatabaseTables.TASK.getTable() + ".class_number = '" + arr.get(2) + "'";

					String query = "SELECT " + DatabaseTables.TASK.getTable() + ".* FROM " + DatabaseTables.TASK.getTable() +"," + DatabaseTables.COURSE_IN_CLASS.getTable() + " WHERE " +
							DatabaseTables.COURSE_IN_CLASS.getTable()+".course_number = " + DatabaseTables.TASK.getTable()+".course_number and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".unit_number = " + DatabaseTables.TASK.getTable()+".unit_number and "+
							DatabaseTables.COURSE_IN_CLASS.getTable()+".class_number = " + DatabaseTables.TASK.getTable()+".class_number and "+
							DatabaseTables.COURSE_IN_CLASS.getTable() + ".semester_id = '" + (String)arr.get(3) +"' and "+
							where;


					
					ArrayList<ArrayList<Object>> resArr = dbController.executeRawQuery(query);
					ArrayList<TasksInCourseInClass> taskInClassInCourseList = new ArrayList<>();


					for(int i=0;i<resArr.size();i++){
						taskInClassInCourseList.add(new TasksInCourseInClass(resArr.get(i)));
					}

					pck.setData(taskInClassInCourseList);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}	

				case GET_STUDENTS_FOR_PARENT:{
					String parentId = (String)pck.getData();
					String query = "SELECT " + DatabaseTables.USER.getTable() + ".* FROM " + DatabaseTables.USER.getTable() +
							"," + DatabaseTables.STUDENT.getTable() + " WHERE " +"((" + DatabaseTables.USER.getTable()+
							".id = " + DatabaseTables.STUDENT.getTable()+".id) and (" +DatabaseTables.STUDENT.getTable()+
							".father_id = '" +parentId+"' or " + DatabaseTables.STUDENT.getTable() + ".mother_id = '" +parentId+"'))";
					ArrayList<ArrayList<Object>> resArr = dbController.executeRawQuery(query);
					ArrayList<User> studentList = new ArrayList<>();

					for(int i=0;i<resArr.size();i++){
						studentList.add(new User(resArr.get(i)));
					}
					pck.setData(studentList);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case GET_TASK_SOLUTION:{

					ArrayList<String> arr = (ArrayList<String>)pck.getData();

					String where = DatabaseTables.SUBMISSION.getTable()+".course_number = '" + arr.get(0) + "' and " + DatabaseTables.SUBMISSION.getTable()+".student_id = '" + arr.get(1) + "' and " + DatabaseTables.SUBMISSION.getTable() + ".unit_number = '" + arr.get(2) + "' and " + DatabaseTables.SUBMISSION.getTable() + ".class_number = '" + arr.get(3) + "' and " + DatabaseTables.SUBMISSION.getTable() + ".task_id = '" + arr.get(4) + "'";

					ArrayList<ArrayList<Object>> resArr = dbController.executeQuery(DatabaseTables.SUBMISSION.getTable(),where);

					pck.setData(null);
					if (resArr.size() == 1){
						pck.setData(new Submission(resArr.get(0)));
					}

					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				}
				case SUBMIT_TASK_SOLUTION:{
					ArrayList<Object> arr = (ArrayList<Object>)pck.getData();
					String where = DatabaseTables.SUBMISSION.getTable()+".course_number = '" + arr.get(0) + "' and " + DatabaseTables.SUBMISSION.getTable()+".student_id = '" + arr.get(1) + "' and " + DatabaseTables.SUBMISSION.getTable() + ".unit_number = '" + arr.get(2) + "' and " + DatabaseTables.SUBMISSION.getTable() + ".class_number = '" + arr.get(3) + "' and " + DatabaseTables.SUBMISSION.getTable() + ".task_id = '" + arr.get(4) + "'";
					Boolean isRemoved = dbController.deleteItem(DatabaseTables.SUBMISSION.getTable(), where);
					Boolean isAdded = dbController.insertItem("INSERT INTO " + DatabaseTables.SUBMISSION.getTable()+" VALUES(?,?,?,?,?,null,CURDATE(),?,null,?)", arr);
					pck.setData(null);
					if (isAdded){
						pck.setData(new Submission(arr));
					}
					pck.setPacketSub(PacketSub.GET_TASK_SOLUTION);
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				}	
				case GET_CHOSEN_SEMESTER:{
					String id = (String)pck.getData();
					String where = "id = '" + id + "'";
					ArrayList<ArrayList<Object>> resArr = dbController.executeQuery(DatabaseTables.SEMESTER.getTable(), where);
					System.out.println("chosen semester result: ");
					System.out.println(resArr);
					Semester semester;
					pck.setData(null);
					if (resArr.size() == 1){
						pck.setData(new Semester(resArr.get(0)));
					}
					pck.setNextStage();
					try {
						client.sendToClient(pck);
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;

				}
				default:
					break;
				}
				break;
			}
			}	
		}else{ /// garbage just for testing strings
			System.out.println("Message received: " + msg + " from " + client);
			frame.setLastMessageLog("Server Recieved: " + msg);
			try {
				client.sendToClient("Server Recieved: " + msg + " so sent this as confirmation back.");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (msg.equals("get teachers")){
				ArrayList<ArrayList<Object>> arr = dbController.executeQuery("teacher", "");
				try {
					client.sendToClient(arr);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Method checks if client with the specific user id is already connected to the server
	 * in such case it will send request to client to disconnect.
	 * @param id id of user that has the client connected to server.
	 */
	private void checkExistanceForDismissal(String id) {
	    Thread[] clientThreadList = getClientConnections();

	    for (int i=0; i<clientThreadList.length; i++)
	    {
	    	ConnectionToClient tempClient = (ConnectionToClient) clientThreadList[i];
	    
	        if (tempClient.getInfo(id) != null){
	        	Packet pck = new Packet(PacketId.REQUIRE_BOOLEAN,PacketSub.REQUEST_LOGOUT,0,"");
	        	try {
					tempClient.sendToClient(pck);
					tempClient.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }
	    }
		
	}






}
//End of EchoServer class
