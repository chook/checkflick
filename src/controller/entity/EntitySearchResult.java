package controller.entity;

public class EntitySearchResult extends AbsEntity {
	int year;
	
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

	public EntitySearchResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EntitySearchResult(int id, String name, int year) {
		super(id, name);
		this.year = year;
	}	
}
