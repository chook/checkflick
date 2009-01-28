package controller.entity;

import java.util.List;
import java.util.Map;

/**
 * This class extends {@link CastingRelation} and adds a name to one of the IDs 
 * the search name can be the person name (if the object represents a casting of movie) 
 * or a movie name (if the object represents a person filmography)
 * @author Chook
 *
 */
public class NamedCastingRelation extends CastingRelation {
	private String name;

	/**
	 * @return the searchName
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the searchName to set
	 */
	protected void setName(String name) {
		this.name = name;
	}

	public NamedCastingRelation(int id, int secondaryId, int productionRole,
			boolean actor, String actorRole, int actorCreditRank,
			String name) {
		super(id, secondaryId, productionRole, actor, actorRole,
				actorCreditRank);
		this.name = name;
	}

	
	@Override
	public String toString() {
		return super.toString() + " NAME: " + getName();
	}

	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(getName());
		return list;
	}

	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("name", getName());
		return map;
	}
}
