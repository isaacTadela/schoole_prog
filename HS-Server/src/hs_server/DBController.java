package hs_server;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import com.mysql.cj.jdbc.result.ResultSetMetaData;




public class DBController {
	Connection con;
	ArrayList<String> link;

	/**
	 * constructor to DBController
	 * @param con is Connection is class which contains the connected jdbc connection
	 * @param link array which contains the database data in order to reconnect in case of disconnection
	 * host,username,password
	 */
	public DBController(Connection con,ArrayList<String> link){
		this.con = con;
		this.link = link;
	}

	/**
	 * method which executes query given table and where statement as string.
	 * 
	 * @param table indicates the table you are trying to select from
	 * @param where indicates conditions in which the rows need to be true
	 * @return ArrayList<ArrayList<Object>> which is matrix with all rows and columns of the query.
	 */

	public ArrayList<ArrayList<Object>> executeQuery(String table,String where){

		Statement stmt;
		String query = "SELECT * FROM ";
		ArrayList<ArrayList<Object>> arr = new ArrayList<>();
		try 
		{
			stmt = con.createStatement();

			query = query + table;
			if (where != null && where.length() > 0){
				query = query + " WHERE " + where;	
			}
			query = query + ";";

			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsm = (ResultSetMetaData) rs.getMetaData();
			int columns = rsm.getColumnCount();

			while(rs.next())
			{
				ArrayList<Object> tuppleArr = new ArrayList<>();
				for(int i=1; i <= columns; i++){
					tuppleArr.add(rs.getObject(i));	
				}
				arr.add(tuppleArr);
				// Print out the values
				System.out.println(rs.getString(1)+"  " +rs.getString(2));
			}

			rs.close();
			return arr;
		} catch (SQLException e){
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return executeQuery(table,where);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return null;
		}
	}		


	/**
	 * method which executes query given the full query as string.
	 * 
	 * @param query indicates the whole query to execute as String
	 * @return ArrayList<ArrayList<Object>> which is matrix with all rows and columns of the query.
	 */

	public ArrayList<ArrayList<Object>> executeRawQuery(String query){
		Statement stmt;
		ArrayList<ArrayList<Object>> arr = new ArrayList<>();
		try 
		{
			stmt = con.createStatement();
			System.out.println(query);
			ResultSet rs = stmt.executeQuery(query);
			ResultSetMetaData rsm = (ResultSetMetaData) rs.getMetaData();
			int columns = rsm.getColumnCount();

			while(rs.next())
			{
				ArrayList<Object> tuppleArr = new ArrayList<>();
				for(int i=1; i <= columns; i++){
					tuppleArr.add(rs.getObject(i));	
				}
				arr.add(tuppleArr);
				// Print out the values

			}


			rs.close();
			return arr;
		} catch (SQLException e){
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return executeRawQuery(query);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			return null;
		}
	}






	/**
	 * updates the the selected tupple and returns if the action successful or not 
	 * as boolean
	 * 
	 * @param table indicates the table that has the tuple that needs to be changed.
	 * @param id indicates the key as in the column for the table.
	 * @param idValue indicates the row id needed
	 * @param set is a String value contains all the columns that need to be changed with , between each.
	 * @return boolean indicating if the operation successful or not.
	 */

