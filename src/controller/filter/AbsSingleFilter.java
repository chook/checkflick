package controller.filter;

import controller.enums.FilterOptionEnum;

/**
 * An abstract representation of a single filter
 * 
 * @author Chook
 * 
 */
public abstract class AbsSingleFilter extends AbsFilter {

	protected FilterOptionEnum option;
	protected String value;

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	protected void setValue(String value) {
		this.value = value;
	}

	protected AbsSingleFilter() {

	}

	protected AbsSingleFilter(FilterOptionEnum option, String table,
			String column, String value) {
		super(table, column);
		this.option = option;
		this.value = value;
	}
}
