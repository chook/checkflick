import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.*;

public class AccessingXmlFile {

 public static void main(String argv[]) {

  try {
  File file = new File("C:\\MyFile.xml");
  DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
  DocumentBuilder db = dbf.newDocumentBuilder();
  Document document = db.parse(file);
  document.getDocumentElement().normalize();
  System.out.println("Root element " + document.getDocumentElement().getNodeName());
  NodeList node = document.getElementsByTagName("student");
  System.out.println("Information of the students");

  for (int i = 0; i < node.getLength(); i++) {
  Node firstNode = node.item(i);
    
    if (firstNode.getNodeType() == Node.ELEMENT_NODE) {
  
           Element element = (Element) firstNode;
      NodeList firstNameElemntList = element.getElementsByTagName("firstname");
      Element firstNameElement = (Element) firstNameElemntList.item(0);
      NodeList firstName = firstNameElement.getChildNodes();
      System.out.println("First Name : "  + ((Node) firstName.item(0)).getNodeValue());
      
	  NodeList lastNameElementList = element.getElementsByTagName("lastname");
      Element lastNameElement = (Element) lastNameElementList.item(0);
      NodeList lastName = lastNameElement.getChildNodes();
      System.out.println("Last Name : " + ((Node) lastName.item(0)).getNodeValue());

      NodeList addressList = element.getElementsByTagName("address");
      Element addressElement = (Element) addressList.item(0);
      NodeList address = addressElement.getChildNodes();
      System.out.println("Address : "  + ((Node) address.item(0)).getNodeValue());

	  NodeList cityList = element.getElementsByTagName("city");
      Element cityElement = (Element) cityList.item(0);
      NodeList city = cityElement.getChildNodes();
      System.out.println("City : "  + ((Node) city.item(0)).getNodeValue());
 }
}
  } catch (Exception e) {
    e.printStackTrace();
  }
 }
}