	// example: set:  name = "amir"
	public Boolean updateQueryById(String table,String id, String idValue, String set) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE " + table +" SET " + set + " WHERE " + id + " = \"" + idValue + "\"";
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return updateQueryById(table,id,idValue,set);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}			
		return false;
	}


	/**
	 * updates the the selected tuples and returns if the action successful or not 
	 * as boolean
	 * 
	 * @param table indicates the table that has the tuple that needs to be changed.
	 * @param set is a String value contains all the columns that need to be changed with , between each.
	 * @param where indicates the conditions of the rows implicated in the update
	 * @return boolean indicating if the operation successful or not.
	 */

	public Boolean updateQuery(String table, String set,String where) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE " + table +" SET " + set + " WHERE " + where;
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return updateQuery(table,set,where);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}			
		return false;
	}		


	/**
	 * updates the the selected tuples and returns the changed tuples as ArrayList<ArrayList<Object>> as matrix
	 * 
	 * @param table indicates the table that has the tuple that needs to be changed.
	 * @param set is a String value contains all the columns that need to be changed with , between each.
	 * @param where indicates the conditions of the rows implicated in the update
	 * @return ArrayList<ArrayList<Object>> as matrix containing the changed tuples.
	 */

	public ArrayList<ArrayList<Object>> updateQueryAndReturnChangedRows(String table, String set,String where) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE " + table +" SET " + set + " WHERE " + where;
			System.out.println(query);
			stmt.executeUpdate(query);

			ArrayList<ArrayList<Object>> resArr	= executeQuery(table,where);
			if (resArr != null && resArr.size() > 0){
				return resArr;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return updateQueryAndReturnChangedRows(table,set,where);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}			
		return null;
	}

	/**
	 * updates the the selected tuples and returns the changed tuple as ArrayList<Object> 
	 * this method is meant for the update queries that changes only 1 row.
	 * 
	 * @param table indicates the table that has the tuple that needs to be changed.
	 * @param set is a String value contains all the columns that need to be changed with , between each.
	 * @param where indicates the conditions of the row implicated in the update
	 * @return ArrayList<Object> contains the changed tuple.
	 */

	public ArrayList<Object> updateQueryAndReturnAChangedRow(String table, String set,String where) {
		ArrayList<ArrayList<Object>> arr = updateQueryAndReturnChangedRows(table, set, where);
		if (arr != null && arr.size() > 0){
			return arr.get(0);
		}
		return null;
	}



	/**
	 * method used to insert new row to table
	 * @param table indicates table name as String
	 * @param data contains the data that the row needs as String with comma separates them
	 * @return boolean indicating if the item was inserted or not.
	 */

	public Boolean insertItem(String table,String data) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "INSERT INTO " + table + " VALUES(" + data + ")";
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {	
			System.out.println("Attempted to enter Duplicate item to table!");
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return insertItem(table,data);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}				
			return false;
		}
	}		

	/**
	 * method used to insert new row to table with their original casting correspondent.
	 * 
	 * @param statementValue indicates prepared statements as String
	 * @param arr contains the data in list that the row needs as their original type.
	 * @return boolean indicating if the item was inserted or not.
	 */		
	public Boolean insertItem(String statementValue,ArrayList<Object> arr) {
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(statementValue);
			for(int i=1;i<arr.size()+1;i++){
				if (arr.get(i-1) instanceof Integer){
					try {
						pstmt.setInt(i,(Integer)arr.get(i-1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (arr.get(i-1) instanceof byte[]){
					try {
						pstmt.setBytes(i,(byte[])arr.get(i-1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}				
				if (arr.get(i-1) instanceof Date){
					try {
						pstmt.setDate(i,(Date)arr.get(i-1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if (arr.get(i-1) instanceof String){
					try {
						pstmt.setString(i,(String)arr.get(i-1));
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
			int result;
			try {
				result = pstmt.executeUpdate();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			if (result == 1){
				return true;
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return insertItem(statementValue,arr);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
		}	
		return false;
	}	


	/**
	 * method which deletes a row/tupple
	 * @param table indicates the table it need to select from.
	 * @param data conditions the row need to be at to delete it.
	 * @return boolean indicating if the operation successfult or not.
	 */


	public Boolean deleteItem(String table,String data) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "DELETE FROM " + table + " WHERE " + data + ";";
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return deleteItem(table,data);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}			

		return false;
	}			




	/**
	 * old and less generic update query method so it better not be used.
	 * @param table table name as String
	 * @param id indicates the primary key the row has
	 * @param value contains the columns and their required ned values for the row
	 * @param columnName indicates the primary key value name
	 * @return boolean indicating if the operation was successful or not.
	 */

	@Deprecated
	public Boolean updateQuery(String table,String id, String value, String columnName) {
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE " + table +" SET " + columnName + " = \"" + value + "\" WHERE id = \"" + id+"\"";
			System.out.println(query);
			stmt.executeUpdate(query);
			return true;
		} catch (SQLException e) {	
			e.printStackTrace();
			if (e instanceof CommunicationsException){
				try {
					con = DriverManager.getConnection(link.get(0),link.get(1),link.get(2));
					return updateQuery(table,id,value,columnName);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}	
		}			

		return false;
	}		


}




