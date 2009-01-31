package model;

import java.util.regex.*;
import java.util.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import controller.ImportHandler;
import controller.enums.*;
import controller.entity.MovieEntity;
import controller.entity.NamedEntity;
import controller.entity.Relation;
import controller.entity.NamedRelation;
import controller.entity.CastingRelation;
import controller.filter.AbsSingleFilter;

public class DataImporter {

	private Map<ListFilesEnum, String> listfilesMap;
	static int PSTMT_BATCH_SIZE = 25000;
	static int SELECT_BUCKET_SIZE = 100000;
	
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
	static String genresCountriesPattern = "(.+[)}])\\s+(.+)";
	
	// moviePattern explanation:
	// (([^\"].*)\\s\\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(?:\\((..*)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?) 	- group 1: the full movie name
	// ([^\"].*)						- group 2: movie name, excluding names that start with '"' (TV shows)
	// \\s\\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\) 	- the parentheses after the movie name that contains the movie year (or '????')
	//												and sometimes '/I' or other greek numbers
	//                      (?:/((?:[IVX])+))	- group 3: the added part '/I' with a roman numbering to differentiate between movies with the same name and year
	//											this group would be added to the movie name
	// \\s?(?:\\((..?)\\))?				- an optional whitespace and then an optional parentheses with TV / V / VG
	//           (..?)					- group 4: the added part '(TV)' / '(V)' / '(VG)' that would be added to the movie name
	// \\s*(?:\\{\\{SUSPENDED\\}\\})?	- an optional whitespace and then an optional '{{SUSPENDED}}' notation
	// \\s*((?:[\\d\\?]){4}).*			- an optional whitespace and then the year of the movie (the same as the one written after the movie name)
	//     ((?:[\\d\\?]){4})			- group 5: the year of the movie
	static String moviesPattern = "(([^\"].*)\\s\\((?:[\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(?:\\((..?)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*((?:[\\d\\?]){4}).*";
	// movies2Pattern: group 1 = full movie name including year, roman notation and madeFor info / group 2 = year 
	static String movies2Pattern = "([^\"].*\\s\\((?:[\\d\\?]){4}(?:/(?:[IVX])+)?\\)\\s?(?:\\((?:..?)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*((?:[\\d\\?]){4}).*";
	static String moviesLanguagesPattern = "([^\"].*)\\s\\(([\\d\\?]){4}(?:/((?:[IVX])+))?\\)\\s?(\\(..?\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?\\s*([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*";
	static String moviesLanguages2Pattern = "([^\"].*\\s\\((?:[\\d\\?]){4}(?:/(?:(?:[IVX])+))?\\)\\s?(?:\\(?:..?\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*";
	static String moviesNameDBPattern = "([^\"](.)+)(?:\\(((?:[IVX])+)\\))?\\s?(\\(..*\\))?";
	// general personsPattern:
	// group 1 = person name
	static String personsPattern = "((?:[^\\t])+)\\t+.+";
	// actorsMoviesPattern groups: 
	//group 1 = actor name
	//group 2 = full movie name
	//group 3 = role
	//group 4 = credit location
	static String actorsMoviesPattern = "((?:[^\\t])+)?\\t+((?:[^\"\\t].*)\\s\\((?:[\\d\\?]){4}(?:/(?:[IVX]+))?\\)\\s?(?:\\((?:..?)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*(?:\\(.+\\)\\s*)*(?:\\[(.+)\\])*\\s*(?:<(\\d+)>)*";
	// nonActorsMoviesPattern groups:
	// group 1 = non actor name
	// group 2 = full movie name
	static String nonActorsMoviesPattern = "((?:[^\\t])+)?\\t+((?:[^\"\\t].*)\\s\\((?:[\\d\\?]){4}(?:/(?:[IVX]+))?\\)\\s?(?:\\((?:..?)\\))?\\s*(?:\\{\\{SUSPENDED\\}\\})?)\\s*(?:\\(.+\\)\\s*)*(?:<(?:[\\d,])+>)?";

	
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
//		ih.addListFile(ListFilesEnum.MOVIES, "lists\\movies.list");
//		ih.addListFile(ListFilesEnum.ACTORS, "lists\\actors.list");
//		ih.addListFile(ListFilesEnum.ACTRESSES, "lists\\actresses.list");
//		ih.addListFile(ListFilesEnum.DIRECTORS, "lists\\directors.list");
//		ih.addListFile(ListFilesEnum.PRODUCERS, "lists\\producers.list");
//		ih.addListFile(ListFilesEnum.WRITERS, "lists\\writers.list");
//		ih.addListFile(ListFilesEnum.MOVIES, "lists\\moviesSHORT.list");
//		ih.addListFile(ListFilesEnum.ACTORS, "lists\\actorsSHORT.list");
//		ih.addListFile(ListFilesEnum.ACTRESSES, "lists\\actressesSHORT.list");
//		ih.addListFile(ListFilesEnum.DIRECTORS, "lists\\directorsSHORT.list");
//		ih.addListFile(ListFilesEnum.PRODUCERS, "lists\\producersSHORT.list");
//		ih.addListFile(ListFilesEnum.WRITERS, "lists\\writersSHORT.list");
		ih.addListFile(ListFilesEnum.MOVIES, "lists\\movies.list");
		ih.addListFile(ListFilesEnum.ACTORS, "lists\\actors.list");
		ih.addListFile(ListFilesEnum.ACTRESSES, "lists\\actresses.list");
		ih.addListFile(ListFilesEnum.DIRECTORS, "lists\\directors.list");
		ih.addListFile(ListFilesEnum.PRODUCERS, "lists\\producers.list");
		ih.addListFile(ListFilesEnum.WRITERS, "lists\\writers.list");
		
