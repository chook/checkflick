package model;
import java.io.*;

import controller.ListFilesEnum;

public class Parser {

	// The strings that are at the beginning of each list
	static String languagesListStartLine1 = "LANGUAGE LIST";
	static String languagesListStartLine2 = "=============";
	static String genresListStartLine1 = "8: THE GENRES LIST";
	static String genresListStartLine2 = "==================";
	static String countriesListStartLine1 = "COUNTRIES LIST";
	static String countriesListStartLine2 = "==============";
	static String moviesListStartLine1 = "MOVIES LIST";
	static String moviesListStartLine2 = "===========";
	
	private BufferedReader listFile;
	private int currentLine;
	private String listStartLine1;
	private String listStartLine2;
	
	public Parser() {
		
	}
	
	/**
	 * Opens the file and resets the line counter
	 * @param filename the file to be loaded
	 * @return boolean if the loading succeeded
	 */
	public boolean loadFile(String filename, ListFilesEnum listType) {
		try {
			// Opening the file
			FileInputStream fstream = new FileInputStream(filename);
			// Convert our input stream to a DataInputStream
			//InputStreamReader insr = new InputStreamReader(fstream, "UTF-8");
			InputStreamReader insr = new InputStreamReader(fstream);
			listFile =  new BufferedReader(insr);
			currentLine = 0;
			
			switch (listType) {
			
			case LANGUAGES:
				listStartLine1 = languagesListStartLine1;
				listStartLine2 = languagesListStartLine2;
				break;
			case GENRES:
				listStartLine1 = genresListStartLine1;
				listStartLine2 = genresListStartLine2;
				break;
			case COUNTRIES:
				listStartLine1 = countriesListStartLine1;
				listStartLine2 = countriesListStartLine2;
				break;
			case MOVIES:
				listStartLine1 = moviesListStartLine1;
				listStartLine2 = moviesListStartLine2;
				break;
			}
		}
		catch (Exception e) {
			System.err.println("File input error");
			return false;
		}		
		return true;
	}
	
	/**
	 * Parser constructor
	 * Calls the loadFile method
	 * @param filename the file to be loaded
	 */
	public Parser(String filename, ListFilesEnum listType) {
		loadFile(filename, listType);
	}
	
	/**
	 * runs to the start of the list in the file (after all the comments in the beginning)
	 * @return boolean if the start of the list was found
	 */
	public boolean findStartOfList() {
		if (findLine(listStartLine1) && findLine(listStartLine2))
			return true;
		else
			return false;
	}
	/**
	 * reading the next line from the file if possible
	 * @return String the line that was read
	 */
	public String readLine() {
		try {
			if (listFile.ready())
				++currentLine;
				return listFile.readLine();
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		return null;
	}
	
	/**
	 * Gets the current line
	 * @return int the line number, or -1 if the file isn't open
	 */
	public int getLineNumber() {
		return currentLine;
	}
	
	/**
	 * Resets the counter of the line numbers
	 * @return boolean whether the method succeeded
	 */
	public boolean resetLineCount() {
		currentLine = 0;
		
		return true;
	}
	
	/**
	 * Runs over the file and searches the given string
	 * @param searchString
	 * @return boolean whether the line was found
	 */
	public boolean findLine(String searchString) {
		String line;
		try {
			while (!isEOF()) {
				line = readLine();
				if (line.equals(searchString))
					return true;
			}
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		
		return false;
	}
	
	/**
	 * @deprecated
	 * @return
	 */
	public boolean printLine() {
		try {
			if (!isEOF()) {
				System.out.println(readLine());
			}
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		
		return false;
	}
	
	/**
	 * Checks whether the EOF was reached
	 * Will also return true if the file wasn't open at all 
	 * @return boolean representing whether the EOF has been reached or the file isn't open
	 */
	public boolean isEOF() {
		try {
			return (!(listFile.ready()));
		}
		catch (Exception e) {
			System.err.println("File input error");
			return true;
		}
	}
	
	/**
	 * Closes the file stream
	 */
	public void closeFile() {
		try {
			listFile.close();
			currentLine = -1;
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
	}
	
	@Override
    protected void finalize() throws Throwable {
			if (listFile != null)
				closeFile();
			
            super.finalize();
    }
	
}