package controller.entity;

public class Movie {
	private int 	id;
	private String 	name;
	private int		year;
	private int    	colorInfo;
	private int		runningTime;
	private String	taglines;
	private String	plot;
	private String	filmingLocations;
	
	/**
	 * Default Constructor
	 */
	public Movie() {
	}

	/**
	 * Simplified Constructor - Only id
	 * @param id
	 */
	public Movie(int id) {
		this.id = id;
	}
	
	/**
	 * A Movie Constructor
	 * @param id
	 * @param name
	 * @param year
	 * @param colorInfo
	 * @param runningTime
	 * @param taglines
	 * @param plot
	 * @param filmingLocations
	 */
	public Movie(int id, String name, int year, int colorInfo, int runningTime,
			String taglines, String plot, String filmingLocations) {
		this.id = id;
		this.name = name;
		this.year = year;
		this.colorInfo = colorInfo;
		this.runningTime = runningTime;
		this.taglines = taglines;
		this.plot = plot;
		this.filmingLocations = filmingLocations;
	}

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
	 * @return the colorInfo
	 */
	public int getColorInfo() {
		return colorInfo;
	}

	/**
	 * @param colorInfo the colorInfo to set
	 */
	public void setColorInfo(int colorInfo) {
		this.colorInfo = colorInfo;
	}

	/**
	 * @return the runningTime
	 */
	public int getRunningTime() {
		return runningTime;
	}

	/**
	 * @param runningTime the runningTime to set
	 */
	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	/**
	 * @return the taglines
	 */
	public String getTaglines() {
		return taglines;
	}

	/**
	 * @param taglines the taglines to set
	 */
	public void setTaglines(String taglines) {
		this.taglines = taglines;
	}

	/**
	 * @return the plot
	 */
	public String getPlot() {
		return plot;
	}

	/**
	 * @param plot the plot to set
	 */
	public void setPlot(String plot) {
		this.plot = plot;
	}

	/**
	 * @return the filmingLocations
	 */
	public String getFilmingLocations() {
		return filmingLocations;
	}

	/**
	 * @param filmingLocations the filmingLocations to set
	 */
	public void setFilmingLocations(String filmingLocations) {
		this.filmingLocations = filmingLocations;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id: " + id + ". Name: " + name + ". Was filmed in: " + filmingLocations;
	}
}
