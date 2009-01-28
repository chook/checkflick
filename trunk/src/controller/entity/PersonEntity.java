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
	private String personRealName;
	private String personNickNames;
	private Date dateOfBirth;
	private int yearOfBirth;
	private String cityOfBirth;
	private int countryOfBirth;
	private Date dateOfDeath;
	private int yearOfDeath;
	private int height;
	private String trademark;
	private String biography;

	/**
	 * @return the personRealName
	 */
	public String getPersonRealName() {
		return personRealName;
	}

	/**
	 * @param personRealName
	 *            the personRealName to set
	 */
	public void setPersonRealName(String personRealName) {
		this.personRealName = personRealName;
	}

	/**
	 * @return the personNickNames
	 */
	public String getPersonNickNames() {
		return personNickNames;
	}

	/**
	 * @param personNickNames
	 *            the personNickNames to set
	 */
	public void setPersonNickNames(String personNickNames) {
		this.personNickNames = personNickNames;
	}

	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	/**
	 * @param dateOfBirth
	 *            the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

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
	 * @return the dateOfDeath
	 */
	public Date getDateOfDeath() {
		return dateOfDeath;
	}

	/**
	 * @param dateOfDeath
	 *            the dateOfDeath to set
	 */
	public void setDateOfDeath(Date dateOfDeath) {
		this.dateOfDeath = dateOfDeath;
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
	 * @return the height
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return the trademark
	 */
	public String getTrademark() {
		return trademark;
	}

	/**
	 * @param trademark
	 *            the trademark to set
	 */
	public void setTrademark(String trademark) {
		this.trademark = trademark;
	}

	/**
	 * @return the biography
	 */
	public String getBiography() {
		return biography;
	}

	/**
	 * @param biography
	 *            the biography to set
	 */
	public void setBiography(String biography) {
		this.biography = biography;
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

	public PersonEntity(int id, String name, String personRealName,
			String personNickNames, Date dateOfBirth, int yearOfBirth,
			String cityOfBirth, int countryOfBirth, Date dateOfDeath,
			int yearOfDeath, int height, String trademark, String biography) {
		super(id, name);
		this.personRealName = personRealName;
		this.personNickNames = personNickNames;
		this.dateOfBirth = dateOfBirth;
		this.yearOfBirth = yearOfBirth;
		this.cityOfBirth = cityOfBirth;
		this.countryOfBirth = countryOfBirth;
		this.dateOfDeath = dateOfDeath;
		this.yearOfDeath = yearOfDeath;
		this.height = height;
		this.trademark = trademark;
		this.biography = biography;
	}

	/* (non-Javadoc)
	 * @see controller.entity.NamedEntity#toStringList()
	 */
	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(personRealName);
		list.add(personNickNames);
		list.add((dateOfBirth == null) ? null : dateOfBirth.toString());
		list.add(String.valueOf(yearOfBirth));
		list.add(cityOfBirth);
		list.add(String.valueOf(countryOfBirth));
		list.add((dateOfDeath == null) ? null : dateOfDeath.toString());
		list.add(String.valueOf(yearOfDeath));
		list.add(String.valueOf(height));
		list.add(trademark);
		list.add(biography);
		return list;
	}
}