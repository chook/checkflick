package controller;

public class Movie {
	private int id;
	private int year;
	private String name;
	private int    directorId;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}
	/**
	 * @param year the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the directorId
	 */
	public int getDirectorId() {
		return directorId;
	}
	/**
	 * @param directorId the directorId to set
	 */
	public void setDirectorId(int directorId) {
		this.directorId = directorId;
	}

	/**
	 * Default Constructor
	 */
	public Movie() {
	}
	
	/**
	 * Initiates a new movie class with parameters
	 * @param id
	 * @param year
	 * @param name
	 * @param directorId
	 */
	public Movie(int id,
				 int year,
				 String name,
				 int directorId) {
		super();
		this.id = id;
		this.year = year;
		this.name = name;
		this.directorId = directorId;
	}
	
}
