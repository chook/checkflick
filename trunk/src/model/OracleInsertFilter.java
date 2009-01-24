/**
 * 
 */
package model;

import java.util.Set;

import controller.filter.AbsInsertFilter;
import controller.filter.AbsSingleFilter;
import controller.filter.FilterOptionEnum;

/**
 * @author Chook
 *
 */
public class OracleInsertFilter extends OracleSingleFilter {

	public OracleInsertFilter(FilterOptionEnum option, String table, String fieldName, String fieldValue) {
		super(option, table, fieldName, fieldValue);
	}

	@Override
	public String getValue() {
		switch(option) {
		case Number:
			return super.getValue();
		case String:
			return "'" + super.getValue() + "'";
		case Date:
			return "'" + super.getValue() + "'"; // TODO: Needs to implement
		}
		return null;
	}
}
