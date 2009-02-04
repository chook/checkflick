package model;

public enum DBTablesEnum {
	COUNTRIES("COUNTRIES"),
	GENRES("GENRES"),
	LANGUAGES("LANGUAGES"),
	MOVIE_COUNTRIES("MOVIE_COUNTRIES"),
	MOVIE_GENRES("MOVIE_GENRES"),
	MOVIE_LANGUAGES("MOVIE_LANGUAGES"),
	MOVIE_QUOTES("MOVIE_QUOTES"),
	MOVIES("MOVIES"),
	PERSON_AKA_NAMES("PERSON_AKA_NAMES"),
	PERSON_MOVIE_CREDITS("PERSON_MOVIE_CREDITS"),
	PERSON_QUOTES("PERSON_QUOTES"),
	PERSON_TRIVIA("PERSON_TRIVIA"),
	PERSONS("PERSONS"),
	PRODUCTION_ROLES("PRODUCTION_ROLES");

	private static int counter;
	public static int getCounter() {
		return counter;
	}
	
	private final String tableName;

	private DBTablesEnum(String value) {
		tableName = value;
		inc();
	}
	
	public String getTableName() {
		return tableName;
	}
	
	@Override
	public String toString() {
		return getTableName();
	}
	
	private void inc() {
		counter++;
	}
}
