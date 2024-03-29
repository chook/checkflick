package controller;

import java.io.*;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;
import org.ini4j.*;

/**
 * This class represents the configurations of the application (fetched from an
 * INI file)
 * 
 * @author Chook
 * 
 */
public class AppData {
	private static AppData instance = null;

	public synchronized static AppData getInstance() {
		if (instance == null)
			instance = new AppData();
		return instance;
	}
	private String adminPass;
	private int bucketMaxSize;
	private String dbHost;
	private String dbPassword;
	private String dbPort;
	private String dbServer;
	private String dbUsername;
	private String importFolder;
	private int maxConnections;
	private int maxFetchFromDB;
	private int maxThreads;
	private int preparedStatementMaxBatchSize;

	protected AppData() {
		resetToDefaults();
	}

	/**
	 * @return the adminPass
	 */
	public String getAdminPass() {
		return adminPass;
	}

	/**
	 * @return the bucketMaxSize
	 */
	public int getBucketMaxSize() {
		return bucketMaxSize;
	}

	/**
	 * @return the dbHost
	 */
	public String getDbHost() {
		return dbHost;
	}

	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}

	/**
	 * @return the dbPort
	 */
	public String getDbPort() {
		return dbPort;
	}

	/**
	 * @return the dbServer
	 */
	public String getDbServer() {
		return dbServer;
	}

	/**
	 * @return the dbUsername
	 */
	public String getDbUsername() {
		return dbUsername;
	}

	/**
	 * @return the importFolder
	 */
	public String getImportFolder() {
		return importFolder;
	}

	/**
	 * @return the maxConnections
	 */
	public int getMaxConnections() {
		return maxConnections;
	}

	/**
	 * @return the maxFetchFromDB
	 */
	public int getMaxFetchFromDB() {
		return maxFetchFromDB;
	}

	/**
	 * @return the maxThreads
	 */
	public int getMaxThreads() {
		return maxThreads;
	}

	/**
	 * @return the preparedStatementMaxBatchSize
	 */
	public int getPreparedStatementMaxBatchSize() {
		return preparedStatementMaxBatchSize;
	}

	/**
	 * Parsing the ini file to get the configurations
	 * 
	 * @param fullpath
	 *            - The path of the ini file
	 */
	public void parseINIFile(String fullpath) {
		Preferences prefs = null;
		try {
			prefs = new IniFile(new File(fullpath));
			System.out.println("About to parse the ini !! my precious");
			setBucketMaxSize(Integer.parseInt(prefs.node("settings").get(
					"maxbucket", null)));
			setDbHost(prefs.node("db").get("host", null));
			setDbPassword(prefs.node("db").get("password", null));
			setDbPort(prefs.node("db").get("port", null));
			setDbServer(prefs.node("db").get("server", null));
			setDbUsername(prefs.node("db").get("username", null));
			setImportFolder(prefs.node("settings").get("importpath", null));
			setMaxConnections(Integer.parseInt(prefs.node("db").get(
					"connections", null)));
			setMaxThreads(Integer.parseInt(prefs.node("settings").get(
					"threads", null)));
			setPreparedStatementMaxBatchSize(Integer.parseInt(prefs.node(
					"settings").get("maxbatch", null)));
			setAdminPass(prefs.node("settings").get("adminPassword", null));
			setMaxFetchFromDB(Integer.parseInt(prefs.node("db").get("maxfetch", null)));
		} catch (BackingStoreException e) {
			resetToDefaults();
		}
	}

	/**
	 * Reseting to defaults
	 */
	public void resetToDefaults() {
		setBucketMaxSize(100000);
		setDbHost("localhost");
		setDbPassword("checkflick");
		setDbPort("1555");
		setDbServer("csodb");
		setDbUsername("checkflick");
		setImportFolder("/users/courses/databases/imdb/extracted/");
		setMaxConnections(6);
		setMaxThreads(6);
		setPreparedStatementMaxBatchSize(20000);
		setAdminPass("boim2");
		setMaxFetchFromDB(1000);
	}

	/**
	 * @param adminPass
	 *            the adminPass to set
	 */
	private void setAdminPass(String adminPass) {
		this.adminPass = adminPass;
	}

	/**
	 * @param bucketMaxSize
	 *            the bucketMaxSize to set
	 */
	private void setBucketMaxSize(int bucketMaxSize) {
		this.bucketMaxSize = bucketMaxSize;
	}

	/**
	 * @param dbHost
	 *            the dbHost to set
	 */
	private void setDbHost(String dbHost) {
		this.dbHost = dbHost;
	}

	/**
	 * @param dbPassword
	 *            the dbPassword to set
	 */
	private void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	/**
	 * @param dbPort
	 *            the dbPort to set
	 */
	private void setDbPort(String dbPort) {
		this.dbPort = dbPort;
	}

	/**
	 * @param dbServer
	 *            the dbServer to set
	 */
	private void setDbServer(String dbServer) {
		this.dbServer = dbServer;
	}

	/**
	 * @param dbUsername
	 *            the dbUsername to set
	 */
	private void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}

	/**
	 * @param importFolder
	 *            the importFolder to set
	 */
	private void setImportFolder(String importFolder) {
		this.importFolder = importFolder;
	}

	/**
	 * @param maxConnections
	 *            the maxConnections to set
	 */
	private void setMaxConnections(int maxConnections) {
		this.maxConnections = maxConnections;
	}

	/**
	 * @param maxFetchFromDB the maxFetchFromDB to set
	 */
	private void setMaxFetchFromDB(int maxFetchFromDB) {
		this.maxFetchFromDB = maxFetchFromDB;
	}

	/**
	 * @param maxThreads
	 *            the maxThreads to set
	 */
	private void setMaxThreads(int maxThreads) {
		this.maxThreads = maxThreads;
	}

	/**
	 * @param preparedStatementMaxBatchSize
	 *            the preparedStatementMaxBatchSize to set
	 */
	private void setPreparedStatementMaxBatchSize(
			int preparedStatementMaxBatchSize) {
		this.preparedStatementMaxBatchSize = preparedStatementMaxBatchSize;
	}
}
