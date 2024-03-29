package controller.entity;

import java.util.List;

/**
 * Movie entity
 * @author Chook
 * 
 */
public class MovieEntity extends NamedEntity {
	private int year;
	private String romanNotation;
	private String madeFor;
	private int runningTime;
	private String fullMovieName;
	private String plot;

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
	 * @return the the fule movie name
	 */
	public String getFullMovieName() {
		return fullMovieName;
	}

	/**
	 * @param fullMovieName
	 *            the full movie name to set
	 */
	public void setFullMovieName(String fullMovieName) {
		this.fullMovieName = fullMovieName;
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
	 * Movie entity
	 */
	public MovieEntity(int id, String name, int year, String romanNotation, String madeFor,
			int runningTime, String fullMovieName, String plot) {
		super(id, name);
		this.year = year;
		this.romanNotation = romanNotation;
		this.madeFor = madeFor;
		this.runningTime = runningTime;
		this.fullMovieName = fullMovieName;
		this.plot = plot;
	}

	/**
	 * A Movie Constructor
	 * 
	 * @param id
	 * @param name
	 * @param year
	 * @param runningTime
	 * @param fullMovieNames
	 * @param plot
	 */
	public MovieEntity(int id, String name, int year, String romanNotation, String madeFor, int colorInfo, int runningTime,
			String fullMovieNames, String plot, String filmingLocations) {
		super(id, name);
		this.year = year;
		this.romanNotation = romanNotation;
		this.madeFor = madeFor;
		this.runningTime = runningTime;
		this.fullMovieName = fullMovieNames;
		this.plot = plot;
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
		StringBuilder movieName = new StringBuilder(name);
		if (romanNotation != null)
			movieName.append(" (").append(romanNotation).append(")");
		if (madeFor != null)
			movieName.append(" (").append(madeFor).append(")");
		
		return movieName.toString();
	}
		
	@Override
	public String toString() {
		return "id: " + getId() + ". Name: " + getName() + ".";
	}

	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(getYear()));
		list.add(String.valueOf(getRunningTime()));
		list.add(getFullMovieName());
		list.add(getPlot());
		return list;
	}
}
