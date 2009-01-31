/**
 * 
 */
package controller.entity;

import java.util.List;
import java.util.Map;

/**
 * A named relation
 * @author Chook
 * 
 */
public class NamedRelation extends Relation {
	protected String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param id
	 * @param secondaryId
	 * @param name
	 */
	public NamedRelation(int id, int secondaryId, String name) {
		super(id, secondaryId);
		this.name = name;
	}
	
	@Override
	public String toString() {
		return super.toString() + " name: " + getName();
	}
	
	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(getName()));
		return list;
	}
	
	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("name", getName());
		return map;
	}
}
