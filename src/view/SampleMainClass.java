package view;
import java.util.ArrayList;

import controller.Filter;
import controller.FilterOption;
import controller.Movie;
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
		testDBManager();
		
		/*SamplePersonClass person = new SamplePersonClass();
		
		person.setName("Nadav (OMG I was bitten by a kitten) Shamgar");
		System.out.println("Hi " + person.getName() + ". Have a nice day!");
		System.out.println(SampleDataBaseClass.getInstance().getVersion());
		*/
		//DBManager db = DBManager.getInstance();
		//db.openConnection();
	}
	
	public static void testDBManager() {
		DBManager db = DBManager.getInstance();
		ArrayList<Filter> list = new ArrayList<Filter>();
		try{
			Filter filter = new Filter(FilterOption.Number, "YEAR", "1999");
			list.add(filter);
			list.add(new Filter(FilterOption.String, "NAME", "%H%"));
			
		} catch(Exception e) {
			System.out.println("bla bla");
			
		}
		
		ArrayList<Movie> searched = db.searchMovies(list);
		System.out.println(searched.size());
	}
}
