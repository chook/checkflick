package model;

import java.util.LinkedHashSet;
import java.util.Set;
import controller.enums.FilterOptionEnum;
import controller.filter.AbsSingleFilter;

/**
 * This class represents an oracle single filter
 * @author Chook
 *
 */
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
		case NUMBER:
			strFilter = table + "." + column + "=" + value;
			break;
		case DATE: 
		case STRING:
			strFilter = table + "." + column + " = '" + value + "'";
			break;
		case STRING_ARRAY:
			strFilter = table + "." + column + " IN (" + value + ")";
			break;
		case STRING_WILDCARD:
			strFilter = "upper(" + table + "." + column + ") LIKE upper('%" + value + "%')";
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
			return (super.getValue() != null ? "'" + ((Boolean.parseBoolean(super.getValue()) == true) ? "Y" : "N") + "'" : null);
		case STRING:
			return (super.getValue() != null ? "'" + super.getValue().replace("'", "''") + "'" : null);
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
