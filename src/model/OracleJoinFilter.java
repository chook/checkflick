/**
 * 
 */
package model;

import java.util.LinkedHashSet;
import java.util.Set;
import controller.filter.AbsJoinFilter;
import controller.filter.AbsSingleFilter;

/**
 * This class represents an oracle join filter
 * @author Chook
 *
 */
public class OracleJoinFilter extends AbsJoinFilter {

	/**
	 * @param single
	 * @param ltable
	 * @param rtable
	 * @param lcolumn
	 * @param rcolumn
	 */
	public OracleJoinFilter(AbsSingleFilter single, String ltable,
			String lcolumn, String rtable, String rcolumn) {
		super(single, ltable, lcolumn, rtable, rcolumn);
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuffer buffer = new StringBuffer();
		if(single != null) {
			buffer.append(single);
			buffer.append(" AND ");
		}
		buffer.append(getTable());
		buffer.append('.');
		buffer.append(getColumn());
		buffer.append('=');
		buffer.append(getOtherTable());
		buffer.append('.');
		buffer.append(getOtherColumn());
		return buffer.toString();
	}

	@Override
	public Set<String> toTablesSet() {
		Set<String> s = new LinkedHashSet<String>();
		if (single.getTable() != null && single.getTable() != "") {
			s.add(single.getTable());
		}
		s.add(getTable());
		s.add(getOtherTable());
				
		return s;
	}
}
