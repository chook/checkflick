package model;

public enum DBTablesEnum {
	COLOR_INFO("COLOR_INFO"),
	CONNECTIONS_RELATIONS("CONNECTIONS_RELATIONS"),
	COUNTRIES("COUNTRIES"),
	GENRES("GENRES"),
	GOOFS_TYPES("GOOFS_TYPES"),
	LANGUAGES("LANGUAGES"),
	MOVIE_AKA_NAMES("MOVIE_AKA_NAMES"),
	MOVIE_APPEARANCES("MOVIE_APPEARANCES"),
	MOVIE_CONNECTIONS("MOVIE_CONNECTIONS"),
	MOVIE_COUNTRIES("MOVIE_COUNTRIES"),
	MOVIE_CRAZY_CREDITS("MOVIE_CRAZY_CREDITS"),
	MOVIE_GENRES("MOVIE_GENRES"),
	MOVIE_GOOFS("MOVIE_GOOFS"),
	MOVIE_LANGUAGES("MOVIE_LANGUAGES"),
	MOVIE_QUOTES("MOVIE_QUOTES"),
	MOVIE_TRIVIA("MOVIE_TRIVIA"),
	MOVIES("MOVIES"),
	PERSON_AKA_NAMES("PERSON_AKA_NAMES"),
	PERSON_QUOTES("PERSON_QUOTES"),
	PERSON_TRIVIA("PERSON_TRIVIA"),
	PERSONS("PERSONS"),
	PRODUCTION_ROLES("PRODUCTION_ROLES");

	static int counter;
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
		// TODO Auto-generated method stub
		return getTableName();
	}
	
	private void inc() {
		counter++;
	}
}
