package model;

public enum DBTablesEnum {
	MOVIES("MOVIES"),
	PERSONS("PERSONS");
	
	private final String tableName;
	
	private DBTablesEnum(String value) {
		tableName = value;
	}
	
	public String getTableName() {
		return tableName;
	}
}
