package controller.entity;

/**
 * Movie entity
 * 
 * @author Chook
 * 
 */
public class MovieEntity extends NamedEntity {
	private int year;
	private String romanNotation;
	private String madeFor;
	private int colorInfo;
	private int runningTime;
	private String taglines;
	private String plot;
	private String filmingLocations;

	/**
	 * Default Constructor
	 */
	public MovieEntity() {
		super();
	}

	/**
	 * Simplified Constructor - Only id
	 * 
	 * @param id
	 */
	public MovieEntity(int id) {
		super(id);
	}

	/**
	 * @return the year
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            the year to set
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return the romanNotation
	 */
	public String getRomanNotation() {
		return romanNotation;
	}

	/**
	 * @param romanNotation
	 *            the romanNotation to set
	 */
	public void setRomanNotation(String romanNotation) {
		this.romanNotation = romanNotation;
	}
	/**
	 * @return the madeFor info
	 */
	public String getMadeFor() {
		return madeFor;
	}

	/**
	 * @param madeFor
	 *            the madeFor info to set
	 */
	public void setMadeFor(String madeFor) {
		this.madeFor = madeFor;
	}
	
	/**
	 * @return the colorInfo
	 */
	public int getColorInfo() {
		return colorInfo;
	}

	/**
	 * @param colorInfo
	 *            the colorInfo to set
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
	 * @param runningTime
	 *            the runningTime to set
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
	 * @param taglines
	 *            the taglines to set
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
	 * @param plot
	 *            the plot to set
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
	 * @param filmingLocations
	 *            the filmingLocations to set
	 */
	public void setFilmingLocations(String filmingLocations) {
		this.filmingLocations = filmingLocations;
	}

	/**
	 * A Movie Constructor
	 * 
	 * @param id
	 * @param name
	 * @param year
	 * @param colorInfo
	 * @param runningTime
	 * @param taglines
	 * @param plot
	 * @param filmingLocations
	 */
	public MovieEntity(int id, String name, int year, String romanNotation, String madeFor, int colorInfo, int runningTime,
			String taglines, String plot, String filmingLocations) {
		super(id, name);

		this.year = year;
		this.romanNotation = romanNotation;
		this.madeFor = madeFor;
		this.colorInfo = colorInfo;
		this.runningTime = runningTime;
		this.taglines = taglines;
		this.plot = plot;
		this.filmingLocations = filmingLocations;
	}

	/**
	 * used to get the raw name of the movie, stripped from its roman notations and madeFor info
	 * @return String the movie name without roman notations and madeFor info
	 */
	public String getRawName() {
		return name;
	}
	
	/**
	 * an overriding of NamedEntity.getName(), since the name here includes also roman notation and madeFor info
	 * @return String the name of the movie, concatenated with roman notation if present, and madeFor info 
	 */
	@Override
	public String getName() {
		StringBuilder movieName;
		
		movieName = new StringBuilder(name);
		if (romanNotation != null)
			movieName.append(" (").append(romanNotation).append(")");
		if (madeFor != null)
			movieName.append(" (").append(madeFor).append(")");
		
		return movieName.toString();
	}
	

	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id: " + getId() + ". Name: " + getName() + ". Was filmed in: "
				+ getFilmingLocations();
	}
}
