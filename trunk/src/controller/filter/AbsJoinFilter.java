package controller.filter;

public abstract class AbsJoinFilter extends AbsFilter {
	protected AbsSingleFilter single = null;

	protected String otable;
	protected String ocolumn;

	/**
	 * @return the rtable
	 */
	public String getOtherTable() {
		return otable;
	}

	/**
	 * @param otable
	 *            the rtable to set
	 */
	protected void setOtherTable(String otable) {
		this.otable = otable;
	}

	/**
	 * @return the rcolumn
	 */
	public String getOtherColumn() {
		return ocolumn;
	}

	/**
	 * @param ocolumn
	 *            the rcolumn to set
	 */
	protected void setOtherColumn(String ocolumn) {
		this.ocolumn = ocolumn;
	}

	/**
	 * @param single
	 * @param table
	 * @param column
	 * @param otable
	 * @param ocolumn
	 */
	protected AbsJoinFilter(AbsSingleFilter single, String table, String column,
			String otable, String ocolumn) {
		super(table, column);
		this.otable = otable;
		this.ocolumn = ocolumn;
		this.single = single;
	}
}
