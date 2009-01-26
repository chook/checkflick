package controller;

import java.util.*;
import controller.entity.*;
import controller.enums.MovieDataEnum;
import controller.enums.NamedEntitiesEnum;
import controller.enums.PersonDataEnum;
import controller.enums.SearchEntitiesEnum;
import controller.filter.AbsFilter;
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
	 * Clears all named entities, forces the data manager to query them again
	 */
	public void clearAllNamedEntities() {
		namedEntities.clear();
	}

	/**
	 * This function gets a list of named entities
	 * It doesn't go to the db for every call, just one time 
	 * TODO: In the future we might want to put a timeout on the data
	 * @param name
	 * @return
	 */
	public List<NamedEntity> getAllNamedEntities(NamedEntitiesEnum name) {
		if(!namedEntities.containsKey(name))
			namedEntities.put(name, db.getAllNamedEntities(name));
		return namedEntities.get(name);
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
	
	public MovieEntity getMovieById(int id) {
		if (id != 0)
			return DBManager.getInstance().getMovieById(id);
		else 
			return null;
	}
	
	/**
	 * This function gets various data about a movie
	 * @param dataType - Which data to get
	 * @param id - The movie id
	 * @return A list of data objects
	 */
	public List<AbsType> getMovieData(MovieDataEnum dataType, int id) {
		if (id != 0)
			return db.getMovieData(dataType, String.valueOf(id));
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
	 * This function gets various data about a person
	 * @param dataType - Which data to get
	 * @param id - The person id
	 * @return List of data objects
	 */
	public List<AbsType> getPersonData(PersonDataEnum dataType, int id) {
		if (id != 0)
			return db.getPersonData(dataType, String.valueOf(id));
		else 
			return null;
	}
	/**
	 * This function inserts a new movie data object
	 * @param dataType - Type of data
	 * @param dataObject - Data
	 * @return - True if successfully inserted, False - Otherwise
	 */
	public boolean insertMovieData(MovieDataEnum dataType, AbsType dataObject) {
		return db.insertMovieData(dataType, dataObject, false);
	}
	
	/**
	 * This function inserts a new person data object
	 * @param dataType - Type of data
	 * @param dataObject - Data
	 * @return - True if successfully inserted, False - Otherwise
	 */
	public boolean insertPersonData(PersonDataEnum dataType, AbsType dataObject) {
		return db.insertPersonData(dataType, dataObject, false);
	}

	public boolean updatePersonData(PersonDataEnum dataType, AbsType dataObject) {
		return db.insertPersonData(dataType, dataObject, true);
	}
	

	public boolean updateMovieData(MovieDataEnum dataType, AbsType dataObject) {
		return db.insertMovieData(dataType, dataObject, true);
	}

	/**
	 * Search function - Gets a list of filters and an entity
	 * @param entity - The entity to search on
	 * @param list - filters
	 * @return a named list
	 */
	public List<DatedEntity> search(SearchEntitiesEnum entity, List<AbsFilter> list) {
		switch(entity)
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
	private List<DatedEntity> getEntitiesBySearch(List<AbsFilter> arlFilters,
													   DBTablesEnum table) {
		return DBManager.getInstance().search(arlFilters, table);
	}
	
	private AbsFilter getFilterFromDB(SearchEntitiesEnum entity, String value, String value2) {
		return db.getSearchFilter(entity, value, value2);
	}

	public boolean deleteMovieEntity(MovieDataEnum dataType, AbsType entity) {
		return db.deleteMovieEntity(dataType, entity);
	}
}
