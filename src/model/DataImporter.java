package model;

import java.util.regex.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.ImportHandler;
import controller.ListFilesEnum;
import controller.NamedEntitiesEnum;
import controller.entity.MovieEntity;
import controller.entity.NamedEntity;
import controller.filter.AbsSingleFilter;

public class DataImporter {

	private Map<ListFilesEnum, String> listfilesMap;
	static int PSTMT_BATCH_SIZE = 15000;
	
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
	static String countriesPattern = ".+[)}]\\s+(.+)";
	
	// moviePattern explanation:
	// ([^\"].+)						- group 1: movie name, excluding names that start with '"' (TV shows)
	// \\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\) 	- the parentheses after the movie name that contains the movie year (or '????')
	//											and sometimes '/I' or other greek numbers
	//                   (?:/((?:[IVX])+))		- group 2: the added part '/I' with a greek numbering to differentiate between movies with the same name and year
	//											this group would be added to the movie name
	// \\s?(\\(..*\\))?					- an optional whitespace and then an optional parentheses with TV / V / VG
	//     (\\(..*\\))					- group 3: the added part '(TV)' / '(V)' / '(VG') that would be added to the movie name
	// \\s*(?:\\{\\{SUSPENDED\\}\\})?	- an optional whitespace and then an optional '{{SUSPENDED}}' notation
	// \\s*((?:[\\d\\?]){4}).*			- an optional whitespace and then the year of the movie (the same as the one written after the movie name)
	//     ((?:[\\d\\?]){4})			- group 4: the year of the movie
	static String moviesPattern = "([^\"].+)\\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(\\(..*\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?\\s*((?:[\\d\\?]){4}).*";
	static String moviesLanguagesPattern = "([^\"].+)\\(([\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(\\(..*\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?\\s*([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*";
	static String moviesNameDBPattern = "([^\"](.)+)(?:\\(((?:[IVX])+)\\))?\\s?(\\(..*\\))?";
	
	
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
		ih.addListFile(ListFilesEnum.LANGUAGES, "lists\\language.list");
		ih.addListFile(ListFilesEnum.GENRES, "lists\\genres.list");
		ih.addListFile(ListFilesEnum.COUNTRIES, "lists\\countries.list");
		ih.addListFile(ListFilesEnum.MOVIES, "lists\\movies.list");
		
		// run the importing
		ih.importIntoDB();
		
