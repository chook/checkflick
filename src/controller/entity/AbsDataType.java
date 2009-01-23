/**
 * 
 */
package controller.entity;

/**
 * This is the most basic and that is why abstract representation of an entity.
 * @author Chook
 * 
 */
public abstract class AbsDataType {
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

	protected AbsDataType(int id) {
		this.id = id;
	}

	protected AbsDataType() {
		id = 0;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "id: " + getId();
	}
}
