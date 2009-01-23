package controller;

import java.util.*;

import controller.entity.*;
import controller.filter.AbsFilter;
import controller.filter.Filter;
import model.DBManager;
import model.DBOperationEnum;
import model.DBTablesEnum;

public class DataManager {
	private static DataManager manager = null;
	private DBManager db = null;
	
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
		db = DBManager.getInstance();
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
	 * @return List of movies
	 */
	private List<BasicSearchEntity> getMoviesBySearch(List<AbsFilter> arlFilters) {
		return DBManager.getInstance().searchMoviesNew(arlFilters);
	}
	
	private List<BasicSearchEntity> getPersonsBySearch(List<AbsFilter> arlFilters) {
		return null; //DBManager.getInstance().searchPersons(arlFilters);
	}
	
	private Movie getMovieById(int id) {
		return DBManager.getInstance().getMovieById(id);
	}
	
	public List<BasicSearchEntity> search(DBTablesEnum table, List<AbsFilter> list) {
		switch(table)
		{
			case MOVIES:
			{
				return getMoviesBySearch(list);
			}
			case PERSONS:
			{
				return getPersonsBySearch(list);
			}
			default:
				return null;
		}
	}
	
	public Object getEntityById(DBTablesEnum table, int id) {
		switch(table)
		{
			case MOVIES:
			{
				return getMovieById(id);
			}
			case PERSONS:
			{
				return null; //getPersonsBySearch(arlFilters);
			}
			default:
				return null;
		}
	}

	/**
	 * This function creates a filter object for the user to help search
	 * @param entity
	 * @param value
	 * @return
	 */
	public AbsFilter getFilter(SearchEntitiesEnum entity, String value) {
		return getFilterFromDB(entity, value, "");
	}
	public AbsFilter getFilter(SearchEntitiesEnum entity, String value, String value2) {
		return getFilterFromDB(entity, value, value2);
	}
	
	private AbsFilter getFilterFromDB(SearchEntitiesEnum entity, String value, String value2) {
		return db.getFilter(entity, value, value2);
	}
}
