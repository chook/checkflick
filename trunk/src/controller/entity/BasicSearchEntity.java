package controller.entity;

import java.util.Map;

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
	}

	/**
	 * @param id
	 */
	public BasicSearchEntity(int id) {
		super(id);
	}

	public BasicSearchEntity(int id, String name, int year) {
		super(id, name);
		this.year = year;
	}

	@Override
	public String toString() {
		return "id: " + getId() + " name: " + getName() + " year: " + getYear();
	}
	
	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("year", String.valueOf(getYear()));
		return map;
	}
}
