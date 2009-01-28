package model;

import java.util.regex.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.ImportHandler;
import controller.enums.*;
import controller.entity.MovieEntity;
import controller.entity.NamedEntity;
import controller.entity.NamedRelation;
import controller.filter.AbsSingleFilter;

public class DataImporter {

	private Map<ListFilesEnum, String> listfilesMap;
	static int PSTMT_BATCH_SIZE = 15000;
	static int SELECT_BUCKET_SIZE = 50000;
	
	// languagePattern explanation:
	// ([^\"].+[)}])					- group 1: movie name
	//									- 		ignore lines that start with '"' (since they are TV shows) and find the end of the movie name
	// 		 							- 		by running along the line over all characters until you encounter ')' or '}'
	// \\s+ 							- run along all the trailing whitespaces
	// ([\\P{InGreek}\\-\\s',&&[^(]]+)	- group 2: run over all different UNICODE letters including '-', ',' (comma), ''' (single quote) and whitespaces
	// 											not including '('
	// (?:\\s+\\(.+\\)\\s*)* 			- check if this group appears zero or more times
	//											the group contains whitespaces, then '(', different characters and whitespaces, and then ')'
	//											(there can be more than one comments in parentheses)
	static String languagePattern = "([^\"].+[)}])\\s+([\\P{InGreek}\\-\\s',&&[^(]]+)(?:\\s+\\(.+\\)\\s*)*";
	static String genresPattern = ".+[)}]\\s+(.+)";
	static String countriesPattern = ".+[)}]\\s+(.+)";
	
	// moviePattern explanation:
	// ([^\"].*)						- group 1: movie name, excluding names that start with '"' (TV shows)
	// \\s\\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\) 	- the parentheses after the movie name that contains the movie year (or '????')
	//												and sometimes '/I' or other greek numbers
	//                      (?:/((?:[IVX])+))	- group 2: the added part '/I' with a roman numbering to differentiate between movies with the same name and year
	//											this group would be added to the movie name
	// \\s?(?:\\((..*)\\))?				- an optional whitespace and then an optional parentheses with TV / V / VG
	//           (..*)					- group 3: the added part '(TV)' / '(V)' / '(VG)' that would be added to the movie name
	// \\s*(?:\\{\\{SUSPENDED\\}\\})?	- an optional whitespace and then an optional '{{SUSPENDED}}' notation
	// \\s*((?:[\\d\\?]){4}).*			- an optional whitespace and then the year of the movie (the same as the one written after the movie name)
	//     ((?:[\\d\\?]){4})			- group 4: the year of the movie
	static String moviesPattern = "([^\"].*)\\s\\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(?:\\((..*)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?\\s*((?:[\\d\\?]){4}).*";
	// movies2Pattern: group 1 = full movie name including year, roman notation and madeFor info / group 2 = year 
	static String movies2Pattern = "([^\"].*\\s\\((?:[\\d\\?]){4}(?:/(?:[IVX])+)?\\)\\s?(?:\\((?:..*)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*((?:[\\d\\?]){4}).*";
	static String moviesLanguagesPattern = "([^\"].*)\\s\\(([\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(\\(..*\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?\\s*([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*";
	static String moviesLanguages2Pattern = "([^\"].*\\s\\((?:[\\d\\?]){4}(?:/(?:(?:[IVX])+))?\\)\\s?(?:\\(?:..*\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*";
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
		int groupNumber = 0;
		
