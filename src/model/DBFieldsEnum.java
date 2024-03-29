package model;

public enum DBFieldsEnum {
// COLOR_INFO
	COLOR_INFO_COLOR_INFO_ID("COLOR_INFO_ID"),
	COLOR_INFO_COLOR_INFO_TEXT("COLOR_INFO_TEXT"),
// CONNECTIONS_RELATIONS	
	CONNECTIONS_RELATIONS_CONNECTION_RELATION_ID("CONNECTION_RELATION_ID"),
	CONNECTIONS_RELATIONS_CONNECTION_RELATION_NAME("CONNECTION_RELATION_NAME"),
// COUNTRIES
	COUNTRIES_COUNTRY_ID("COUNTRY_ID"),
	COUNTRIES_COUNTRY_NAME("COUNTRY_NAME"),
// GENRES
	GENRES_GENRE_ID("GENRE_ID"),
	GENRES_GENRE_NAME("GENRE_NAME"),
// GOOFS_TYPES
	GOOFS_TYPES_GOOF_TYPE_ID("GOOF_TYPE_ID"),
	GOOFS_TYPES_GOOF_TYPE_NAME("GOOF_NAME"),
// LANGUAGES
	LANGUAGES_LANGUAGE_ID("LANGUAGE_ID"),
	LANGUAGES_LANGUAGE_NAME("LANGUAGE_NAME"),
// PRODUCTION_ROLES
	PRODUCTION_ROLES_PRODUCTION_ROLE_ID("PRODUCTION_ROLE_ID"),
	PRODUCTION_ROLES_PRODUCTION_ROLE_NAME("PRODUCTION_ROLE_NAME"),
// PERSONS TABLE
	PERSONS_PERSON_ID("PERSON_ID"),
	PERSONS_PERSON_NAME("PERSON_NAME"),
	PERSONS_REAL_NAME("REAL_NAME"),
	PERSONS_NICKNAMES("NICKNAMES"),
	PERSONS_DATE_OF_BIRTH("DATE_OF_BIRTH"),
	PERSONS_YEAR_OF_BIRTH("YEAR_OF_BIRTH"),
	PERSONS_CITY_OF_BIRTH("CITY_OF_BIRTH"),
	PERSONS_COUNTRY_OF_BIRTH_ID("COUNTRY_OF_BIRTH_ID"),
	PERSONS_DATE_OF_DEATH("DATE_OF_DEATH"),
	PERSONS_YEAR_OF_DEATH("YEAR_OF_DEATH"),
	PERSONS_HEIGHT("HEIGHT"),
	PERSONS_TRADEMARK("TRADEMARK"),
	PERSONS_BIOGRAPHY_TEXT("BIOGRAPHY_TEXT"),
	PERSONS_TEMP_PERSON_ID("TEMP_PERSON_ID"),						// temporary field used only for the import and dropped afterwards
	PERSONS_TEMP_PERSON_LINE_NUMBER("TEMP_PERSON_LINE_NUMBER"),		// temporary field used only for the import and dropped afterwards
	PERSONS_TEMP_IS_DUPLICATE("TEMP_IS_DUPLICATE"),					// temporary field used only for the import and dropped afterwards
// PERSON_TRIVIA
	PERSON_TRIVIA_PERSON_ID("PERSON_ID"),
	PERSON_TRIVIA_TRIVIA_TEXT("TRIVIA_TEXT"),
// PERSON_QUOTES
	PERSON_QUOTES_PERSON_ID("PERSON_ID"),
	PERSON_QUOTES_QUOTE_TEXT("QUOTE_TEXT"),
//	PERSON_AKA_NAMES
	PERSON_AKA_NAMES_PERSON_ID("PERSON_ID"),
	PERSON_AKA_NAMES_PERSON_AKA_NAME("PERSON_AKA_NAME"),
// MOVIES
	MOVIES_MOVIE_ID("MOVIE_ID"),
	MOVIES_MOVIE_NAME("MOVIE_NAME"),
	MOVIES_MOVIE_YEAR("MOVIE_YEAR"),
	MOVIES_MOVIE_ROMAN_NOTATION("MOVIE_ROMAN_NOTATION"),
	MOVIES_MOVIE_MADE_FOR("MOVIE_MADE_FOR"),
	MOVIES_MOVIE_COLOR_INFO_ID("MOVIE_COLOR_INFO_ID"),
	MOVIES_MOVIE_RUNNING_TIME("MOVIE_RUNNING_TIME"),
	MOVIES_MOVIE_TAGLINE("MOVIE_TAGLINE"),
	MOVIES_MOVIE_PLOT_TEXT("MOVIE_PLOT_TEXT"),
	MOVIES_MOVIE_FILMING_LOCATION_NAME("MOVIE_FILMING_LOCATION_NAME"),
	MOVIES_TEMP_MOVIE_NAME("TEMP_MOVIE_NAME"),		// temporary field used only for the import and dropped afterwards 
// MOVIE_TRIVIA
	MOVIE_TRIVIA_MOVIE_ID("MOVIE_ID"),
	MOVIE_TRIVIA_TRIVIA_TEXT("TRIVIA_TEXT"),
// MOVIE_QUOTES
	MOVIE_QUOTES_MOVIE_ID("MOVIE_ID"),
	MOVIE_QUOTES_QUOTE_TEXT("QUOTE_TEXT"),
// MOVIE_LANGUAGES
	MOVIE_LANGUAGES_MOVIE_ID("MOVIE_ID"),
	MOVIE_LANGUAGES_LANGUAGE_ID("LANGUAGE_ID"),
// MOVIE_GOOFS
	MOVIE_GOOFS_MOVIE_ID("MOVIE_ID"),
	MOVIE_GOOFS_GOOF_TYPE_ID("GOOF_TYPE_ID"),
	MOVIE_GOOFS_GOOF_TEXT("GOOF_TEXT"),
// MOVIE_GENRES
	MOVIE_GENRES_MOVIE_ID("MOVIE_ID"),
	MOVIE_GENRES_GENRE_ID("GENRE_ID"),
// MOVIE_CRAZY_CREDITS
	MOVIE_CRAZY_CREDITS_MOVIE_ID("MOVIE_ID"),
	MOVIE_CRAZY_CREDITS_CRAZY_CREDIT_TEXT("CRAZY_CREDIT_TEXT"),
// MOVIE_COUNTRIES
	MOVIE_COUNTRIES_MOVIE_ID("MOVIE_ID"),
	MOVIE_COUNTRIES_COUNTRY_ID("COUNTRY_ID"),
// MOVIE_CONNECTIONS
	MOVIE_CONNECTIONS_MOVIE_ID("MOVIE_ID"),
	MOVIE_CONNECTIONS_CONNECTED_MOVIE_ID("CONNECTED_MOVIE_ID"),
	MOVIE_CONNECTIONS_MOVIE_CONNECTION_RELATION_ID("MOVIE_CONNECTION_RELATION_ID"),
// PERSON_MOVIE_CREDITS
	PERSON_MOVIE_CREDITS_PERSON_ID("PERSON_ID"),
	PERSON_MOVIE_CREDITS_MOVIE_ID("MOVIE_ID"),
	PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID("PRODUCTION_ROLE_ID"),
	PERSON_MOVIE_CREDITS_IS_ACTOR("IS_ACTOR"),
	PERSON_MOVIE_CREDITS_ACTOR_ROLE("ACTOR_ROLE"),
	PERSON_MOVIE_CREDITS_ACTOR_CREDITS_RANK("ACTOR_CREDITS_RANK"),
// MOVIE_AKA_NAMES
	MOVIE_AKA_NAMES_MOVIE_ID("MOVIE_ID"),
	MOVIE_AKA_NAMES_MOVIE_AKA_NAME("MOVIE_AKA_NAME"),
	MOVIE_AKA_NAMES_MOVIE_AKA_NAME_YEAR("MOVIE_AKA_NAME_YEAR"),
	MOVIE_AKA_NAMES_MOVIE_AKA_NAME_COUNTRY("MOVIE_AKA_NAME_COUNTRY");
	
	private final String fieldName;
	
	private DBFieldsEnum(String value) {
		fieldName = value;
	}
	
	public String getFieldName() {
		return fieldName;
		
	}
	
	@Override
	public String toString() {
		return getFieldName();
	}
	
}