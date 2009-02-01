package controller.entity;

import java.util.Date;
import java.util.List;

/**
 * Person Entity
 * 
 * @author Chook
 * 
 */
public class PersonEntity extends NamedEntity {
	private int yearOfBirth;
	private String cityOfBirth;
	private int countryOfBirth;
	private int yearOfDeath;

	/**
	 * @return the yearOfBirth
	 */
	public int getYearOfBirth() {
		return yearOfBirth;
	}

	/**
	 * @param yearOfBirth
	 *            the yearOfBirth to set
	 */
	public void setYearOfBirth(int yearOfBirth) {
		this.yearOfBirth = yearOfBirth;
	}

	/**
	 * @return the cityOfBirth
	 */
	public String getCityOfBirth() {
		return cityOfBirth;
	}

	/**
	 * @param cityOfBirth
	 *            the cityOfBirth to set
	 */
	public void setCityOfBirth(String cityOfBirth) {
		this.cityOfBirth = cityOfBirth;
	}

	/**
	 * @return the countryOfBirth
	 */
	public int getCountryOfBirth() {
		return countryOfBirth;
	}

	/**
	 * @param countryOfBirth
	 *            the countryOfBirth to set
	 */
	public void setCountryOfBirth(int countryOfBirth) {
		this.countryOfBirth = countryOfBirth;
	}

	/**
	 * @return the yearOfDeath
	 */
	public int getYearOfDeath() {
		return yearOfDeath;
	}

	/**
	 * @param yearOfDeath
	 *            the yearOfDeath to set
	 */
	public void setYearOfDeath(int yearOfDeath) {
		this.yearOfDeath = yearOfDeath;
	}

	/**
	 * 
	 */
	public PersonEntity() {
		super();
	}
	
	public PersonEntity(int id) {
		super(id);
	}

	public PersonEntity(int id, String name) {
		super(id, name);
	}

	public PersonEntity(int id, String name, int yearOfBirth, String cityOfBirth,
			int countryOfBirth, int yearOfDeath) {
		super(id, name);
		this.yearOfBirth = yearOfBirth;
		this.cityOfBirth = cityOfBirth;
		this.countryOfBirth = countryOfBirth;
		this.yearOfDeath = yearOfDeath;
	}

	@Deprecated
	public PersonEntity(int id, String name, String personRealName,
			String personNickNames, Date dateOfBirth, int yearOfBirth,
			String cityOfBirth, int countryOfBirth, Date dateOfDeath,
			int yearOfDeath, int height, String trademark, String biography) {
		super(id, name);
		this.yearOfBirth = yearOfBirth;
		this.cityOfBirth = cityOfBirth;
		this.countryOfBirth = countryOfBirth;
		this.yearOfDeath = yearOfDeath;
	}

	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(yearOfBirth));
		list.add(cityOfBirth);
		list.add(String.valueOf(countryOfBirth));
		list.add(String.valueOf(yearOfDeath));
		return list;
	}
}
