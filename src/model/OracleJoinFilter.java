/**
 * 
 */
package model;

import java.util.HashSet;
import java.util.Set;

import controller.filter.AbsJoinFilter;
import controller.filter.AbsSingleFilter;

/**
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
		buffer.append(ltable);
		buffer.append('.');
		buffer.append(lcolumn);
		buffer.append('=');
		buffer.append(rtable);
		buffer.append('.');
		buffer.append(rcolumn);
		return buffer.toString();
	}

	@Override
	public Set<String> toTablesSet() {
		Set<String> s = new HashSet<String>(3);
		if (single.getTable() != null && single.getTable() != "") {
			
			s.add(single.getTable());
		}

		s.add(getLtable());
		s.add(getRtable());
				
		return s;
	}
}
