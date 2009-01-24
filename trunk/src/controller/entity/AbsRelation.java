/**
 * 
 */
package controller.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * An abstract relation entity, has two id's
 * 
 * @author Chook
 * 
 */
public abstract class AbsRelation extends AbsDataType {
	int secondaryId;

	/**
	 * @param id
	 *            - The main id of the relation
	 * @param secondaryId
	 *            - The secondary id of the relation
	 */
	protected AbsRelation(int id, int secondaryId) {
		super(id);
		this.secondaryId = secondaryId;
	}

	/**
	 * @return the secondaryId
	 */
	protected int getSecondaryId() {
		return secondaryId;
	}

	/**
	 * @param secondaryId
	 *            the secondaryId to set
	 */
	protected void setSecondaryId(int secondaryId) {
		this.secondaryId = secondaryId;
	}

	@Override
	public String toString() {
		return super.toString() + " second id: " + getSecondaryId();
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
		map.put("secondId", String.valueOf(getSecondaryId()));
		return map;
	}
}
