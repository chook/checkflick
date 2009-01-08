package model;

import java.sql.*;
import java.util.*;

/**
 * This class is used as a connection pool
 * @author CHENH
 *
 */
public class DBConnectionPool {
	
	// Data Members
	private String 	URI;
	private String 	user; 
	private String 	pass;
	private String 	drivername; 
	private int 	maxconn;
	private static DBConnectionPool pool;
	private ArrayList<Connection> freeConnections;
	
	/**
	 * Default Constructor
	 */
	public DBConnectionPool() {
		
	}
	
	/**
	 * The private constructor of the DBConnectionPool Class
	 * @param uri - the database host string
	 * @param user - user name
	 * @param pass - password
	 * @param drivername - driver name (default - jdbc)
	 * @param maxconn - The maximum number of connections
	 */
	private DBConnectionPool(String uri, String user, String pass,
			String drivername, int maxconn) {
		super();
		URI = uri; 
		this.user = user;
		this.pass = pass;
		this.drivername = drivername;
		this.maxconn = maxconn;
		freeConnections = new ArrayList<Connection>(maxconn);
		loadJDBCDriver();
	}

	/**
	 * Loading the JDBC driver and register it
	 */
	private void loadJDBCDriver() {
		try {
			Driver driver = (Driver)Class.forName(this.drivername).newInstance();
			DriverManager.registerDriver(driver);
		} catch (Exception e) {
			System.out.println("Can't load/register JDBC driver ");
		}
	}
	
	/**
	 * Gets the instance of DBConnectionPool (Singleton)
	 * @param uri - the database host string
	 * @param user - user name
	 * @param pass - password
	 * @param drivername - driver name (default - jdbc)
	 * @param maxconn - The maximum number of connections
	 * @return The DBConnectionPool object
	 */
	public static synchronized DBConnectionPool getInstance(String uri, 
															String user, 
															String pass, 
															String drivername, 
															int maxconn) {
		if (pool == null) {
			pool = new DBConnectionPool(uri, user, pass, 
										drivername, maxconn);
			}
		return pool;
	}
	
	/**
	 * Gets a valid connection to the database
	 * @return the connection
	 */
	public synchronized Connection getConnection() {
		Connection rescon = null;
		if (!freeConnections.isEmpty()) {
			rescon = freeConnections.get(freeConnections.size()-1);
			freeConnections.remove(rescon);
			try {
				if (rescon.isClosed()) {
					System.out.println("Removed closed connection!");
					// Try again recursively
					rescon = getConnection();
				}
			} catch (SQLException e) {
				System.out.println("Removed closed connection!");
				// Try again recursively
				rescon = getConnection();
			} catch (Exception e) {
				System.out.println("Removed closed connection!");
				// Try again recursively
				rescon = getConnection();
			}
		} else {
			rescon = createConnection();
		}
		return rescon;
	}
	
	/**
	 * Creates a new connection 
	 * @return The created connection or null
	 */
	private Connection createConnection() {
		Connection rescon = null;
		try {
			if (user == null) {
				rescon = DriverManager.getConnection(URI);
			} else {
				rescon = DriverManager.getConnection(URI, user, pass);
			}
			// new connection in connection pool created
		} catch (SQLException e) {
			System.out.println("Cannot create a new connection!");
			rescon = null;
		}
		return rescon;
	}
	
	/**
	 * Returns a connection back to the connection pool
	 * @param con - The connection to be returned to the pool
	 */
	public synchronized void returnConnection(Connection con) {
		if ((con != null) 
			&&
			(freeConnections.size() <= maxconn)) {
			freeConnections.add(con);
		}
	}
	
	/**
	 * Iterates and releasing the connections 
	 */
	public synchronized void release() {
		for(Connection con : freeConnections) {
			try {
				con.close();
			} catch (SQLException e) {
	 			System.out.println("Cannot close connection! (Probably already closed?)");
			}
		}
		freeConnections.clear();
	}
}
