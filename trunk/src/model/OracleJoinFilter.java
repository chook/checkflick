/**
 * 
 */
package model;

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
}
