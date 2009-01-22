package model;
import java.io.*;
import java.util.regex.*;
import java.util.*;

public class DataImporter {

 public static void main(String argv[]) {
	 
	 // Searching for "LANGUAGE LIST" & "============="
	 Parser languageParser = new Parser("language.list");
	 String line;
	 
	 // Making sure we find the start of the language list
	 if (languageParser.findLine("LANGUAGE LIST")) {
		 if (languageParser.findLine("=============")) {
			 // defining a dictionary to find all the different languages
			 Set<String> languagesSet = new HashSet<String>();  
			 
			 System.out.println("Found the start of the list!");
			 //Pattern p = Pattern.compile("[^\"].+[)}]\\s+([a-zA-Z_\\-\\s',0-9]+)(\\s+\\(.+\\)\\s*)*");
			 // Pattern explanation:
			 // [^\"] 							- ignore lines that start with '"' (since they are TV shows)
			 // .+[)}] 							- run along the line over all characters until you encounter ')' or '}'
			 // \\s+ 							- run along all the trailing whitespaces
			 // ([\\P{InGreek}\\-\\s',&&[^(]]+)	- group 1: run over all different UNICODE letters including '-', ',' (comma), ''' (single quote) and whitespaces
			 // 											not including '('
			 // (\\s+\\(.+\\)\\s*)* 			- check if this group appears zero or more times
			 //										the group contains whitespaces, then '(', different characters and whitespaces, and then ')'
			 //										(there can be more than one comments in parentheses)
			 //Pattern p = Pattern.compile("[^\"].+[)}]\\s+([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*");
			 Pattern p = Pattern.compile("[^\"].+[)}]\\s+([\\P{InGreek}\\-\\s',&&[^(]]+)(\\s+\\(.+\\)\\s*)*");
			 int i = 15;
			 while (!languageParser.isEOF()) {
				 line = languageParser.getNextLine();
				 //System.out.println(line);
				 Matcher m = p.matcher(line);
				 boolean b = m.matches();
				 if (!b) {
					 if (!(line.charAt(0) == '"')) {
						 System.out.println(line);
						 System.out.println("couldn't found a match on line " + i + "!");
					 }
				 }
//				 System.out.println(m.group(1));
				 else {
					 languagesSet.add(m.group(1));
//					 if (m.group(1).equals("Language")) {
//						 System.out.println(line);
//					 }
//					 System.out.println(m.group(1));
//					 System.out.println("couldn't found a match!");
				 }
				 i++;
			 }
			 DBManager.getInstance().insertSetToDB(languagesSet, DBTablesEnum.LANGUAGES, DBFieldsEnum.LANGUAGES_LANGUAGE_NAME);
		 }
	 }
	 
	 languageParser.closeFile();
 }

}