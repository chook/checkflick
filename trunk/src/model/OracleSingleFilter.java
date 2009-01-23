package model;

import java.util.HashSet;
import java.util.Set;

import controller.filter.AbsSingleFilter;
import controller.filter.FilterOptionEnum;

public class OracleSingleFilter extends AbsSingleFilter {

	public OracleSingleFilter() {

	}

	/**
	 * @param option
	 * @param table
	 * @param column
	 * @param value
	 */
	public OracleSingleFilter(FilterOptionEnum option, String table,
			String column, String value) {
		super(option, table, column, value);
	}

	@Override
	public String toString() {

		String strFilter = "";

		switch (option) {
		case Date: // TODO: This is wrong but i still haven't checked how to
					// properly do it
		case Number: {
			strFilter = table + "." + column + "=" + value;
			break;
		}
		case String: {
			strFilter = table + "." + column + " LIKE '" + value + "'";
			break;
		}
		case StringArray: {
			strFilter = table + "." + column + " IN (" + value + ")";
			break;
		}
		case NumberRange: {
			// We rely on the fact that value is of type "3 and 10"
			// Will mean: 3<=x<=10
			strFilter = table + "." + column + " BETWEEN " + value;
			break;
		}
		}

		return strFilter;
	}

	@Override
	public Set<String> toTablesSet() {
		if (table != null && table != "") {
			Set<String> s = new HashSet<String>(1);
			s.add(table);
			return s;
		} else {
			return null;
		}
	}
}
