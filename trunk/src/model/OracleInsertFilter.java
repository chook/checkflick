/**
 * 
 */
package model;

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
		case Date: // TODO: Implement this
		case Boolean: 
			return "'" + ((Boolean.parseBoolean(super.getValue()) == true) ? "Y" : "N") + "'";
		case String:
			return "'" + super.getValue() + "'";
		}
		return null;
	}
}
