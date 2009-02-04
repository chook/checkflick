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
		boolean isOkay = true;
		
		// going over all the different elements in the ListFilesEnum,
		// and making sure every list was attached a filename, and that all files exist
		for (ListFilesEnum enumField: ListFilesEnum.values()) {
			if (!(listfilesMap.containsKey(enumField))) {
				isOkay = false;
				errMsg += "a filename for the " + enumField.toString().toLowerCase() + " wasn't given\n";
			} 
		}
		
		if (!isOkay)
			System.out.println(errMsg);
			
		return isOkay;
	}
	

	
	/**
	 * imports all the data that was given to the handler to the DB using the Parser
	 * @return true if there was no error
	 */
	
	public synchronized boolean importIntoDB() {
		
		DataImporter importer;
		
		// check that all files were given 
		if (!verifyFiles())
			return false;
	
		importer = new DataImporter(listfilesMap);
		
		importer.importDataTypes();
		
		// creating temp field used to import movies and other entities
		importer.createMoviesTempField();
		importer.importMovies();
		
		importer.importMoviesDataTypes();
		importer.importPersonsAndCredits();
		
		// removing the temp field used to import movies and other entities
		importer.deleteMoviesTempField();
		
		// add references to new primary keys that were created during the import
		importer.finishImport();
		
		return true;
		
	}
	
}
