/**
 * 
 */
package controller.entity;

/**
 * @author CHENH
 *
 */
public abstract class AbsEntity {
	private int 	id;
	private String 	name;
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public AbsEntity() {
		
	}
	
	public AbsEntity(int id) {
		super();
		this.id = id;
	}
	
	public AbsEntity(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
}
