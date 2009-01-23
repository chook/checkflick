package controller;

import java.util.HashMap;
import java.util.Map;

import model.DataImporter;

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
		
//		importer.getLanguagesGenresCountries();
		importer.getMovies();
/*
		
		// ------------------ dependent only on Movies
		importer.getMoviesLanguages();	// dependent on Languages
		importer.getMoviesGenres();		// dependent on Genres
		importer.getMoviesCountries();	// dependent on Countries
		importer.getColorInfo();
		importer.getAKATitles();
		importer.getCrazyCredits();
		importer.getGoofs();
		importer.getLocations();
		importer.getMovieConnections();
		importer.getPlots();
		importer.getMovieQuotes();
		importer.getRunningTimes();
		importer.getTaglines();
		importer.getMovieTrivias();
		
		importer.getPersons();
		// ------------------ dependent only on Persons
		importer.getAKANames();
		importer.getBiographies();
		
		// ------------------ dependent on both Persons and Movies
		importer.getMovieAppearances();
			
		
*/
		
		
		

		
		return true;
		
	}
	
}