		// The same code is for 3 different data tables - Languages, Genres & Countries
		// therefore, only minor changes would be made to the same code
		for (int i = 1; i < 4; ++i) {
			switch (i) {
			case 1:
				System.out.println("Working on the languages file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.LANGUAGES), ListFilesEnum.LANGUAGES);
				patternRegExp = languagePattern;
				tablesEnum = DBTablesEnum.LANGUAGES;
				fieldsEnum = DBFieldsEnum.LANGUAGES_LANGUAGE_NAME;
				groupNumber = 2;
				break;
				
			case 2:
				System.out.println("Working on the genres file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.GENRES), ListFilesEnum.GENRES);
				patternRegExp = genresPattern;
				tablesEnum = DBTablesEnum.GENRES;
				fieldsEnum = DBFieldsEnum.GENRES_GENRE_NAME;
				groupNumber = 1;
				break;
				
			case 3:
				System.out.println("Working on the countries file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.COUNTRIES), ListFilesEnum.COUNTRIES);
				patternRegExp = countriesPattern;
				tablesEnum = DBTablesEnum.COUNTRIES;
				fieldsEnum = DBFieldsEnum.COUNTRIES_COUNTRY_NAME;
				groupNumber = 1;
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
						elementsSet.add(matcher.group(groupNumber));
//					if (parser.getLineNumber() % 5000 == 0)
//						System.out.println("got to line " + parser.getLineNumber() + " / elementsSet.size = " + elementsSet.size());
				}
				
				for (Object object: elementsSet) {
					System.out.println(object);
				}
				
				System.out.println("Inserting " + elementsSet.size() + " elements to the DB");
				DBManager.getInstance().insertSingleDataTypeSetToDB(elementsSet, tablesEnum, fieldsEnum);
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
		Set<MovieEntity> moviesSet = new LinkedHashSet<MovieEntity>();
		String lineFromFile;
		String patternRegExp = null;
		String movieName;
		String movieRomanNotation;
		String movieMadeFor;
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
					movieName = matcher.group(1);
					movieRomanNotation = matcher.group(2);
					movieMadeFor = matcher.group(3);
					try {
						movieYear = Integer.parseInt(matcher.group(4));
					} catch (Exception e) {
						if (matcher.group(4).equals("????")) {
							System.out.println("year was ????, changed to 0");
							movieYear = 0;
						} else
							movieYear = 1900;
					}
					moviesSet.add(new MovieEntity(0, movieName, movieYear, movieRomanNotation, movieMadeFor, 0, 0, null, null, null));
				}
				// every a certain amount of movies entered to the set, flush the results via one prepared statement batch to the DB 
				if ((moviesSet.size() > 0) && (moviesSet.size() % PSTMT_BATCH_SIZE == 0)) {
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
	
public boolean getMovies2() {
		
		Parser parser = new Parser();
		Set<MovieEntity> moviesSet = new LinkedHashSet<MovieEntity>();
		String lineFromFile;
		String patternRegExp = null;
		String movieName;
		String movieRomanNotation;
		String movieMadeFor;
		int movieYear;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		int totalElementsNum = 0;
		
		parser.loadFile(listfilesMap.get(ListFilesEnum.MOVIES), ListFilesEnum.MOVIES);
		patternRegExp = movies2Pattern;
			
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
					movieName = matcher.group(1);
					try {
						movieYear = Integer.parseInt(matcher.group(2));
					} catch (Exception e) {
						if (matcher.group(2).equals("????")) {
							System.out.println("year was ????, changed to 0");
							movieYear = 0;
						} else
							movieYear = 1900;
					}
//					System.out.println("movie name: " + movieName);
//					System.out.println("movie year: " + movieYear);
					moviesSet.add(new MovieEntity(0, movieName, movieYear, null, null, 0, 0, null, null, null));
				}
				// every a certain amount of movies entered to the set, flush the results via one prepared statement batch to the DB 
				if ((moviesSet.size() > 0) && (moviesSet.size() % PSTMT_BATCH_SIZE == 0)) {
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

	public void runOverResultSet(ResultSet moviesResultSet) {
		Map<String, Integer> moviesMap;
		Map<String, Integer> languagesMap = new HashMap<String, Integer>();
		Set<NamedRelation> movieLanguagesSet = new LinkedHashSet<NamedRelation>();
		String lineFromFile;
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		int totalElementsNum = 0;
		List<NamedEntity> languagesList;
		StringBuilder movieNameBuilder;
		String tempMovieName;
		String tempMovieDBName;
		int tempMovieId;
		int tempMovieDBYear;
		String tempMovieYear;
		String tempMovieRomanNotation;
		String tempMovieMadeFor;
		int moviesStartRow;
		int moviesEndRow;
		int tempMoviesSetCounter;
		
		moviesMap = new HashMap<String, Integer>();
		tempMoviesSetCounter = 0;
		try {
		while (moviesResultSet.next()) {
			if (moviesMap.size() == 0)
				System.out.println("moviesMap.size = 0");
			if (moviesMap.size() == 100)
				System.out.println("moviesMap.size = 100");
			tempMovieId = moviesResultSet.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName());
			tempMovieDBName = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName());
			tempMovieDBYear = moviesResultSet.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName());
			tempMovieRomanNotation = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_ROMAN_NOTATION.getFieldName());
			tempMovieMadeFor = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_MADE_FOR.getFieldName());
			if (tempMovieDBYear == 0)
				tempMovieYear = "????";
			else
				tempMovieYear = String.valueOf(tempMovieDBYear);
			
			// rebuilding the movie name as it appears on the lists, for comparing
			movieNameBuilder = new StringBuilder(tempMovieDBName);
			movieNameBuilder.append(" (").append(tempMovieYear);
			if (tempMovieRomanNotation != null)
				movieNameBuilder.append("/").append(tempMovieRomanNotation);
			movieNameBuilder.append(")");
			if (tempMovieMadeFor != null)
				movieNameBuilder.append(" (").append(tempMovieMadeFor).append(")");
			tempMovieName = movieNameBuilder.toString();
			
//			tempMovieName = "name";
//			tempMovieId = 1;
			if (moviesMap.size() == 0)
				System.out.println("before putting first element");
			moviesMap.put(tempMovieName, tempMovieId);
			if (moviesMap.size() == 1)
				System.out.println("after putting first element");
			if (moviesMap.size() == 10)
				System.out.println("moviesMap.size = 10");
			if (tempMoviesSetCounter % 5000 == 0)
				System.out.println("moviesMap - reached element no. " + tempMoviesSetCounter);
			++tempMoviesSetCounter;
		}
		} catch (SQLException e){
			
		}
	}
	
