package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import controller.SearchEntitiesEnum;
import controller.entity.Movie;
import controller.entity.BasicSearchEntity;
import controller.filter.AbsFilter;
import controller.filter.AbsSingleFilter;
import controller.filter.Filter;
import controller.filter.FilterOptionEnum;

public class DBManager {
	// The strings for prepared statements
	private static String INSERT_MOVIE_PSTMT = "INSERT INTO MOVIES(fname,lname) VALUES(?,?)";
	private static String UPDATE_MOVIE_PSTMT = "UPDATE MOVIES SET ? WHERE MOVIE_ID=?";
	private static String DELETE_MOVIE_PSTMT = "DELETE FROM MOVIES WHERE MOVIE_ID=?";
	private static String SEARCH_MOVIE_STMT = "SELECT MOVIE_ID, MOVIE_NAME, MOVIE_YEAR FROM MOVIES ";
	private static String SELECT_MOVIE_PSTMT = "SELECT * FROM MOVIES WHERE MOVIE_ID=?";

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
		pool = DBConnectionPool.getInstance(
				"jdbc:oracle:thin:@localhost:1521:XE", "chook", "shoochi",
				"oracle.jdbc.OracleDriver", 6);

		/*
		 * getInstance("jdbc:oracle:thin:@localhost:1555:csodb", "chenhare",
		 * "Shoochi0", "oracle.jdbc.OracleDriver", 6);
		 */

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
	public boolean sendMovieToDB(DBOperationEnum oper, Movie movie) {
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
	 * Search movies in the database
	 * 
	 * @param arlFilters
	 *            - A list of filters to prepare the WHERE clause
	 * @return - An array of movies that were fetched from the select
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
				if ((result = fillSearchResult(set)) != null) {
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

	public Movie getMovieById(int id) {
		Movie tempMovie = null;
		ResultSet set = null;

		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();

		try {
			// TODO: Change SEARCH_MOVIE_PSTMT to something like
			// SELECT_MOVIE_PSTMT
			pstmt = conn.prepareStatement(SELECT_MOVIE_PSTMT);
			pstmt.setInt(1, id);

			// Executing the query and building the movies array
			set = pstmt.executeQuery();
			if (set.next() == true) {
				tempMovie = fillMovieFromSet(set);
			}
			return tempMovie;
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		return null;
	}

	/**
	 * Search persons by filters
	 * 
	 * @param arlFilters
	 * @return List of persons
	 */
	public List<BasicSearchEntity> searchPersons(List<Filter> arlFilters) {
		return null;
	}

	private BasicSearchEntity fillSearchResult(ResultSet set) {
		BasicSearchEntity res = null;
		try {
			res = new BasicSearchEntity();
			res.setId(set.getInt("MOVIE_ID"));
			res.setName(set.getString("MOVIE_NAME"));
			res.setYear(set.getInt("MOVIE_YEAR"));
			return res;
		} catch (SQLException e) {
			return null;
		}
	}

	private Movie fillMovieFromSet(ResultSet set) {
		Movie movie = null;
		try {
			movie = new Movie();
			movie.setId(set.getInt("MOVIE_ID"));
			movie.setName(set.getString("MOVIE_NAME"));
			movie.setYear(set.getInt("MOVIE_YEAR"));
			movie.setColorInfo(set.getInt("MOVIE_COLOR_INFO_ID"));
			movie.setRunningTime(set.getInt("MOVIE_RUNNING_TIME"));
			movie.setTaglines(set.getString("MOVIE_TAGLINE"));
			movie.setPlot(set.getString("MOVIE_PLOT_TEXT"));
			movie.setFilmingLocations(set
					.getString("MOVIE_FILMING_LOCATION_NAME"));
		} catch (SQLException e) {
			System.out.println("SQLException error");
			return null;
		}
		return movie;
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
}
