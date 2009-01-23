package model;

/* ###################################### */
/* WILL PROBABLY BE REMOVED IN THE FUTURE */
/* ###################################### */

import java.util.regex.*;
import java.util.*;

import controller.ImportHandler;
import controller.ListFilesEnum;

public class DataImporter {

	private Map<ListFilesEnum, String> listfilesMap;
	
	// languagePattern explanation:
	// [^\"] 							- ignore lines that start with '"' (since they are TV shows)
	// .+[)}] 							- run along the line over all characters until you encounter ')' or '}'
	// \\s+ 							- run along all the trailing whitespaces
	// ([\\P{InGreek}\\-\\s',&&[^(]]+)	- group 1: run over all different UNICODE letters including '-', ',' (comma), ''' (single quote) and whitespaces
	// 											not including '('
	// (\\s+\\(.+\\)\\s*)* 				- check if this group appears zero or more times
	//										the group contains whitespaces, then '(', different characters and whitespaces, and then ')'
	//										(there can be more than one comments in parentheses)
	static String languagePattern = "[^\"].+[)}]\\s+([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*";
	
	/**
	 * DataImporter Constructor
	 * Receives the map of filenames and adds it to its private listfilesMap field
	 * @param listfilesMap
	 */
	public DataImporter(Map<ListFilesEnum, String> listfilesMap) {
		this.listfilesMap = listfilesMap;
	}
	
	/**
	 *  a test to see how things would work on the controller side
	 **/
	public static void main(String argv[]) {
	 
		ImportHandler ih;
		
		// create new ImportHandler
		ih = new ImportHandler();
		// add the different list files given from the GUI
		ih.addListFile(ListFilesEnum.LANGUAGES, "language.list");
		
		// run the importing
		ih.importIntoDB();
		
		return;
	}
	
	public boolean getLanguages() {
		
		Parser languageParser = new Parser("language.list");
		Set<String> languagesSet = new HashSet<String>();		// a dictionary to collect all the different languages
		String line;
		
		
		// Making sure we find the start of the language list
		// Searching for "LANGUAGE LIST" & "============="
		if (languageParser.findLine("LANGUAGE LIST") && languageParser.findLine("=============")) {
				  
			System.out.println("Found the start of the list!");
			Pattern pattern = Pattern.compile(languagePattern);
			Matcher matcher;
			boolean matchFound;
			
			while (!languageParser.isEOF()) {
				line = languageParser.readLine();
				matcher = pattern.matcher(line);
				matchFound = matcher.matches();
				if (!matchFound) {
					if (!(line.charAt(0) == '"')) {
						System.out.println(line);
						System.out.println("couldn't found a match on line " + languageParser.getLineNumber() + "!");
					}
				} else
					  languagesSet.add(matcher.group(1));
			}
			
			DBManager.getInstance().insertSetToDB(languagesSet, DBTablesEnum.LANGUAGES, DBFieldsEnum.LANGUAGES_LANGUAGE_NAME);
		} else
			return false;
		
		languageParser.closeFile();
		return true;
	}
	 
}
