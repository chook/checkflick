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
	static String genresPattern = ".+[)}]\\s+(.+)";
	
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
//		ih.addListFile(ListFilesEnum.LANGUAGES, "language.list");
		ih.addListFile(ListFilesEnum.GENRES, "genres.list");
		
		// run the importing
		ih.importIntoDB();
		
		return;
	}
	
	public boolean getLanguages() {
		
		Parser languageParser = new Parser("lists\\language.list");
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
				
				if (languageParser.getLineNumber() % 1000 == 0)
					System.out.println("reached line " + languageParser.getLineNumber());
			}
			
			System.out.println("Inserting " + languagesSet.size() + " languages to the DB");
			DBManager.getInstance().insertSetToDB(languagesSet, DBTablesEnum.LANGUAGES, DBFieldsEnum.LANGUAGES_LANGUAGE_NAME);
		} else
			return false;
		
		languageParser.closeFile();
		return true;
	}
	

	public boolean getLanguagesGenresCountries() {
		
		Parser parser = new Parser();
		Set<String> elementsSet = new HashSet<String>();		// a dictionary to collect all the different elements
		String line;
		String listStartLine1 = null;
		String listStartLine2 = null;
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		DBTablesEnum tablesEnum = null;
		DBFieldsEnum fieldsEnum = null;
		
		// The same code is for 3 different data tables - Languages, Genres & Countries
		// therefore, only minor changes would be made to the same code
		for (int i = 1; i < 3; ++i) {
			switch (i) {
			case 1:
				parser.loadFile("lists\\language.list");
				listStartLine1 = "LANGUAGE LIST";
				listStartLine2 = "=============";
				patternRegExp = languagePattern;
				tablesEnum = DBTablesEnum.LANGUAGES;
				fieldsEnum = DBFieldsEnum.LANGUAGES_LANGUAGE_NAME;
				break;
				
			case 2:
				parser.loadFile("lists\\genres.list");
				listStartLine1 = "8: THE GENRES LIST";
				listStartLine2 = "==================";
				patternRegExp = genresPattern;
				tablesEnum = DBTablesEnum.GENRES;
				fieldsEnum = DBFieldsEnum.GENRES_GENRE_NAME;
				break;
				
			case 3:
				parser.loadFile("lists\\countries.list");
				break;
			}
		
			
			// Making sure we find the start of the language list
			// Searching for "LANGUAGE LIST" & "============="
			if (parser.findLine(listStartLine1) && parser.findLine(listStartLine2)) {
				System.out.println("Found the start of the list!");
				
				pattern = Pattern.compile(patternRegExp);
				elementsSet.clear();
				
				while (!parser.isEOF()) {
					line = parser.readLine();
					matcher = pattern.matcher(line);
					matchFound = matcher.matches();
					if (!matchFound) {
						if (line.length() > 0)
							if (!(line.charAt(0) == '"')) {
								System.out.println("couldn't found a match on line " + parser.getLineNumber() + "!");
								System.out.println(line);
							}
					} else {
	//					System.out.println("found the genre: " + matcher.group(2));
						elementsSet.add(matcher.group(1));
					}
	/*				if (parser.getLineNumber() % 1000 == 0)
						System.out.println("reached line " + parser.getLineNumber());
	*/			}
				
				for (Object object: elementsSet) {
					System.out.println(object);
				}
				
				System.out.println("Inserting " + elementsSet.size() + " elements to the DB");
//				DBManager.getInstance().insertSetToDB(elementsSet, tablesEnum, fieldsEnum);
			} else
				return false;
			
			parser.closeFile();
		}
		
		return true;
	}
	 
}
