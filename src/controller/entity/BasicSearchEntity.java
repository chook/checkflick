package controller.entity;

public class BasicSearchEntity extends NamedEntity {
	protected int year;

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

	public BasicSearchEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BasicSearchEntity(int id, String name, int year) {
		super(id, name);
		this.year = year;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id: " + getId() + " name: " + getName() + " year: " + getYear();
	}
}