	public boolean getMoviesLanguages() {
		
		Parser parser = new Parser();
		Map<String, Integer> moviesMap = new WeakHashMap<String, Integer>();
		Map<String, Integer> languagesMap = new HashMap<String, Integer>();
		Set<NamedRelation> movieLanguagesSet = new LinkedHashSet<NamedRelation>();
		ResultSet moviesResultSet;
		String lineFromFile;
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		int totalElementsNum = 0;
		List<NamedEntity> languagesList;
		StringBuilder movieNameBuilder;
		String tempMovieName;
		String tempMovieDBName;
		int tempMovieId;
		int tempMovieDBYear;
		String tempMovieYear;
		String tempMovieRomanNotation;
		String tempMovieMadeFor;
		int moviesStartRow;
		int moviesEndRow;
		int tempMoviesSetCounter;
		
		moviesStartRow = 1;
		moviesEndRow = moviesStartRow + SELECT_BUCKET_SIZE;
		// compiling the pattern to look for in the list file
		patternRegExp = languagePattern;
		pattern = Pattern.compile(patternRegExp);
		
		// retrieving the languages list to put inside a map
		// the language map would be small enough to stay resident in memory during the whole method
		// TODO: for now, the list received from getAllNamedEntities is changed into a HashMap
		// change it so that DBManager would already return a HashMap
		languagesList = DBManager.getInstance().getAllNamedEntities(NamedEntitiesEnum.LANGUAGES);
		for (NamedEntity entity : languagesList) {
			languagesMap.put(entity.getName(), entity.getId());
		}
		
		boolean isEmpty = false;
		do {
			// retrieving part of the movies list to put inside a map
			// since the movie list is huge, we select buckets and iterate over all of them
			moviesMap = DBManager.getInstance().getAllMovies(moviesStartRow, moviesEndRow);
			if (moviesMap.size() == 0)
				isEmpty = true;
			
			if (!isEmpty) {
					// run over the Result Set, and enter all the movies there to the moviesMap
					tempMoviesSetCounter = 1;
//					System.out.println("moviesMap size = " + moviesMap.size());
//					System.out.println("trying to change setFetchSize");
//					System.out.println(moviesResultSet.getFetchSize());
//					moviesResultSet.setFetchSize(2000);
//					runOverResultSet(moviesResultSet);
//					moviesMap = new HashMap<String, Integer>();
//					while (moviesResultSet.next()) {
//						if (moviesMap.size() == 0)
//							System.out.println("moviesMap.size = 0");
//						if (moviesMap.size() == 100)
//							System.out.println("moviesMap.size = 100");
//						tempMovieId = moviesResultSet.getInt(DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName());
//						tempMovieDBName = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_NAME.getFieldName());
//						tempMovieDBYear = moviesResultSet.getInt(DBFieldsEnum.MOVIES_MOVIE_YEAR.getFieldName());
//						tempMovieRomanNotation = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_ROMAN_NOTATION.getFieldName());
//						tempMovieMadeFor = moviesResultSet.getString(DBFieldsEnum.MOVIES_MOVIE_MADE_FOR.getFieldName());
//						if (tempMovieDBYear == 0)
//							tempMovieYear = "????";
//						else
//							tempMovieYear = String.valueOf(tempMovieDBYear);
//						
//						// rebuilding the movie name as it appears on the lists, for comparing
//						movieNameBuilder = new StringBuilder(tempMovieDBName);
//						movieNameBuilder.append(" (").append(tempMovieYear);
//						if (tempMovieRomanNotation != null)
//							movieNameBuilder.append("/").append(tempMovieRomanNotation);
//						movieNameBuilder.append(")");
//						if (tempMovieMadeFor != null)
//							movieNameBuilder.append(" (").append(tempMovieMadeFor).append(")");
//						tempMovieName = movieNameBuilder.toString();
//						
////						tempMovieName = "name";
////						tempMovieId = 1;
//						if (moviesMap.size() == 0)
//							System.out.println("before putting first element");
//						moviesMap.put(tempMovieName, tempMovieId);
//						if (moviesMap.size() == 1)
//							System.out.println("after putting first element");
//						if (moviesMap.size() == 10)
//							System.out.println("moviesMap.size = 10");
//						if (tempMoviesSetCounter % 5000 == 0)
//							System.out.println("moviesMap - reached element no. " + tempMoviesSetCounter);
//						++tempMoviesSetCounter;
//					}
//					++tempMoviesSetCounter;
				
				parser.loadFile(listfilesMap.get(ListFilesEnum.LANGUAGES), ListFilesEnum.LANGUAGES);
				movieLanguagesSet = new LinkedHashSet<NamedRelation>();
				// Making sure we find the start of the list
				if (parser.findStartOfList()) {
					System.out.println("Found the start of the list!");
					while (!parser.isEOF()) {
						lineFromFile = parser.readLine();
						matcher = pattern.matcher(lineFromFile);
						matchFound = matcher.matches();
						if (!matchFound) {
							if (lineFromFile.length() > 0)
								if (!(lineFromFile.charAt(0) == '"')) {
									System.out.println("couldn't find a match on line " + parser.getLineNumber() + "!");
									System.out.println(lineFromFile);
								}
						} else {
							if (moviesMap.containsKey(matcher.group(1)) && languagesMap.containsKey(matcher.group(2))) {
//								System.out.println("found a match for " + matcher.group(1) + " AND " + matcher.group(2));
								movieLanguagesSet.add(new NamedRelation(moviesMap.get(matcher.group(1)), languagesMap.get(matcher.group(2)), null));
							}
						}
						// every a certain amount of results entered to the set, flush the results via one prepared statement batch to the DB 
						if ((movieLanguagesSet.size() > 0) && (movieLanguagesSet.size() % PSTMT_BATCH_SIZE == 0)) {
							System.out.println("reached the movies on rows " + moviesStartRow + " - " + moviesEndRow);
							System.out.println("Inserting " + movieLanguagesSet.size() + " elements to the DB");
//							DBManager.getInstance().insertMovieSingleDataTypeSetToDB(movieLanguagesSet, DBTablesEnum.MOVIE_LANGUAGES, DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID, DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID);
							totalElementsNum += movieLanguagesSet.size();
							movieLanguagesSet.clear();
						}
					}
					
					System.out.println("Inserting " + movieLanguagesSet.size() + " elements to the DB");
//					DBManager.getInstance().insertMovieSingleDataTypeSetToDB(movieLanguagesSet, DBTablesEnum.MOVIE_LANGUAGES, DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID, DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID);
					totalElementsNum += movieLanguagesSet.size();
					System.out.println("clearing movieLanguagesSet");
					movieLanguagesSet.clear();
					System.out.println("clearing moviesMap");
					moviesMap.clear();
					moviesMap = null;
					System.out.println("calling GarbageCollector");
					System.gc();
					//System.out.println("movieLanguagesSet's size after clearing is " + movieLanguagesSet.size());
					
					System.out.println("Total number of elements entered into DB: " + totalElementsNum);
		
				} else
					return false;
			}
			moviesStartRow += SELECT_BUCKET_SIZE;
			moviesEndRow += SELECT_BUCKET_SIZE;
			parser.closeFile();
			//moviesMap.clear();
			System.gc();
//			System.out.println("about to iterate over moviesMap and assign NULL to all the entries");
//			moviesMap.entrySet().removeAll();
//			for (Map.Entry<String, Integer> entry : moviesMap.entrySet()) {
//				moviesMap.entrySet().iterator().remove();				
//			}
//			System.out.println("finished assigning NULLs to moviesMap");
				
		} while (!isEmpty);

		return true;
	}
	
