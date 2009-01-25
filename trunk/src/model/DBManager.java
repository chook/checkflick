package model;

import java.sql.*;
import java.util.*;
import controller.filter.*;
import controller.entity.*;
import controller.enums.*;

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
	private static String LIMIT_RESULTS_PSTMT = "SELECT * FROM (SELECT bottomLimitTable.*, ROWNUM topLimit FROM (%s) bottomLimitTable WHERE ROWNUM <= %d) WHERE topLimit >= %d";
	
	// Singleton instance
	private static DBManager instance = null;
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
	private DBConnectionPool pool = null;
	
	private OracleFilterManager filters = null;

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
		
		filters = OracleFilterManager.getInstance();
	}

	/**
	 * This is the only function for getting information on the persons/movies
	 * @param data - Which type of entity do we want
	 * @param filter - The single filter to build the query
	 * @return a list of data types of data (encapsulated)
	 */
	public List<AbsType> getAbsDataType(EntityEnum data, AbsSingleFilter filter) {
		return getAbsDataType(data, filter, null);
	}
	public List<AbsType> getAbsDataType(EntityEnum data, AbsSingleFilter filter, AbsSingleFilter filter2) {
		Connection conn = pool.getConnection();
		List<AbsType> retList = new ArrayList<AbsType>();
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
			if(filter2 != null && filter.getTable() != filter2.getTable()) {
				sbQuery.append(", ");
				sbQuery.append(filter.getTable());
			}
			sbQuery.append(" WHERE ");
			sbQuery.append(filter);
			if(filter2 != null) {
				sbQuery.append(" AND ");
				sbQuery.append(filter2);
			}
			
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
	public Map<String, Integer> getAllMovies(int topLimit, int bottomLimit) {
		ResultSet set = null;

		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();
		String genericStr = String.format(SELECT_GENERIC_ORDERED_STMT, DBTablesEnum.MOVIES, DBFieldsEnum.MOVIES_MOVIE_NAME);
		String pstmtStr = String.format(LIMIT_RESULTS_PSTMT, genericStr, bottomLimit, topLimit);
		int tempMovieId, tempMovieDBYear;
		String tempMovieDBName, tempMovieRomanNotation, tempMovieMadeFor, tempMovieYear, tempMovieName;
		StringBuilder movieNameBuilder = null;
		Map<String, Integer> moviesMap = null;
		try {
			pstmt = conn.prepareStatement(pstmtStr);
			set = pstmt.executeQuery();
			set.setFetchSize(1000);
			moviesMap = new HashMap<String, Integer>();
			while (set.next()) {
				if (moviesMap.size() == 0)
					System.out.println("moviesMap.size = 0");
				if (moviesMap.size() == 100)
					System.out.println("moviesMap.size = 100");
				tempMovieId = set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName());
				tempMovieDBName = set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName());
				tempMovieDBYear = set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName());
				tempMovieRomanNotation = set.getString(DBFieldsEnum.MOVIES_MOVIE_ROMAN_NOTATION.getFieldName());
				tempMovieMadeFor = set.getString(DBFieldsEnum.MOVIES_MOVIE_MADE_FOR.getFieldName());
				if (tempMovieDBYear == 0)
					tempMovieYear = "????";
				else
					tempMovieYear = String.valueOf(tempMovieDBYear);
				
				// rebuilding the movie name as it appears on the lists, for comparing
				movieNameBuilder = new StringBuilder(tempMovieDBName);
				movieNameBuilder.append(" (").append(tempMovieYear);
				if (tempMovieRomanNotation != null)
					movieNameBuilder.append("/").append(tempMovieRomanNotation);
				movieNameBuilder.append(")");
				if (tempMovieMadeFor != null)
					movieNameBuilder.append(" (").append(tempMovieMadeFor).append(")");
				tempMovieName = movieNameBuilder.toString();
				moviesMap.put(tempMovieName, tempMovieId);
			}
			set.close();
			pstmt.close();
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		
		pool.returnConnection(conn);
		return moviesMap;
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
	public List<AbsType> getMovieData(MovieDataEnum data, String id) {
		AbsSingleFilter filter = filters.getMovieDataFilter(data, id);
		List<AbsType> list = null;
		switch(data) {
		case MOVIE_AKAS:
			list = getAbsDataType(EntityEnum.GEO_ENTITY, filter);
			break;
		
		case MOVIE_GENRES:
		case MOVIE_LANGUAGES:
		case MOVIE_COUNTRIES:
			list = getAbsDataType(EntityEnum.RELATION, filter);
			break;
		case MOVIE_CRAZY_CREDITS:
		case MOVIE_TRIVIA:
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
			list = getAbsDataType(EntityEnum.CASTING_RELATION, filter);
			break;
		}
		return list;
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
	public List<AbsType> getPersonData(PersonDataEnum data, String id) {
		AbsSingleFilter filter = filters.getPersonDataFilter(data, id);
		List<AbsType> list = null;
		switch(data) {
		case PERSON_AKAS:
		case PERSON_QUOTES:
		case PERSON_TRIVIA:
			list = getAbsDataType(EntityEnum.NAMED_ENTITY, filter);
			break;
		case PERSON_ROLES:
			list = getAbsDataType(EntityEnum.CASTING_RELATION, filter);
			break;
		}
		
		return list;
	}

	/**
	 * Returns a search filter
	 * @param entity - The search option
	 * @param value - First search value
	 * @param value2 - Second search value
	 * @return a filter object
	 */
	public AbsFilter getSearchFilter(SearchEntitiesEnum entity, String value,
			String value2) {
		return filters.getSearchFilter(entity, value, value2);
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
							+ parseClauseFromFilters(arlFilters));
				
				System.out.println("top important: " + set.getFetchSize());
				break;

			case PERSONS:
				if(arlFilters.size() == 0)
					set = s.executeQuery(SEARCH_PERSON_STMT + DBTablesEnum.PERSONS);
				else
					set = s.executeQuery(SEARCH_PERSON_STMT
							+ parseClauseFromFilters(arlFilters));
				break;

			}
			// Executing the query and building the movies array

			while (set.next() == true) {
				if ((result = fillDatedSearchResult(set, tableToSearch)) != null) {
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
	 * This function inserts a movie data into the DB
	 * @param dataType - Which movie data to insert
	 * @param dataObject - The movie data itself
	 * @return
	 */
	public Boolean insertMovieData(MovieDataEnum dataType, AbsType dataObject) {
		List<AbsSingleFilter> filterList = filters.getMovieInsertFilter(dataType, dataObject);
		switch(dataType) {
		case MOVIE_COUNTRIES:
		case MOVIE_QUOTES:
		case MOVIE_GENRES:
			return insertAbsDataType(EntityEnum.NAMED_ENTITY, filterList);
		case MOVIE_CAST:
			return insertAbsDataType(EntityEnum.CASTING_RELATION, filterList);
		}
		return false;
	}
	
	/**
	 * This function inserts a person data into the DB
	 * @param dataType - Which person data to insert
	 * @param dataObject - The person data itself
	 * @return
	 */
	public Boolean insertPersonData(PersonDataEnum dataType, AbsType dataObject) {
		List<AbsSingleFilter> filterList = filters.getPersonInsertFilter(dataType, dataObject);
		switch(dataType) {
		case PERSON_AKAS:
		case PERSON_QUOTES:
		case PERSON_TRIVIA:
			return insertAbsDataType(EntityEnum.NAMED_ENTITY, filterList);
		case PERSON_ROLES:
			return insertAbsDataType(EntityEnum.CASTING_RELATION, filterList);
		}
		return false;
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
		DatedEntity retEntity = null;
		try {
			retEntity = new DatedEntity(set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName()));
			retEntity.setName(set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName()));
			retEntity.setYear(set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName()));
			return retEntity;
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
		DatedEntity retEntity = null;
		try {
			retEntity = new DatedEntity(set.getInt(DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));
			retEntity.setName(set.getString(DBFieldsEnum.PERSONS_PERSON_NAME.getFieldName()));
			retEntity.setYear(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_BIRTH.getFieldName()));
			return retEntity;
		} catch (SQLException e) {
			return null;
		}
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
				if(filter != null) {
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
	 * @param rs - The tuple to parse
	 * @param entity - The type of tuple
	 * @return - The data type
	 */
	private AbsType resultSetToAbsEntity(ResultSet rs, EntityEnum entity) {
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
			case CASTING_RELATION:
				return new CastingRelation(rs.getInt(1), rs.getInt(2), rs.getInt(3),
										   (rs.getString(4).equals("Y") ? true : false), rs.getString(5), rs.getInt(6));
			}
		}
		catch (SQLException e) {
			System.out.println("Error in resultSetToAbsEntity");
		}
		return null;
	}

	/**
	 * Inserts an abstract data type into the database
	 * @param entity - The entity 
	 * @param filterList - The filter list
	 * @return
	 */
	private Boolean insertAbsDataType(EntityEnum entity,
									  List<AbsSingleFilter> filterList) {
		Connection conn = pool.getConnection();
		Statement stmt = null;
		StringBuffer stbQuery = new StringBuffer();
		int i = 0;
		StringBuffer strFields = new StringBuffer();
		StringBuffer strValues = new StringBuffer();
		String strTable = "";
		
		// Trying to get a connection statement
		try {
			stmt = conn.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
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
			stmt.executeUpdate(stbQuery.toString());
		} catch (SQLException e) {
			return false;
		}
		pool.returnConnection(conn);
		return true;
	}
}