		// run the importing
		ih.importIntoDB();
		
		return;
	}
	
	/**
	 * parses the languages, genres & countries lists
	 * and adds all the data to the DB
	 * @return boolean if the method succeeded
	 */
	public boolean importLanguagesGenresCountries() {
		
		Parser parser = new Parser();
		Set<String> elementsSet = new HashSet<String>();		// a dictionary to collect all the different elements
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
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
				break;
				
			case 2:
				System.out.println("Working on the genres file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.GENRES), ListFilesEnum.GENRES);
				patternRegExp = genresCountriesPattern;
				tablesEnum = DBTablesEnum.GENRES;
				fieldsEnum = DBFieldsEnum.GENRES_GENRE_NAME;
				break;
				
			case 3:
				System.out.println("Working on the countries file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.COUNTRIES), ListFilesEnum.COUNTRIES);
				patternRegExp = genresCountriesPattern;
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
					matcher = pattern.matcher(parser.readLine());
					if (matcher.matches())
						elementsSet.add(matcher.group(2));
				}

				System.out.println("Inserting " + elementsSet.size() + " elements to the DB");
				DBManager.getInstance().insertSingleDataTypeSetToDB(elementsSet, tablesEnum, fieldsEnum);
			} else
				return false;

			parser.closeFile();
		}
		return true;
	}


	/**
	 * imports all the different movie names from the movies list
	 * @return boolean whether the method succeeded
	 */
	public boolean importMovies() {
		
		Parser parser = new Parser();
		Set<MovieEntity> moviesSet = new LinkedHashSet<MovieEntity>();
		String patternRegExp = null;
		String fullMovieName, movieName, movieRomanNotation, movieMadeFor;
		int movieYear;
		Pattern pattern;
		Matcher matcher;
		int totalElementsNum = 0;
		
		parser.loadFile(listfilesMap.get(ListFilesEnum.MOVIES), ListFilesEnum.MOVIES);
		patternRegExp = moviesPattern;
			
		// Making sure we find the start of the list
		if (parser.findStartOfList()) {
			System.out.println("Found the start of the list!");
			
			pattern = Pattern.compile(patternRegExp);
			
			while (!parser.isEOF()) {
				matcher = pattern.matcher(parser.readLine());
				if (matcher.matches()) {
					fullMovieName = matcher.group(1).trim();
					movieName = matcher.group(2);
					movieRomanNotation = matcher.group(3);
					movieMadeFor = matcher.group(4);
					try {
						movieYear = Integer.parseInt(matcher.group(5));
					} catch (Exception e) {
						if (matcher.group(5).equals("????")) {
							movieYear = 0;
						} else
							movieYear = 1900;
					}
					// NOTE: the fullMovieName is a temporary field used during the import and dropped from the DB at the end
					// it doesn't have a field in the MovieEntity, so for this occasion only, it is sent instead of the taglines field
					moviesSet.add(new MovieEntity(0, movieName, movieYear, movieRomanNotation, movieMadeFor, 0, 0, fullMovieName, null, null));
				}
				// flush results every BATCH_SIZE 
				if ((moviesSet.size() > 0) && (moviesSet.size() % PSTMT_BATCH_SIZE == 0)) {
					System.out.println("Inserting " + moviesSet.size() + " elements to the DB");
					DBManager.getInstance().insertMoviesSetToDB(moviesSet);
					totalElementsNum += moviesSet.size();
					moviesSet.clear();
				}
			}
			// flush the results that were left
			if ((moviesSet.size() > 0)) {
				System.out.println("Inserting " + moviesSet.size() + " elements to the DB");
				DBManager.getInstance().insertMoviesSetToDB(moviesSet);
				totalElementsNum += moviesSet.size();
			}
			System.out.println("Total number of elements entered into DB: " + totalElementsNum);

		} else
			return false;

		parser.closeFile();

		return true;
	}
	
	/**
	 * the function receives the name of the list to extract persons, and imports them to the DB
	 * @return int the number of persons entered to the DB. Returns -1 if failed.
	 */
	private int getPersons(ListFilesEnum listType) {

		Parser parser = new Parser();
		Set<NamedEntity> personsSet = new LinkedHashSet<NamedEntity>();
		String patternRegExp = null;
		String personName;
		Pattern pattern;
		Matcher matcher;
		int totalElementsNum = 0;
		int tempLineNumber;

		switch (listType) {
		case ACTORS:
		case ACTRESSES:
		case DIRECTORS:
		case PRODUCERS:
		case WRITERS:
			patternRegExp = personsPattern;
			break;
		}

		parser.loadFile(listfilesMap.get(listType), listType);
		// Making sure we find the start of the list
		if (parser.findStartOfList()) {
			System.out.println("Found the start of the list!");
			pattern = Pattern.compile(patternRegExp);
			while (!parser.isEOF()) {
				matcher = pattern.matcher(parser.readLine());
				if (matcher.matches()) {
					tempLineNumber = parser.getLineNumber() - 1;
					personName = matcher.group(1);
					// NOTE: tempLineNumber is a temporary field used during the import and dropped from the DB at the end
					// it doesn't have a field in the NamedEntity, so for this occasion only, it is sent instead of the ID field 
					personsSet.add(new NamedEntity(tempLineNumber, personName));
				}
				// flush results every BATCH_SIZE 
				if ((personsSet.size() > 0) && (personsSet.size() % PSTMT_BATCH_SIZE == 0)) {
					System.out.println("Inserting " + personsSet.size() + " elements to the DB");
					DBManager.getInstance().insertPersonsSetToDB(personsSet);
					totalElementsNum += personsSet.size();
					personsSet.clear();
				}
			}
			// flush the results that were left
			if ((personsSet.size() > 0)) {
				System.out.println("Inserting " + personsSet.size() + " elements to the DB");
				DBManager.getInstance().insertPersonsSetToDB(personsSet);
				totalElementsNum += personsSet.size();
			}
			System.out.println("Total number of elements entered into DB: " + totalElementsNum);

		} else
			return -1;

		parser.closeFile();

		return totalElementsNum;
	}
	
	public void importPersonsAndCredits() {
		
		int[] personIndexNextMarkStart = new int[5]; 
		
		// creating the temporary fields in PERSONS needed for the import
		createTempFields();
		
		System.out.println("==========================================================");
		// adding the different persons from all the different lists
		personIndexNextMarkStart[0] = 1;
		personIndexNextMarkStart[1] = personIndexNextMarkStart[0] + getPersons(ListFilesEnum.ACTORS);
		personIndexNextMarkStart[2] = personIndexNextMarkStart[1] + getPersons(ListFilesEnum.ACTRESSES);
		personIndexNextMarkStart[3] = personIndexNextMarkStart[2] + getPersons(ListFilesEnum.DIRECTORS);
		personIndexNextMarkStart[4] = personIndexNextMarkStart[3] + getPersons(ListFilesEnum.PRODUCERS);
		getPersons(ListFilesEnum.WRITERS);
		
		// entering a tempPersonID for the persons, exactly as their personID
		preparePersonsTempFields();
		findAndUpdateDuplicates();
		
		getPersonMovieCredits(ListFilesEnum.ACTORS, personIndexNextMarkStart[0]);
		getPersonMovieCredits(ListFilesEnum.ACTRESSES, personIndexNextMarkStart[1]);
		getPersonMovieCredits(ListFilesEnum.DIRECTORS, personIndexNextMarkStart[2]);
		getPersonMovieCredits(ListFilesEnum.PRODUCERS, personIndexNextMarkStart[3]);
		getPersonMovieCredits(ListFilesEnum.WRITERS, personIndexNextMarkStart[4]);
		
		// removing the records in the PRESONS table marked as duplicates
		removeDuplicates();
//		deletePersonsTempFields();
	}
	
	
	/**
	 * creates 3 fields needed for the import in the PERSONS table:
	 * TEMP_PERSON_ID, TEMP_PERSON_LINE_NUMBER & TEMP_IS_DUPLICATE
	 */
	private void createTempFields() {

		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD %s NUMBER",
														DBTablesEnum.PERSONS.getTableName(), 
														DBFieldsEnum.PERSONS_TEMP_PERSON_ID.getFieldName()));
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD %s NUMBER",
														DBTablesEnum.PERSONS.getTableName(), 
														DBFieldsEnum.PERSONS_TEMP_PERSON_LINE_NUMBER.getFieldName()));
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD %s CHAR(1)",
														DBTablesEnum.PERSONS.getTableName(), 
														DBFieldsEnum.PERSONS_TEMP_IS_DUPLICATE.getFieldName()));
	}
	
	/**
	 * prepares the temporary fields for the import
	 * - copy the PERSON_ID to the TEMP_PERSON_ID
	 * - mark the IS_DUPLICATE field as 'N'
	 */
	private void preparePersonsTempFields() {
		
		// copy the PERSON_ID to the TEMP_PERSON_ID
		DBManager.getInstance().executeSQL(String.format("UPDATE %s SET %s = %s",
														DBTablesEnum.PERSONS.getTableName(), 
														DBFieldsEnum.PERSONS_TEMP_PERSON_ID.getFieldName(),
														DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));
		// mark the TO_DELETE field as 'N'
		DBManager.getInstance().executeSQL(String.format("UPDATE %s SET %s = 'N'",
														DBTablesEnum.PERSONS.getTableName(), 
														DBFieldsEnum.PERSONS_TEMP_IS_DUPLICATE.getFieldName()));
	}
	
	private void findAndUpdateDuplicates() {
		DBManager.getInstance().findAndUpdateDuplicates();
	}
	
	/**
	 * removes the records that are duplicates and aren't needed
	 */
	private void removeDuplicates() {
	
		// removing the records that are marked as duplicates and aren't needed
		DBManager.getInstance().executeSQL(String.format("DELETE FROM %s WHERE %s = 'Y'",
														DBTablesEnum.PERSONS.getTableName(),
														DBFieldsEnum.PERSONS_TEMP_IS_DUPLICATE.getFieldName()));
	}

	/**
	 * TEMP_PERSON_ID, TEMP_PERSON_LINE_NUMBER & TEMP_IS_DUPLICATE
	 * removes the temporary fields that were used for the import in the PERSONS table:
	 */
	private void deletePersonsTempFields() {
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s DROP COLUMN %s",
														DBTablesEnum.PERSONS.getTableName(),
														DBFieldsEnum.PERSONS_TEMP_IS_DUPLICATE.getFieldName()));
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s DROP COLUMN %s",
														DBTablesEnum.PERSONS.getTableName(),
														DBFieldsEnum.PERSONS_TEMP_PERSON_ID.getFieldName()));
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s DROP COLUMN %s",
														DBTablesEnum.PERSONS.getTableName(),
														DBFieldsEnum.PERSONS_TEMP_PERSON_LINE_NUMBER.getFieldName()));
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
	
	/**
	 * retrieves all the different languages for each and every movie
	 * @return boolean whether the method succeeded
	 */
	public boolean importMoviesLanguagesGenresCountries() {
		
		Parser parser = new Parser();
		Map<String, Integer> moviesMap = new WeakHashMap<String, Integer>();
		Map<String, Integer> datatypesMap = new HashMap<String, Integer>();
		Set<NamedRelation> movieDatatypeSet = new LinkedHashSet<NamedRelation>();
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		int totalElementsNum = 0;
		List<NamedEntity> datatypesList;
		int moviesStartRow;
		int moviesEndRow;
		String tempMovieName = null;
		DBTablesEnum tablesEnum = null;
		DBFieldsEnum movieFieldsEnum = null;
		DBFieldsEnum datatypeFieldsEnum = null;
		NamedEntitiesEnum namedEntitiesEnum = null;

		// The same code is for 3 different data tables - Languages, Genres & Countries
		// therefore, only minor changes would be made to the same code
		for (int i = 1; i < 4; ++i) {
			switch (i) {
			case 1:
				System.out.println("Working on the languages file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.LANGUAGES), ListFilesEnum.LANGUAGES);
				patternRegExp = languagePattern;
				tablesEnum = DBTablesEnum.MOVIE_LANGUAGES;
				movieFieldsEnum = DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID;
				datatypeFieldsEnum = DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID;
				namedEntitiesEnum = namedEntitiesEnum.LANGUAGES;
				break;
				
			case 2:
				System.out.println("Working on the genres file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.GENRES), ListFilesEnum.GENRES);
				patternRegExp = genresCountriesPattern;
				tablesEnum = DBTablesEnum.MOVIE_GENRES;
				movieFieldsEnum = DBFieldsEnum.MOVIE_GENRES_MOVIE_ID;
				datatypeFieldsEnum = DBFieldsEnum.MOVIE_GENRES_GENRE_ID;
				namedEntitiesEnum = namedEntitiesEnum.GENRES;
				break;
				
			case 3:
				System.out.println("Working on the countries file");
				parser.loadFile(listfilesMap.get(ListFilesEnum.COUNTRIES), ListFilesEnum.COUNTRIES);
				patternRegExp = genresCountriesPattern;
				tablesEnum = DBTablesEnum.MOVIE_COUNTRIES;
				movieFieldsEnum = DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID;
				datatypeFieldsEnum = DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID;
				namedEntitiesEnum = namedEntitiesEnum.COUNTRIES;
				break;
			}
		}
			
		moviesStartRow = 1;
		moviesEndRow = moviesStartRow + SELECT_BUCKET_SIZE;
		// compiling the pattern to look for in the list file
		pattern = Pattern.compile(patternRegExp);

		// retrieving the list to put inside a map
		// the language map would be small enough to stay resident in memory during the whole method
		datatypesList = DBManager.getAllNamedEntities(namedEntitiesEnum);
		for (NamedEntity entity : datatypesList) {
			datatypesMap.put(entity.getName(), entity.getId());
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
				movieDatatypeSet = new LinkedHashSet<NamedRelation>();
				// Making sure we find the start of the list
				if (parser.findStartOfList()) {
					System.out.println("Found the start of the list!");
					while (!parser.isEOF()) {
						matcher = pattern.matcher(parser.readLine());
						if (matcher.matches()) {
							tempMovieName = matcher.group(1).trim();
							if (moviesMap.containsKey(tempMovieName) && datatypesMap.containsKey(matcher.group(2)))
								movieDatatypeSet.add(new NamedRelation(moviesMap.get(tempMovieName), datatypesMap.get(matcher.group(2)), null));
						}
						// flush results every BATCH_SIZE 
						if ((movieDatatypeSet.size() > 0) && (movieDatatypeSet.size() % PSTMT_BATCH_SIZE == 0)) {
							System.out.println("Inserting " + movieDatatypeSet.size() + " elements to the DB");
							DBManager.getInstance().insertMovieSingleDataTypeSetToDB(movieDatatypeSet, tablesEnum, movieFieldsEnum, datatypeFieldsEnum);
							totalElementsNum += movieDatatypeSet.size();
							movieDatatypeSet.clear();
						}
					}
					// flush the results that were left
					System.out.println("Inserting " + movieDatatypeSet.size() + " elements to the DB");
					DBManager.getInstance().insertMovieSingleDataTypeSetToDB(movieDatatypeSet, tablesEnum, movieFieldsEnum, datatypeFieldsEnum);
					totalElementsNum += movieDatatypeSet.size();
					System.out.println("Total number of elements entered into DB: " + totalElementsNum);

					movieDatatypeSet.clear();
					moviesMap.clear();
					moviesMap = null;
					System.gc();
				} else
					return false;
			}
			moviesStartRow += SELECT_BUCKET_SIZE;
			moviesEndRow += SELECT_BUCKET_SIZE;
			parser.closeFile();
			moviesMap.clear();
			System.gc();
		} while (!isEmpty);

		return true;
	}

	/**
	 * imports to the DB the different movie appearances of each actor / producer / director / writer
	 * @param listType the type of the list to be parsed
	 * @param personIndexMarkStart the start row of this specific person's list
	 * @return boolean whether the method succeeded or not
	 */
	private boolean getPersonMovieCredits(ListFilesEnum listType, int personIndexMarkStart) {

		Parser parser = new Parser();
		Map<String, Integer> moviesMap = new HashMap<String, Integer>();
		Relation[] personsArray = null;
		Set<CastingRelation> personMovieCreditsSet = new LinkedHashSet<CastingRelation>();
		String lineFromFile;
		String patternRegExp = null;
		Pattern pattern;
		Matcher matcher;
		boolean isMoviesEmpty;
		boolean isPersonsEmpty;
		boolean matchFound;
		int totalElementsNum = 0;
		String tempMovieFullName;
		int tempMovieId;
		int moviesStartRow, personsStartRow;
		int moviesEndRow, personsEndRow;
		int tempArrayIndex;
		String tempActorRole;
		int tempActorCreditRank;
		boolean isActor;
		int productionRoleId;

		isActor = false;
		tempActorRole = null;
		tempActorCreditRank = 0;
		productionRoleId = 0;
		
		switch (listType) {
		case ACTORS:
		case ACTRESSES:
			System.out.println("entering movie credits of ACTORS / ACTRESSES");
			patternRegExp = actorsMoviesPattern;
			isActor = true;
			productionRoleId = 1;
			break;
		case PRODUCERS:
			System.out.println("entering movie credits of PRODUCERS");
			patternRegExp = nonActorsMoviesPattern;
			productionRoleId = 2;
			break;
		case DIRECTORS:
			System.out.println("entering movie credits of DIRECTORS");
			patternRegExp = nonActorsMoviesPattern;
			productionRoleId = 3;
			break;
		case WRITERS:
			System.out.println("entering movie credits of WRITERS");
			patternRegExp = nonActorsMoviesPattern;
			productionRoleId = 4;
			break;
		}
		// compiling the pattern to look for in the list file
		pattern = Pattern.compile(patternRegExp);

		isPersonsEmpty = false;
		personsStartRow = personIndexMarkStart;
		personsEndRow = personsStartRow + SELECT_BUCKET_SIZE - 1;
		do {
			// retrieving part of the persons list to put inside a map
			// since the movie list is huge, we select buckets and iterate over all of them
			personsArray = DBManager.getInstance().getAllPersonsAndLineNumbersArray(personsStartRow, personsEndRow, SELECT_BUCKET_SIZE);
			if (personsArray[0] == null)
				isPersonsEmpty = true;

			if (!isPersonsEmpty) {
				parser.loadFile(listfilesMap.get(listType), listType);
				moviesStartRow = 1;
				moviesEndRow = moviesStartRow + SELECT_BUCKET_SIZE - 1;
				isMoviesEmpty = false;
				// going in the list to the first person in the array
				do {
					// retrieving part of the movies list to put inside a map
					// since the movie list is huge, we select buckets and iterate over all of them
					moviesMap = DBManager.getInstance().getAllMovies(moviesStartRow, moviesEndRow);
					if (moviesMap.size() == 0)
						isMoviesEmpty = true;

					if (!isMoviesEmpty) {
						tempArrayIndex = 0;
						parser.findLine(personsArray[0].getSecondaryId());
						personMovieCreditsSet = new LinkedHashSet<CastingRelation>();
						// Making sure we find the start of the list
						while (personsArray[tempArrayIndex] != null) {
							lineFromFile = parser.readLine();
							matcher = pattern.matcher(lineFromFile);
							if (matcher.matches()) {
								tempMovieFullName = matcher.group(2).trim();
								if (moviesMap.containsKey(tempMovieFullName)) {
									tempMovieId = moviesMap.get(tempMovieFullName).intValue();
									// if this is an actor/actresses list, there are more parameters on the line
									if (isActor) {
										tempActorRole = matcher.group(3);
										try {
											tempActorCreditRank = Integer.parseInt(matcher.group(4));
										} catch (Exception e) {
											tempActorCreditRank = 0;
										}
									}
									personMovieCreditsSet.add(new CastingRelation(personsArray[tempArrayIndex].getId(), 
											tempMovieId, productionRoleId, isActor, tempActorRole, tempActorCreditRank));
								}
							} else {
								if (lineFromFile.length() == 0)
									// if the line was empty, this signifies moving on to the next actor in the list
									++tempArrayIndex;
							}
							// flush results every BATCH_SIZE  
							if ((personMovieCreditsSet.size() > 0) && (personMovieCreditsSet.size() % PSTMT_BATCH_SIZE == 0)) {
								System.out.println("Inserting " + personMovieCreditsSet.size() + " elements to the DB");
								DBManager.getInstance().insertPersonMovieCreditsSetToDB(personMovieCreditsSet);
								totalElementsNum += personMovieCreditsSet.size();
								personMovieCreditsSet.clear();
							}
						}
						// flush the results that were left
						System.out.println("Inserting " + personMovieCreditsSet.size() + " elements to the DB");
						DBManager.getInstance().insertPersonMovieCreditsSetToDB(personMovieCreditsSet);
						totalElementsNum += personMovieCreditsSet.size();
						personMovieCreditsSet.clear();
						System.out.println("clearing moviesMap");
						moviesMap.clear();
						moviesMap = null;
						System.out.println("calling GarbageCollector");
						System.gc();
						System.out.println("Total number of elements entered into DB: " + totalElementsNum);
						moviesStartRow += SELECT_BUCKET_SIZE;
						moviesEndRow += SELECT_BUCKET_SIZE;
					}
				} while (!isMoviesEmpty);
			}
			personsStartRow += SELECT_BUCKET_SIZE;
			personsEndRow += SELECT_BUCKET_SIZE;
		} while (!isPersonsEmpty);

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