	public boolean getMoviesLanguages2() {
		
		Parser parser = new Parser();
		Map<String, Integer> moviesMap = new HashMap<String, Integer>();
		Map<String, Integer> languagesMap = new HashMap<String, Integer>();
		Set<NamedRelation> movieLanguagesSet = new LinkedHashSet<NamedRelation>();
		String[][] testArray = new String[2][PSTMT_BATCH_SIZE];
		ResultSet moviesResultSet;
		String lineFromFile;
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		boolean matchFound;
		int totalElementsNum = 0;
		List<NamedEntity> languagesList;
		StringBuilder movieNameBuilder;
		String tempMovieName;
		String tempMovieDBName;
		int tempMovieId;
		int tempMovieDBYear;
		String tempMovieYear;
		String tempMovieRomanNotation;
		String tempMovieMadeFor;
		int moviesStartRow;
		int moviesEndRow;
		int tempMoviesSetCounter;
		int stringArrayCount;

		// compiling the pattern to look for in the list file
		patternRegExp = moviesLanguages2Pattern;
		pattern = Pattern.compile(patternRegExp);
		stringArrayCount = 0;
						
		parser.loadFile(listfilesMap.get(ListFilesEnum.LANGUAGES), ListFilesEnum.LANGUAGES);
		// Making sure we find the start of the list
		if (parser.findStartOfList()) {
			System.out.println("Found the start of the list!");

			while (!parser.isEOF()) {
				lineFromFile = parser.readLine();
				matcher = pattern.matcher(lineFromFile);
				matchFound = matcher.matches();
				if (!matchFound) {
					if (lineFromFile.length() > 0)
						if (!(lineFromFile.charAt(0) == '"')) {
							System.out.println("couldn't find a match on line " + parser.getLineNumber() + "!");
							System.out.println(lineFromFile);
						}
				} else {
					testArray[0][stringArrayCount] = matcher.group(1);
					testArray[1][stringArrayCount] = matcher.group(2);
//					System.out.println("movie name = " + testArray[0][stringArrayCount] + " / language = " + testArray[1][stringArrayCount]);
					++stringArrayCount;
				}
				// every a certain amount of results entered to the set, flush the results via one prepared statement batch to the DB 
				if (stringArrayCount == PSTMT_BATCH_SIZE) {
					System.out.println("Inserting " + stringArrayCount + " elements to the DB");
					DBManager.getInstance().insertMovieLanguagesToDB(testArray, stringArrayCount);
					totalElementsNum += stringArrayCount;
					stringArrayCount = 0;
				}
			}
		}
			
			System.out.println("Inserting " + movieLanguagesSet.size() + " elements to the DB");
			DBManager.getInstance().insertMovieLanguagesToDB(testArray, stringArrayCount);
			totalElementsNum += stringArrayCount;
			System.out.println("Total number of elements entered into DB: " + totalElementsNum);

		return true;
	}
}
