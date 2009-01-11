package controller;

import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class AppData {
	private String importFolder;
	private String dbConnectionString;
	private String dbUsername;
	private String dbPassword;
	
	/**
	 * @return the importFolder
	 */
	public String getImportFolder() {
		return importFolder;
	}
	/**
	 * @return the dbConnectionString
	 */
	public String getDbConnectionString() {
		return dbConnectionString;
	}
	/**
	 * @return the dbUsername
	 */
	public String getDbUsername() {
		return dbUsername;
	}
	/**
	 * @return the dbPassword
	 */
	public String getDbPassword() {
		return dbPassword;
	}
	
	public AppData() {
		
	}
	
	/**
	 * Parsing the configuration xml file
	 */
	public void parseXMLFile() {
		//TODO: For now this sucks. big time.
		try {
			  File file = new File("c:\\MyFile.xml");
			  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			  DocumentBuilder db = dbf.newDocumentBuilder();
			  Document document = db.parse(file);
			  document.getDocumentElement().normalize();
			  System.out.println("Root element " + document.getDocumentElement().getNodeName());
			  NodeList node = document.getElementsByTagName("checkflick");
			  
			  for (int i = 0; i < node.getLength(); i++) {
				  Node firstNode = node.item(i);
				  if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
				      Element element = (Element) firstNode;
				      NodeList firstNameElemntList = element.getElementsByTagName("dbUsername");
				      Element firstNameElement = (Element) firstNameElemntList.item(0);
				      NodeList firstName = firstNameElement.getChildNodes();
				      dbUsername = ((Node) firstName.item(0)).getNodeValue();
					  
				      NodeList lastNameElementList = element.getElementsByTagName("dbPassword");
				      Element lastNameElement = (Element) lastNameElementList.item(0);
				      NodeList lastName = lastNameElement.getChildNodes();
				      dbPassword = ((Node) lastName.item(0)).getNodeValue();
				    }
			  }
		} catch (IOException ioe) {
			
		} catch (SAXException saxe) {
			
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
