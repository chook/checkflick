package model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import controller.entity.AbsType;
import controller.entity.PersonEntity;
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
	protected AbsFilter getMovieDataFilter(MovieDataEnum entity, String id) {
		AbsFilter filter = null;
		switch (entity) {
		case MOVIE_GOOFS:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_AKAS:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_COUNTRIES:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_GENRES:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_LANGUAGES:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_QUOTES:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_CONNECTIONS:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_MOVIE_ID.getFieldName(), id);
			break;
		case MOVIE_CAST:
			filter = new OracleJoinFilter(new OracleSingleFilter(FilterOptionEnum.NUMBER,
											DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
											DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID.getFieldName(), id),
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(),
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_PERSON_ID.getFieldName());
			break;
		case MOVIE:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName(), id);
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
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID.getFieldName(),
					ls.get(1)));
			break;
		case MOVIE_AKAS:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME.getFieldName(),
					ls.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_YEAR
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_COUNTRY
							.getFieldName(), ls.get(3)));
			break;
		case MOVIE_QUOTES:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_QUOTE_TEXT.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_LANGUAGES:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_GENRES:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_TRIVIA:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_TRIVIA.getTableName(),
					DBFieldsEnum.MOVIE_TRIVIA_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIE_TRIVIA.getTableName(),
					DBFieldsEnum.MOVIE_TRIVIA_TRIVIA_TEXT.getFieldName(), ls
							.get(1)));
			break;
		case MOVIE_GOOFS:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
							DBTablesEnum.MOVIE_GOOFS.getTableName(),
							DBFieldsEnum.MOVIE_GOOFS_MOVIE_ID.getFieldName(),
							ls.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_GOOF_TEXT.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_GOOF_TYPE_ID.getFieldName(), ls
							.get(2)));
			break;
		case MOVIE_CONNECTIONS:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_MOVIE_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_CONNECTED_MOVIE_ID
							.getFieldName(), ls.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_CONNECTIONS.getTableName(),
					DBFieldsEnum.MOVIE_CONNECTIONS_MOVIE_CONNECTION_RELATION_ID
							.getFieldName(), ls.get(2)));
			break;
		case MOVIE_CRAZY_CREDITS:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_CRAZY_CREDITS.getTableName(),
					DBFieldsEnum.MOVIE_CRAZY_CREDITS_MOVIE_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIE_CRAZY_CREDITS.getTableName(),
					DBFieldsEnum.MOVIE_CRAZY_CREDITS_CRAZY_CREDIT_TEXT
							.getFieldName(), ls.get(1)));
			break;
		case MOVIE_CAST:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.BOOLEAN,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_IS_ACTOR.getFieldName(), ls
							.get(3)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_ACTOR_ROLE.getFieldName(),
					ls.get(4)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_ACTOR_CREDITS_RANK
							.getFieldName(), ls.get(5)));
			break;
		case MOVIE:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName(),
					ls.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName(),
					ls.get(2)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_COLOR_INFO_ID.getFieldName(),
					ls.get(3)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_RUNNING_TIME.getFieldName(),
					ls.get(4)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_TAGLINE.getFieldName(),
					ls.get(5)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_PLOT_TEXT.getFieldName(),
					ls.get(6)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_FILMING_LOCATION_NAME.getFieldName(),
					ls.get(7)));
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
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_QUOTES.getTableName(),
					DBFieldsEnum.PERSON_QUOTES_PERSON_ID.getFieldName(),
					ls.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSON_QUOTES.getTableName(),
					DBFieldsEnum.PERSON_QUOTES_QUOTE_TEXT.getFieldName(),
					ls.get(1)));
			break;
		case PERSON_TRIVIA:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_TRIVIA.getTableName(),
					DBFieldsEnum.PERSON_TRIVIA_PERSON_ID.getFieldName(),
					String.valueOf(t.getId())));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSON_TRIVIA.getTableName(),
					DBFieldsEnum.PERSON_TRIVIA_TRIVIA_TEXT.getFieldName(),
					ls.get(1)));
			break;
		case PERSON_AKAS:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
					DBFieldsEnum.PERSON_AKA_NAMES_PERSON_ID.getFieldName(),
					String.valueOf(t.getId())));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
					DBFieldsEnum.PERSON_AKA_NAMES_PERSON_AKA_NAME.getFieldName(),
					ls.get(1)));
			break;
		case PERSON_ROLES:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.BOOLEAN,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_IS_ACTOR.getFieldName(), ls
							.get(3)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_ACTOR_ROLE.getFieldName(),
					ls.get(4)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_ACTOR_CREDITS_RANK
							.getFieldName(), ls.get(5)));
			break;
		case PERSON:
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_PERSON_ID.getFieldName(), ls
							.get(0)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_PERSON_NAME.getFieldName(), ls
							.get(1)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_REAL_NAME
							.getFieldName(), ls.get(2)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_NICKNAMES.getFieldName(), ls
							.get(3)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.DATE,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_DATE_OF_BIRTH.getFieldName(),
					ls.get(4)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_YEAR_OF_BIRTH
							.getFieldName(), ls.get(5)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_CITY_OF_BIRTH
							.getFieldName(), ls.get(6)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID
							.getFieldName(), ls.get(7)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.DATE,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_DATE_OF_DEATH
							.getFieldName(), ls.get(8)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_YEAR_OF_DEATH
							.getFieldName(), ls.get(9)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_HEIGHT
							.getFieldName(), ls.get(10)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_TRADEMARK
							.getFieldName(), ls.get(11)));
			filterList.add(new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_BIOGRAPHY_TEXT
							.getFieldName(), ls.get(12)));
		default:
			break;
		}
		return filterList;
	}
	
	protected AbsFilter getPersonDataFilter(PersonDataEnum data, String id) {
		AbsFilter filter = null;
		switch (data) {
		case PERSON_AKAS:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
					DBFieldsEnum.PERSON_AKA_NAMES_PERSON_ID.getFieldName(), id);
			break;
		case PERSON_QUOTES:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_QUOTES.getTableName(),
					DBFieldsEnum.PERSON_QUOTES_PERSON_ID.getFieldName(), id);
			break;
		case PERSON_TRIVIA:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_TRIVIA.getTableName(),
					DBFieldsEnum.PERSON_TRIVIA_PERSON_ID.getFieldName(), id);
			break;
		case PERSON_ROLES:
			filter = new OracleJoinFilter(new OracleSingleFilter(FilterOptionEnum.NUMBER,
																DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
																DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(), id),
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID.getFieldName(),
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName());
			break;
		case PERSON:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_PERSON_ID.getFieldName(), id);
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
			filter = new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.PERSONS.getTableName(), "PERSON_NAME", value);
			break;
		case PERSON_ORIGIN_COUNTRY:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
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
					(value2 == "") ? FilterOptionEnum.NUMBER
							: FilterOptionEnum.NUMBER_RANGE,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_YEAR_OF_BIRTH.getFieldName(),
					(value2 == "") ? value : value + " AND " + value2);
			break;
		case MOVIE_NAME:
			filter = new OracleSingleFilter(FilterOptionEnum.STRING,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName(), value);
			break;
		case MOVIE_YEAR:
			filter = new OracleSingleFilter(
					(value2 == "") ? FilterOptionEnum.NUMBER
							: FilterOptionEnum.NUMBER_RANGE, DBTablesEnum.MOVIES
							.getTableName(), DBFieldsEnum.MOVIES_MOVIE_YEAR
							.getFieldName(), (value2 == "") ? value : value
							+ " AND " + value2);
			break;
		case MOVIE_GENRE:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName(), value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.MOVIES
					.getTableName(), DBFieldsEnum.MOVIES_MOVIE_ID
					.getFieldName(), DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName());

			break;
		case MOVIE_LANGUAGES:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(), value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.MOVIES
					.getTableName(), DBFieldsEnum.MOVIES_MOVIE_ID
					.getFieldName(), DBTablesEnum.MOVIE_LANGUAGES
					.getTableName(), DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID
					.getFieldName());

			break;
		case MOVIE_COLOR_INFO:
			filter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.MOVIES.getTableName(),
					DBFieldsEnum.MOVIES_MOVIE_COLOR_INFO_ID.getFieldName(),
					value);
			break;
		case PERSON_PRODUCTION_ROLE:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.NUMBER,
					DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID
							.getFieldName(), value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.PERSONS
					.getTableName(), DBFieldsEnum.PERSONS_PERSON_ID
					.getFieldName(), DBTablesEnum.PERSON_MOVIE_CREDITS
					.getTableName(), DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID
					.getFieldName());
			break;
		}

		return filter;
	}

	protected List<AbsSingleFilter> getMovieDeleteFilter(MovieDataEnum dataType, AbsType entity) {
		List<AbsSingleFilter> filter = new ArrayList<AbsSingleFilter>();
		List<String> ls = entity.toStringList();
		switch(dataType) {
		case MOVIE:
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIES.getTableName(),
										  DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName(), ls.get(0)));
			break;
		case MOVIE_AKAS: // BUG
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					  DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName(), ls.get(0)));
			break;
		case MOVIE_COUNTRIES:
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					  DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(), ls.get(0)));
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					  DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID.getFieldName(), ls.get(1)));
			break;
		case MOVIE_GENRES:
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIE_GENRES.getTableName(),
					  DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName(), ls.get(0)));
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIE_GENRES.getTableName(),
					  DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName(), ls.get(1)));
			break;
		case MOVIE_CAST:
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					  DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(), ls.get(0)));
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					  DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID.getFieldName(), ls.get(1)));
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					  DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID.getFieldName(), ls.get(2)));
			break;
		}
		return filter;
	}
	
	protected List<AbsSingleFilter> getPersonDeleteFilter(PersonDataEnum dataType, AbsType entity) {
		List<AbsSingleFilter> filter = new ArrayList<AbsSingleFilter>();
		List<String> ls = entity.toStringList();
		switch(dataType) {
		case PERSON:
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.MOVIES.getTableName(),
										  DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName(), ls.get(0)));
			break;
		case PERSON_ROLES:
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					  DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(), ls.get(0)));
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					  DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID.getFieldName(), ls.get(1)));
			filter.add(new OracleSingleFilter(FilterOptionEnum.NUMBER, DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
					  DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID.getFieldName(), ls.get(2)));
			break;
		}
		return filter;
	}
}
