/**
 * 
 */
package controller.entity;

/**
 * @author Chook
 * 
 */
public class NamedRelation extends AbsRelation {
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

	/**
	 * @param id
	 * @param secondaryId
	 * @param name
	 */
	public NamedRelation(int id, int secondaryId, String name) {
		super(id, secondaryId);
		this.name = name;
	}
}
