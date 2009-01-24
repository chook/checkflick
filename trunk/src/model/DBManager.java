package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import controller.InsertEntitiesEnum;
import controller.MovieDataEnum;
import controller.NamedEntitiesEnum;
import controller.PersonDataEnum;
import controller.SearchEntitiesEnum;
import controller.entity.AbsDataType;
import controller.entity.CategorizedRelation;
import controller.entity.EntityEnum;
import controller.entity.GeoEntity;
import controller.entity.MovieAppearance;
import controller.entity.MovieEntity;
import controller.entity.DatedEntity;
import controller.entity.NamedEntity;
import controller.entity.NamedRelation;
import controller.entity.PersonEntity;
import controller.filter.AbsFilter;
import controller.filter.AbsSingleFilter;
import controller.filter.Filter;
import controller.filter.FilterOptionEnum;

public class DBManager {
	// The strings for prepared statements
	private static String INSERT_MOVIE_PSTMT = "INSERT INTO MOVIES(fname,lname) VALUES(?,?)";
	private static String UPDATE_MOVIE_PSTMT = "UPDATE MOVIES SET ? WHERE MOVIE_ID=?";
	private static String DELETE_MOVIE_PSTMT = "DELETE FROM MOVIES WHERE MOVIE_ID=?";
	private static String SEARCH_MOVIE_STMT = "SELECT MOVIES.MOVIE_ID, MOVIES.MOVIE_NAME, MOVIES.MOVIE_YEAR FROM ";
	private static String SEARCH_PERSON_STMT = "SELECT PERSONS.PERSON_ID, PERSONS.PERSON_NAME, PERSONS.YEAR_OF_BIRTH FROM ";
	private static String SELECT_MOVIE_PSTMT = "SELECT * FROM MOVIES WHERE MOVIE_ID=?";
	private static String SELECT_PERSON_PSTMT = "SELECT * FROM PERSONS WHERE PERSON_ID=?";
	private static String SELECT_GENERIC_STMT = "SELECT * FROM ";
	private static String SELECT_GENERIC_ORDERED_STMT = "SELECT * FROM %s ORDER BY %s";
	
	private static String INSERT_SINGLE_DATATYPE = "INSERT INTO %s (%s) VALUES (?)";
	private static String INSERT_MOVIE_SINGLE_DATATYPE = "INSERT INTO %s (%s, %s) VALUES (?, ?)";
	private static String INSERT_MOVIE_PSTMT_GOOD = "INSERT INTO %s (%s, %s, %s, %s) VALUES (?, ?, ?, ?)";
	private static String INSERT_DOUBLE_DATATYPE = "INSERT INTO %s (%s) VALUES %s";
	private static String LIMIT_RESULTS_PSTMT = "SELECT * FROM (SELECT bottomLimitTable.*, ROWNUM topLimit FROM (%s) bottomLimitTable WHERE ROWNUM <= %d) WHERE topLimit >= %d";
	
	// Singleton instance
	private static DBManager instance = null;
	private DBConnectionPool pool = null;

	/**
	 * Singleton
	 * 
	 * @return a DB manager instance
	 */
	public static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	/**
	 * DWefault Constructor - Initiates the connection pool
	 */
	protected DBManager() {
		pool = DBConnectionPool./*getInstance(
				"jdbc:oracle:thin:@localhost:1521:XE", "chook", "shoochi",
				"oracle.jdbc.OracleDriver", 6);*/
		  getInstance("jdbc:oracle:thin:@localhost:1555:csodb", "chenhare",
		  "Shoochi0", "oracle.jdbc.OracleDriver", 6);
//				getInstance("jdbc:oracle:thin:@localhost:1521:XE", "checkflick",
//		  "checkflick", "oracle.jdbc.OracleDriver", 6);
//		getInstance("jdbc:oracle:thin:@localhost:1555:csodb", "nadavsh2",
//				  "nadavsh2", "oracle.jdbc.OracleDriver", 6);
	}

