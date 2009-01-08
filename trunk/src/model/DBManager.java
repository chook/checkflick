package model;

import java.sql.*;
import java.util.ArrayList;
import controller.Filter;
import controller.Movie;

public class DBManager {
	// The strings for prepared statments
	private static String INSERT_MOVIE_PSTMT = "INSERT INTO demo(fname,lname) VALUES(?,?)";
	private static String DELETE_MOVIE_PSTMT = "DELETE FROM demo where id=?";
	private static String SEARCH_MOVIE_PSTMT = "SELECT * FROM MOVIES ?";
	
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
							"admin",
							"admin",
							"oracle.jdbc.OracleDriver",
							6);
	}

	/**
	 * This function used to send a movie query into the DB.
	 * The function supports Insert/Update/Delete
	 * @param oper - The operation to do
	 * @param movie - The movie to send
	 * @return If the operation was successful or not
	 */
	public boolean sendMovieToDB(DBOperation oper, Movie movie) {
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
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
			case DeleteMovie:
			{
				break;
			}
			case UpdateMovie:
			{
				break;
			}
		}
		bReturn = executePreparedStatement(pstmt);
		pool.returnConnection(conn);
		return bReturn;		
	}
	
	/**
	 * 
	 * @param oper
	 * @param id
	 * @return
	 */
	public boolean deleteOperation(DBOperation oper, int id) {
		PreparedStatement pstmt = null;
		boolean bReturn = false;
		Connection conn = pool.getConnection();
		
		switch(oper.ordinal()) {
			case(1):
			{
				try {
					pstmt = conn.prepareStatement(DELETE_MOVIE_PSTMT);
					pstmt.setInt(0, id);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
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
	 * Shows the executePreparedStatement()
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
	 * Search movies in the database
	 * @param arlFilters - A list of filters to prepare the WHERE clause
	 * @return - An array of movies that were fetched from the select
	 */
	public ArrayList<Movie> searchMovies(ArrayList<Filter> arlFilters) {
		// Variables Declaration
		ArrayList<Movie> arlMovies = new ArrayList<Movie>();
		StringBuilder stbFilter = new StringBuilder();
		Movie tempMovie = null;
		ResultSet set = null;
		int nFilterCounter = 0;
		int nNumberOfFilters = arlFilters.size();
		PreparedStatement pstmt = null;
		Connection conn = pool.getConnection();
		
		try {
			pstmt = conn.prepareStatement(SEARCH_MOVIE_PSTMT);
			
			// Building the WHERE clause 
			if(arlFilters.size() > 0) {
				stbFilter.append("WHERE ");
				for(Filter filter : arlFilters) {
					++nFilterCounter;
					stbFilter.append(filter);
					if(nFilterCounter < nNumberOfFilters) {
						stbFilter.append(" AND ");
					}
				}
				
				pstmt.setString(0, stbFilter.toString());
			// Else - No WHERE clause
			} else {
				pstmt.setString(0, "");
			}
			
			// Executing the query and building the movies array
			set = pstmt.executeQuery();
			while(set.next() == true) {				
				tempMovie = getMovieFromSetRecord(set);
				if (tempMovie != null)
					arlMovies.add(tempMovie);
			}
		} catch (SQLException e) {
			System.out.println("Error in searchMovies");
		} catch (NullPointerException e) {
			System.out.println("Null pointer in searchMovies");
		}
		
		return arlMovies;
	}

	/**
	 * Builds a movie object from a result set tuple
	 * @param set - The set containing the tuple
	 * @return - The movie object initialized
	 */
	private Movie getMovieFromSetRecord(ResultSet set) {
		try {
			Movie movie = new Movie();
			movie.setId(set.getInt("ID"));
			movie.setDirectorId(set.getInt("DIRECTOR_ID"));
			movie.setName(set.getString("NAME"));
			movie.setYear(set.getInt("YEAR"));
			return movie;
		} catch(SQLException e) {
			return null;
		}
	}
}
