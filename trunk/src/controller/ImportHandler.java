package controller;

import java.util.HashMap;
import java.util.Map;

import controller.enums.ListFilesEnum;

import model.DataImporter;
import model.Parser;

public class ImportHandler {
	
	private Map<ListFilesEnum, String> listfilesMap;
	//private String importFolderPath;

	public boolean addListFile(ListFilesEnum listname, String filename) {
		listfilesMap.put(listname, filename);
		return true;
	}
	
	public ImportHandler(/*String importPath*/) {
		listfilesMap = new HashMap<ListFilesEnum, String>();
		//importFolderPath = importPath;
	}
	
	/**
	 * checks if all files needed for the import were given and if they all exist
	 * @return true if there was no error
	 */
	private boolean verifyFiles() {
		
		// preparing an error string
		String errMsg = "The following error has occurred\n";
		boolean isError = false;
		
		// going over all the different elements in the ListFilesEnum,
		// and making sure every list was attached a filename, and that all files exist
		for (ListFilesEnum enumField: ListFilesEnum.values()) {
			if (!(listfilesMap.containsKey(enumField))) {
				isError = true;
				errMsg += "a filename for the " + enumField.toString().toLowerCase() + " wasn't given\n";
			} else {
				// TODO: check if the filename that was given actually exists
			}
		}
		
		if (isError)
			System.out.println(errMsg);
			
		return isError;
	}
	
	/**
	 * imports all the data that was given to the handler to the DB using the Parser
	 * @return true if there was no error
	 */
	
	public synchronized boolean importIntoDB() {
		
		DataImporter importer;
		
		// TODO: bring back the verification when testings are over
		// check all files were given and that they exist 
/*		if (!verifyFiles())
			return false;
*/	
		importer = new DataImporter(listfilesMap);
		
		Parser parser = new Parser();
		parser.loadFile("lists//language.list", ListFilesEnum.LANGUAGES);
		System.out.println("first line:");
		System.out.println(parser.readLine());
		if (parser.findLine(1000) == 1) {
			System.out.println("got to line 1000:");
			System.out.println(parser.readLine());
			if (parser.findStartOfList()) {
				System.out.println("got back to start of list");
				System.out.println("line number = " + parser.getLineNumber() + ":");
				System.out.println(parser.readLine());
			}
		}
		
		
//		importer.getLanguagesGenresCountries();
//		importer.getMovies();
		
		// ------------------ dependent only on Movies
//		importer.getMoviesLanguages();	// dependent on Languages
//		importer.getMoviesLanguages2();	// dependent on Languages - NOT TO BE USED
/*		importer.getMoviesGenres();		// dependent on Genres
		importer.getMoviesCountries();	// dependent on Countries
		importer.getMoviesColorInfo();
		importer.getMoviesAKATitles();
		importer.getMoviesCrazyCredits();
		importer.getMoviesGoofs();
		importer.getMoviesLocations();
		importer.getMoviesConnections();
		importer.getMoviesQuotes();
		importer.getMoviesPlots();
		importer.getMoviesRunningTimes();
		importer.getMoviesTaglines();
		importer.getMoviesTrivias();
*/		
//		importer.getPersons();
/*		// ------------------ dependent only on Persons
		importer.getAKANames();
		importer.getBiographies();
		
*/		// ------------------ dependent on both Persons and Movies
//		importer.getPersonMovieCredits();
		
		return true;
		
	}
	
}
