package controller;

import java.util.*;
import model.DBManager;
import model.DBOperationEnum;

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

	/**
	 * Save the movie (Update or Insert according to movie id)
	 * @param movie - The movie to update/insert
	 * @return True - If update succeeded, Else - Otherwise
	 */
	public boolean saveMovie(Movie movie) {
		// If we have a movie id, then we need to update
		if(movie.getId() != 0)
		{
			DBManager.getInstance().sendMovieToDB(DBOperationEnum.UpdateMovie, movie);
		}
		// Else - Insert a new movie into the DB
		else
		{
			DBManager.getInstance().sendMovieToDB(DBOperationEnum.InsertMovie, movie);
		}
		return false;
	}
	
	/**
	 * Delete a movie from the database
	 * @param id - The id to delete
	 * @return True - If delete succeeded, Else - Otherwise
	 */
	public boolean deleteMovie(int id) {
		// Try to delete the movie
		return DBManager.getInstance().sendMovieToDB(DBOperationEnum.DeleteMovie, new Movie(id));
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
		//		 receives a 'table' and filters, and the DB manager will build
		//		 the query.
		return DBManager.getInstance().searchMovies(arlFilters);
	}
}
