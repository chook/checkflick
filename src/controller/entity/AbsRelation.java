/**
 * 
 */
package controller.entity;

/**
 * An abstract relation entity, has two id's
 * @author Chook
 * 
 */
public abstract class AbsRelation extends AbsEntity {
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
}
