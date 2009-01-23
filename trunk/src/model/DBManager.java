package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import controller.NamedEntitiesEnum;
import controller.NamedRelationsEnum;
import controller.SearchEntitiesEnum;
import controller.SearchRelationsEnum;
import controller.entity.MovieEntity;
import controller.entity.BasicSearchEntity;
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
	private static String SEARCH_MOVIE_STMT = "SELECT MOVIE_ID, MOVIE_NAME, MOVIE_YEAR FROM ";
	private static String SEARCH_PERSON_STMT = "SELECT PERSON_ID, PERSON_NAME, YEAR_OF_BIRTH FROM ";
	private static String SELECT_MOVIE_PSTMT = "SELECT * FROM MOVIES WHERE MOVIE_ID=?";
	private static String SELECT_PERSON_PSTMT = "SELECT * FROM PERSONS WHERE PERSON_ID=?";
	private static String SELECT_GENERIC_STMT = "SELECT * FROM ";
	
	
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
	 * Default Constructor - Initiates the connection pool
	 */
	protected DBManager() {
		pool = DBConnectionPool./*getInstance(
				"jdbc:oracle:thin:@localhost:1521:XE", "chook", "shoochi",
				"oracle.jdbc.OracleDriver", 6);*/
		  getInstance("jdbc:oracle:thin:@localhost:1555:csodb", "chenhare",
		  "Shoochi0", "oracle.jdbc.OracleDriver", 6);
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
	public boolean insertSetToDB(Set<String> set, DBTablesEnum table,
			DBFieldsEnum field) {

		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		String INSERT_SINGLE_DATATYPE = "INSERT INTO " + table.getTableName()
				+ " (" + field.getFieldName() + ") VALUES (?)";

		try {
			pstmt = conn.prepareStatement(INSERT_SINGLE_DATATYPE);

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

	/**
	 * Private method to parse a where clause from the filters
	 * 
	 * @param arlFilters
	 * @return a WHERE clause to use in SELECT statements
	 * @deprecated
	 */
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
				++filterCounter;
				stbFilter.append(filter);
				s.addAll(filter.toTablesSet());

				// Making sure the clause won't end with an AND
				if (filterCounter < arlFilters.size()) {
					stbFilter.append(" AND ");
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
	 * Search movies in the database
	 * 
	 * @param arlFilters
	 *            - A list of filters to prepare the WHERE clause
	 * @return - An array of movies that were fetched from the select
	 * @deprecated
	 */
	public List<BasicSearchEntity> searchMovies(List<Filter> arlFilters) {
		// Variables Declaration
		List<BasicSearchEntity> arlSearchResults = new ArrayList<BasicSearchEntity>();
		BasicSearchEntity result = null;
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

	public List<BasicSearchEntity> search(List<AbsFilter> arlFilters,
			DBTablesEnum tableToSearch) {
		// Variables Declaration
		List<BasicSearchEntity> arlSearchResults = new ArrayList<BasicSearchEntity>();
		BasicSearchEntity result = null;
		ResultSet set = null;
		Statement s = null;
		Connection conn = pool.getConnection();

		try {
			s = conn.createStatement();

			switch (tableToSearch) {
			case MOVIES:
				set = s.executeQuery(SEARCH_MOVIE_STMT
						+ parseWhereClauseFromFiltersNew(arlFilters));
				break;

			case PERSONS:
				set = s.executeQuery(SEARCH_PERSON_STMT
						+ parseWhereClauseFromFiltersNew(arlFilters));
				break;

			}
			// Executing the query and building the movies array

			while (set.next() == true) {
				if ((result = fillSearchResult(set, tableToSearch)) != null) {
					arlSearchResults.add(result);
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
	 * Search persons by filters
	 * 
	 * @param arlFilters
	 * @return List of persons
	 * @deprecated
	 */
	public List<BasicSearchEntity> searchPersons(List<Filter> arlFilters) {
		return null;
	}

	private BasicSearchEntity fillSearchResult(ResultSet set, DBTablesEnum table) {
		switch (table) {
		case MOVIES:
			return fillMovieSearchResult(set);
		case PERSONS:
			return fillPersonSearchResult(set);
		}
		return null;
	}

	private BasicSearchEntity fillMovieSearchResult(ResultSet set) {
		BasicSearchEntity res = null;
		try {
			res = new BasicSearchEntity(set.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName()));
			res.setName(set.getString(DBFieldsEnum.MOVIES_MOVIE_NAME
					.getFieldName()));
			res.setYear(set.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR
					.getFieldName()));
			return res;
		} catch (SQLException e) {
			return null;
		}
	}

	private BasicSearchEntity fillPersonSearchResult(ResultSet set) {
		BasicSearchEntity res = null;
		try {
			res = new BasicSearchEntity(set.getInt(DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));
			res.setName(set.getString(DBFieldsEnum.PERSONS_PERSON_NAME
					.getFieldName()));
			res.setYear(set.getInt(DBFieldsEnum.PERSONS_YEAR_OF_BIRTH
					.getFieldName()));
			return res;
		} catch (SQLException e) {
			return null;
		}
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

	public AbsFilter getFilter(SearchEntitiesEnum entity, String value,
			String value2) {
		AbsFilter filter = null;
		AbsSingleFilter singleFilter = null;
		switch (entity) {
		case PERSON_NAME: {
			filter = new OracleSingleFilter(FilterOptionEnum.String,
					DBTablesEnum.PERSONS.getTableName(), "PERSON_NAME", value);
			break;
		}
		case PERSON_ORIGIN_COUNTRY: {
			singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.PERSONS.getTableName(),
					DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID.getFieldName(),
					value);

			filter = new OracleJoinFilter(singleFilter, DBTablesEnum.PERSONS
					.getTableName(), DBFieldsEnum.PERSONS_COUNTRY_OF_BIRTH_ID
					.getFieldName(), DBTablesEnum.COUNTRIES.getTableName(),
					DBFieldsEnum.COUNTRIES_COUNTRY_ID.getFieldName());

			break;
		}
		case PERSON_AGE: {
			if (value2 == "") {
				singleFilter = new OracleSingleFilter(FilterOptionEnum.Number,
						DBTablesEnum.PERSONS.getTableName(),
						DBFieldsEnum.PERSONS_DATE_OF_BIRTH.getFieldName(),
						value);
			} else {
				singleFilter = new OracleSingleFilter(
						FilterOptionEnum.NumberRange, DBTablesEnum.PERSONS
								.getTableName(),
						DBFieldsEnum.PERSONS_DATE_OF_BIRTH.getFieldName(),
						value + " AND" + value2);
			}

			break;
		}
		}

		return filter;
	}
	
	public AbsSingleFilter getFilter(SearchRelationsEnum entity, String value) {
		AbsSingleFilter filter = null;
		switch (entity) {
		case AKAS:
			break;
		case GOOFS:
			filter = new OracleSingleFilter(FilterOptionEnum.Number,
					DBTablesEnum.MOVIE_GOOFS.getTableName(),
					DBFieldsEnum.MOVIE_GOOFS_MOVIE_ID.getFieldName(), value);
			break;
		}
		return filter;
	}

	public List<NamedEntity> getNamedEntities(NamedEntitiesEnum entity) {
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
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		pool.returnConnection(c);
		return list;
	}

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
				retList.add(new NamedRelation(resultSet.getInt(1), resultSet.getInt(2), resultSet.getString(3)));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		pool.returnConnection(conn);
		return retList;
	}
}
