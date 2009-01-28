package model;
import java.io.*;

import controller.enums.ListFilesEnum;

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
	static String actorsListStartLine1 = "Name			Titles ";
	static String actorsListStartLine2 = "----			------";
	static String actorsListEndLine = "SUBMITTING UPDATES";
	
	private BufferedReader listFile;
	private String listStartLine1;
	private String listStartLine2;
	private boolean hasEndLine;
	private String listEndLine;
	
	private int currentLine;
	private boolean isEOF;
	
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
			isEOF = false;
			listEndLine = "";
			
			switch (listType) {
			
			case LANGUAGES:
				listStartLine1 = languagesListStartLine1;
				listStartLine2 = languagesListStartLine2;
				hasEndLine = false;
				break;
			case GENRES:
				listStartLine1 = genresListStartLine1;
				listStartLine2 = genresListStartLine2;
				hasEndLine = false;
				break;
			case COUNTRIES:
				listStartLine1 = countriesListStartLine1;
				listStartLine2 = countriesListStartLine2;
				hasEndLine = false;
				break;
			case MOVIES:
				listStartLine1 = moviesListStartLine1;
				listStartLine2 = moviesListStartLine2;
				hasEndLine = false;
				break;
			case ACTORS:
				listStartLine1 = actorsListStartLine1;
				listStartLine2 = actorsListStartLine2;
				hasEndLine = true;
				listEndLine = actorsListEndLine;
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
		if (findLine(listStartLine1) && findLine(listStartLine2)) {
			checkIfEOF();
			return true;
		} else
			return false;
	}
	/**
	 * reading the next line from the file if possible
	 * @return String the line that was read
	 */
	public String readLine() {
		String line;
		try {
			if (listFile.ready()) {
				++currentLine;
				line = listFile.readLine();
				// checking if the line is the end of the needed list
				if (hasEndLine && line.equals(listEndLine)) {
					isEOF = true;
					return "";
				}
				checkIfEOF();
				return line;
			}
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		return "";
	}
	
	private boolean checkIfEOF() {
		try {
			if (!(listFile.ready()))
				isEOF = true;
		} catch (Exception e) {
			System.err.println("File input error");
		}
		return isEOF;
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
		return isEOF;
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