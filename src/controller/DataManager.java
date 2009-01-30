package controller;

import java.util.*;

import view.RibbonInterface;
import view.SampleRibbonClass;
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
	 * @deprecated
	 * @return data manager object initialized
	 */
	public static DataManager getInstance() {
		if(manager == null) {
			manager = new DataManager();
		}
		return manager;
	}
	private DBManager db = null;
	
	private static Map<NamedEntitiesEnum, List<NamedEntity>> namedEntities;
	
	/**
	 * Protected Constructor
	 */
	public DataManager() {
		db = DBManager.getInstance();
		namedEntities = new TreeMap<NamedEntitiesEnum, List<NamedEntity>>();
	}

	/**
	 * Clears all named entities, forces the data manager to query them again
	 */
	public synchronized void clearAllNamedEntities() {
		namedEntities.clear();
	}

	/**
	 * This function gets a list of named entities
	 * It doesn't go to the db for every call, just one time 
	 * TODO: In the future we might want to put a timeout on the data
	 * @param name
	 * @return
	 */
	public static Runnable getAllNamedEntities(final NamedEntitiesEnum name) {
		return new Runnable() {
			public void run() {
				RibbonInterface.SetNamedList(DBManager.getAllNamedEntities(name), name);		
			}
		};
	}
	
	/*public synchronized List<NamedEntity> getAllNamedEntities(NamedEntitiesEnum name) {
		if(!namedEntities.containsKey(name))
			namedEntities.put(name, db.getAllNamedEntities(name));
		return namedEntities.get(name);
	}*/
	
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
	
	public static Runnable getMovieById(final int id) { 
		return new Runnable() {
			public void run() {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if (id != 0){
					//RibbonInterface.settingShellText(DBManager.getInstance().getMovieById(id).getName());
					RibbonInterface.settingMovieTab(DBManager.getInstance().getMovieById(id));
				}
			}
		};
	}
	public MovieEntity getMovieByIdOld(int id) {
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
	public List<AbsType> getMovieDataOld(MovieDataEnum dataType, int id) {
		if (id != 0)
			return db.getMovieData(dataType, String.valueOf(id));
		else 
			return null;
	}
	public static Runnable getMovieData(final MovieDataEnum dataType, final int id) {
		return new Runnable() {
			public void run() {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if (id != 0){
					RibbonInterface.drawMovieData(DBManager.getInstance().getMovieData(dataType, String.valueOf(id)), dataType);
				}
			}
		};
	}
	
	public static Runnable getPersonById(final int id) {
		return new Runnable() {
			public void run() {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if (id != 0){
					RibbonInterface.settingPersonTab(DBManager.getInstance().getPersonById(id));
				}
			}
		};
	}
	public PersonEntity getPersonByIdOld(int id) {
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
	public List<AbsType> getPersonDataOld(PersonDataEnum dataType, int id) {
		if (id != 0)
			return db.getPersonData(dataType, String.valueOf(id));
		else 
			return null;
	}
	public static Runnable getPersonData(final PersonDataEnum dataType, final int id) {
		return new Runnable() {
			public void run() {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if (id != 0){
					RibbonInterface.drawPersonData(DBManager.getInstance().getPersonData(dataType, String.valueOf(id)), dataType);
				}
			}
		};
	}
	/**
	 * This function inserts a new movie data object
	 * @param dataType - Type of data
	 * @param dataObject - Data
	 * @return - True if successfully inserted, False - Otherwise
	 */
	public static Runnable insertMovieData(final MovieDataEnum dataType, final AbsType dataObject) {
		return new Runnable() {
			public void run() {
				if(dataType == MovieDataEnum.MOVIE)
					RibbonInterface.InsertMovie(DBManager.getInstance().insertMovieData(dataType, dataObject, false));
				else
					RibbonInterface.InsertMovieExtraData(DBManager.getInstance().insertMovieData(dataType, dataObject, false));
			}
		};
	}
	
	public int insertMovieDataOld(MovieDataEnum dataType, AbsType dataObject) {
		return db.insertMovieData(dataType, dataObject, false);
	}
	
	/**
	 * This function inserts a new person data object
	 * @param dataType - Type of data
	 * @param dataObject - Data
	 * @return - True if successfully inserted, False - Otherwise
	 */
	public int insertPersonDataOld(PersonDataEnum dataType, AbsType dataObject) {
		return db.insertPersonData(dataType, dataObject, false);
	}
	public static Runnable insertPersonData(final PersonDataEnum dataType, final AbsType dataObject) {
		return new Runnable() {
			public void run() {
				RibbonInterface.InsertPerson(DBManager.getInstance().insertPersonData(dataType, dataObject, false));
			}
		};
	}
	public int updatePersonData(PersonDataEnum dataType, AbsType dataObject) {
		return db.insertPersonData(dataType, dataObject, true);
	}
	

	public int updateMovieData(MovieDataEnum dataType, AbsType dataObject) {
		return db.insertMovieData(dataType, dataObject, true);
	}

	/**
	 * Search function - Gets a list of filters and an entity
	 * @param entity - The entity to search on
	 * @param list - filters
	 * @return a named list
	 */
	public static Runnable search(final SearchEntitiesEnum entity, final List<AbsFilter> list ) {
		return new Runnable() {
			public void run() {
				System.out.println("About to search for " + entity.toString());
				switch(entity)
				{
					case MOVIES:
						RibbonInterface.drawMovieSearch(DBManager.getInstance().search(list, DBTablesEnum.MOVIES), entity);
						break;
					case PERSONS:
						RibbonInterface.drawPersonSearch(DBManager.getInstance().search(list, DBTablesEnum.PERSONS), entity );
						break;
					default:
						break;
				}
			}
		};
	}
	public static Runnable search(final SearchEntitiesEnum entity, final List<AbsFilter> list  ,final  int id) {
		return new Runnable() {
			public void run() {
				System.out.println("About to search for " + entity.toString());
				switch(entity)
				{
					case MOVIES:
						RibbonInterface.drawMovieSearch(DBManager.getInstance().search(list, DBTablesEnum.MOVIES), entity , id);
						break;
					case PERSONS:
						RibbonInterface.drawPersonSearch(DBManager.getInstance().search(list, DBTablesEnum.PERSONS), entity , id);
						break;
					default:
						break;
				}
			}
		};
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
