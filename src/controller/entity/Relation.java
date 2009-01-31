/**
 * 
 */
package controller.entity;

import java.util.List;
import java.util.Map;

/**
 * A relation entity, has two id's
 * 
 * @author Chook
 * 
 */
public class Relation extends AbsType {
	int secondaryId;

	/**
	 * @return the secondaryId
	 */
	public int getSecondaryId() {
		return secondaryId;
	}
	
	/**
	 * @param secondaryId
	 *            the secondaryId to set
	 */
	protected void setSecondaryId(int secondaryId) {
		this.secondaryId = secondaryId;
	}
	
	/**
	 * @param id
	 *            - The main id of the relation
	 * @param secondaryId
	 *            - The secondary id of the relation
	 */
	public Relation(int id, int secondaryId) {
		super(id);
		this.secondaryId = secondaryId;
	}

	@Override
	public String toString() {
		return super.toString() + " secondary id: " + getSecondaryId();
	}

	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(getSecondaryId()));
		return list;
	}

	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("secondaryId", String.valueOf(getSecondaryId()));
		return map;
	}
}
