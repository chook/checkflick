package view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import controller.*;
import controller.entity.*;
import controller.filter.AbsFilter;
import controller.filter.Filter;
import controller.filter.FilterOptionEnum;
import model.*;

/**
 * This is a sample of a main class
 * In our program we might use the main to bring up the GUI
 * @author Chen Harel
 *
 */
public class SampleMainClass {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Creates a GUI thread
		//testDBManager();
		
		testDataManager();
		Thread guiThread = new SampleRibbonThread();
		guiThread.start();
		
		try {
			// This is a join try
			guiThread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// In the mean time, make some more tests for DB
		
		//AppData app = new AppData();
		//app.parseXMLFile();
	}

	public static void testDataManager() {
		DataManager dm = DataManager.getInstance();
		
		/**
		 * Searching for a person
		 */
		List<AbsFilter> list = new ArrayList<AbsFilter>();
		AbsFilter af = dm.getFilter(SearchEntitiesEnum.PERSON_ORIGIN_COUNTRY, "1");
		list.add(af);
		list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME, "ete"));
		List<BasicSearchEntity> searched = dm.search(SearchEntitiesEnum.PERSONS, list);
		System.out.println(searched.get(0));
		
		/**
		 * Get person entity
		 */
		PersonEntity p = dm.getPersonById(searched.get(0).getId());
		System.out.println(p);
		System.out.println("Count:" + DBTablesEnum.getCounter());
		// This is the way to get a named entity (id - name list)
		List<NamedEntity> genres = dm.getNamedEntity(NamedEntitiesEnum.COUNTRIES);
		System.out.println("genres count " + genres.size());
		
		/**
		 * Try to find goofs
		 */
		int movieId = 43;
		List<NamedRelation> goofs = dm.getNamedRelationsById(String.valueOf(movieId),
															 NamedRelationsEnum.GOOFS);
		System.out.println("goofs count " + goofs.size());
		
		List<GeoEntity> akas = dm.getGeoEntities(String.valueOf(movieId),
											 	 SearchEntitiesEnum.AKAS);	
		
	}
}
