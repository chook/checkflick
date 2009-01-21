package view;

import java.util.ArrayList;
import java.util.List;

import controller.*;
import controller.entity.Movie;
import model.DBManager;
import model.DBTablesEnum;

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
		testDBManager();
		Thread guiThread = new SampleRibbonThread();
		//guiThread.start();

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

	public static void testDBManager() {
		DBManager db = DBManager.getInstance();
		DataManager dm = DataManager.getInstance();
		List<Filter> list = new ArrayList<Filter>();
		try{
			Filter filter = new Filter(FilterOptionEnum.Number, "MOVIE_YEAR", "2010");
			list.add(filter);
			list.add(new Filter(FilterOptionEnum.String, "MOVIE_NAME", "%o%"));
			
		} catch(Exception e) {
			System.out.println("bla bla");
		}
		
		List<Movie> searched = (List<Movie>) dm.search(DBTablesEnum.MOVIES, list);
		Movie mo = (Movie)dm.getEntityById(DBTablesEnum.MOVIES, searched.get(0).getId());
		
		System.out.println(mo.toString());
		//searched = db.searchMovies(list);
		//System.out.println(searched.size());
	}
}
