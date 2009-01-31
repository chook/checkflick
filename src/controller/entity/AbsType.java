/**
 * 
 */
package controller.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is the most basic and that is why abstract representation of an entity.
 * @author Chook
 * 
 */
public abstract class AbsType {
	private int id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	protected AbsType(int id) {
		this.id = id;
	}

	protected AbsType() {
		id = 0;
	}
	
	@Override
	public String toString() {
		return "id: " + getId();
	}
	
	/**
	 * This function returns a list of all the properties as strings 
	 * @return A list of properties as string
	 */
	public List<String> toStringList() {
		List<String> list = new ArrayList<String>(1);
		list.add(String.valueOf(getId()));
		return list;
	}
	
	/**
	 * This function returns a map of strings by property name / value
	 * @return The map
	 */
	public Map<String, String> toStringMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", String.valueOf(getId()));
		return map;
	}
}
