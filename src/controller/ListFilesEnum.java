/**
 * 
 */
package controller;

/**
 * @author Nadav Shamgar
  */
public enum ListFilesEnum {
	ACTORS,
	ACTRESSES,
	AKA_NAMES,
	AKA_TITLES,
	BIOGRAPHIES,
	COLOR_INFO,
	COUNTRIES,
	CRAZY_CREDITS,
	DIRECTORS,
	GENRES,
	GOOFS,
	LANGUAGES,
	LOCATIONS,
	MOVIE_LINKS,
	MOVIES,
	PLOT,
	PRODUCERS,
	QUOTES,
	RUNNING_TIMES,
	TAGLINES,
	TRIVIA,
	WRITERS;
	
	private static int counter;
	public static int getCounter() {
		return counter;
	}
	
	private void inc() {
		counter++;
	}
	
	private ListFilesEnum() {
		inc();
	}
}


