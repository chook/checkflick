package model;

/**
 * Sample Person Class
 * @author Chen Harel
 *
 */
public class SamplePersonClass {
	private String name;
	private int    age;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public SamplePersonClass() {
		this.name = "";
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}
}
