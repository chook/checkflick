/**
 * 
 */
package controller.entity;

import java.util.List;
import java.util.Map;

/**
 * A named entity
 * 
 * @author CHENH
 * 
 */
public class NamedEntity extends AbsDataType {
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

	public NamedEntity() {

	}

	public NamedEntity(int id) {
		super(id);
	}

	public NamedEntity(String name) {
		super();
		this.name = name;
	}

	public NamedEntity(int id, String name) {
		super(id);
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
	
	@Override
	public String toInsertString() {
		return super.toInsertString() + ", '" + getName() + "'";
	}
}
