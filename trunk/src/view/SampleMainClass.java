package view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import controller.*;
import controller.entity.*;
import controller.filter.AbsFilter;
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
		
		/**
		 * Get all genres
		 */
		List<NamedEntity> genres = dm.getAllNamedEntities(NamedEntitiesEnum.COUNTRIES);
		System.out.println("genres count " + genres.size());
		
		int movieId = 43;
		/**
		 * Searching for a movie
		 */
		list = new ArrayList<AbsFilter>();
		af = dm.getFilter(SearchEntitiesEnum.MOVIE_NAME, "e");
		list.add(af);
		list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_YEAR, "2009"));
		list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_GENRE, "1"));
		searched = dm.search(SearchEntitiesEnum.MOVIES, list);
		System.out.println("movie " + searched.get(0));
		
		/**
		 * Get Movie entity
		 */
		MovieEntity m = dm.getMovieById(searched.get(0).getId());
		System.out.println(m);

		/**
		 * Getting movie aka names - it returns always null for now
		 */
		/*List<GeoEntity> akas = dm.getGeoEntities(String.valueOf(movieId),
											 	 SearchEntitiesEnum.MOVIE_AKAS);	
		
		System.out.println("akas count " + akas.size());
		*/
		/**
		 * Getting movie countries 
		 */
		List<NamedEntity> countries = dm.getNamedEntity(NamedEntitiesEnum.COUNTRIES, String.valueOf(movieId));
		System.out.println("countries count " + countries.size());
		
		/**
		 * Getting movie countries revised
		 */
		//List<? extends NamedEntity> countriesRevised = dm.getMovieData(MovieDataEnum.MOVIE_COUNTRIES, movieId);
		//System.out.println("countries revised count " + countriesRevised.size());
		
		/**
		 * Getting movie countries revised 2
		 */
		List<AbsDataType> countriesGood = dm.getMovieData(MovieDataEnum.MOVIE_COUNTRIES, movieId);
		System.out.println("countries revised count " + countriesGood.toString());
		
		List<AbsDataType> mgenres = dm.getMovieData(MovieDataEnum.MOVIE_GENRES, movieId);
		System.out.println("countries revised count " + mgenres.toString());
		
		List<AbsDataType> mgoofs = dm.getMovieData(MovieDataEnum.MOVIE_GOOFS, movieId);
		System.out.println("good goofs " + mgoofs.toString());
		
		for (AbsDataType t: mgoofs) {
			Map<String, String> mgoofsmap = t.toStringMap();
			System.out.println("name of goof is " + mgoofsmap.get("name"));
		}
		
		List<AbsDataType> makas = dm.getMovieData(MovieDataEnum.MOVIE_AKAS, movieId);
		System.out.println("good makas " + makas.toString());
		
		/**
		 * Try to find goofs
		 */
		List<NamedRelation> goofs = dm.getNamedRelationsById(String.valueOf(movieId),
															 NamedRelationsEnum.GOOFS);
		System.out.println("goofs count " + goofs.size());
	}
}
