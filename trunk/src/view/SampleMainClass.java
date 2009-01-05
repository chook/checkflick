package view;
import controller.SamplePersonClass;
import model.SampleDataBaseClass;

/**
 * This is a sample of a main class
 * In our program we might use the main to bring up the GUI
 * @author Chen Harel
 *
 */
public class SampleMainClass {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SamplePersonClass person = new SamplePersonClass();
		
		person.setName("Netzer Zaidenberg");
		System.out.println("Hi " + person.getName() + ". Have a nice day!");
		System.out.println(SampleDataBaseClass.getInstance().getVersion());
	}
}
