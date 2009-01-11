package view;

import java.util.ArrayList;
import controller.*;
import model.DBManager;

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
		Thread guiThread = new SampleRibbonThread();
		guiThread.start();

		try {
			// This is a join try
			guiThread.join(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// In the mean time, make some more tests for DB
		testDBManager();
		
		//AppData app = new AppData();
		//app.parseXMLFile();
	}

	public static void testDBManager() {
		DBManager db = DBManager.getInstance();
		ArrayList<Filter> list = new ArrayList<Filter>();
		try{
			Filter filter = new Filter(FilterOptionEnum.Number, "YEAR", "1999");
			list.add(filter);
			list.add(new Filter(FilterOptionEnum.String, "NAME", "%H%"));
			
		} catch(Exception e) {
			System.out.println("bla bla");
		}
		
		ArrayList<Movie> searched = db.searchMovies(list);
		System.out.println(searched.size());
	}
}
