/**
 * 
 */
package controller.entity;

/**
 * This is the most basic and that is why abstract representation of an entity.
 * @author Chook
 * 
 */
public abstract class AbsEntity {
	private int id;

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	protected AbsEntity(int id) {
		this.id = id;
	}

	protected AbsEntity() {
		id = 0;
	}
}
