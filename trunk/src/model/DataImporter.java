package model;

import java.util.regex.*;
import java.util.*;

import controller.ImportHandler;
import controller.enums.*;
import controller.entity.MovieEntity;
import controller.entity.NamedEntity;
import controller.entity.Relation;
import controller.entity.NamedRelation;
import controller.entity.CastingRelation;

public class DataImporter {

	static int PSTMT_BATCH_SIZE = 50000;
	static int SELECT_BUCKET_SIZE = 1000000;
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
	 *  a test to see how things would work on the controller side
	 **/
	public static void main(String argv[]) {
	 
		ImportHandler ih;
		
		// create new ImportHandler
		ih = new ImportHandler();
		// add the different list files given from the GUI
//		ih.addListFile(ListFilesEnum.LANGUAGES, "lists\\language.list");
//		ih.addListFile(ListFilesEnum.GENRES, "lists\\genres.list");
//		ih.addListFile(ListFilesEnum.COUNTRIES, "lists\\countries.list");
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
//		ih.addListFile(ListFilesEnum.MOVIES, "lists\\movies.list");
//		ih.addListFile(ListFilesEnum.ACTORS, "lists\\actors.list");
//		ih.addListFile(ListFilesEnum.ACTRESSES, "lists\\actresses.list");
//		ih.addListFile(ListFilesEnum.DIRECTORS, "lists\\directors.list");
//		ih.addListFile(ListFilesEnum.PRODUCERS, "lists\\producers.list");
//		ih.addListFile(ListFilesEnum.WRITERS, "lists\\writers.list");
		ih.addListFile(ListFilesEnum.MOVIES, "/users/courses/databases/imdb/extracted/movies.list");
		ih.addListFile(ListFilesEnum.LANGUAGES, "/users/courses/databases/imdb/extracted/language.list");
		ih.addListFile(ListFilesEnum.GENRES, "/users/courses/databases/imdb/extracted/genres.list");
		ih.addListFile(ListFilesEnum.COUNTRIES, "/users/courses/databases/imdb/extracted/countries.list");
		ih.addListFile(ListFilesEnum.ACTORS, "/users/courses/databases/imdb/extracted/actors.list");
		ih.addListFile(ListFilesEnum.ACTRESSES, "/users/courses/databases/imdb/extracted/actresses.list");
		ih.addListFile(ListFilesEnum.DIRECTORS, "/users/courses/databases/imdb/extracted/directors.list");
		ih.addListFile(ListFilesEnum.PRODUCERS, "/users/courses/databases/imdb/extracted/producers.list");
		ih.addListFile(ListFilesEnum.WRITERS, "/users/courses/databases/imdb/extracted/writers.list");
		
		// run the importing
		ih.importIntoDB();
		
		return;
	}

	
	private Map<ListFilesEnum, String> listfilesMap;
	
	/**
	 * DataImporter Constructor
	 * Receives the map of filenames and adds it to its private listfilesMap field
	 * @param listfilesMap
	 */
	public DataImporter(Map<ListFilesEnum, String> listfilesMap) {
		this.listfilesMap = listfilesMap;
	}
	
