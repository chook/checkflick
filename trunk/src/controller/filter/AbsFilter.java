package controller.filter;

import java.util.List;
import java.util.Set;


public abstract class AbsFilter {
	protected String table;
	protected String column;
	

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
	 * @param table
	 *            the table to set
	 */
	protected void setTable(String table) {
		this.table = table;
	}

	/**
	 * @return the table
	 */
	public String getTable() {
		return table;
	}

	public abstract String toString();

	protected AbsFilter() {
		
	}
	
	/**
	 * @param table
	 * @param column
	 * @param value
	 */
	protected AbsFilter(String table, String column) {
		super();
		this.table = table;
		this.column = column;
	}

	public abstract Set<String> toTablesSet();
}
