package controller.filter;

public abstract class AbsJoinFilter extends AbsFilter{
	protected AbsSingleFilter single = null;
	
	protected String ltable;
	protected String rtable;
	protected String lcolumn;
	protected String rcolumn;
	
	/**
	 * @param single
	 * @param ltable
	 * @param rtable
	 * @param lcolumn
	 * @param rcolumn
	 */
	public AbsJoinFilter(AbsSingleFilter single, String ltable, 
			String lcolumn, String rtable, String rcolumn) {
		super();
		this.single  = single;
		this.ltable  = ltable;
		this.lcolumn = lcolumn;
		this.rtable  = rtable;
		this.rcolumn = rcolumn;
	}
}
