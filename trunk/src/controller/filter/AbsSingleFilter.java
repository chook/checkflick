package controller.filter;

/**
 * @author Chook
 * 
 */
public abstract class AbsSingleFilter extends AbsFilter {

	protected FilterOptionEnum option;
	protected String table;
	protected String column;
	protected String value;

	/**
	 * @return the option
	 */
	protected FilterOptionEnum getOption() {
		return option;
	}

	/**
	 * @param option
	 *            the option to set
	 */
	protected void setOption(FilterOptionEnum option) {
		this.option = option;
	}

	/**
	 * @return the column
	 */
	public String getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            the column to set
	 */
	protected void setColumn(String column) {
		this.column = column;
	}

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

	/**
	 * @param table
	 *            the table to set
	 */
	protected void setTable(String table) {
		this.table = table;
	}

	protected AbsSingleFilter() {

	}

	protected AbsSingleFilter(FilterOptionEnum option, String table,
			String column, String value) {
		this.option = option;
		this.table = table;
		this.column = column;
		this.value = value;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}
}
