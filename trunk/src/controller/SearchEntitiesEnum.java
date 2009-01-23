package controller;

/**
 * This enum is used to help the view select which entity filter
 * he wishes to invoke
 * @author Chook
 *
 */
public enum SearchEntitiesEnum {
	MOVIE_NAME,
	MOVIE_YEAR,
	MOVIE_GENRE,
	MOVIE_COLOR_INFO,
	MOVIE_LANGUAGES,
	PERSON_NAME,
	PERSON_AGE,
	PERSON_ORIGIN_COUNTRY,
	PERSON_PRODUCTION_ROLE,
	MOVIES,
	PERSONS,
	MOVIE_AKAS,
	MOVIE_GOOFS, // TODO: This is not an entity, need to think how to represent it
}
