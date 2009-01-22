package model;
import java.io.*;

public class Parser {

	private BufferedReader listFile;
	
	public Parser(String filename) {
		try {
			// Opening the file
			FileInputStream fstream = new FileInputStream(filename);
			// Convert our input stream to a DataInputStream
			//InputStreamReader insr = new InputStreamReader(fstream, "UTF-8");
			InputStreamReader insr = new InputStreamReader(fstream);
			listFile =  new BufferedReader(insr);
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
	}
			 
	public boolean findLine(String searchString) {
		String line;
		try {
			while (listFile.ready()) {
				line = listFile.readLine();
				if (line.equals(searchString))
					return true;
			}
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		
		return false;
	}
	
	public boolean printLine() {
		try {
			if (listFile.ready()) {
				System.out.println(listFile.readLine());
			}
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		
		return false;
	}
	
	public String getNextLine() {
		try {
			if (listFile.ready())
				return listFile.readLine();
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
		
		return null;
	}
	
	public boolean isEOF() {
		try {
			return (!(listFile.ready()));
		}
		catch (Exception e) {
			System.err.println("File input error");
			return true;
		}
	}
	
	public void closeFile() {
		try {
			listFile.close();
		}
		catch (Exception e) {
			System.err.println("File input error");
		}
	}
	
}