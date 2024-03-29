package model;

import java.sql.*;
import java.util.*;
import controller.AppData;
import controller.filter.*;
import controller.entity.*;
import controller.enums.*;

/**
 * This class is used to handle all the transactions and connection to the
 * database This class handles the connection through JDBC
 * 
 * @author Chook
 * 
 */
public class DBManager {

	private static String CONNECTION_DRIVER_NAME = "oracle.jdbc.OracleDriver";
	private static String CONNECTION_URI = "jdbc:oracle:thin:@";
	private static OracleFilterManager filters = null;

	private static String INSERT_MOVIE_SINGLE_DATATYPE = "INSERT INTO %s (%s, %s) VALUES (?, ?)";
	// Singleton instance
	private static DBManager instance = null;
	private static String LIMIT_RESULTS_PSTMT = "SELECT * FROM (SELECT bottomLimitTable.*, ROWNUM topLimit FROM (%s) bottomLimitTable WHERE ROWNUM <= %d) WHERE topLimit >= %d";
	// used strings - not to delete
	private static int maxFetches = AppData.getInstance().getMaxFetchFromDB();
	private static DBConnectionPool pool = null;

	private static int RESULTS_FETCH_SIZE = 1000;
	// The strings for prepared statements
	private static String SEARCH_MOVIE_STMT = "SELECT DISTINCT MOVIES.MOVIE_ID, MOVIES.MOVIE_NAME, MOVIES.MOVIE_YEAR FROM ";

	private static String SEARCH_PERSON_STMT = "SELECT DISTINCT PERSONS.PERSON_ID, PERSONS.PERSON_NAME, PERSONS.YEAR_OF_BIRTH FROM ";

	// used strings - not to delete
	private static String SELECT_GENERIC_ORDERED_STMT = "SELECT %s FROM %s ORDER BY %s";

	private static String SELECT_GENERIC_STMT = "SELECT * FROM ";
	private static String SELECT_MOVIE_PSTMT = "SELECT * FROM MOVIES WHERE MOVIE_ID=?";
	private static String SELECT_PERSON_PSTMT = "SELECT * FROM PERSONS WHERE PERSON_ID=?";

