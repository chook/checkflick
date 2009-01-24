/**
 * 
 */
package controller.entity;

import java.util.List;
import java.util.Map;

/**
 * @author Chook
 * 
 */
public class CategorizedRelation extends AbsRelation {
	protected int type;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	public CategorizedRelation(int id, int secondaryId, int type) {
		super(id, secondaryId);
		this.type = type;
	}

	@Override
	public String toString() {
		return super.toString() + " type: " + getType();
	}
	
	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(getType()));
		return list;
	}

	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("type", String.valueOf(getType()));
		return map;
	}
}