		return;
	}
	
	/**
	 * parses the languages, genres & countries lists
	 * and adds all the data to the DB
	 * @return boolean if the method succeeded
	 */
	public boolean getLanguagesGenresCountries() {
		
		Parser parser = new Parser();
		Set<String> elementsSet = new HashSet<String>();		// a dictionary to collect all the different elements
		String lineFromFile;
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		DBTablesEnum tablesEnum = null;
		DBFieldsEnum fieldsEnum = null;
		
		// The same code is for 3 different data tables - Languages, Genres & Countries
		// therefore, only minor changes would be made to the same code
		for (int i = 1; i < 4; ++i) {
			switch (i) {
			case 1:
				parser.loadFile(listfilesMap.get(ListFilesEnum.LANGUAGES), ListFilesEnum.LANGUAGES);
				patternRegExp = languagePattern;
				tablesEnum = DBTablesEnum.LANGUAGES;
				fieldsEnum = DBFieldsEnum.LANGUAGES_LANGUAGE_NAME;
				break;
				
			case 2:
				parser.loadFile(listfilesMap.get(ListFilesEnum.GENRES), ListFilesEnum.GENRES);
				patternRegExp = genresPattern;
				tablesEnum = DBTablesEnum.GENRES;
				fieldsEnum = DBFieldsEnum.GENRES_GENRE_NAME;
				break;
				
			case 3:
				parser.loadFile(listfilesMap.get(ListFilesEnum.COUNTRIES), ListFilesEnum.COUNTRIES);
				patternRegExp = countriesPattern;
				tablesEnum = DBTablesEnum.COUNTRIES;
				fieldsEnum = DBFieldsEnum.COUNTRIES_COUNTRY_NAME;
				break;
			}
		
			
			// Making sure we find the start of the list
			if (parser.findStartOfList()) {
				System.out.println("Found the start of the list!");
				
				pattern = Pattern.compile(patternRegExp);
				elementsSet.clear();
				
				while (!parser.isEOF()) {
					lineFromFile = parser.readLine();
					matcher = pattern.matcher(lineFromFile);
					matchFound = matcher.matches();
					if (!matchFound) {
						if (lineFromFile.length() > 0)
							if (!(lineFromFile.charAt(0) == '"')) {
								System.out.println("couldn't found a match on line " + parser.getLineNumber() + "!");
								System.out.println(lineFromFile);
							}
					} else
						elementsSet.add(matcher.group(1));
				}
				
				for (Object object: elementsSet) {
					System.out.println(object);
				}
				
				System.out.println("Inserting " + elementsSet.size() + " elements to the DB");
				DBManager.getInstance().insertSetToDB(elementsSet, tablesEnum, fieldsEnum);
			} else
				return false;
			
			// TODO: after inserting the generic data, there are some exception that for
			// efficiency reasons, aren't dealt with during parsing.
			// this method will handle the specific problems rising from the IMDb lists
			// i.e. language "Aboriginal" has text after it
			//fixSpecialCases(tablesEnum);
			
			parser.closeFile();
		}
		
		return true;
	}


	public boolean getMovies() {
		
		Parser parser = new Parser();
		Set<MovieEntity> moviesSet = new HashSet<MovieEntity>();
		String lineFromFile;
		String patternRegExp = null;
		StringBuilder movieName;
		int movieYear;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		int totalElementsNum = 0;
		
		parser.loadFile(listfilesMap.get(ListFilesEnum.MOVIES), ListFilesEnum.MOVIES);
		patternRegExp = moviesPattern;
			
		// Making sure we find the start of the list
		if (parser.findStartOfList()) {
			System.out.println("Found the start of the list!");
			
			pattern = Pattern.compile(patternRegExp);
			

			while (!parser.isEOF()) {
				lineFromFile = parser.readLine();
				matcher = pattern.matcher(lineFromFile);
				matchFound = matcher.matches();
				if (!matchFound) {
					if (lineFromFile.length() > 0)
						if (!(lineFromFile.charAt(0) == '"')) {
							System.out.println("couldn't found a match on line " + parser.getLineNumber() + "!");
							System.out.println(lineFromFile);
						}
				} else {
					movieName = new StringBuilder(matcher.group(1));
					if (matcher.group(2) != null)
						movieName.append(" (").append(matcher.group(2)).append(")");
					if (matcher.group(3) != null)
						movieName.append(" ").append(matcher.group(3));
					try {
						movieYear = Integer.parseInt(matcher.group(4));
					} catch (Exception e) {
						if (matcher.group(4).equals("????")) {
							System.out.println("year was ????, changed to 0");
							movieYear = 0;
						} else
							movieYear = 1900;
					}
					moviesSet.add(new MovieEntity(0, movieName.toString(), movieYear, 0, 0, null, null, null));
				}
				// every 10,000 movies, flush the results via one prepared statement batch to the DB 
				if (parser.getLineNumber() % PSTMT_BATCH_SIZE == 0) {
					System.out.println("got to line " + parser.getLineNumber() + ", uploading to DB...");
					System.out.println("Inserting " + moviesSet.size() + " elements to the DB");
					DBManager.getInstance().insertMoviesSetToDB(moviesSet);
					totalElementsNum += moviesSet.size();
					moviesSet.clear();
				}
			}
			
			System.out.println("Inserting " + moviesSet.size() + " elements to the DB");
			DBManager.getInstance().insertMoviesSetToDB(moviesSet);
			totalElementsNum += moviesSet.size();
			
			System.out.println("Total number of elements entered into DB: " + totalElementsNum);

		} else
			return false;
		
		parser.closeFile();
		
		return true;
	}

	
	public boolean getMoviesLanguages() {
		
		Parser parser = new Parser();
		Map<Integer, String> moviesMap = new HashMap<Integer, String>();
		Map<Integer, String> languagesMap = new HashMap<Integer, String>();
		ResultSet moviesResultSet;
		String lineFromFile;
		String patternRegExp = null;
		int movieYear;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		int totalElementsNum = 0;
		List<NamedEntity> languagesList;
		AbsSingleFilter moviesFilter;
		StringBuilder movieNameBuilder;
		String tempMovieOriginalName;
		String tempMovieDBName;
		int tempMovieDBId;
		int tempMovieDBYear;
		String tempMovieOriginalYear;
		
		// retrieving first set of results from DB
		moviesResultSet = DBManager.getInstance().getAllMovies(0, 100);
		
		try {
			pattern = Pattern.compile(moviesNameDBPattern);
			while (moviesResultSet.next()) {
				tempMovieDBId = moviesResultSet.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName());
				tempMovieDBName = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName());
				tempMovieDBYear = moviesResultSet.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName());
				if (tempMovieDBYear == 0)
					tempMovieOriginalYear = "????";
				else
					tempMovieOriginalYear = String.valueOf(tempMovieDBYear);
				
				matcher = pattern.matcher(tempMovieDBName);
				matchFound = matcher.matches();
				if (matchFound) {
					movieNameBuilder = new StringBuilder(matcher.group(1));
					movieNameBuilder.append(" (").append(tempMovieOriginalYear);
					if (matcher.group(2) != null)
						movieNameBuilder.append("/").append(matcher.group(2));
					movieNameBuilder.append(")");
					if (matcher.group(3) != null)
						movieNameBuilder.append(" ").append(matcher.group(3));
					System.out.println(movieNameBuilder.toString());
				} else
					System.out.println("no match found");
				
				