	/**
	 * This function retrieves named entities from enum
	 * 
	 * @param entity
	 *            - The entity to retrieve
	 * @return a list of named entities
	 */
	public static List<NamedEntity> getAllNamedEntities(NamedEntitiesEnum entity) {
		List<NamedEntity> list = new ArrayList<NamedEntity>();
		Statement stmt;
		ResultSet resultSet;
		String query = "SELECT * FROM ";
		Connection conn = null;

		// Trying to get a connection statement
		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		switch (entity) {
		case GENRES:
			query += DBTablesEnum.GENRES + " ORDER BY "
					+ DBFieldsEnum.GENRES_GENRE_NAME;
			break;
		case LANGUAGES:
			query += DBTablesEnum.LANGUAGES + " ORDER BY "
					+ DBFieldsEnum.LANGUAGES_LANGUAGE_NAME;
			break;
		case PRODUCTION_ROLES:
			query += DBTablesEnum.PRODUCTION_ROLES + " ORDER BY "
					+ DBFieldsEnum.PRODUCTION_ROLES_PRODUCTION_ROLE_NAME;
			break;
		case COUNTRIES:
			query += DBTablesEnum.COUNTRIES + " ORDER BY "
					+ DBFieldsEnum.COUNTRIES_COUNTRY_NAME;
		}

		// Executing the query and building the movies array
		try {
			resultSet = stmt.executeQuery(query);
			while (resultSet.next() == true) {
				list.add(new NamedEntity(resultSet.getInt(1), resultSet
						.getString(2)));
				if (list.size() > maxFetches)
					break;
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
		return list;
	}

	/**
	 * Singleton
	 * 
	 * @return a DB manager instance
	 */
	public synchronized static DBManager getInstance() {
		if (instance == null) {
			instance = new DBManager();
		}
		return instance;
	}

	protected static DBConnectionPool getPool() {
		return pool;
	}

	protected static void setPool(DBConnectionPool pool) {
		DBManager.pool = pool;
	}

	/**
	 * Default Constructor - Initiates the connection pool
	 */
	protected DBManager() {
		String conURI = CONNECTION_URI + AppData.getInstance().getDbHost()
				+ ":" + AppData.getInstance().getDbPort() + ":"
				+ AppData.getInstance().getDbServer();

		pool = DBConnectionPool.getInstance(conURI, AppData.getInstance()
				.getDbUsername(), AppData.getInstance().getDbPassword(),
				CONNECTION_DRIVER_NAME, AppData.getInstance()
						.getMaxConnections());

		filters = OracleFilterManager.getInstance();
	}

	/**
	 * Deletes a movie
	 * 
	 * @param dataType
	 *            - The movie data type
	 * @param dataObject
	 *            - The object
	 * @return True if delete was successful or False otherwise
	 */
	public boolean deleteMovieEntity(MovieDataEnum dataType, AbsType dataObject) {
		return deleteAbsDataType(filters.getMovieDeleteFilter(dataType,
				dataObject));
	}

	public boolean deletePersonEntity(PersonDataEnum dataType,
			AbsType dataObject) {
		return deleteAbsDataType(filters.getPersonDeleteFilter(dataType,
				dataObject));
	}

	/**
	 * The function receives a hard-coded query and executes it
	 * 
	 * @param query
	 *            the query to be executed
	 */
	public void executeSQL(Connection conn, String query) {

		PreparedStatement pstmt = null;
		String pstmtStr = query;

		System.out.println(pstmtStr);
		try {
			if (conn == null)
				return;
			pstmt = conn.prepareStatement(pstmtStr);
			pstmt.executeQuery();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("Error in executeSQL");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
	}

	public void findAndUpdateDuplicates(Connection conn) {
		ResultSet set = null;
		Set<Relation> duplicatesSet = new LinkedHashSet<Relation>();

		PreparedStatement pstmt = null;
		String pstmtStr = "SELECT * FROM "
				+ DBTablesEnum.PERSONS.getTableName() + " ORDER BY "
				+ DBFieldsEnum.PERSONS_PERSON_NAME.getFieldName() + ", "
				+ DBFieldsEnum.PERSONS_PERSON_ID.getFieldName();
		System.out.println(pstmtStr);
		try {
			if (conn == null)
				return;
			pstmt = conn.prepareStatement(pstmtStr);
			set = pstmt.executeQuery();
			set.setFetchSize(RESULTS_FETCH_SIZE);

			String tempPrevPersonName = null;
			String tempCurrPersonName = null;
			int tempPersonId = 0;
			// insert primary data before iterating over the results
			if (set.next()) {
				tempPrevPersonName = set
						.getString(DBFieldsEnum.PERSONS_PERSON_NAME
								.getFieldName());
				tempPersonId = set.getInt(DBFieldsEnum.PERSONS_PERSON_ID
						.getFieldName());
			}
			// for each result, check if the person's name is the same as the
			// previous result
			// if it is, add it to the duplicatesSet
			while (set.next()) {
				tempCurrPersonName = set
						.getString(DBFieldsEnum.PERSONS_PERSON_NAME
								.getFieldName());
				// if a duplicate was found
				if (tempPrevPersonName.equals(tempCurrPersonName)) {
					// the structure of the Relation that is added to the
					// duplicatesSet is:
					// Relation(originalPersonId, currentPersonId)
					duplicatesSet.add(new Relation(tempPersonId, set
							.getInt(DBFieldsEnum.PERSONS_PERSON_ID
									.getFieldName())));
				} else {
					// if a duplicate wasn't found
					tempPrevPersonName = tempCurrPersonName;
					tempPersonId = set.getInt(DBFieldsEnum.PERSONS_PERSON_ID
							.getFieldName());
				}
				// flush results every BATCH_SIZE
				if ((duplicatesSet.size() > 0)
						&& (duplicatesSet.size()
								% DataImporter.PSTMT_BATCH_SIZE == 0)) {
					System.out.println("DUPLICATES: updating "
							+ duplicatesSet.size() + " elements to the DB");
					updateDuplicates(conn, duplicatesSet);
					duplicatesSet.clear();
				}
			}
			// flush the results that were left
			if ((duplicatesSet.size() > 0)) {
				System.out.println("DUPLICATES: updating "
						+ duplicatesSet.size() + " elements to the DB");
				updateDuplicates(conn, duplicatesSet);
				duplicatesSet.clear();
			}
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}

	}

	/**
	 * This is the only function for getting information on the persons/movies
	 * 
	 * @param data
	 *            - Which type of entity do we want
	 * @param filter
	 *            - The single filter to build the query
	 * @return a list of data types of data (encapsulated)
	 */
	public List<AbsType> getAbsDataType(EntityEnum data, AbsFilter filter) {
		return getAbsDataType(data, filter, null);
	}

	public List<AbsType> getAbsDataType(EntityEnum data, AbsFilter filter,
			AbsFilter filter2) {
		Connection conn = null;
		List<AbsType> retList = new ArrayList<AbsType>();
		Statement stmt;
		ResultSet resultSet;
		Set<String> tablesSet = null;
		int i = 0;
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append(SELECT_GENERIC_STMT);

		// Trying to get a connection statement
		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

		// Executing the query and building the movies array
		try {
			tablesSet = filter.toTablesSet();
			for (String s : tablesSet) {
				++i;
				sbQuery.append(s);
				if (i < tablesSet.size()) {
					sbQuery.append(", ");
				}
			}

			sbQuery.append(" WHERE ");
			sbQuery.append(filter);
			if (filter2 != null) {
				sbQuery.append(" AND ");
				sbQuery.append(filter2);
			}

			resultSet = stmt.executeQuery(sbQuery.toString());
			while (resultSet.next() == true) {
				retList.add(resultSetToAbsEntity(resultSet, data));
				if (retList.size() > maxFetches)
					break;
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		pool.returnConnection(conn);
		return retList;
	}

	/**
	 * returns a map of movies selected from the DB, that were limited by (top,
	 * bottom) limit on the SELECT results that are sent are ordered beforehand
	 * by MOVIE_ID Note: there is no way to return the whole movies list, and
	 * you must specify a bottomLimit (memory reasons) Note: the rows starts
	 * with 1 and not 0 (meaning, topLimit = 0 returns exactly what topLimit = 1
	 * returns)
	 * 
	 * @param topLimit
	 *            the start of the limit (the beginning ROWNUM of the ResultSet
	 *            returned)
	 * @param bottomLimit
	 *            the end of the limit (the ending ROWNUM of the ResultSet
	 *            returned)
	 * @return
	 */
	public Map<String, Integer> getAllMovies(int topLimit, int bottomLimit) {
		ResultSet set = null;
		int tempMovieId;
		String tempMovieName;
		Map<String, Integer> moviesMap = null;

		PreparedStatement pstmt = null;
		Connection conn = null;

		// creating the generic statement that contains the table and field
		// names
		String genericStr = String.format(SELECT_GENERIC_ORDERED_STMT, "*",
				DBTablesEnum.MOVIES, DBFieldsEnum.MOVIES_MOVIE_ID);
		// creating the prepared statement template including the ROWNUM limits
		// for the SELECT
		String pstmtStr = String.format(LIMIT_RESULTS_PSTMT, genericStr,
				bottomLimit, topLimit);

		System.out.println("getting " + (bottomLimit - topLimit + 1)
				+ " movies from the DB");
		System.out.println(pstmtStr);

		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			pstmt = conn.prepareStatement(pstmtStr);
			set = pstmt.executeQuery();
			set.setFetchSize(RESULTS_FETCH_SIZE);
			moviesMap = new HashMap<String, Integer>();

			// going over the movies retrieved from the DB and adding the full
			// movie name to the map for comparison
			while (set.next()) {
				// retrieving the different fields
				tempMovieId = set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID
						.getFieldName());
				tempMovieName = set
						.getString(DBFieldsEnum.MOVIES_TEMP_MOVIE_NAME
								.getFieldName());
				moviesMap.put(tempMovieName, tempMovieId);
			}
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}

		System.out.println("finished getting " + (bottomLimit - topLimit + 1)
				+ " movies from the DB");
		pool.returnConnection(conn);
		return moviesMap;
	}

	/**
	 * returns a map of persons selected from the DB, that were limited by (top,
	 * bottom) limit on the SELECT results that are sent are ordered beforehand
	 * by MOVIE_ID Note: there is no way to return the whole movies list, and
	 * you must specify a bottomLimit (memory reasons) Note: the rows starts
	 * with 1 and not 0 (meaning, topLimit = 0 returns exactly what topLimit = 1
	 * returns)
	 * 
	 * @param topLimit
	 *            the start of the limit (the beginning ROWNUM of the ResultSet
	 *            returned)
	 * @param bottomLimit
	 *            the end of the limit (the ending ROWNUM of the ResultSet
	 *            returned)
	 * @return
	 */
	public Map<String, Integer> getAllPersons(int topLimit, int bottomLimit) {
		ResultSet set = null;

		PreparedStatement pstmt = null;
		Connection conn = null;
		// creating the generic statement that contains the table and field
		// names
		String fields = DBFieldsEnum.PERSONS_PERSON_ID + ","
				+ DBFieldsEnum.PERSONS_PERSON_NAME;
		String genericStr = String.format(SELECT_GENERIC_ORDERED_STMT, fields,
				DBTablesEnum.PERSONS, DBFieldsEnum.PERSONS_PERSON_NAME);
		// creating the prepared statement template including the ROWNUM limits
		// for the SELECT
		String pstmtStr = String.format(LIMIT_RESULTS_PSTMT, genericStr,
				bottomLimit, topLimit);

		int tempPersonId = 0;
		String tempPersonName = null;
		Map<String, Integer> personsMap = null;
		System.out.println(pstmtStr);
		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			pstmt = conn.prepareStatement(pstmtStr);
			set = pstmt.executeQuery();
			set.setFetchSize(1000);
			personsMap = new HashMap<String, Integer>();
			// adding the persons retrieved to the map
			while (set.next()) {
				// retrieving the different fields
				tempPersonId = set.getInt(DBFieldsEnum.PERSONS_PERSON_ID
						.getFieldName());
				tempPersonName = set.getString(DBFieldsEnum.PERSONS_PERSON_NAME
						.getFieldName());
				// inserting the full movie name into the moviesMap
				personsMap.put(tempPersonName, tempPersonId);
			}
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
			System.out.println("stopped on this record - " + tempPersonId
					+ " / " + tempPersonName);
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}

		pool.returnConnection(conn);
		return personsMap;
	}

	/**
	 * returns a map of persons selected from the DB, that were limited by (top,
	 * bottom) limit on the SELECT results that are sent are ordered beforehand
	 * by MOVIE_ID Note: there is no way to return the whole movies list, and
	 * you must specify a bottomLimit (memory reasons) Note: the rows starts
	 * with 1 and not 0 (meaning, topLimit = 0 returns exactly what topLimit = 1
	 * returns)
	 * 
	 * @param topLimit
	 *            the start of the limit (the beginning ROWNUM of the ResultSet
	 *            returned)
	 * @param bottomLimit
	 *            the end of the limit (the ending ROWNUM of the ResultSet
	 *            returned)
	 * @return
	 */
	public Relation[] getAllPersonsAndLineNumbersArray(int topLimit,
			int bottomLimit, int bucketSize) {
		ResultSet set = null;
		Relation[] personsArray = new Relation[bucketSize + 1]; // if the bucket
																// is full, then
																// the last cell
																// is null as a
																// delimiter
		int arrayIndex = 0;

		System.out.println("getting " + bucketSize + " persons from the DB");
		PreparedStatement pstmt = null;
		Connection conn = null;
		// creating the generic statement that contains the table and field
		// names
		String fields = "TEMP_PERSON_ID,"
				+ DBFieldsEnum.PERSONS_TEMP_PERSON_LINE_NUMBER;
		String genericStr = String.format(SELECT_GENERIC_ORDERED_STMT, fields,
				DBTablesEnum.PERSONS, DBFieldsEnum.PERSONS_PERSON_ID);
		// creating the prepared statement template including the ROWNUM limits
		// for the SELECT
		String pstmtStr = String.format(LIMIT_RESULTS_PSTMT, genericStr,
				bottomLimit, topLimit);

		int tempPersonId = 0;
		int tempPersonLineNumber = 0;
		System.out.println(pstmtStr);
		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			pstmt = conn.prepareStatement(pstmtStr);
			set = pstmt.executeQuery();
			set.setFetchSize(RESULTS_FETCH_SIZE);
			// adding the persons retrieved to the array
			while (set.next()) {
				// retrieving the different fields
				tempPersonId = set.getInt(DBFieldsEnum.PERSONS_TEMP_PERSON_ID
						.getFieldName());
				tempPersonLineNumber = set
						.getInt(DBFieldsEnum.PERSONS_TEMP_PERSON_LINE_NUMBER
								.getFieldName());
				// inserting the full movie name into the moviesMap
				personsArray[arrayIndex] = new Relation(tempPersonId,
						tempPersonLineNumber);
				++arrayIndex;

				// alive check
				if (arrayIndex > 0 && arrayIndex % 10000 == 0)
					System.out.println("- already entered " + arrayIndex
							+ " elements to the personsArray");
			}
			// if the number of elements is smaller than the bucket size, enter
			// in the following column NULL,
			// for the program to know that we have reached the end of the array
			if (arrayIndex < bucketSize)
				personsArray[arrayIndex] = null;
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}

		pool.returnConnection(conn);
		System.out.println("finished getting " + bucketSize
				+ " persons from the DB");
		return personsArray;
	}

	/**
	 * Get movie by Id
	 * 
	 * @param id
	 *            - the movie id
	 * @return a movie entity
	 */
	public MovieEntity getMovieById(int id) {
		MovieEntity tempMovie = null;
		ResultSet resultSet = null;

		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			pstmt = conn.prepareStatement(SELECT_MOVIE_PSTMT);
			pstmt.setInt(1, id);

			// Executing the query and building the movies array
			resultSet = pstmt.executeQuery();
			if (resultSet.next() == true) {
				tempMovie = fillMovieFromSet(resultSet);
			}
			resultSet.close();
			pstmt.close();
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
	 * 
	 * @param data
	 *            - The Data
	 * @param id
	 *            - The movie id
	 * @return the list of data types
	 */
	public List<AbsType> getMovieData(MovieDataEnum data, String id) {
		AbsFilter filter = filters.getMovieDataFilter(data, id);
		List<AbsType> list = null;
		switch (data) {
		case MOVIE_GENRES:
		case MOVIE_LANGUAGES:
		case MOVIE_COUNTRIES:
			list = getAbsDataType(EntityEnum.RELATION, filter);
			break;
		case MOVIE_QUOTES:
			list = getAbsDataType(EntityEnum.NAMED_ENTITY, filter);
			break;
		case MOVIE_CAST:
			list = getAbsDataType(EntityEnum.NAMED_CASTING_RELATION, filter);
			break;
		case MOVIE:
			list = getAbsDataType(EntityEnum.MOVIE_ENTITY, filter);
			break;
		}
		return list;
	}

	/**
	 * Get a person by an id
	 * 
	 * @param id
	 *            - The person id
	 * @return A person entity
	 */
	public PersonEntity getPersonById(int id) {
		PersonEntity tempMovie = null;
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		Connection conn = null;

		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			pstmt = conn.prepareStatement(SELECT_PERSON_PSTMT);
			pstmt.setInt(1, id);

			// Executing the query and building the movies array
			resultSet = pstmt.executeQuery();
			if (resultSet.next() == true) {
				tempMovie = fillPersonFromSet(resultSet);
			}
			resultSet.close();
			pstmt.close();
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
	 * 
	 * @param data
	 *            - The data option
	 * @param id
	 *            - The person id
	 * @return the list of data type
	 */
	public List<AbsType> getPersonData(PersonDataEnum data, String id) {
		AbsFilter filter = filters.getPersonDataFilter(data, id);
		List<AbsType> list = null;
		switch (data) {
		case PERSON_AKAS:
		case PERSON_QUOTES:
		case PERSON_TRIVIA:
			list = getAbsDataType(EntityEnum.NAMED_ENTITY, filter);
			break;
		case PERSON_ROLES:
			list = getAbsDataType(EntityEnum.NAMED_CASTING_RELATION, filter);
			break;
		case PERSON:
			list = getAbsDataType(EntityEnum.PERSON_ENTITY, filter);
		}

		return list;
	}

	/**
	 * Returns a search filter
	 * 
	 * @param entity
	 *            - The search option
	 * @param value
	 *            - First search value
	 * @param value2
	 *            - Second search value
	 * @return a filter object
	 */
	public AbsFilter getSearchFilter(SearchEntitiesEnum entity, String value,
			String value2) {
		return filters.getSearchFilter(entity, value, value2);
	}

	/**
	 * This function inserts a movie data into the DB
	 * 
	 * @param dataType
	 *            - Which movie data to insert
	 * @param dataObject
	 *            - The movie data itself
	 * @return
	 */
	public int insertMovieData(MovieDataEnum dataType, AbsType dataObject,
			boolean update) {
		List<AbsSingleFilter> filterList = filters.getMovieInsertFilter(
				dataType, dataObject);
		EntityEnum entity = null;
		switch (dataType) {
		case MOVIE_QUOTES:
			entity = EntityEnum.NAMED_ENTITY;
			break;
		case MOVIE_CAST:
			entity = EntityEnum.CASTING_RELATION;
			break;
		case MOVIE:
			entity = EntityEnum.MOVIE_ENTITY;
			break;
		case MOVIE_LANGUAGES:
		case MOVIE_GENRES:
		case MOVIE_COUNTRIES:
			entity = EntityEnum.RELATION;
			break;
		}
		if (update) {
			return updateAbsDataType(entity, filterList);
		} else {
			return insertAbsDataType(entity, filterList);
		}
	}

	/**
	 * This function receives a set of NamedEntities, and a definition of a
	 * table, and adds all the values to the DB with one PreparedStatementBatch
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
	public boolean insertMovieSingleDataTypeSetToDB(Connection conn,
			Set<NamedRelation> set, DBTablesEnum table, DBFieldsEnum field1,
			DBFieldsEnum field2) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		String statementStr;

		statementStr = String.format(INSERT_MOVIE_SINGLE_DATATYPE, table
				.getTableName(), field1.getFieldName(), field2.getFieldName());

		try {
			if (conn == null)
				return false;
			pstmt = conn.prepareStatement(statementStr);

			for (NamedRelation setNamedRelation : set) {
				pstmt.setInt(1, setNamedRelation.getId());
				pstmt.setInt(2, setNamedRelation.getSecondaryId());
				pstmt.addBatch();
			}
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		bReturn = executePreparedStatementBatch(pstmt);
		return bReturn;
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
	public boolean insertMoviesSetToDB(Connection conn, Set<MovieEntity> set) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		try {
			if (conn == null)
				return false;
			pstmt = conn
					.prepareStatement(String
							.format(
									"INSERT INTO %s (%s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?)",
									DBTablesEnum.MOVIES.getTableName(),
									DBFieldsEnum.MOVIES_MOVIE_NAME
											.getFieldName(),
									DBFieldsEnum.MOVIES_MOVIE_YEAR
											.getFieldName(),
									DBFieldsEnum.MOVIES_MOVIE_ROMAN_NOTATION
											.getFieldName(),
									DBFieldsEnum.MOVIES_MOVIE_MADE_FOR
											.getFieldName(),
									DBFieldsEnum.MOVIES_TEMP_MOVIE_NAME
											.getFieldName()));

			for (MovieEntity setMovie : set) {
				pstmt.setString(1, setMovie.getRawName());
				pstmt.setInt(2, setMovie.getYear());
				pstmt.setString(3, setMovie.getRomanNotation());
				pstmt.setString(4, setMovie.getMadeFor());
				pstmt.setString(5, setMovie.getFullMovieName());
				pstmt.addBatch();
			}

			bReturn = executePreparedStatementBatch(pstmt);
			pstmt.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bReturn;
	}

	/**
	 * This function inserts a person data into the DB
	 * 
	 * @param dataType
	 *            - Which person data to insert
	 * @param dataObject
	 *            - The person data itself
	 * @return
	 */
	public int insertPersonData(PersonDataEnum dataType, AbsType dataObject,
			boolean update) {
		List<AbsSingleFilter> filterList = filters.getPersonInsertFilter(
				dataType, dataObject);
		EntityEnum entity = null;
		switch (dataType) {
		case PERSON_AKAS:
		case PERSON_QUOTES:
		case PERSON_TRIVIA:
			entity = EntityEnum.NAMED_ENTITY;
			break;
		case PERSON_ROLES:
			entity = EntityEnum.CASTING_RELATION;
			break;
		case PERSON:
			entity = EntityEnum.PERSON_ENTITY;
			break;
		}
		if (update) {
			return updateAbsDataType(entity, filterList);
		} else {
			return insertAbsDataType(entity, filterList);
		}
	}

	public boolean insertPersonMovieCreditsSetToDB(Connection conn,
			Set<CastingRelation> set) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		String statementStr;
		statementStr = String
				.format(
						"INSERT INTO %s (%s, %s, %s, %s, %s, %s) VALUES (?, ?, ?, ?, ?, ?)",
						DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
						DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID
								.getFieldName(),
						DBFieldsEnum.PERSON_MOVIE_CREDITS_MOVIE_ID
								.getFieldName(),
						DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID
								.getFieldName(),
						DBFieldsEnum.PERSON_MOVIE_CREDITS_IS_ACTOR
								.getFieldName(),
						DBFieldsEnum.PERSON_MOVIE_CREDITS_ACTOR_ROLE
								.getFieldName(),
						DBFieldsEnum.PERSON_MOVIE_CREDITS_ACTOR_CREDITS_RANK
								.getFieldName());
		System.out.println(statementStr);
		try {
			if (conn == null)
				return false;
			pstmt = conn.prepareStatement(statementStr);

			for (CastingRelation setRelation : set) {
				pstmt.setInt(1, setRelation.getId());
				pstmt.setInt(2, setRelation.getSecondaryId());
				pstmt.setInt(3, setRelation.getType());
				if (setRelation.isActor())
					pstmt.setString(4, "Y");
				else
					pstmt.setString(4, "N");
				pstmt.setString(5, setRelation.getActorRole());
				pstmt.setInt(6, setRelation.getActorCreditRank());
				pstmt.addBatch();
			}
			bReturn = executePreparedStatementBatch(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bReturn;
	}

	public boolean insertPersonsSetToDB(Connection conn, Set<NamedEntity> set) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		String statementStr;
		statementStr = String.format("INSERT INTO %s (%s, %s) VALUES (?, ?)",
				DBTablesEnum.PERSONS.getTableName(),
				DBFieldsEnum.PERSONS_PERSON_NAME.getFieldName(),
				DBFieldsEnum.PERSONS_TEMP_PERSON_LINE_NUMBER.getFieldName());
		try {
			if (conn == null)
				return false;
			pstmt = conn.prepareStatement(statementStr);

			for (NamedEntity setEntity : set) {
				pstmt.setString(1, setEntity.getName());
				pstmt.setInt(2, setEntity.getId());
				pstmt.addBatch();
			}

			bReturn = executePreparedStatementBatch(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return bReturn;
	}

	/**
	 * This function receives a set of values, and a definition of a table, and
	 * adds all the values to the DB with one PreparedStatementBatch This refers
	 * to the LANGUAGES, COUNTRIES & GENRES tables
	 * 
	 * @param set
	 *            - The set of values to add to the DB
	 * @param table
	 *            - The name of the table
	 * @param field
	 *            - The name of the Value_name field
	 * 
	 **/
	public boolean insertSingleDataTypeSetToDB(Connection conn,
			Set<String> set, DBTablesEnum table, DBFieldsEnum field) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;

		try {
			if (conn == null)
				return false;
			pstmt = conn.prepareStatement(String.format(
					"INSERT INTO %s (%s) VALUES (?)", table.getTableName(),
					field.getFieldName()));
			for (Object setObject : set) {
				pstmt.setString(1, setObject.toString());
				pstmt.addBatch();
			}
			bReturn = executePreparedStatementBatch(pstmt);
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}

		return bReturn;
	}

	/**
	 * The search function - searches movies/persons
	 * 
	 * @param arlFilters
	 *            - List of filters
	 * @param tableToSearch
	 *            - The table to search no need to send it as an extra parameter
	 *            (Chen 24.01.09)
	 * @return List of search objects
	 */
	public List<DatedEntity> search(List<AbsFilter> arlFilters,
			DBTablesEnum tableToSearch) {
		// Variables Declaration
		List<DatedEntity> arlSearchResults = new ArrayList<DatedEntity>();
		DatedEntity result = null;
		ResultSet resultSet = null;
		Statement stmt = null;
		Connection conn = null;

		try {
			conn = pool.getConnection();
			if (conn == null)
				return null;
			stmt = conn.createStatement();
			switch (tableToSearch) {
			case MOVIES:

				if (arlFilters.size() == 0)
					resultSet = stmt.executeQuery(SEARCH_MOVIE_STMT
							+ DBTablesEnum.MOVIES);
				else
					resultSet = stmt.executeQuery(SEARCH_MOVIE_STMT
							+ parseClauseFromFilters(arlFilters));

				System.out
						.println("top important: " + resultSet.getFetchSize());
				break;

			case PERSONS:
				if (arlFilters.size() == 0)
					resultSet = stmt.executeQuery(SEARCH_PERSON_STMT
							+ DBTablesEnum.PERSONS);
				else
					resultSet = stmt.executeQuery(SEARCH_PERSON_STMT
							+ parseClauseFromFilters(arlFilters));
				break;

			}
			// Executing the query and building the movies array

			while (resultSet.next() == true) {
				if ((result = fillDatedSearchResult(resultSet, tableToSearch)) != null) {
					arlSearchResults.add(result);
					if (arlSearchResults.size() > maxFetches)
						break;
				}
			}
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			System.out.println("Error in searchMovies " + e.toString());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		pool.returnConnection(conn);
		return arlSearchResults;
	}

	public void updateDuplicates(Connection conn, Set<Relation> duplicatesSet) {

		PreparedStatement pstmt = null;
		String pstmtStr = "UPDATE " + DBTablesEnum.PERSONS.getTableName()
				+ " SET "
				+ DBFieldsEnum.PERSONS_TEMP_IS_DUPLICATE.getFieldName()
				+ " = 'Y', "
				+ DBFieldsEnum.PERSONS_TEMP_PERSON_ID.getFieldName() + " = ? "
				+ "WHERE " + DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()
				+ " = ?";

		System.out.println("updating duplicates");
		try {
			if (conn == null)
				return;
			pstmt = conn.prepareStatement(pstmtStr);

			for (Relation setEntity : duplicatesSet) {
				pstmt.setInt(1, setEntity.getId());
				pstmt.setInt(2, setEntity.getSecondaryId());
				pstmt.addBatch();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("executing updateDuplicates batch");
		executePreparedStatementBatch(pstmt);
	}

	private boolean deleteAbsDataType(List<AbsSingleFilter> filterList) {
		Connection conn = null;
		Statement stmt = null;
		StringBuffer stbQuery = new StringBuffer();
		int i = 0;

		// Trying to get a connection statement
		try {
			conn = pool.getConnection();
			if (conn == null)
				return false;
			stmt = conn.createStatement();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Executing the query and building the movies array
		try {
			stbQuery.append("DELETE FROM ");
			for (AbsSingleFilter filter : filterList) {
				++i;
				if (i == 1) {
					stbQuery.append(filter.getTable());
					stbQuery.append(" WHERE ");
				}
				stbQuery.append(filter.getColumn());
				stbQuery.append("=");
				stbQuery.append(filter.getValue());

				if (i < filterList.size()) {
					stbQuery.append(" AND ");
				}
			}
			stmt.executeUpdate(stbQuery.toString());
			stmt.close();
		} catch (SQLException e) {
			return false;
		}
		pool.returnConnection(conn);
		return true;
	}

	/**
	 * executes the executePreparedStatementBatch
	 */
	private boolean executePreparedStatementBatch(PreparedStatement pstmt) {
		try {
			pstmt.executeBatch();

			// closing
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("ERROR executeUpdate - " + e.toString());
			java.lang.System.exit(0);
			return false;
		}
		return true;
	}

	private DatedEntity fillDatedSearchResult(ResultSet set, DBTablesEnum table) {
		switch (table) {
		case MOVIES:
			return fillMovieSearchResult(set);
		case PERSONS:
			return fillPersonSearchResult(set);
		}
		return null;
	}

	private MovieEntity fillMovieFromSet(ResultSet set) {
		MovieEntity movie = null;
		try {
			movie = new MovieEntity();
			movie
					.setId(set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID
							.getFieldName()));
			movie.setName(set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME
					.getFieldName()));
			movie.setYear(set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR
					.getFieldName()));
			movie.setRunningTime(set
					.getInt(DBFieldsEnum.MOVIES_MOVIE_RUNNING_TIME
							.getFieldName()));
			movie.setPlot(set.getString(DBFieldsEnum.MOVIES_MOVIE_PLOT_TEXT
					.getFieldName()));
		} catch (SQLException e) {
			System.out.println("SQLException error");
			return null;
		}
		return movie;
	}

	private DatedEntity fillMovieSearchResult(ResultSet set) {
		DatedEntity retEntity = null;
		try {
			retEntity = new DatedEntity(set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID
					.getFieldName()));
			retEntity.setName(set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME
					.getFieldName()));
			retEntity.setYear(set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR
					.getFieldName()));
			return retEntity;
		} catch (SQLException e) {
			return null;
		}
	}

	private PersonEntity fillPersonFromSet(ResultSet set) {
		PersonEntity person = null;
		try {
			person = new PersonEntity();
			person.setId(set.getInt(DBFieldsEnum.PERSONS_PERSON_ID
					.getFieldName()));
			person.setName(set.getString(DBFieldsEnum.PERSONS_PERSON_NAME
					.getFieldName()));
			person.setYearOfBirth(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_BIRTH
					.getFieldName()));
			person.setCityOfBirth(set
					.getString(DBFieldsEnum.PERSONS_CITY_OF_BIRTH
							.getFieldName()));
			person.setCountryOfBirth(set
					.getInt(DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID
							.getFieldName()));
			person.setYearOfDeath(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_DEATH
					.getFieldName()));
		} catch (SQLException e) {
			System.out.println("SQLException error");
			return null;
		}
		return person;
	}

