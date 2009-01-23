package controller.entity;

public class GeoEntity extends BasicSearchEntity {
	protected int country;

	/**
	 * @param country
	 */
	public GeoEntity(int country) {
		super();
		this.country = country;
	}

	/**
	 * @param id
	 * @param name
	 * @param year
	 */
	public GeoEntity(int id, String name, int year, int country) {
		super(id, name, year);
		this.country = country;
	}
}
