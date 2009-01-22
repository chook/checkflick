/**
 * 
 */
package controller.entity;

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
}
