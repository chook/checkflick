package controller.entity;

import java.util.List;
import java.util.Map;

/**
 * This is an entity with a country
 * @author Chook
 *
 */
public class GeoEntity extends DatedEntity {
	protected int country;

	/**
	 * @return the country
	 */
	public int getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	protected void setCountry(int country) {
		this.country = country;
	}

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
	
	@Override
	public String toString() {
		return super.toString() + " country: " + getCountry();
	}
	
	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(getCountry()));
		return list;
	}
	
	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("country", String.valueOf(getCountry()));
		return map;
	}
}
