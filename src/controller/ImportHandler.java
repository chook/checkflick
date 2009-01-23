package controller;

import java.util.HashMap;
import java.util.Map;

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
	 * imports all the data that was given to the handler to the DB using the Parser
	 * @return true if there was no error
	 */
	
	public synchronized boolean importIntoDB() {
		
	
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
	
}
