package model;

/**
 * This sample class represents the Singleton Design Pattern
 * This pattern is to make sure that only one database class
 * in the whole program, and other classes who wishes
 * to communicate with this class, simply uses it's getInstance method 
 * @author Chen Harel
 * @date   05/01/2009
 *
 */
public class SampleDataBaseClass {
	private static SampleDataBaseClass database = null;
	
	/**
	 * This function retrieves the database object
	 * @return database object initialized
	 */
	public static SampleDataBaseClass getInstance() {
		if(database == null) {
			database = new SampleDataBaseClass();
		}
		return database;
	}
	
	/**
	 * Protected Constructor
	 */
	protected SampleDataBaseClass() {
	}
	
	public String getVersion() {
		return "This is Oracle 10g database server";
	}
}
