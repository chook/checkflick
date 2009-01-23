/**
 * 
 */
package controller.entity;

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
		// TODO Auto-generated method stub
		return super.toString() + " second id: " + getSecondaryId();
	}
}
