package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import controller.Filter;
import controller.entity.Movie;
import controller.entity.Person;

public class DBManager {
	// The strings for prepared statements
	private static String INSERT_MOVIE_PSTMT = "INSERT INTO MOVIES(fname,lname) VALUES(?,?)";
	private static String UPDATE_MOVIE_PSTMT = "UPDATE MOVIES SET ? WHERE MOVIE_ID=?";
	private static String DELETE_MOVIE_PSTMT = "DELETE FROM MOVIES WHERE MOVIE_ID=?";
	private static String SEARCH_MOVIE_STMT  = "SELECT * FROM MOVIES ";
	private static String SELECT_MOVIE_PSTMT = "SELECT * FROM MOVIES WHERE MOVIE_ID=?";
	
	
	// Singleton instance
	private static DBManager instance = null;
	private DBConnectionPool pool  = null;
	/**
	 * Singleton
	 * @return a DB manager instance
	 */
	public static DBManager getInstance() {
		if(instance == null) {
			instance = new DBManager();
		}
		return instance;
	}
	
	/**
	 * Default Constructor - Initiates the connection pool
	 */
	protected DBManager() {
		pool = DBConnectionPool.
				getInstance("jdbc:oracle:thin:@localhost:1521:XE",
							"chook",
							"shoochi",
							"oracle.jdbc.OracleDriver",
							6);
		/*
		 * getInstance("jdbc:oracle:thin:@localhost:1555:csodb",
							"hr_readonly",
							"hrro",
							"oracle.jdbc.OracleDriver",
							6);
		 */
	}

	/**
	 * This function used to send a movie query into the DB.
	 * The function supports Insert/Update/Delete
	 * @param oper - The operation to do
	 * @param movie - The movie to send
	 * @return If the operation was successful or not
	 * TODO: Check this function (22/01/09)
	 */
	public boolean sendMovieToDB(DBOperationEnum oper, Movie movie) {
		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		switch(oper) {
			case InsertMovie:
			{
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
			case DeleteMovie:
			{
				try {
					pstmt = conn.prepareStatement(DELETE_MOVIE_PSTMT);
					pstmt.setInt(1, movie.getId());
				} catch(SQLException e) {
					System.out.println(e.getMessage());
				}
				break;
			}
			case UpdateMovie:
			{
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
	private boolean executePreparedStatement(PreparedStatement pstmt)
	{
		int	    			result;
		
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
	 * Private method to parse a where clause from the filters
	 * @param arlFilters
	 * @return a WHERE clause to use in SELECT statements
	 */
	private String parseWhereClauseFromFilters(List<Filter> arlFilters) {
		StringBuilder stbFilter = new StringBuilder();
		int filterCounter = 0;
		
		// Building the WHERE clause 
		if(arlFilters.size() > 0) {
			stbFilter.append("WHERE ");
			for(Filter filter : arlFilters) {
				++filterCounter;
				stbFilter.append(filter);
				
				// Making sure the clause won't end with an AND
				if(filterCounter < arlFilters.size()) {
					stbFilter.append(" AND ");
				}
			}
		}
		
		return stbFilter.toString();
	}
	
	/**
	 * Search movies in the database
	 * @param arlFilters - A list of filters to prepare the WHERE clause
	 * @return - An array of movies that were fetched from the select
	 */
	public List<Movie> searchMovies(List<Filter> arlFilters) {
		// Variables Declaration
		List<Movie> arlMovies = new ArrayList<Movie>();
		Movie tempMovie = null;
		ResultSet set = null;
		Statement s = null;
		//PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();
		
		try {
			s = conn.createStatement();
			//pstmt.setString(1, "WHERE MOVIE_YEAR=2010"); //parseWhereClauseFromFilters(arlFilters));
			
			// Executing the query and building the movies array
			set = s.executeQuery(SEARCH_MOVIE_STMT + parseWhereClauseFromFilters(arlFilters));
			while(set.next() == true) {				
				if ((tempMovie = fillMovieSearch(set)) != null) {
					arlMovies.add(tempMovie);
				}
			}
		} catch (SQLException e) {
			System.out.println("Error in searchMovies " + e.toString());
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		
		return arlMovies;
	}
	
	public Movie getMovieById(int id) {
		Movie tempMovie = null;
		ResultSet set = null;

		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();
		
		try {
			// TODO: Change SEARCH_MOVIE_PSTMT to something like SELECT_MOVIE_PSTMT
			pstmt = conn.prepareStatement(SELECT_MOVIE_PSTMT);
			pstmt.setInt(1, id);
			
			// Executing the query and building the movies array
			set = pstmt.executeQuery();
			if (set.next() == true) {
				tempMovie = fillMovieSearch(set);
				tempMovie = fillRestOfMovieFromSet(set, tempMovie);
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
	 * @param arlFilters
	 * @return List of persons
	 */
	public List<Person> searchPersons(List<Filter> arlFilters) {
		return null;
	}
	
	/**
	 * Builds a movie object from a result set tuple
	 * @param set - The set containing the tuple
	 * @return - The movie object initialized
	 */
	private Movie fillMovieSearch(ResultSet set) {
		Movie movie = null;
		try {
			movie = new Movie();
			movie.setId(set.getInt("MOVIE_ID"));
			movie.setName(set.getString("MOVIE_NAME"));
			movie.setYear(set.getInt("MOVIE_YEAR"));
			return movie;
		} catch(SQLException e) {
			return null;
		}
	}
	
	private Movie fillRestOfMovieFromSet(ResultSet set, Movie movie) {
		try {
			movie.setColorInfo(set.getInt("MOVIE_COLOR_INFO_ID"));
			movie.setRunningTime(set.getInt("MOVIE_RUNNING_TIME"));
			movie.setTaglines(set.getString("MOVIE_TAGLINE"));
			movie.setPlot(set.getString("MOVIE_PLOT_TEXT"));
			movie.setFilmingLocations(set.getString("MOVIE_FILMING_LOCATION_NAME"));
		} catch (SQLException e) {
			System.out.println("SQLException error");
			return null;
		}
		return movie;
	}
}
