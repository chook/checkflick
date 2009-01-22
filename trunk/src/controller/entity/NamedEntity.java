/**
 * 
 */
package controller.entity;

/**
 * A named entity
 * 
 * @author CHENH
 * 
 */
public class NamedEntity extends AbsEntity {
	protected String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public NamedEntity() {

	}

	public NamedEntity(int id) {
		super(id);
	}

	public NamedEntity(String name) {
		super();
		this.name = name;
	}

	public NamedEntity(int id, String name) {
		super(id);
		this.name = name;
	}
}
