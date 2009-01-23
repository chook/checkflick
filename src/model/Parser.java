package model;
import java.io.*;

public class Parser {

	private BufferedReader listFile;
	private int currentLine;
	
	public Parser() {
		
	}
	
	/**
	 * Opens the file and resets the line counter
	 * @param filename the file to be loaded
	 * @return boolean if the loading succeeded
	 */
	public boolean loadFile(String filename) {
		try {
			// Opening the file
			FileInputStream fstream = new FileInputStream(filename);
			// Convert our input stream to a DataInputStream
			//InputStreamReader insr = new InputStreamReader(fstream, "UTF-8");
			InputStreamReader insr = new InputStreamReader(fstream);
			listFile =  new BufferedReader(insr);
			currentLine = 0;
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
	public Parser(String filename) {
		loadFile(filename);
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
	
}