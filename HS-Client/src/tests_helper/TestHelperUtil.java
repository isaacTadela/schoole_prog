package tests_helper;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import com.mysql.jdbc.ResultSetMetaData;
import app.HSClient;


import org.junit.Assert;

/**
 * Singleton class used as util to help the tests connect to server and to database directly.
 * giving us methods that could help us communicate with server and database.
 * @author Amir
 *
 */
public class TestHelperUtil implements RefreshConnectionIF {
	
	private HSClient client;
	private Connection conn;
	private String DB_HOST = "sql11.freemysqlhosting.net:3306/sql11175785"; // 127.0.0.1
	private String DB_USER = "sql11175785"; // root
	private String DB_PASSWORD = "QHUUxthJB2"; // Braude
	private String SERVER_HOST = "127.0.0.1";
	private Integer SERVER_PORT = 5555;
	private static TestHelperUtil instance;
	private ArrayList<TestInterface> listeners;
	private boolean responseArrived = false;
	private TestConnectorUI ui;
	private Thread testUIThread;
	
	
	/**
	 * static method to build a singleton from this test helper class
	 * @return this class only and unique generated instance.
	 */
	public static TestHelperUtil getInstance(){
		if (instance == null){
			instance = new TestHelperUtil();
		}
		return instance;
	}
	
	
	/**
	 * method that adds any test that implements TestInterface into ArrayList
	 * in order to send an update whenever the client sends response to pass it 
	 * to the TestInterfaces in the ArrayList.
	 * @param test
	 */
	public void addListener(TestInterface test){
		if (listeners == null){
			listeners = new ArrayList<>();
		}
		if (!listeners.contains(test)){
			listeners.add(test);
		}
	}
	
	/**
	 * method that removes TestInterface if its available in listeners list.
	 * @param test
	 */
	public void removeListener(TestInterface test){
		if (listeners != null && listeners.contains(test)){
			listeners.remove(test);
		}
	}
	
