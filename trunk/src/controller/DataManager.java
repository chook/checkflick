package controller;

import java.util.*;


import controller.entity.*;
import controller.filter.AbsFilter;
import controller.filter.AbsSingleFilter;
import model.DBManager;
import model.DBOperationEnum;
import model.DBTablesEnum;

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
	private DBManager db = null;
	
	private Map<NamedEntitiesEnum, List<NamedEntity>> namedEntities;
	
	/**
	 * Protected Constructor
	 */
	protected DataManager() {
		db = DBManager.getInstance();
		namedEntities = new TreeMap<NamedEntitiesEnum, List<NamedEntity>>();
	}

	/**
	 * Delete a movie from the database
	 * @param id - The id to delete
	 * @return True - If delete succeeded, Else - Otherwise
	 */
	public boolean deleteMovie(int id) {
		// Try to delete the movie
		return DBManager.getInstance().sendMovieToDB(DBOperationEnum.DeleteMovie, new MovieEntity(id));
	}
	
	public MovieEntity getMovieById(int id) {
		if (id != 0)
			return DBManager.getInstance().getMovieById(id);
		else 
			return null;
	}
	
	public PersonEntity getPersonById(int id) {
		if (id != 0)
			return DBManager.getInstance().getPersonById(id);
		else 
			return null;
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
	
	/**
	 * This function gets a list of named entities
	 * It doesn't go to the db for every call, just one time 
	 * TODO: In the future we might want to put a timeout on the data
	 * TODO: Change function name to plural
	 * @param name
	 * @return
	 */
	public List<NamedEntity> getNamedEntity(NamedEntitiesEnum name) {
		if(!namedEntities.containsKey(name))
			namedEntities.put(name, db.getNamedEntities(name));
		return namedEntities.get(name);
	}
	
	/**
	 * Save the movie (Update or Insert according to movie id)
	 * @param movie - The movie to update/insert
	 * @return True - If update succeeded, Else - Otherwise
	 */
	public boolean saveMovie(MovieEntity movie) {
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

	public List<BasicSearchEntity> search(SearchEntitiesEnum table, List<AbsFilter> list) {
		//return db.search(list, table);
		switch(table)
		{
			case MOVIES:
			{
				return getEntitiesBySearch(list, DBTablesEnum.MOVIES);
			}
			case PERSONS:
			{
				return getEntitiesBySearch(list, DBTablesEnum.PERSONS);
			}
			default:
				return null;
		}
	}
	/**
	 * Search movies using filters
	 * @param arlFilters - List of filters for WHERE clause
	 * @return List of movies
	 */
	private List<BasicSearchEntity> getEntitiesBySearch(List<AbsFilter> arlFilters,
													   DBTablesEnum table) {
		return DBManager.getInstance().search(arlFilters, table);
	}
	
	private AbsFilter getFilterFromDB(SearchEntitiesEnum entity, String value, String value2) {
		return db.getFilter(entity, value, value2);
	}

	public List<NamedRelation> getNamedRelationsById(String movieId, NamedRelationsEnum rel) {
		AbsSingleFilter filter = db.getFilter(SearchRelationsEnum.GOOFS, movieId);
		switch (rel){
		case GOOFS:
			return db.getNamedRalations(filter);
		}	
		return null;
	}

	public List<GeoEntity> getGeoEntities(String movieId,
										  SearchEntitiesEnum entity) {
		AbsSingleFilter filter = db.getFilter(SearchRelationsEnum.GOOFS, movieId);
		switch (entity){
		case AKAS:
			return db.getGeoEntities(filter);
		}
		return null;
	}
}