	/**
	 * creates a field needed for the import in the MOVIES table:
	 * TEMP_MOVIE_NAME
	 */
	public void createMoviesTempField() {
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD %s VARCHAR2(300 CHAR)",
														DBTablesEnum.MOVIES.getTableName(), 
														DBFieldsEnum.MOVIES_TEMP_MOVIE_NAME.getFieldName()));
	}
	
	/**
	 * TEMP_MOVIE_NAME
	 * removes the temporary fields that were used for the import in the MOVIES table:
	 */
	public void deleteMoviesTempField() {
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s DROP COLUMN %s",
														DBTablesEnum.MOVIES.getTableName(),
														DBFieldsEnum.MOVIES_TEMP_MOVIE_NAME.getFieldName()));
	}
	
	/**
	 * retrieves all the different languages for each and every movie
	 * @return boolean whether the method succeeded
	 */
	public boolean getMoviesDataTypes(ListFilesEnum dataType, String patternRegExp, 
			DBTablesEnum tablesEnum, DBFieldsEnum movieFieldsEnum, DBFieldsEnum datatypeFieldsEnum, NamedEntitiesEnum namedEntitiesEnum) {
		
		Parser parser = new Parser();
		Map<String, Integer> moviesMap = new WeakHashMap<String, Integer>();
		Map<String, Integer> datatypesMap = new HashMap<String, Integer>();
		Set<NamedRelation> movieDatatypeSet = new LinkedHashSet<NamedRelation>();
		Pattern pattern;
		Matcher matcher;
		int totalElementsNum = 0;
		List<NamedEntity> datatypesList;
		int moviesStartRow;
		int moviesEndRow;
		String tempMovieName = null;

		parser.loadFile(listfilesMap.get(dataType), dataType);

		moviesStartRow = 1;
		moviesEndRow = moviesStartRow + SELECT_BUCKET_SIZE;
		// compiling the pattern to look for in the list file
		pattern = Pattern.compile(patternRegExp);

		// retrieving the list to put inside a map
		// the language map would be small enough to stay resident in memory during the whole method
		datatypesList = DBManager.getInstance().getAllNamedEntities(namedEntitiesEnum);
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
			System.gc();
		} while (!isEmpty);

		return true;
	}

	public boolean importDataTypes() {
		
		for (int i = 1; i < 4; ++i) {
			switch (i) {
			case 1:
				System.out.println("Working on the languages file");
				getDatatypes(ListFilesEnum.LANGUAGES, languagePattern, DBTablesEnum.LANGUAGES, DBFieldsEnum.LANGUAGES_LANGUAGE_NAME);
				break;

			case 2:
				System.out.println("Working on the genres file");
				getDatatypes(ListFilesEnum.LANGUAGES, genresCountriesPattern, DBTablesEnum.GENRES, DBFieldsEnum.GENRES_GENRE_NAME);
				break;

			case 3:
				System.out.println("Working on the countries file");
				getDatatypes(ListFilesEnum.LANGUAGES, genresCountriesPattern, DBTablesEnum.COUNTRIES, DBFieldsEnum.COUNTRIES_COUNTRY_NAME);
				break;
			}
		}

		createDataTypesIndex();
		
		return true;
	}
	
	/**
	 * imports all the different movie names from the movies list
	 * @return boolean whether the method succeeded
	 */
	public boolean importMovies() {
		
		getMovies();
		createMoviesIndex();
		
		return true;
	}
	
	public boolean importMoviesDataTypes() {

		for (int i = 1; i < 4; ++i) {
			switch (i) {
			case 1:
				System.out.println("Working on the languages file - entering movies");
				getMoviesDataTypes(ListFilesEnum.LANGUAGES, languagePattern, DBTablesEnum.MOVIE_LANGUAGES,
						DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID, DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID, NamedEntitiesEnum.LANGUAGES);
				break;

			case 2:
				System.out.println("Working on the genres file - entering movies");
				getMoviesDataTypes(ListFilesEnum.GENRES, genresCountriesPattern, DBTablesEnum.MOVIE_GENRES,
						DBFieldsEnum.MOVIE_GENRES_MOVIE_ID, DBFieldsEnum.MOVIE_GENRES_GENRE_ID, NamedEntitiesEnum.GENRES);
				break;

			case 3:
				System.out.println("Working on the countries file - entering movies");
				getMoviesDataTypes(ListFilesEnum.COUNTRIES, genresCountriesPattern, DBTablesEnum.MOVIE_COUNTRIES,
						DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID, DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID, NamedEntitiesEnum.COUNTRIES);
				break;
			}
		}

		createDataTypesIndex();

		return true;
	}
	
	public void importPersonsAndCredits() {
		
		int[] personIndexNextMarkStart = new int[5]; 
		
		// creating the temporary fields in PERSONS needed for the import
		createPersonsTempFields();
		
		System.out.println("==========================================================");
		// adding the different persons from all the different lists
		personIndexNextMarkStart[0] = 1;
		personIndexNextMarkStart[1] = personIndexNextMarkStart[0] + getPersons(ListFilesEnum.ACTORS);
		personIndexNextMarkStart[2] = personIndexNextMarkStart[1] + getPersons(ListFilesEnum.ACTRESSES);
		personIndexNextMarkStart[3] = personIndexNextMarkStart[2] + getPersons(ListFilesEnum.DIRECTORS);
		personIndexNextMarkStart[4] = personIndexNextMarkStart[3] + getPersons(ListFilesEnum.PRODUCERS);
		getPersons(ListFilesEnum.WRITERS);
		
		// creating the PERSONS indexes
		createPersonsIndex();
		
		// entering a tempPersonID for the persons, exactly as their personID
		preparePersonsTempFields();
		findAndUpdateDuplicates();
		
		getPersonMovieCredits(ListFilesEnum.ACTORS, personIndexNextMarkStart[0]);
		getPersonMovieCredits(ListFilesEnum.ACTRESSES, personIndexNextMarkStart[1]);
		getPersonMovieCredits(ListFilesEnum.DIRECTORS, personIndexNextMarkStart[2]);
		getPersonMovieCredits(ListFilesEnum.PRODUCERS, personIndexNextMarkStart[3]);
		getPersonMovieCredits(ListFilesEnum.WRITERS, personIndexNextMarkStart[4]);
		
		// creating the PERSONS indexes
		createPersonMovieCreditsIndex();
		
		// removing the records in the PRESONS table marked as duplicates
		removeDuplicates();
		deletePersonsTempFields();
	}
		
	private boolean createDataTypesIndex() {
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s, %s) ENABLE",
											DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
											DBTablesEnum.MOVIE_LANGUAGES.getTableName(),
											DBFieldsEnum.MOVIE_LANGUAGES_MOVIE_ID.getFieldName(),
											DBFieldsEnum.MOVIE_LANGUAGES_LANGUAGE_ID.getFieldName()));
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s, %s) ENABLE",
											DBTablesEnum.MOVIE_GENRES.getTableName(),
											DBTablesEnum.MOVIE_GENRES.getTableName(),
											DBFieldsEnum.MOVIE_GENRES_MOVIE_ID.getFieldName(),
											DBFieldsEnum.MOVIE_GENRES_GENRE_ID.getFieldName()));
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s, %s) ENABLE",
											DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
											DBTablesEnum.MOVIE_COUNTRIES.getTableName(),
											DBFieldsEnum.MOVIE_COUNTRIES_MOVIE_ID.getFieldName(),
											DBFieldsEnum.MOVIE_COUNTRIES_COUNTRY_ID.getFieldName()));

		return true;
	}
	
	private boolean createPersonMovieCreditsIndex() {
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s, %s, %s) ENABLE",
											DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
											DBTablesEnum.PERSON_MOVIE_CREDITS.getTableName(),
											DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(),
											DBFieldsEnum.PERSON_MOVIE_CREDITS_PERSON_ID.getFieldName(),
											DBFieldsEnum.PERSON_MOVIE_CREDITS_PRODUCTION_ROLE_ID.getFieldName()));
		return true;
	}
	
	private boolean createPersonsIndex() {

		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s) ENABLE",
														DBTablesEnum.PERSONS.getTableName(),
														DBTablesEnum.PERSONS.getTableName(), 
														DBFieldsEnum.PERSONS_PERSON_ID.getFieldName()));

		return true;
	}

	private boolean createMoviesIndex() {
		
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD CONSTRAINT %s_PK PRIMARY KEY (%s) ENABLE",
											DBTablesEnum.MOVIES.getTableName(),
											DBTablesEnum.MOVIES.getTableName(), 
											DBFieldsEnum.MOVIES_MOVIE_ID.getFieldName()));
		
		return true;
	}
	
	/**
	 * creates 3 fields needed for the import in the PERSONS table:
	 * TEMP_PERSON_ID, TEMP_PERSON_LINE_NUMBER & TEMP_IS_DUPLICATE
	 */
	private void createPersonsTempFields() {

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

	private void findAndUpdateDuplicates() {
		DBManager.getInstance().findAndUpdateDuplicates();
	}
	
	/**
	 * parses the languages, genres & countries lists
	 * and adds all the data to the DB
	 * @return boolean if the method succeeded
	 */
	private boolean getDatatypes(ListFilesEnum dataType, 
			String patternRegExp, DBTablesEnum tablesEnum, DBFieldsEnum fieldsEnum) {
		
		Parser parser = new Parser();
		Set<String> elementsSet = new HashSet<String>();		// a dictionary to collect all the different elements
		Pattern pattern;
		Matcher matcher;

		parser.loadFile(listfilesMap.get(dataType), dataType);		

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
		return true;
	}
	
	private boolean getMovies() {

		Parser parser = new Parser();
		Set<MovieEntity> moviesSet = new LinkedHashSet<MovieEntity>();
		String patternRegExp = null;
		String fullMovieName, movieName, movieRomanNotation, movieMadeFor;
		int movieYear;
		Pattern pattern;
		Matcher matcher;
		int totalElementsNum = 0;
		
		// add temp field
		DBManager.getInstance().executeSQL(String.format("ALTER TABLE %s ADD %s NUMBER",
				DBTablesEnum.PERSONS.getTableName(), 
				DBFieldsEnum.PERSONS_TEMP_PERSON_ID.getFieldName()));
		
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


	/**
	 * removes the records that are duplicates and aren't needed
	 */
	private void removeDuplicates() {
	
		// removing the records that are marked as duplicates and aren't needed
		DBManager.getInstance().executeSQL(String.format("DELETE FROM %s WHERE %s = 'Y'",
														DBTablesEnum.PERSONS.getTableName(),
														DBFieldsEnum.PERSONS_TEMP_IS_DUPLICATE.getFieldName()));
	}
}
