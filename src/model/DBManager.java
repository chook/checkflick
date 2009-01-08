package model;

import java.sql.*;
import java.util.ArrayList;

import controller.Movie;

public class DBManager {
	Connection			  conn;			// DB connection
	ArrayList<Connection> arlConnections; // Conn pool
	
	// The strings for prepared statments
	private static String INSERT_MOVIE_PSTMT = "INSERT INTO demo(fname,lname) VALUES(?,?)";
	private static String DELETE_MOVIE_PSTMT = "DELETE FROM demo where id=?";
	
	// Singleton instance
	private static DBManager instance = null;

	/**
	 * Singleton
	 * @return a DB manager instance
	 */
	public static DBManager getInstance() {
		if(instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	
	protected DBManager() {
		
	}
	
	/**
	 * 
	 * @return the connection (null on error)
	 */
	private void openConnection()
	{
		// loading the driver
		try
		{
			Class.forName("oracle.jdbc.OracleDriver");
		}
		catch (ClassNotFoundException e)
		{
			System.out.println("Unable to load the Oracle JDBC driver..");
			java.lang.System.exit(0); 
		}
		System.out.println("Driver loaded successfully");
		
		// creating the connection
		System.out.print("Trying to connect.. ");
		try
		{
			conn = DriverManager.getConnection(
					"jdbc:oracle:thin:@localhost:1521:XE","dvir","dvir");
		}
		catch (SQLException e)
		{
			System.out.println("Unable to connect - " + e.toString());
			java.lang.System.exit(0); 
		}
		System.out.println("Connected!");
	}
	
	/**
	 * close the connection
	 */
	public void closeConnections()
	{
		// closing the connection
		for(Connection conn : arlConnections)
		{
			try
			{
				conn.close();
			}
			catch (SQLException e)
			{
				System.out.println("Unable to close the connection - " + e.toString());
				java.lang.System.exit(0); 
			}
		}
	}	

	/**
	 * This function used to send a movie query into the DB.
	 * The function supports Insert/Update/Delete
	 * @param oper - The operation to do
	 * @param movie - The movie to send
	 * @return If the operation was successful or not
	 */
	public boolean sendMovieToDB(DBOperation oper, Movie movie) {
		PreparedStatement pstmt = null;// = conn.prepareStatement
		
		// This is a bug for now, cause I don't remember how to work with ENUMs
		switch(oper.ordinal()) {
			case(1):
			{
				try {
					pstmt = conn.prepareStatement(INSERT_MOVIE_PSTMT);
					pstmt.setInt(0, movie.getId());
					pstmt.setString(1, movie.getName());
					pstmt.setInt(2, movie.getYear());
					
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			case(2):
			{
				break;
			}
			case(3):
			{
				break;
			}
		}
		
		return executePreparedStatement(pstmt);		
	}
	
	/**
	 * 
	 * @param oper
	 * @param id
	 * @return
	 */
	public boolean deleteOperation(DBOperation oper, int id) {
		PreparedStatement pstmt = null;
		switch(oper.ordinal()) {
			case(1):
			{
				try {
					pstmt = conn.prepareStatement(DELETE_MOVIE_PSTMT);
					pstmt.setInt(0, id);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			}
		}
		return executePreparedStatement(pstmt);
	}
	
	/**
	 * Shows the executePreparedStatement()
	 */
	private boolean executePreparedStatement(PreparedStatement pstmt)
	{
		//PreparedStatement   pstmt;
		int	    			result;
		//int 				i;
		
		try
		{
			/*pstmt	=	conn.prepareStatement(s);
			for(int i=0;i<objects.size();++i) {
				if(objects.get(i))
			}
			
			pstmt.setString(1, "Rubi-");
			pstmt.setString(2, "Boim-");
			*/
			result = pstmt.executeUpdate();
			
			// closing
			pstmt.close();
		}
		catch (SQLException e)
		{
			System.out.println("ERROR executeUpdate - " + e.toString());
			java.lang.System.exit(0); 
			return false;
		}
		return (result == 0);
	}
}