	/**
	 * private constructor that only getInstance() can access it. in order to keep
	 * this class generated only as singleton.
	 */
	private TestHelperUtil() {
		Preferences prefs = Preferences.userNodeForPackage(TestHelperUtil.class);
		SERVER_HOST = prefs.get("server_host", SERVER_HOST);
		SERVER_PORT = prefs.getInt("server_port", SERVER_PORT);
		DB_HOST = prefs.get("db_host", DB_HOST);
		DB_USER = prefs.get("db_user", DB_USER);
		DB_PASSWORD = prefs.get("db_pass", DB_PASSWORD);
		try {
			client = new HSClient(SERVER_HOST, SERVER_PORT){
				@Override
				 public void handleMessageFromServer(Object msg) 
				 {
					  for (int i = 0; i < listeners.size(); i++){
						  listeners.get(i).onResponse(msg);
					  }
					  responseArrived = true;
				 }
			};
		} catch (IOException e) {
			client = null;
		}
		connectWithDatabase(DB_HOST,DB_USER,DB_PASSWORD);
		
		testUIThread = new Thread(new Runnable(){

			@Override
			public void run() {
				setUIInstance(TestConnectorUI.getInstance());
			}
			
		});
		testUIThread.start();
		while(!isEverythingConnected()){
			try {
				Thread.sleep(100);
				if (this.ui != null && !this.ui.isVisible()){
					ui.setVisible(true);
					System.out.println("displaying test ui");
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	/**
	 * method checks if both connections with server and database been established or not.
	 * @return boolean indicating if both connection been established
	 */
	public boolean isEverythingConnected(){
		if (conn == null || client == null){
			return false;
		}
		if (ui != null){
			ui.setVisible(false);
		}
		return true;
	}
	
	
	/**
	 * Method gives this singleton a reference to the JFrame of the ui that will be displayed in case of problem
	 * connecting to server or database.
	 * @param ui JFrame mean to be used for setting correct data for connecting to server and client.
	 */
	
	public void setUIInstance(TestConnectorUI ui){
		ui.setRefreshConnectionIF(this);
		this.ui = ui;
		this.ui.setConnectionToClient(SERVER_HOST, SERVER_PORT, (client != null));
		this.ui.setConnectedToDatabase(DB_HOST, DB_USER, DB_PASSWORD, (conn != null));
	}
	
	
	/**
	 * Method which deletes a row/tuple if it exist.
	 * Meant for testing purposes only.
	 * Sometimes in tests we change some data in database.
	 * Where we might require to remove it after the testing is done.
	 * 
	 * @param table indicates the table it need to select from.
	 * @param condition conditions the row need to be at to delete it.
	 */

	public void deleteItemIfExisting(String table,String condition) {
		Statement stmt;
		try {
			stmt = getDatabaseConnection().createStatement();
			String query = "DELETE FROM " + table + " WHERE " + condition + ";";
			System.out.println(query);
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}			
	}
	
	
	/**
	 * Method which executes query given the full query as string.
	 * This method is used to perform queries to check if certain data is available in database or not.
	 * Meant for testing purposes only.
	 * 
	 * @param query indicates the whole query to execute as String
	 * @return ArrayList<ArrayList<Object>> which is matrix with all rows and columns of the query.
	 */

	public ArrayList<ArrayList<Object>> executeRawQuery(String query){
		Statement stmt;
		ArrayList<ArrayList<Object>> arr = new ArrayList<>();
		try 
		{
			stmt = getDatabaseConnection().createStatement();
			System.out.println(query);
			ResultSet rs;
			rs = stmt.executeQuery(query);
			ResultSetMetaData rsm = (ResultSetMetaData) rs.getMetaData();
			int columns = rsm.getColumnCount();

			while(rs.next())
			{
				ArrayList<Object> tuppleArr = new ArrayList<>();
				for(int i=1; i <= columns; i++){
					tuppleArr.add(rs.getObject(i));	
				}
				arr.add(tuppleArr);
			}
			rs.close();
			return arr;
		} catch (SQLException e){
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Method for inserting tuples in table through database.
	 * @param table table to insert to.
	 * @param data tuple data.
	 * @return true if the operation inserted successfully otherwise false.
	 */
	public Boolean insertItem(String table,String data) {
		Statement stmt;
		try {
			stmt = getDatabaseConnection().createStatement();
			String query = "INSERT INTO " + table + " VALUES(" + data + ")";
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {	
			System.out.println("Attempted to enter Duplicate item to table!");	
			return false;
		}
	}		
	
	
	/**
	 * method contains in it the tools to connect to database
	 * @param host database host
	 * @param user database username
	 * @param pass database password
	 */
	public void connectWithDatabase(String host,String user,String pass){
		try 
		{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (Exception ex) 
		{/* handle the error*/}

		try 
		{
			ArrayList<String> link = new ArrayList<>();
			link.add("jdbc:mysql://" + host + "?zeroDateTimeBehavior=convertToNull");
			link.add(user);
			link.add(pass);
			conn = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
			System.out.println("SQL connection succeed");
		} catch (SQLException ex) 
		{
			conn = null;
		}    
	}

	/**
	 * Method to obtain client instance;
	 * @return Client that is connected to server.
	 */
	public HSClient getClient() {
		return client;
	}

	/**
	 * Method to obtain database connection.
	 * @return Connection with database.
	 */
	public Connection getDatabaseConnection() {
		return conn;
	}

	/**
	 * method used to make thread sleeps until we receive response from server.
	 * if not then the test will end up with fail.
	 * @param timeout is maximum time in milliseconds we allow the wait for response to happen.
	 */
	public void waitUntilResponse(long timeout){
		responseArrived = false;
		long timer = 0;
    	while(!responseArrived){
	    	try {
				Thread.sleep(100);
				timer = timer+100;
				if (timer >= timeout){
					responseArrived = true;
					Assert.fail("Test failed because of timeout.");
				}
			} catch (InterruptedException e) {
				Assert.fail("Couldnt make test thread wait before executing next task.");
			}
    	}
	}

	/**
	 * when user clicks to reconnect to servers with new info it will pass the data and execute this method.
	 */
	@Override
	public void onClickRefresh(String host, String port, String dbHost, String dbUser, String dbPassword) {
		System.out.println("refreshed");
		Integer portInt = 5555;
		try{
			portInt = Integer.parseInt(port);
			
			Preferences prefs = Preferences.userNodeForPackage(TestHelperUtil.class);
			prefs.put("server_host", host);
			prefs.putInt("server_port", portInt);
			prefs.put("db_host", dbHost);
			prefs.put("db_user", dbUser);
			prefs.put("db_pass", dbPassword);
			
			SERVER_HOST = host;
			SERVER_PORT = portInt;
			DB_HOST = dbHost;
			DB_USER = dbUser;
			DB_PASSWORD = dbPassword;			
			
		}catch(Exception e){
			
		}
		try {
			System.out.println("host: " + host + ", port: " + portInt.toString());
			if (client != null){
				client.closeConnection();
			}
			client = new HSClient(host, portInt){
				@Override
				 public void handleMessageFromServer(Object msg) 
				 {
					 for (int i = 0; i < listeners.size(); i++){
					  listeners.get(i).onResponse(msg);
					 }
					 responseArrived = true;
				 }
			};
			
			System.out.println("passed server connection");
		} catch (IOException e) {
			client = null;
		}
		connectWithDatabase(dbHost,dbUser,dbPassword);
		this.ui.setConnectionToClient(SERVER_HOST, SERVER_PORT, (client != null));
		this.ui.setConnectedToDatabase(DB_HOST, DB_USER, DB_PASSWORD, (conn != null));
	}

	
}
