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
