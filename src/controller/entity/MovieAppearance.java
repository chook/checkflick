package controller.entity;

public class MovieAppearance extends AbsRelation {
	private int productionRole;
	private boolean actor;
	private int actorRole;
	private int actorCreditRank;

	/**
	 * @return the productionRole
	 */
	public int getProductionRole() {
		return productionRole;
	}

	/**
	 * @param productionRole
	 *            the productionRole to set
	 */
	public void setProductionRole(int productionRole) {
		this.productionRole = productionRole;
	}

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
		super(id, secondaryId);
		this.productionRole = productionRole;
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
		super(id, secondaryId);
		this.productionRole = productionRole;
		this.actor = actor;
		this.actorRole = actorRole;
		this.actorCreditRank = actorCreditRank;
	}
}
