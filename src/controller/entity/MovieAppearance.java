package controller.entity;

import java.util.List;
import java.util.Map;

public class MovieAppearance extends CategorizedRelation {
	private boolean actor;
	private int actorRole;
	private int actorCreditRank;

	/**
	 * @return the actor
	 */
	public boolean isActor() {
		return actor;
	}

	/**
	 * @param actor
	 *            the actor to set
	 */
	public void setActor(boolean actor) {
		this.actor = actor;
	}

	/**
	 * @return the actorRole
	 */
	public int getActorRole() {
		return actorRole;
	}

	/**
	 * @param actorRole
	 *            the actorRole to set
	 */
	public void setActorRole(int actorRole) {
		this.actorRole = actorRole;
	}

	/**
	 * @return the actorCreditRank
	 */
	public int getActorCreditRank() {
		return actorCreditRank;
	}

	/**
	 * @param actorCreditRank
	 *            the actorCreditRank to set
	 */
	public void setActorCreditRank(int actorCreditRank) {
		this.actorCreditRank = actorCreditRank;
	}

	/**
	 * @param id
	 * @param secondaryId
	 * @param productionRole
	 */
	public MovieAppearance(int id, int secondaryId, int productionRole) {
		super(id, secondaryId, productionRole);
	}

	/**
	 * @param id
	 * @param secondaryId
	 * @param productionRole
	 * @param actor
	 * @param actorRole
	 * @param actorCreditRank
	 */
	public MovieAppearance(int id, int secondaryId, int productionRole,
			boolean actor, int actorRole, int actorCreditRank) {
		super(id, secondaryId, productionRole);
		this.actor = actor;
		this.actorRole = actorRole;
		this.actorCreditRank = actorCreditRank;
	}

	@Override
	public String toString() {
		return super.toString() + " actor: " + actor + " actorRole: " +
								  actorRole + " actorCreditRank: " + actorCreditRank; 
	}

	@Override
	public List<String> toStringList() {
		List<String> list = super.toStringList();
		list.add(String.valueOf(actor));
		list.add(String.valueOf(actorRole));
		list.add(String.valueOf(actorCreditRank));
		return list;
	}

	@Override
	public Map<String, String> toStringMap() {
		Map<String, String> map = super.toStringMap();
		map.put("actor", String.valueOf(actor));
		map.put("role", String.valueOf(actorRole));
		map.put("creditRank", String.valueOf(actorCreditRank));
		return map;
	}
}