	/**
	 * This is the only function for getting information on the persons/movies
	 * @param data - Which type of entity do we want
	 * @param filter - The single filter to build the query
	 * @return a list of data types of data (encapsulated)
	 */
	public List<AbsDataType> getAbsDataType(EntityEnum data, AbsSingleFilter filter) {
		Connection conn = pool.getConnection();
		List<AbsDataType> retList = new ArrayList<AbsDataType>();
		Statement stmt;
		ResultSet resultSet;
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(SELECT_GENERIC_STMT);
		
		// Trying to get a connection statement
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		// Executing the query and building the movies array
		try {
			sbQuery.append(filter.getTable());
			sbQuery.append(" WHERE ");
			sbQuery.append(filter);
			resultSet = stmt.executeQuery(sbQuery.toString());
			while (resultSet.next() == true) {
				retList.add(resultSetToAbsEntity(resultSet, data));
				if(retList.size() > 100)
					break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
		return retList;
	}

	/**
	 * returns a map of movies selected from the DB, that were limited by (top, bottom) limit on the SELECT
	 * results that are sent are ordered beforehand by MOVIE_ID
	 * Note: there is no way to return the whole movies list, and you must specify a bottomLimit (memory reasons)
	 * Note: the rows starts with 1 and not 0 (meaning, topLimit = 0 returns exactly what topLimit = 1 returns)
	 * @param topLimit the start of the limit
	 * @param bottomLimit the end of the limit
	 * @return
	 */
	public ResultSet getAllMovies(int topLimit, int bottomLimit) {
		ResultSet set = null;

		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();
		String genericStr = String.format(SELECT_GENERIC_ORDERED_STMT, DBTablesEnum.MOVIES, DBFieldsEnum.MOVIES_MOVIE_NAME);
		String pstmtStr = String.format(LIMIT_RESULTS_PSTMT, genericStr, bottomLimit, topLimit);
		
		try {
			pstmt = conn.prepareStatement(pstmtStr);
			set = pstmt.executeQuery();
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		pool.returnConnection(conn);
		return set;
	}
	
	/**
	 * This function retrieves named entities from enum
	 * @param entity - The entity to retrieve 
	 * @return a list of named entities
	 */
	public List<NamedEntity> getAllNamedEntities(NamedEntitiesEnum entity) {
		Connection c = pool.getConnection();
		List<NamedEntity> list = new ArrayList<NamedEntity>();
		Statement s;
		ResultSet set;
		String query = "SELECT * FROM ";
		
		// Trying to get a connection statement
		try {
			s = c.createStatement();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		switch (entity) {
		case GENRES:
			query += DBTablesEnum.GENRES;
			break;
		case COLOR_INFOS:
			query += DBTablesEnum.COLOR_INFO;
			break;
		case LANGUAGES:
			query += DBTablesEnum.LANGUAGES;
			break;
		case PRODUCTION_ROLES:
			query += DBTablesEnum.PRODUCTION_ROLES;
			break;
		case CONNECTION_RELATIONS:
			query += DBTablesEnum.CONNECTIONS_RELATIONS;
			break;
		case COUNTRIES:
			query += DBTablesEnum.COUNTRIES;
		}
		
		// Executing the query and building the movies array
		try {
			set = s.executeQuery(query);
			while (set.next() == true) {
				list.add(new NamedEntity(set.getInt(1), set.getString(2)));
				if(list.size() > 1000)
					break;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool.returnConnection(c);
		return list;
	}

	@Deprecated
	public AbsSingleFilter getFilter(NamedEntitiesEnum entity, String id) {
		AbsSingleFilter filter = null;
		switch(entity) {
		case COUNTRIES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(), id);
			break;
		}
		return filter;
	}

	@Deprecated
	public List<AbsDataType> getGeoEntities(AbsSingleFilter filter) {
		Connection conn = pool.getConnection();
		List<AbsDataType> retList = new ArrayList<AbsDataType>();
		Statement stmt;
		ResultSet resultSet;
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(SELECT_GENERIC_STMT);
		
		// Trying to get a connection statement
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		// Executing the query and building the movies array
		try {
			sbQuery.append(filter.getTable());
			sbQuery.append(" WHERE ");
			sbQuery.append(filter);
			resultSet = stmt.executeQuery(sbQuery.toString());
			while (resultSet.next() == true) {
				retList.add(new GeoEntity(resultSet.getInt(1), resultSet.getString(2),
										  resultSet.getInt(3), resultSet.getInt(4)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
		return retList;
	}

	/**
	 * Get movie by Id
	 * @param id - the movie id
	 * @return a movie entity
	 */
	public MovieEntity getMovieById(int id) {
		MovieEntity tempMovie = null;
		ResultSet set = null;

		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();

		try {
			pstmt = conn.prepareStatement(SELECT_MOVIE_PSTMT);
			pstmt.setInt(1, id);

			// Executing the query and building the movies array
			set = pstmt.executeQuery();
			if (set.next() == true) {
				tempMovie = fillMovieFromSet(set);
			}
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		pool.returnConnection(conn);
		return tempMovie;
	}

	/**
	 * Returns a list of movie data objects
	 * @param data - The Data
	 * @param id - The movie id
	 * @return the list of data types
	 */
	public List<AbsDataType> getMovieData(MovieDataEnum data, String id) {
		AbsSingleFilter filter = getMovieDataFilter(data, id);
		List<AbsDataType> list = null;
		switch(data) {
		case MOVIE_AKAS:
			list = getAbsDataType(EntityEnum.GEO_ENTITY, filter);
			break;
		case MOVIE_COUNTRIES:
		case MOVIE_GENRES:
		case MOVIE_LOCATIONS:
		case MOVIE_QUOTES:
			list = getAbsDataType(EntityEnum.NAMED_ENTITY, filter);
			break;
		case MOVIE_GOOFS:
			list = getAbsDataType(EntityEnum.NAMED_RELATION, filter);
			break;
		case MOVIE_CONNECTIONS:
			list = getAbsDataType(EntityEnum.CATEGORIZED_RELATION, filter);
			break;
		case MOVIE_CAST:
			list = getAbsDataType(EntityEnum.MOVIE_APPEARANCE, filter);
			break;
		}
		return list;
	}
	
	/**
	 * Get movie data filter
	 * @param entity - the Movie entity
	 * @param id - The movie id
	 * @return
	 */
	public AbsSingleFilter getMovieDataFilter(MovieDataEnum entity, String id) {
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

	@Deprecated
	public List<NamedEntity> getNamedEntities(AbsSingleFilter filter) {
		Connection conn = pool.getConnection();
		List<NamedEntity> retList = new ArrayList<NamedEntity>();
		Statement stmt;
		ResultSet resultSet;
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(SELECT_GENERIC_STMT);
		
		// Trying to get a connection statement
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		// Executing the query and building the movies array
		try {
			sbQuery.append(filter.getTable());
			sbQuery.append(" WHERE ");
			sbQuery.append(filter);
			resultSet = stmt.executeQuery(sbQuery.toString());
			while (resultSet.next() == true) {
				retList.add(new NamedEntity(resultSet.getInt(1), resultSet.getString(2)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
		return retList;
	}

	@Deprecated
	public List<NamedRelation> getNamedRalations(AbsSingleFilter filter) {
		Connection conn = pool.getConnection();
		List<NamedRelation> retList = new ArrayList<NamedRelation>();
		Statement stmt;
		ResultSet resultSet;
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(SELECT_GENERIC_STMT);
		
		// Trying to get a connection statement
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		// Executing the query and building the movies array
		try {
			sbQuery.append(filter.getTable());
			sbQuery.append(" WHERE ");
			sbQuery.append(filter);
			resultSet = stmt.executeQuery(sbQuery.toString());
			while (resultSet.next() == true) {
				retList.add(new NamedRelation(resultSet.getInt(1), resultSet.getInt(2),
												resultSet.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
		return retList;
	}
	
	/**
	 * Get a person by an id
	 * @param id - The person id
	 * @return A person entity
	 */
	public PersonEntity getPersonById(int id) {
		PersonEntity tempMovie = null;
		ResultSet set = null;
		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();

		try {
			pstmt = conn.prepareStatement(SELECT_PERSON_PSTMT);
			pstmt.setInt(1, id);

			// Executing the query and building the movies array
			set = pstmt.executeQuery();
			if (set.next() == true) {
				tempMovie = fillPersonFromSet(set);
			}
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		pool.returnConnection(conn);
		return tempMovie;
	}

	/**
	 * Gets a person data
	 * @param data - The data option
	 * @param id - The person id
	 * @return the list of data type
	 */
	public List<AbsDataType> getPersonData(PersonDataEnum data, String id) {
		AbsSingleFilter filter = getPersonDataFilter(data, id);
		List<AbsDataType> list = null;
		switch(data) {
		case PERSON_AKAS:
		case PERSON_QUOTES:
		case PERSON_TRIVIA:
			list = getAbsDataType(EntityEnum.NAMED_ENTITY, filter);
			break;
		case MOVIE_APPEARANCES:
			list = getAbsDataType(EntityEnum.MOVIE_APPEARANCE, filter);
			break;
		}
		return list;
	}
	
	public List<AbsSingleFilter> getSearchFilter(InsertEntitiesEnum entity, String value) {
		List<AbsSingleFilter> filter = new ArrayList<AbsSingleFilter>();
		switch (entity) {
		case MOVIE_QUOTE:
			filter.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_QUOTES.getTableName(),
											DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName(), value));
			filter.add(new OracleInsertFilter(FilterOptionEnum.String, DBTablesEnum.MOVIE_QUOTES.getTableName(),
					DBFieldsEnum.MOVIE_QUOTES_QUOTE_TEXT.getFieldName(), value));

			break;
		}
		return filter;
	}

	/**
	 * Gets a search filter (for movies/persons)
	 * @param entity - The search option
	 * @param value - First value
	 * @param value2 - Second value
	 * @return
	 */
	public AbsFilter getSearchFilter(SearchEntitiesEnum entity, String value,
			String value2) {
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
			filter = new OracleSingleFilter((value2 == "") ? FilterOptionEnum.Number : FilterOptionEnum.NumberRange,
										DBTablesEnum.PERSONS.getTableName(), 
										DBFieldsEnum.PERSONS_YEAR_OF_BIRTH.getFieldName(),
										(value2 == "") ? value : value + " AND " + value2 );
			break;
		case MOVIE_NAME:
			filter = new OracleSingleFilter(FilterOptionEnum.String,
											DBTablesEnum.MOVIES.getTableName(), 
											DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName(),
											value);
			break;
		case MOVIE_YEAR: 
			filter = new OracleSingleFilter((value2 == "") ? FilterOptionEnum.Number : FilterOptionEnum.NumberRange,
											DBTablesEnum.MOVIES.getTableName(), 
											DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName(),
											(value2 == "") ? value : value + " AND " + value2 );
			break;
		case MOVIE_GENRE:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GENRES.getTableName(),
					DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName(),
					value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.MOVIES.getTableName(),
														DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName(),
														DBTablesEnum.MOVIE_GENRES.getTableName(),
														DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName());

			break;
		case MOVIE_LANGUAGES:
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
					DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(),
					value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.MOVIES.getTableName(),
														DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName(),
														DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
														DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName());

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
					DBFieldsEnum.MOVIE_APPEARANCES_PRODUCTION_ROLE_ID.getFieldName(),
					value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.PERSONS.getTableName(),
														DBFieldsEnum.PERSONS_PERSON_ID.getFieldName(),
														DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
														DBFieldsEnum.MOVIE_APPEARANCES_PERSON_ID.getFieldName());
			break;
		}

		return filter;
	}

	/**
	 * This function receives a set of values, and a definition of a table, and
	 * adds all the values to the DB with one PreparedStatementBatch
	 * 
	 * @param set
	 *            - The set of values to add to the DB
	 * @param table
	 *            - The name of the table
	 * @param field
	 *            - The name of the Value_name field
	 * 
	 **/
	public boolean insertMoviesSetToDB(Set<MovieEntity> set) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		String statementStr;
		statementStr = String.format(INSERT_MOVIE_PSTMT_GOOD, 
										DBTablesEnum.MOVIES.getTableName(), 
										DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName(),
										DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName(),
										DBFieldsEnum.MOVIES_MOVIE_ROMAN_NOTATION.getFieldName(),
										DBFieldsEnum.MOVIES_MOVIE_MADE_FOR.getFieldName());

		try {
			pstmt = conn.prepareStatement(statementStr);

			for (MovieEntity setMovie : set) {
				pstmt.setString(1, setMovie.getRawName());
				pstmt.setInt(2, setMovie.getYear());
				pstmt.setString(3, setMovie.getRomanNotation());
				pstmt.setString(4, setMovie.getMadeFor());
				pstmt.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		bReturn = executePreparedStatementBatch(pstmt);
		pool.returnConnection(conn);
		return bReturn;
	}

	/**
	 * This function receives a set of NamedEntities, and a definition of a table, and
	 * adds all the values to the DB with one PreparedStatementBatch
	 * This refers to the MOVIE_LANGUAGES, MOVIE_COUNTRIES & MOVIE_GENRES tables
	 * 
	 * @param set
	 *            - The set of values to add to the DB
	 * @param table
	 *            - The name of the table
	 * @param field
	 *            - The name of the Value_name field
	 * 
	 **/
	public boolean insertMovieSingleDataTypeSetToDB(Set<NamedRelation> set, DBTablesEnum table,
			DBFieldsEnum field1, DBFieldsEnum field2) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		String statementStr;
		statementStr = String.format(INSERT_MOVIE_SINGLE_DATATYPE, 
										DBTablesEnum.MOVIE_LANGUAGES.getTableName(), 
										DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(),
										DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID.getFieldName());

		try {
			pstmt = conn.prepareStatement(statementStr);

			for (NamedRelation setNamedRelation : set) {
				pstmt.setInt(1, setNamedRelation.getId());
				pstmt.setInt(2, setNamedRelation.getSecondaryId());
				pstmt.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		bReturn = executePreparedStatementBatch(pstmt);
		pool.returnConnection(conn);
		return bReturn;
	}
	
	/**
	 * This function receives a set of values, and a definition of a table, and
	 * adds all the values to the DB with one PreparedStatementBatch
	 * This refers to the LANGUAGES, COUNTRIES & GENRES tables
	 * 
	 * @param set
	 *            - The set of values to add to the DB
	 * @param table
	 *            - The name of the table
	 * @param field
	 *            - The name of the Value_name field
	 * 
	 **/
	public boolean insertSingleDataTypeSetToDB(Set<String> set, DBTablesEnum table,
			DBFieldsEnum field) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		String statementStr;
		statementStr = String.format(INSERT_SINGLE_DATATYPE, table.getTableName(), field.getFieldName());

		try {
			pstmt = conn.prepareStatement(statementStr);

			for (Object setObject : set) {
				pstmt.setString(1, setObject.toString());
				pstmt.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		bReturn = executePreparedStatementBatch(pstmt);
		pool.returnConnection(conn);
		return bReturn;
	}

	/**
	 * The search function - searches movies/persons
	 * @param arlFilters - List of filters
	 * @param tableToSearch - The table to search
	 * TODO: Change this function a little bit, we know which table we use from the filters,
	 * no need to send it as an extra parameter (Chen 24.01.09)
	 * @return List of search objects (TODO: DatedEntity in the future)
	 */
	public List<DatedEntity> search(List<AbsFilter> arlFilters,
			DBTablesEnum tableToSearch) {
		// Variables Declaration
		List<DatedEntity> arlSearchResults = new ArrayList<DatedEntity>();
		DatedEntity result = null;
		ResultSet set = null;
		Statement s = null;
		Connection conn = pool.getConnection();

		try {
			s = conn.createStatement();

			switch (tableToSearch) {
			case MOVIES:
				if(arlFilters.size() == 0)
					set = s.executeQuery(SEARCH_MOVIE_STMT + DBTablesEnum.MOVIES);
				else
					set = s.executeQuery(SEARCH_MOVIE_STMT
							+ parseWhereClauseFromFiltersNew(arlFilters));
				
				break;

			case PERSONS:
				if(arlFilters.size() == 0)
					set = s.executeQuery(SEARCH_PERSON_STMT + DBTablesEnum.PERSONS);
				else
					set = s.executeQuery(SEARCH_PERSON_STMT
							+ parseWhereClauseFromFiltersNew(arlFilters));
				break;

			}
			// Executing the query and building the movies array

			while (set.next() == true) {
				if ((result = fillSearchResult(set, tableToSearch)) != null) {
					arlSearchResults.add(result);
					if(arlSearchResults.size() > 100)
						break;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error in searchMovies " + e.toString());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		pool.returnConnection(conn);
		return arlSearchResults;
	}
	
	/**
	 * Search movies in the database
	 * 
	 * @param arlFilters
	 *            - A list of filters to prepare the WHERE clause
	 * @return - An array of movies that were fetched from the select
	 * @deprecated
	 */
	@Deprecated
	public List<DatedEntity> searchMovies(List<Filter> arlFilters) {
		// Variables Declaration
		List<DatedEntity> arlSearchResults = new ArrayList<DatedEntity>();
		DatedEntity result = null;
		ResultSet set = null;
		Statement s = null;
		Connection conn = pool.getConnection();

		try {
			s = conn.createStatement();

			// Executing the query and building the movies array
			set = s.executeQuery(SEARCH_MOVIE_STMT
					+ parseWhereClauseFromFilters(arlFilters));
			while (set.next() == true) {
				if ((result = fillMovieSearchResult(set)) != null) {
					arlSearchResults.add(result);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error in searchMovies " + e.toString());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}

		return arlSearchResults;
	}

	/**
	 * Search persons by filters
	 * 
	 * @param arlFilters
	 * @return List of persons
	 * @deprecated
	 */
	@Deprecated
	public List<DatedEntity> searchPersons(List<Filter> arlFilters) {
		return null;
	}
	
	/**
	 * This function used to send a movie query into the DB. The function
	 * supports Insert/Update/Delete
	 * 
	 * @param oper
	 *            - The operation to do
	 * @param movie
	 *            - The movie to send
	 * @return If the operation was successful or not TODO: Check this function
	 *         (22/01/09)
	 */
	public boolean sendMovieToDB(DBOperationEnum oper, MovieEntity movie) {
		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		switch (oper) {
		case InsertMovie: {
			try {
				pstmt = conn.prepareStatement(INSERT_MOVIE_PSTMT);
				pstmt.setInt(0, movie.getId());
				pstmt.setString(1, movie.getName());
				pstmt.setInt(2, movie.getYear());

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		case DeleteMovie: {
			try {
				pstmt = conn.prepareStatement(DELETE_MOVIE_PSTMT);
				pstmt.setInt(1, movie.getId());
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
			break;
		}
		case UpdateMovie: {
			try {
				pstmt = conn.prepareStatement(UPDATE_MOVIE_PSTMT);

				// TODO: Get the "set" clause
				pstmt.setString(1, movie.getName());

				pstmt.setInt(2, movie.getId());

			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		}
		}
		bReturn = executePreparedStatement(pstmt);
		pool.returnConnection(conn);
		return bReturn;
	}

	/**
	 * executes the executePreparedStatement
	 */
	private boolean executePreparedStatement(PreparedStatement pstmt) {
		int result;

		try {
			result = pstmt.executeUpdate();

			// closing
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR executeUpdate - " + e.toString());
			java.lang.System.exit(0);
			return false;
		}
		return (result == 0);
	}

	/**
	 * executes the executePreparedStatementBatch TODO: check the return values
	 * of the executeBatch method (Nadav 23/01/09 0:30am)
	 */
	private boolean executePreparedStatementBatch(PreparedStatement pstmt) {
		int[] result;

		try {
			result = pstmt.executeBatch();

			// closing
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR executeUpdate - " + e.toString());
			java.lang.System.exit(0);
			return false;
		}
		return true;
	}

	private MovieEntity fillMovieFromSet(ResultSet set) {
		MovieEntity movie = null;
		try {
			movie = new MovieEntity();
			movie.setId(set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName()));
			movie.setName(set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName()));
			movie.setYear(set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName()));
			movie.setColorInfo(set.getInt(DBFieldsEnum.MOVIES_MOVIE_COLOR_INFO_ID.getFieldName()));
			movie.setRunningTime(set.getInt(DBFieldsEnum.MOVIES_MOVIE_RUNNING_TIME.getFieldName()));
			movie.setTaglines(set.getString(DBFieldsEnum.MOVIES_MOVIE_TAGLINE.getFieldName()));
			movie.setPlot(set.getString(DBFieldsEnum.MOVIES_MOVIE_PLOT_TEXT.getFieldName()));
			movie.setFilmingLocations(set
					.getString(DBFieldsEnum.MOVIES_MOVIE_FILMING_LOCATION_NAME.getFieldName()));
		} catch (SQLException e) {
			System.out.println("SQLException error");
			return null;
		}
		return movie;
	}

	private DatedEntity fillMovieSearchResult(ResultSet set) {
		DatedEntity res = null;
		try {
			res = new DatedEntity(set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName()));
			res.setName(set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME
					.getFieldName()));
			res.setYear(set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR
					.getFieldName()));
			return res;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private PersonEntity fillPersonFromSet(ResultSet set) {
		PersonEntity person = null;
		try {
			person = new PersonEntity(); 
			person.setId(set.getInt(DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));
			person.setName(set.getString(DBFieldsEnum.PERSONS_PERSON_NAME.getFieldName()));
			person.setPersonRealName(set.getString(DBFieldsEnum.PERSONS_REAL_NAME.getFieldName()));
			person.setPersonNickNames(set.getString(DBFieldsEnum.PERSONS_NICKNAMES.getFieldName()));
			person.setDateOfBirth(set.getDate(DBFieldsEnum.PERSONS_DATE_OF_BIRTH.getFieldName()));
			person.setYearOfBirth(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_BIRTH.getFieldName()));
			person.setCityOfBirth(set.getString(DBFieldsEnum.PERSONS_CITY_OF_BIRTH.getFieldName()));
			person.setCountryOfBirth(set.getInt(DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID.getFieldName()));
			person.setDateOfDeath(set.getDate(DBFieldsEnum.PERSONS_DATE_OF_DEATH.getFieldName()));
			person.setYearOfDeath(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_DEATH.getFieldName()));
			person.setHeight(set.getInt(DBFieldsEnum.PERSONS_HEIGHT.getFieldName()));
			person.setTrademark(set.getString(DBFieldsEnum.PERSONS_TRADEMARK.getFieldName()));
			person.setBiography(set.getString(DBFieldsEnum.PERSONS_BIOGRAPHY_TEXT.getFieldName()));
		} catch (SQLException e) {
			System.out.println("SQLException error");
			return null;
		}
		return person;
	}
	
	private DatedEntity fillPersonSearchResult(ResultSet set) {
		DatedEntity res = null;
		try {
			res = new DatedEntity(set.getInt(DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));
			res.setName(set.getString(DBFieldsEnum.PERSONS_PERSON_NAME
					.getFieldName()));
			res.setYear(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_BIRTH
					.getFieldName()));
			return res;
		} catch (SQLException e) {
			return null;
		}
	}
	
	private DatedEntity fillSearchResult(ResultSet set, DBTablesEnum table) {
		switch (table) {
		case MOVIES:
			return fillMovieSearchResult(set);
		case PERSONS:
			return fillPersonSearchResult(set);
		}
		return null;
	}

	private AbsSingleFilter getPersonDataFilter(PersonDataEnum data, String id) {
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
		case MOVIE_APPEARANCES:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_APPEARANCES.getTableName(),
					DBFieldsEnum.MOVIE_APPEARANCES_PERSON_ID.getFieldName(), id);
			break;
		}
		
		return filter;
	}

	/**
	 * Private method to parse a where clause from the filters
	 * 
	 * @param arlFilters
	 * @return a WHERE clause to use in SELECT statements
	 * @deprecated
	 */
	@Deprecated
	private String parseWhereClauseFromFilters(List<Filter> arlFilters) {
		StringBuilder stbFilter = new StringBuilder();
		int filterCounter = 0;

		// Building the WHERE clause
		if (arlFilters.size() > 0) {
			stbFilter.append("WHERE ");
			for (Filter filter : arlFilters) {
				++filterCounter;
				stbFilter.append(filter);

				// Making sure the clause won't end with an AND
				if (filterCounter < arlFilters.size()) {
					stbFilter.append(" AND ");
				}
			}
		}

		return stbFilter.toString();
	}
	
	/**
	 * Private method to parse a where clause from the filters
	 * 
	 * @param arlFilters
	 * @return a WHERE clause to use in SELECT statements
	 */
	private String parseWhereClauseFromFiltersNew(List<AbsFilter> arlFilters) {
		StringBuilder stbFilter = new StringBuilder();
		int filterCounter = 0;

		// Get the tables names we need
		Set<String> s = new HashSet<String>();

		// Building the WHERE clause
		if (arlFilters.size() > 0) {
			stbFilter.append(" WHERE ");
			for (AbsFilter filter : arlFilters) {
				if(filter != null) {
					++filterCounter;
					// Making sure the clause won't end with an AND
					if (filterCounter != 1) {
						stbFilter.append(" AND ");
					}
					stbFilter.append(filter);
					s.addAll(filter.toTablesSet());
				}
			}
		}
		String fromClause = "";
		for (String t : s) {
			fromClause += t + " ,";
		}
		fromClause = fromClause.substring(0, fromClause.length() - 2);

		return fromClause + stbFilter.toString();
	}

	/**
	 * This function is a factory for data types
	 * @param rs - The tuple to parse
	 * @param entity - The type of tuple
	 * @return - The data type
	 */
	private AbsDataType resultSetToAbsEntity(ResultSet rs, EntityEnum entity) {
		try {
			switch(entity) {
			case NAMED_ENTITY:
				return new NamedEntity(rs.getInt(1), rs.getString(2));
			case GEO_ENTITY:
				return new GeoEntity(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4));
			case NAMED_RELATION:
				return new NamedRelation(rs.getInt(1), rs.getInt(2), rs.getString(3));
			case CATEGORIZED_RELATION:
				return new CategorizedRelation(rs.getInt(1), rs.getInt(2), rs.getInt(3));
			case MOVIE_APPEARANCE:
				return new MovieAppearance(rs.getInt(1), rs.getInt(2), rs.getInt(3),
										   (rs.getString(4).equals("Y") ? true : false), rs.getInt(5), rs.getInt(6));
			}
		}
		catch (SQLException e) {
			System.out.println("Error in resultSetToAbsEntity");
		}
		return null;
	}

	/**
	 * This insert function inserts a person data
	 * @param data - A person data
	 * @param type - The type
	 * @return If the insert was successful or not
	 */
	public boolean insertAbsDataType(PersonDataEnum data, AbsDataType type) {
		List<String> list = null;
		switch(data) {
		case PERSON_QUOTES:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.PERSON_QUOTES_PERSON_ID.getFieldName());
			list.add(DBFieldsEnum.PERSON_QUOTES_QUOTE_TEXT.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.PERSON_QUOTES.getTableName(),
								list, type);
			break;
		case PERSON_AKAS:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.PERSON_AKA_NAMES_PERSON_ID.getFieldName());
			list.add(DBFieldsEnum.PERSON_AKA_NAMES_PERSON_AKA_NAME.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.PERSON_AKA_NAMES.getTableName(),
								list, type);
			break;
		case PERSON_TRIVIA:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.PERSON_TRIVIA_PERSON_ID.getFieldName());
			list.add(DBFieldsEnum.PERSON_TRIVIA_TRIVIA_TEXT.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.PERSON_TRIVIA.getTableName(),
								list, type);
			break;
		}
		return false;
	}
	
	public boolean insertAbsDataType(MovieDataEnum data, AbsDataType type) {
		List<String> list = null;
		switch(data) {
		case MOVIE_COUNTRIES:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName());
			list.add(DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
								list, type);
			break;
		case MOVIE_GENRES:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName());
			list.add(DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.MOVIE_GENRES.getTableName(),
								list, type);
			break;
		case MOVIE_QUOTES:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName());
			list.add(DBFieldsEnum.MOVIE_QUOTES_QUOTE_TEXT.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.MOVIE_QUOTES.getTableName(),
								list, type);
			break;
		case MOVIE_AKAS:
			list = new ArrayList<String>();
			list.add(DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName());
			list.add(DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME.getFieldName());
			list.add(DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_YEAR.getFieldName());
			list.add(DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_COUNTRY.getFieldName());
			sendAbsDataTypeToDb(DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
								list, type);
		}
		return false;
	}
	
	/**
	 * Sends a data type to the db
	 * @param table
	 * @param fields
	 * @param type
	 * TODO: Needs to make this GENERIC :)
	 * @return
	 */
	private boolean sendAbsDataTypeToDb(String table, List<String> fields, AbsDataType type) {
		Statement s = null;
		Connection conn = pool.getConnection();
		Map<String, String> map = null;
		try {
			String parsedFields = "";
			for(String stemp: fields) {
				parsedFields += stemp + " ,";
			}
			
			if (fields.size()> 0)
				parsedFields = parsedFields.substring(0, parsedFields.length() - 2);
			
			map = type.toStringMap();
			s = conn.createStatement();
			
			s.executeUpdate(String.format(INSERT_DOUBLE_DATATYPE,
							table, parsedFields, type));
			return true;
		} catch (SQLException e) {
			System.out.println("Error in searchMovies " + e.toString());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		return false;
	}

	public void sendMovieData(MovieDataEnum data, AbsDataType t) {
		List<AbsSingleFilter> filterList = null;
		switch(data) {
		case MOVIE_COUNTRIES:
		case MOVIE_QUOTES:
		case MOVIE_GENRES:
			filterList = getMovieInsertFilter(data, t);
			sendAbsDataType(EntityEnum.NAMED_ENTITY, filterList);
			  
			break;
		}
	}
	
	private void sendAbsDataType(EntityEnum named_entity,
			List<AbsSingleFilter> filterList) {
		Connection conn = pool.getConnection();
		Statement stmt = null;
		String str = "insert into %s (%s) values (%s)";
		String strFields = "";
		String strValues = "";
		String strTable = "";
		
		// Trying to get a connection statement
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		// Executing the query and building the movies array
		try {
			for (AbsSingleFilter filter : filterList) {
				strTable = filter.getTable();
				strFields += filter.getColumn() + " ,";
				strValues += filter.getValue() + " ,";
			}
			strFields = strFields.substring(0, strFields.length()-2);
			strValues = strValues.substring(0, strValues.length()-2);

			stmt.executeUpdate(String.format(str, strTable, strFields, strValues));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
	}

	private List<AbsSingleFilter> getMovieInsertFilter(MovieDataEnum data, AbsDataType t) {
		List<AbsSingleFilter> filterList = new ArrayList<AbsSingleFilter>();
		List<String> ls = null;
		switch (data) {
		case MOVIE_COUNTRIES:
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
							DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(), String.valueOf(t.getId())));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
					DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID.getFieldName(), String.valueOf(t.toStringList().get(1))));
			break;
		case MOVIE_AKAS:
			ls = t.toStringList();
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
							DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_ID.getFieldName(), ls.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String, DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
							DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME.getFieldName(), ls.get(1)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_YEAR.getFieldName(), ls.get(2)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_AKA_NAMES.getTableName(),
					DBFieldsEnum.MOVIE_AKA_NAMES_MOVIE_AKA_NAME_COUNTRY.getFieldName(), ls.get(3)));
			break;
		case MOVIE_QUOTES:
			ls = t.toStringList();
			filterList.add(new OracleInsertFilter(FilterOptionEnum.Number, DBTablesEnum.MOVIE_QUOTES.getTableName(),
							DBFieldsEnum.MOVIE_QUOTES_MOVIE_ID.getFieldName(), ls.get(0)));
			filterList.add(new OracleInsertFilter(FilterOptionEnum.String, DBTablesEnum.MOVIE_QUOTES.getTableName(),
							DBFieldsEnum.MOVIE_QUOTES_QUOTE_TEXT.getFieldName(), ls.get(1)));
			break;
		default:
			break;
		}
		return filterList;
	}
}