//				tempMovie = DBMananger.fillMovieFromSet(moviesResultSet);
//				moviesMap.put(tempMovie.getId(), tempMovie.getName());				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// TODO: for now, the list received from getAllNamedEntities is changed into a HashMap
		// change it so that DBManager would already return a HashMap
//		languagesList = DBManager.getInstance().getAllNamedEntities(NamedEntitiesEnum.LANGUAGES);
//		for (NamedEntity entity : languagesList) {
//			languagesMap.put(entity.getId(), entity.getName());
//		}
//		
//		parser.loadFile(listfilesMap.get(ListFilesEnum.LANGUAGES), ListFilesEnum.LANGUAGES);
//		
//		patternRegExp = moviesLanguagesPattern;
//			
//		// Making sure we find the start of the list
//		if (parser.findStartOfList()) {
//			System.out.println("Found the start of the list!");
//			
//			pattern = Pattern.compile(patternRegExp);
//
//			while (!parser.isEOF()) {
//				lineFromFile = parser.readLine();
//				matcher = pattern.matcher(lineFromFile);
//				matchFound = matcher.matches();
//				if (!matchFound) {
//					if (lineFromFile.length() > 0)
//						if (!(lineFromFile.charAt(0) == '"')) {
//							System.out.println("couldn't found a match on line " + parser.getLineNumber() + "!");
//							System.out.println(lineFromFile);
//						}
//				} else {
//					
//
////					moviesSet.add(new MovieEntity(0, movieName.toString(), movieYear, 0, 0, null, null, null));
//				}
////				// every 10,000 movies, flush the results via one prepared statement batch to the DB 
////				if (parser.getLineNumber() % PSTMT_BATCH_SIZE == 0) {
////					System.out.println("got to line " + parser.getLineNumber() + ", uploading to DB...");
////					System.out.println("Inserting " + moviesSet.size() + " elements to the DB");
////					DBManager.getInstance().insertMoviesSetToDB(moviesSet);
////					totalElementsNum += moviesSet.size();
////					moviesSet.clear();
////				}
//			}
////			
////			System.out.println("Inserting " + moviesSet.size() + " elements to the DB");
////			DBManager.getInstance().insertMoviesSetToDB(moviesSet);
////			totalElementsNum += moviesSet.size();
////			
////			System.out.println("Total number of elements entered into DB: " + totalElementsNum);
////
//		} else
//			return false;
		
		parser.closeFile();
		
		return true;
	}
}
