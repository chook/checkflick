/**
 * 
 */
package controller.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This is the most basic and that is why abstract representation of an entity.
 * @author Chook
 * 
 */
public abstract class AbsDataType {
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

	protected AbsDataType(int id) {
		this.id = id;
	}

	protected AbsDataType() {
		id = 0;
	}
	
	@Override
	public String toString() {
		return "id: " + getId();
	}
	
	public List<String> toStringList() {
		List<String> list = new ArrayList<String>(1);
		list.add(String.valueOf(getId()));
		return list;
	}
	
	public Map<String, String> toStringMap() {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", String.valueOf(getId()));
		return map;
	}
}