	private DatedEntity fillPersonSearchResult(ResultSet set) {
		DatedEntity retEntity = null;
		try {
			retEntity = new DatedEntity(set
					.getInt(DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));
			retEntity.setName(set.getString(DBFieldsEnum.PERSONS_PERSON_NAME
					.getFieldName()));
			retEntity.setYear(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_BIRTH
					.getFieldName()));
			return retEntity;
		} catch (SQLException e) {
			return null;
		}
	}

	/**
	 * Inserts an abstract data type into the database
	 * 
	 * @param entity
	 *            - The entity
	 * @param filterList
	 *            - The filter list
	 * @return
	 */
	private int insertAbsDataType(EntityEnum entity,
			List<AbsSingleFilter> filterList) {
		Connection conn = null;
		Statement stmt = null;
		StringBuffer stbQuery = new StringBuffer();
		int i = 0;
		StringBuffer strFields = new StringBuffer();
		StringBuffer strValues = new StringBuffer();
		String strTable = "";
		ResultSet resultSet = null;
		int returnValue = 0;

		// Trying to get a connection statement
		try {
			conn = pool.getConnection();
			if (conn == null)
				return -2;
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			return -2;
		}

		// Executing the query and building the movies array
		try {
			stbQuery.append("INSERT INTO ");
			for (AbsSingleFilter filter : filterList) {
				++i;
				strFields.append(filter.getColumn());
				strValues.append(filter.getValue());
				if (i < filterList.size()) {
					strFields.append(", ");
					strValues.append(", ");
				} else {
					strTable = filter.getTable();
				}
			}

			stbQuery.append(strTable);
			stbQuery.append("(").append(strFields).append(")");
			stbQuery.append(" VALUES ");
			stbQuery.append("(").append(strValues).append(")");
			stmt.executeUpdate(stbQuery.toString(), new String[] { filterList
					.get(0).getColumn() });
			resultSet = stmt.getGeneratedKeys();
			resultSet.next();
			returnValue = resultSet.getInt(1);
			resultSet.close();
			stmt.close();
		} catch (SQLException e) {
			try {
				// If the return of the key failed, send the statement without
				// return value and return 0
				stmt.executeUpdate(stbQuery.toString());
				stmt.close();
			} catch (SQLException e2) {
				returnValue = -1;
			}
		}
		pool.returnConnection(conn);
		return returnValue;
	}

	/**
	 * Private method to parse a where clause from the filters
	 * 
	 * @param arlFilters
	 * @return a WHERE clause to use in SELECT statements
	 */
	private String parseClauseFromFilters(List<AbsFilter> arlFilters) {
		StringBuilder stbFilter = new StringBuilder();
		StringBuilder fromClause = new StringBuilder();
		int i = 0;
		int filterCounter = 0;

		// Get the tables names we need
		Set<String> tablesSet = new HashSet<String>();

		// Building the WHERE clause
		if (arlFilters.size() > 0) {
			stbFilter.append(" WHERE ");
			for (AbsFilter filter : arlFilters) {
				if (filter != null) {
					++filterCounter;
					// Making sure the clause won't end with an AND
					if (filterCounter != 1) {
						stbFilter.append(" AND ");
					}
					stbFilter.append(filter);
					tablesSet.addAll(filter.toTablesSet());
				}
			}
		}

		for (String tableName : tablesSet) {
			++i;
			fromClause.append(tableName);
			if (i < tablesSet.size())
				fromClause.append(", ");
		}
		fromClause.append(stbFilter);
		return fromClause.toString();
	}

	/**
	 * This function is a factory for data types
	 * 
	 * @param rs
	 *            - The tuple to parse
	 * @param entity
	 *            - The type of tuple
	 * @return - The data type
	 */
	private AbsType resultSetToAbsEntity(ResultSet rs, EntityEnum entity) {
		try {
			switch (entity) {
			case NAMED_ENTITY:
				return new NamedEntity(rs.getInt(1), rs.getString(2));
			case GEO_ENTITY:
				return new GeoEntity(rs.getInt(1), rs.getString(2), rs
						.getInt(3), rs.getInt(4));
			case NAMED_RELATION:
				return new NamedRelation(rs.getInt(1), rs.getInt(2), rs
						.getString(3));
			case CATEGORIZED_RELATION:
				return new CategorizedRelation(rs.getInt(1), rs.getInt(2), rs
						.getInt(3));
			case CASTING_RELATION:
				return new CastingRelation(rs.getInt(1), rs.getInt(2), rs
						.getInt(3),
						(rs.getString(4).equals("Y") ? true : false), rs
								.getString(5), rs.getInt(6));
			case NAMED_CASTING_RELATION:
				return new NamedCastingRelation(rs.getInt(1), rs.getInt(2), rs
						.getInt(3),
						(rs.getString(4).equals("Y") ? true : false), rs
								.getString(5), rs.getInt(6), rs.getString(8));
			case RELATION:
				return new Relation(rs.getInt(1), rs.getInt(2));
			case MOVIE_ENTITY:
				return fillMovieFromSet(rs);
			case PERSON_ENTITY:
				return fillPersonFromSet(rs);
			}
		} catch (SQLException e) {
			System.out.println("Error in resultSetToAbsEntity");
		}
		return null;
	}

	/**
	 * This function updates an abstract data type in the database
	 * 
	 * @param entity
	 *            - The entity to update
	 * @param filterList
	 *            - The filter list to use
	 * @return - Return 1 if update was successful, 0 or -1 otherwise
	 */
	private int updateAbsDataType(EntityEnum entity,
			List<AbsSingleFilter> filterList) {
		Connection conn = null;
		Statement stmt = null;
		StringBuffer stbQuery = new StringBuffer();
		StringBuffer stbWhere = new StringBuffer();
		int i = 0;

		// Trying to get a connection statement
		try {
			conn = pool.getConnection();
			if (conn == null)
				return -2;
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		// Executing the query and building the movies array
		try {
			stbQuery.append("UPDATE ");
			for (AbsSingleFilter filter : filterList) {
				++i;
				if (i == 1) {
					stbQuery.append(filter.getTable());
					stbQuery.append(" SET ");
					stbWhere.append(" WHERE ");
					stbWhere.append(filter.getColumn());
					stbWhere.append("=");
					stbWhere.append(filter.getValue());
				}
				stbQuery.append(filter.getColumn());
				stbQuery.append("=");
				stbQuery.append(filter.getValue());

				if (i < filterList.size()) {
					stbQuery.append(",");
				}
			}
			stbQuery.append(stbWhere);
			stmt.executeUpdate(stbQuery.toString());
			stmt.close();
		} catch (SQLException e) {
			return -1;
		}
		pool.returnConnection(conn);
		return 1;
	}
}
