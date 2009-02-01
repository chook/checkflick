package controller;

import java.util.*;
import view.RibbonInterface;
import controller.entity.*;
import controller.enums.ListFilesEnum;
import controller.enums.MovieDataEnum;
import controller.enums.NamedEntitiesEnum;
import controller.enums.PersonDataEnum;
import controller.enums.SearchEntitiesEnum;
import controller.filter.AbsFilter;
import model.DBManager;
import model.DBTablesEnum;

/**
 * This class acts as the broker between the view and model.
 * It gets requests from the viewer and returns them to the viewer interface 
 * to support multi-requests via threads
 * @author Chook
 *
 */
public class DataManager {
	private static DataManager manager = null;
	public static Runnable deleteMovieEntity(final MovieDataEnum dataType, final AbsType entity , final int id) {
		return new Runnable() {
			public void run() {
				if (dataType == MovieDataEnum.MOVIE)
					RibbonInterface.deleteEntity(DBManager.getInstance().deleteMovieEntity(dataType, entity));
				else
					RibbonInterface.deleteMovieExtraData(DBManager.getInstance().deleteMovieEntity(dataType, entity) , id , dataType);
			}
		};
	}
	public static Runnable deletePersonEntity(final PersonDataEnum dataType, final AbsType entity , final int id) {
		return new Runnable() {
			public void run() {
				if (dataType == PersonDataEnum.PERSON)
					RibbonInterface.deleteEntity(DBManager.getInstance().deletePersonEntity(dataType, entity));
				else
					RibbonInterface.deletePersonExtraData(DBManager.getInstance().deletePersonEntity(dataType, entity),id ,dataType );
			}
		};
	}
	
	public static Runnable importIntoDb(final String path) {
		return new Runnable() {
			public void run() {
				ImportHandler ih = new ImportHandler();
				ih.addListFile(ListFilesEnum.MOVIES, path + "/movies.list");
				ih.addListFile(ListFilesEnum.LANGUAGES, path + "/language.list");
				ih.addListFile(ListFilesEnum.GENRES, path + "/genres.list");
				ih.addListFile(ListFilesEnum.COUNTRIES, path + "/countries.list");
				ih.addListFile(ListFilesEnum.ACTORS, path + "/actors.list");
				ih.addListFile(ListFilesEnum.ACTRESSES, path + "/actresses.list");
				ih.addListFile(ListFilesEnum.DIRECTORS, path + "/directors.list");
				ih.addListFile(ListFilesEnum.PRODUCERS, path + "/producers.list");
				ih.addListFile(ListFilesEnum.WRITERS, path + "/writers.list");
				RibbonInterface.finishImport(ih.importIntoDB());
			}
		};
	}
	
	
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
					RibbonInterface.drawMovieData(DBManager.getInstance().getMovieData(dataType, String.valueOf(id)), dataType , id);
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
	
	/*public synchronized List<NamedEntity> getAllNamedEntities(NamedEntitiesEnum name) {
		if(!namedEntities.containsKey(name))
			namedEntities.put(name, db.getAllNamedEntities(name));
		return namedEntities.get(name);
	}*/
	
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
					RibbonInterface.drawPersonData(DBManager.getInstance().getPersonData(dataType, String.valueOf(id)), dataType , id);
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
	public static Runnable insertMovieData(final MovieDataEnum dataType, final AbsType dataObject , final boolean update) {
		return new Runnable() {
			public void run() {
				if(dataType == MovieDataEnum.MOVIE)
					RibbonInterface.InsertMovie(DBManager.getInstance().insertMovieData(dataType, dataObject, false));
				else
					RibbonInterface.InsertMovieExtraData(DBManager.getInstance().insertMovieData(dataType, dataObject, false ), update , dataType);
				
			}
		};
	}
	
	public static Runnable insertPersonData(final PersonDataEnum dataType, final AbsType dataObject  ,final boolean update) {
		return new Runnable() {
			public void run() {
				if(dataType == PersonDataEnum.PERSON)
					RibbonInterface.InsertPerson(DBManager.getInstance().insertPersonData(dataType, dataObject, false));
				else
					RibbonInterface.InsertPersonExtraData(DBManager.getInstance().insertPersonData(dataType, dataObject, false ), update , dataType);
			}
		};
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
				List<DatedEntity> list2 = new ArrayList<DatedEntity>();
				list2.add(new DatedEntity(1, "maayan", 2009));
				list2.add(new DatedEntity(2, "chook", 2009));
				
				RibbonInterface.drawMovieSearch(list2, entity);
				System.out.println("About to search for " + entity.toString());
//				switch(entity)
//				{
//					case MOVIES:
//						RibbonInterface.drawMovieSearch(DBManager.getInstance().search(list, DBTablesEnum.MOVIES), entity);
//						break;
//					case PERSONS:
//						RibbonInterface.drawPersonSearch(DBManager.getInstance().search(list, DBTablesEnum.PERSONS), entity );
//						break;
//					default:
//						break;
//				}
			}
		};
	}
	
	public static Runnable search(final SearchEntitiesEnum entity, final List<AbsFilter> list  ,final  int id , final boolean update) {
		return new Runnable() {
			public void run() {
				System.out.println("About to search for " + entity.toString());
				List<DatedEntity> list2 = new ArrayList<DatedEntity>();
				list2.add(new DatedEntity(1, "maayan", 2009));
				list2.add(new DatedEntity(2, "chook", 2009));
				
				RibbonInterface.drawMovieSearch(list2, entity);
				return;
//				switch(entity)
//				{
//					case MOVIES:
//						RibbonInterface.drawMovieSearch(DBManager.getInstance().search(list, DBTablesEnum.MOVIES), entity );
//						break;
//					case PERSONS:
//						RibbonInterface.drawPersonSearch(DBManager.getInstance().search(list, DBTablesEnum.PERSONS), entity , id , update);
//						break;
//					default:
//						break;
//				}
			}
		};
	}
	public static Runnable updateMovieData(final MovieDataEnum dataType, final AbsType dataObject){
		return new Runnable() {
			public void run() {
				RibbonInterface.updateMovie(DBManager.getInstance().insertMovieData(dataType, dataObject, true));
			}
		};
	}
	
	public static Runnable updatePersonData(final PersonDataEnum dataType, final AbsType dataObject) {
		return new Runnable() {
			public void run() {
				RibbonInterface.updatePerson(DBManager.getInstance().insertPersonData(dataType, dataObject, true));
			}
		};
	}
	private DBManager db = null;

	private static Map<NamedEntitiesEnum, List<NamedEntity>> namedEntities;
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
		return db.getSearchFilter(entity, value, value2);
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
	
	public int updateMovieDataOld(MovieDataEnum dataType, AbsType dataObject) {
		return db.insertMovieData(dataType, dataObject, true);
	}

	public int updatePersonDataOld(PersonDataEnum dataType, AbsType dataObject) {
		return db.insertPersonData(dataType, dataObject, true);
	}
}
