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
	private String searchName;

	/**
	 * @return the searchName
	 */
	public String getSearchName() {
		return searchName;
	}

	/**
	 * @param searchName
	 *            the searchName to set
	 */
	protected void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public NamedCastingRelation(int id, int secondaryId, int productionRole,
			boolean actor, String actorRole, int actorCreditRank,
			String searchName) {
		super(id, secondaryId, productionRole, actor, actorRole,
				actorCreditRank);
		this.searchName = searchName;
	}

	
	@Override
	public String toString() {
		return super.toString() + " SEARCH NAME: " + getSearchName();
	}

	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(getSearchName());
		return list;
	}

	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("searchName", getSearchName());
		return map;
	}
}
