package model;

import java.util.ArrayList;
import java.util.List;

import controller.entity.AbsType;
import controller.enums.MovieDataEnum;
import controller.enums.PersonDataEnum;
import controller.enums.SearchEntitiesEnum;
import controller.filter.AbsFilter;
import controller.filter.AbsSingleFilter;
import controller.filter.FilterOptionEnum;

public class OracleFilterManager {
	private static OracleFilterManager instance = null;

	public static OracleFilterManager getInstance() {
		if (instance == null) {
			instance = new OracleFilterManager();
		}
		return instance;
	}

	protected OracleFilterManager() {

	}

	/**
	 * Get movie data filter
	 * 
	 * @param entity
	 *            - the Movie entity
	 * @param id
	 *            - The movie id TODO: Add more enum values here
	 * @return
	 */
	protected AbsSingleFilter getMovieDataFilter(MovieDataEnum entity, String id) {
		AbsSingleFilter filter = null;
		switch (entity) {
		case MOVIE_GOOFS:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_AKAS:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_COUNTRIES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_GENRES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_QUOTES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_CONNECTIONS:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_CAST:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_MOVIE_ID.getFieldName(), id);
			break;
		}

		return filter;
	}

	protected List<AbsSingleFilter> getMovieInsertFilter(MovieDataEnum data,
			AbsType t) {
		List<AbsSingleFilter> filterList = new ArrayList<AbsSingleFilter>();
		List<String> ls = t.toStringList();
		switch (data) {
		case MOVIE_COUNTRIES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID.getFieldName(),
					ls.get(1)));
			break;
		case MOVIE_AKAS:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME.getFieldName(),
					ls.get(1)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_YEAR
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_COUNTRY
							.getFieldName(), ls.get(3)));
			break;
		case MOVIE_QUOTES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_QUOTE_TEXT.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_LANGUAGES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_GENRES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_TRIVIA:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_TRIVIA.getTableName(),
					DBFieldsEnum.MOVIE_TRIVIA_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_TRIVIA.getTableName(),
					DBFieldsEnum.MOVIE_TRIVIA_TRIVIA_TEXT.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_GOOFS:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
							DBTablesEnum.MOVIE_GOOFS.getTableName(),
							DBFieldsEnum.MOVIE_GOOFS_MOVIE_ID.getFieldName(),
							ls.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_GOOF_TEXT.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_GOOF_TYPE_ID.getFieldName(), ls
							.get(2)));
			break;
		case MOVIE_CONNECTIONS:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_CONNECTED_MOVIE_ID
							.getFieldName(), ls.get(1)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_MOVIE_CONNECTION_RELATION_ID
							.getFieldName(), ls.get(2)));
			break;
		case MOVIE_CRAZY_CREDITS:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_CRAZY_CREDITS.getTableName(),
					DBFieldsEnum.MOVIE_CRAZY_CREDITS_MOVIE_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_CRAZY_CREDITS.getTableName(),
					DBFieldsEnum.MOVIE_CRAZY_CREDITS_CRAZY_CREDIT_TEXT
							.getFieldName(), ls.get(1)));
			break;
		case MOVIE_CAST:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PERSON_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_MOVIE_ID.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PRODUCTION_ROLE_ID
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Boolean,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_IS_ACTOR.getFieldName(), ls
							.get(3)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_ACTOR_ROLE.getFieldName(),
					ls.get(4)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_ACTOR_CREDITS_RANK
							.getFieldName(), ls.get(5)));
			break;
		default:
			break;
		}
		return filterList;
	}

	protected List<AbsSingleFilter> getPersonInsertFilter(PersonDataEnum data,
			AbsType t) {
		List<AbsSingleFilter> filterList = new ArrayList<AbsSingleFilter>();
		List<String> ls = t.toStringList();
		switch (data) {
		case PERSON_QUOTES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSON_QUOTES.getTableName(),
					DBFieldsEnum.PERSON_QUOTES_PERSON_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.PERSON_QUOTES.getTableName(),
					DBFieldsEnum.PERSON_QUOTES_QUOTE_TEXT.getFieldName(),
					ls.get(1)));
			break;
		case PERSON_TRIVIA:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSON_TRIVIA.getTableName(),
					DBFieldsEnum.PERSON_TRIVIA_PERSON_ID.getFieldName(),
					String.valueOf(t.getId())));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.PERSON_TRIVIA.getTableName(),
					DBFieldsEnum.PERSON_TRIVIA_TRIVIA_TEXT.getFieldName(),
					ls.get(1)));
			break;
		case PERSON_AKAS:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
					DBFieldsEnum.PERSON_AKA_NAMES_PERSON_ID.getFieldName(),
					String.valueOf(t.getId())));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
					DBFieldsEnum.PERSON_AKA_NAMES_PERSON_AKA_NAME.getFieldName(),
					ls.get(1)));
			break;
		case PERSON_ROLES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PERSON_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_MOVIE_ID.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PRODUCTION_ROLE_ID
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Boolean,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_IS_ACTOR.getFieldName(), ls
							.get(3)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_ACTOR_ROLE.getFieldName(),
					ls.get(4)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_ACTOR_CREDITS_RANK
							.getFieldName(), ls.get(5)));
			break;
		default:
			break;
		}
		return filterList;
	}
	
	protected AbsSingleFilter getPersonDataFilter(PersonDataEnum data, String id) {
		AbsSingleFilter filter = null;
		switch (data) {
		case PERSON_AKAS:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
					DBFieldsEnum.PERSON_AKA_NAMES_PERSON_ID.getFieldName(), id);
			break;
		case PERSON_QUOTES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSON_QUOTES.getTableName(),
					DBFieldsEnum.PERSON_QUOTES_PERSON_ID.getFieldName(), id);
			break;
		case PERSON_TRIVIA:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSON_TRIVIA.getTableName(),
					DBFieldsEnum.PERSON_TRIVIA_PERSON_ID.getFieldName(), id);
			break;
		case PERSON_ROLES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PERSON_ID.getFieldName(), id);
			break;
		}

		return filter;
	}

	/**
	 * Gets a search filter (for movies/persons)
	 * 
	 * @param entity
	 *            - The search option
	 * @param value
	 *            - First value
	 * @param value2
	 *            - Second value
	 * @return
	 */
	protected AbsFilter getSearchFilter(SearchEntitiesEnum entity,
			String value, String value2) {
		AbsFilter filter = null;
		AbsSingleFilter singleFilter = null;
		switch (entity) {
		case PERSON_NAME:
			filter = new OracleSingleFilter(FilterOptionEnum.String,
					DBTablesEnum.PERSONS.getTableName(), "PERSON_NAME", value);
			break;
		case PERSON_ORIGIN_COUNTRY:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID.getFieldName(),
					value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.PERSONS
					.getTableName(), DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID
					.getFieldName(), DBTablesEnum.COUNTRIES.getTableName(),
					DBFieldsEnum.COUNTRIES_COUNTRY_ID.getFieldName());

			break;
		case PERSON_AGE:
			filter = new OracleSingleFilter(
					(value2 == "") ? FilterOptionEnum.Number
							: FilterOptionEnum.NumberRange,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_YEAR_OF_BIRTH.getFieldName(),
					(value2 == "") ? value : value + " AND " + value2);
			break;
		case MOVIE_NAME:
			filter = new OracleSingleFilter(FilterOptionEnum.String,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName(), value);
			break;
		case MOVIE_YEAR:
			filter = new OracleSingleFilter(
					(value2 == "") ? FilterOptionEnum.Number
							: FilterOptionEnum.NumberRange, DBTablesEnum.MOVIES
							.getTableName(), DBFieldsEnum.MOVIES_MOVIE_YEAR
							.getFieldName(), (value2 == "") ? value : value
							+ " AND " + value2);
			break;
		case MOVIE_GENRE:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName(), value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.MOVIES
					.getTableName(), DBFieldsEnum.MOVIES_MOVIE_ID
					.getFieldName(), DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName());

			break;
		case MOVIE_LANGUAGES:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(), value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.MOVIES
					.getTableName(), DBFieldsEnum.MOVIES_MOVIE_ID
					.getFieldName(), DBTablesEnum.MOVIE_LANGUAGES
					.getTableName(), DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID
					.getFieldName());

			break;
		case MOVIE_COLOR_INFO:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_COLOR_INFO_ID.getFieldName(),
					value);
			break;
		case PERSON_PRODUCTION_ROLE:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PRODUCTION_ROLE_ID
							.getFieldName(), value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.PERSONS
					.getTableName(), DBFieldsEnum.PERSONS_PERSON_ID
					.getFieldName(), DBTablesEnum.MOVIE_APPEARANCES
					.getTableName(), DBFieldsEnum.MOVIE_APPEARANCES_PERSON_ID
					.getFieldName());
			break;
		}

		return filter;
	}
}
