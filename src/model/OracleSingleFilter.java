package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
		case DATE: // TODO: This is wrong but i still haven't checked how to
					// properly do it
		case NUMBER:
			strFilter = table + "." + column + "=" + value;
			break;
		case STRING:
			strFilter = table + "." + column + " LIKE '%" + value + "%'";
			break;
		case STRING_ARRAY:
			strFilter = table + "." + column + " IN (" + value + ")";
			break;
		case NUMBER_RANGE:
			// We rely on the fact that value is of type "3 and 10"
			// Will mean: 3<=x<=10
			strFilter = table + "." + column + " BETWEEN " + value;
			break;
		}

		return strFilter;
	}

	@Override
	public String getValue() {
		switch(option) {
		case NUMBER:
			return super.getValue();
		case DATE: 
			return super.getValue();
		case BOOLEAN: 
			return "'" + ((Boolean.parseBoolean(super.getValue()) == true) ? "Y" : "N") + "'";
		case STRING:
			return "'" + super.getValue().replace("'", "''") + "'";
		}
		return null;
	}
	
	@Override
	public Set<String> toTablesSet() {
		if (table != null && table != "") {
			Set<String> s = new LinkedHashSet<String>();
			s.add(table);
			return s;
		} else {
			return null;
		}
	}
}
