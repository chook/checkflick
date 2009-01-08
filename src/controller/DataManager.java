package controller;
import java.util.*;

import model.DBManager;
import model.DBOperation;

public class DataManager {
	private static DataManager manager = null;
	
	/**
	 * This function retrieves the database object
	 * @return data manager object initialized
	 */
	public static DataManager getInstance() {
		if(manager == null) {
			manager = new DataManager();
		}
		return manager;
	}
	
	/**
	 * Protected Constructor
	 */
	protected DataManager() {
	}
	
	public ArrayList<Movie> getMoviesByName(String name) {
		
		// Initiates a new array of movies
		ArrayList<Movie> arlMoviesList = new ArrayList<Movie>();
		
		// Get movies from DB
		
		return arlMoviesList;
	}
	
	public Movie getMovieById(int id) {
		
		return null;
	}
	
	public boolean saveMovie(Movie movie) {
		// Get conn from pool
		//Connection conn = DBManager.getInstance().getConnection();
		
		// If we have a movie id, then we need to update
		if(movie.getId() != 0)
		{
			DBManager.getInstance().sendMovieToDB(DBOperation.UpdateMovie, movie);
		}
		// Else - Insert a new movie into the DB
		else
		{
			DBManager.getInstance().sendMovieToDB(DBOperation.InsertMovie, movie);
		}
		return false;
	}
	
	public boolean deleteMovie(int id) {
		// Try to delete the movie
		return DBManager.getInstance().deleteOperation(DBOperation.DeleteMovie, id);
	}
	
	/**
	 * Search movies using filters
	 * @param arlFilters - List of filters for WHERE clause
	 * @see  todo inside
	 * @return List of movies
	 */
	public ArrayList<Movie> getMoviesBySearch(ArrayList<Filter> arlFilters) {
		// TODO: For now this doesn't make much sense, I agree.
		//		 But once we have a full DB, we will make the search generic
		//		 i.e. We will have a generic "search" function that 
		//		 receives a 'table' and filters, and the dbmanager will build
		//		 the query.
		ArrayList<Movie> arlMovies;
		
		arlMovies = DBManager.getInstance().searchMovies(arlFilters);
		
		return arlMovies;
	}
}
