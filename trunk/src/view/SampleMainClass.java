package view;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

//import com.sun.xml.internal.ws.api.fastinfoset.FastInfosetFeature;

import controller.*;
import controller.entity.*;
import controller.enums.MovieDataEnum;
import controller.enums.NamedEntitiesEnum;
import controller.enums.PersonDataEnum;
import controller.enums.SearchEntitiesEnum;
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
	//public static DataManager dm = null;
	public static void main(String[] args) {
		// Creates a GUI thread
//		testDBManager();
//		DataManager dm = new DataManager();
//		dm.start();
		//ControllerInterface c = new ControllerInterface();
		//c.start();
				
		Thread guiThread = new SampleRibbonThread();
		guiThread.start();
		try {
			//Thread.sleep(2000);
			testDataManager();
		// This is a join try
			
			guiThread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// In the mean time, make some more tests for DB
		
		//AppData app = new AppData();
		//app.parseXMLFile();
	}

//	@Deprecated
	public static void testDataManager() {
//		DataManager dm = DataManager.getInstance();
//		
//		/**
//		 * Searching for a person
//		 */
//		List<AbsFilter> list = new ArrayList<AbsFilter>();
//		AbsFilter af = dm.getFilter(SearchEntitiesEnum.PERSON_ORIGIN_COUNTRY, "1");
//		list.add(af);
//		list.add(dm.getFilter(SearchEntitiesEnum.PERSON_NAME, "h"));
//		List<DatedEntity> searched = dm.search(SearchEntitiesEnum.PERSONS, list);
//		System.out.println(searched.get(0));
//		
//		/**
//		 * Get person entity
//		 */
//		PersonEntity p = dm.getPersonById(searched.get(0).getId());
//		System.out.println(p);
//		System.out.println("Count:" + DBTablesEnum.getCounter());
//		
//		//AbsFilter prrFilter = dm.getFilter(SearchEntitiesEnum.PERSON_PRODUCTION_ROLE, "1");
//		List<AbsType> pmovies = dm.getPersonData(PersonDataEnum.PERSON_ROLES, 7);
//		System.out.println("person movies " + pmovies.toString());
//		
//		List<AbsType> ptrivia = dm.getPersonData(PersonDataEnum.PERSON_TRIVIA, 5);
//		System.out.println("person trivia " + ptrivia.toString());
//		
//		/**
//		 * Get all genres
//		 */
//		List<NamedEntity> genres = dm.getAllNamedEntities(NamedEntitiesEnum.COUNTRIES);
//		System.out.println("genres count " + genres.size());
//		
//		int movieId = 42;
//		/**
//		 * Searching for a movie
//		 */
//		list = new ArrayList<AbsFilter>();
//		af = dm.getFilter(SearchEntitiesEnum.MOVIE_NAME, "e");
//		list.add(af);
//		list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_YEAR, "2009"));
//		list.add(dm.getFilter(SearchEntitiesEnum.MOVIE_GENRE, "1"));
//		searched = dm.search(SearchEntitiesEnum.MOVIES, list);
//		System.out.println("movie " + searched.get(0));
//		
//		/**
//		 * Get Movie entity
//		 */
//		MovieEntity m = dm.getMovieById(searched.get(0).getId());
//		System.out.println(m);
//
//		/**
//		 * Getting movie aka names - it returns always null for now
//		 */
//		/*List<GeoEntity> akas = dm.getGeoEntities(String.valueOf(movieId),
//											 	 SearchEntitiesEnum.MOVIE_AKAS);	
//		
//		System.out.println("akas count " + akas.size());
//		*/
//		/**
//		 * Getting movie countries 
//		 */
//		//List<NamedEntity> countries = dm.getNamedEntity(NamedEntitiesEnum.COUNTRIES, String.valueOf(movieId));
//		//System.out.println("countries count " + countries.size());
//		
//		/**
//		 * Getting movie countries revised
//		 */
//		//List<? extends NamedEntity> countriesRevised = dm.getMovieData(MovieDataEnum.MOVIE_COUNTRIES, movieId);
//		//System.out.println("countries revised count " + countriesRevised.size());
//		
//		/**
//		 * Getting movie countries revised 2
//		 */
//		List<AbsType> countriesGood = dm.getMovieData(MovieDataEnum.MOVIE_COUNTRIES, movieId);
//		System.out.println("countries revised count " + countriesGood.toString());
//		
//		List<AbsType> mgenres = dm.getMovieData(MovieDataEnum.MOVIE_GENRES, movieId);
//		System.out.println("countries revised count " + mgenres.toString());
//		
//		List<AbsType> mgoofs = dm.getMovieData(MovieDataEnum.MOVIE_GOOFS, movieId);
//		System.out.println("good goofs " + mgoofs.toString());
//		
//		for (AbsType t: mgoofs) {
//			Map<String, String> mgoofsmap = t.toStringMap();
//			System.out.println("name of goof is " + mgoofsmap.get("name"));
//		}
//		
//		List<AbsType> makas = dm.getMovieData(MovieDataEnum.MOVIE_AKAS, movieId);
//		System.out.println("good makas " + makas.toString());
//		
//		List<AbsType> mconnections = dm.getMovieData(MovieDataEnum.MOVIE_CONNECTIONS, movieId);
//		System.out.println("good connections :" + mconnections.toString());
//		
//		
//		List<AbsType> mquotes = dm.getMovieData(MovieDataEnum.MOVIE_QUOTES, 43);
//		System.out.println("good quotes :" + mquotes.toString());
//		
//		
//		List<AbsType> mappear = dm.getMovieData(MovieDataEnum.MOVIE_CAST, movieId);
//		System.out.println("good cast of movie:" + mappear.toString());
//
//		List<AbsType> pappear = dm.getPersonData(PersonDataEnum.PERSON_ROLES, 7);
//		System.out.println("good movies of person:" + pappear.toString());
//
//		//List<AbsType> mcast = dm.getMovieData(MovieDataEnum.MOVIE_CAST, id)
//		
//		//List<NamedRelation> goofs = dm.getNamedRelationsById(String.valueOf(movieId),
//		//													 NamedRelationsEnum.GOOFS);
//		//System.out.println("goofs count " + goofs.size());
//		
//		
//		//AbsFilter f = dm.getFilter(SearchEntitiesEnum.MOVIE_AKAS, "");
//		
//		//dm.savePersonData(PersonDataEnum.PERSON_QUOTES, t);
//		
//		//dm.getInsertFilter(InsertEntitiesEnum.MOVIE_QUOTE, value)
//		//AbsType t = new NamedEntity(7, "Trying to insert a person quote!");
//		//dm.insertPersonData(PersonDataEnum.PERSON_QUOTES, t);
//		//dm.sendMovieData(MovieDataEnum.MOVIE_QUOTES, t);
//		
//		//AbsType t = new CastingRelation(7, 43, 1, true, "Shrek", 1);
//		//System.out.println(dm.insertMovieData(MovieDataEnum.MOVIE_CAST, t));
//		
//		//AbsType nadav = new PersonEntity(0, "Nadav Shamgar", "Nash-Control","DjNash",
//		//				null, 1982, "Tel-Aviv",1,null, 0, 160, "Fuckbook", "Some isoteric data here");
//		//System.out.println(dm.insertPersonData(PersonDataEnum.PERSON, nadav));
//		
//		//dm.updatePersonData(PersonDataEnum.PERSON, p);
//		
		AbsType shindlist = new MovieEntity(0, "Shindler's list", 1999, null, null, 1, 90, "Shoa", "Shoa", "Shoa");
		System.out.println(DataManager.insertMovieData(MovieDataEnum.MOVIE, shindlist));
//		
//		AbsType entity = new CastingRelation(7, 43, 1);
//		System.out.println(dm.deleteMovieEntity(MovieDataEnum.MOVIE_CAST, entity));
	}
}
