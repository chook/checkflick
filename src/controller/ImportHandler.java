package controller;

import java.util.HashMap;
import java.util.Map;

import controller.enums.ListFilesEnum;

import model.DataImporter;
//import model.Parser;

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
		int personIndexNextMarkStart;	// the location where starts the specific persons list
		int personIndexNextMarkEnd;		// the location where ends the specific persons list
		
		// TEST: ================================================================
		importer.TESTcreateTempFields();
		int[] TESTpersonIndexNextMarkStart = new int[5]; 
		System.out.println("==========================================================");
		TESTpersonIndexNextMarkStart[0] = 1;
		TESTpersonIndexNextMarkStart[1] = TESTpersonIndexNextMarkStart[0] + importer.getPersons(ListFilesEnum.ACTORS);
		TESTpersonIndexNextMarkStart[2] = TESTpersonIndexNextMarkStart[1] + importer.getPersons(ListFilesEnum.ACTRESSES);
		TESTpersonIndexNextMarkStart[3] = TESTpersonIndexNextMarkStart[2] + importer.getPersons(ListFilesEnum.DIRECTORS);
		TESTpersonIndexNextMarkStart[4] = TESTpersonIndexNextMarkStart[3] + importer.getPersons(ListFilesEnum.PRODUCERS);
		importer.getPersons(ListFilesEnum.WRITERS);
		importer.TESTaddTempIdForPersons();
		importer.TESTmarkAllPersonsNotToDelete();
		importer.TESTfindAndUpdateDuplicates();
		
		importer.getPersonMovieCredits(ListFilesEnum.ACTORS, TESTpersonIndexNextMarkStart[0]);
		importer.getPersonMovieCredits(ListFilesEnum.ACTRESSES, TESTpersonIndexNextMarkStart[1]);
		importer.getPersonMovieCredits(ListFilesEnum.DIRECTORS, TESTpersonIndexNextMarkStart[2]);
		importer.getPersonMovieCredits(ListFilesEnum.PRODUCERS, TESTpersonIndexNextMarkStart[3]);
		importer.getPersonMovieCredits(ListFilesEnum.WRITERS, TESTpersonIndexNextMarkStart[4]);
		
		importer.TESTRemoveDuplicates();
		importer.TESTdeleteTempFields();
		
		// TEST END : ===========================================================
		
//		System.out.println("==========================================================");
//		personIndexNextMarkStart = 1;
//		personIndexNextMarkEnd = importer.getPersons(ListFilesEnum.ACTORS) + 1;
//		importer.getPersonMovieCredits(ListFilesEnum.ACTORS, personIndexNextMarkStart);
//		
//		System.out.println("==========================================================");
//		personIndexNextMarkStart = personIndexNextMarkEnd;
//		personIndexNextMarkEnd += importer.getPersons(ListFilesEnum.ACTRESSES) + 1;
//		importer.getPersonMovieCredits(ListFilesEnum.ACTRESSES, personIndexNextMarkStart);
//		
//		System.out.println("==========================================================");
//		personIndexNextMarkStart = personIndexNextMarkEnd;
//		personIndexNextMarkEnd += importer.getPersons(ListFilesEnum.DIRECTORS) + 1;
//		importer.getPersonMovieCredits(ListFilesEnum.DIRECTORS, personIndexNextMarkStart);
//		
//		System.out.println("==========================================================");
//		personIndexNextMarkStart = personIndexNextMarkEnd;
//		personIndexNextMarkEnd += importer.getPersons(ListFilesEnum.PRODUCERS) + 1;
//		importer.getPersonMovieCredits(ListFilesEnum.PRODUCERS, personIndexNextMarkStart);
//		
//		System.out.println("==========================================================");
//		personIndexNextMarkStart = personIndexNextMarkEnd;
//		personIndexNextMarkEnd += importer.getPersons(ListFilesEnum.WRITERS) + 1;
//		importer.getPersonMovieCredits(ListFilesEnum.WRITERS, personIndexNextMarkStart);
		
		// removing temporary fields TEMP_PERSON_LINE_NUMBER & TEMP_MOVIE_FULL_NAME
		
/*		// ------------------ dependent only on Persons
		importer.getAKANames();
		importer.getBiographies();
		
*/		// ------------------ dependent on both Persons and Movies
		
		
		return true;
		
	}
	
}
