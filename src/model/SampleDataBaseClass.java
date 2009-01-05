package model;

public class SampleDataBaseClass {
	private static SampleDataBaseClass database = null;
	
	public static SampleDataBaseClass getInstance() {
		if(database == null) {
			database = new SampleDataBaseClass();
		}
		return database;
	}
	
	protected SampleDataBaseClass() {
	}
	
	public String getVersion() {
		return "This is Oracle 10g database server";
	}
}
