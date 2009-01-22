/**
 * 
 */
package controller.entity;

/**
 * @author Chook
 * 
 */
public class CategorizedEntity extends NamedEntity {
	int type;

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            - the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * 
	 */
	public CategorizedEntity() {
		super();
	}

	/**
	 * @param name
	 */
	public CategorizedEntity(int id, String name, int type) {
		super(id, name);
		this.type = type;
	}
}
