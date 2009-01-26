package controller.filter;
@Deprecated
public class Filter {
	FilterOptionEnum option;
	String	 	 columnName;
	String		 value;
	
	public Filter() {
		
	}
	
	/**
	 * Initiates a new filter movie object
	 * @param option - Which search type should be used
	 * @param columnName - Which column name should be used
	 * @param value - Which value to search on
	 */
	public Filter(FilterOptionEnum option, String columnName, String value) {
		super();
		this.option = option;
		this.columnName = columnName;
		this.value = value;
	}

	@Override
	public String toString() {
		
		String strFilter = "";
		
		switch(option)
		{
			case DATE: // TODO: This is wrong but i still haven't checked how to properly do it
			case NUMBER:
			{
				strFilter = columnName + "=" + value;
				break;
			}
			case STRING:
			{
				strFilter = columnName + " LIKE '" + value + "'";
				break;
			}
			case STRING_ARRAY:
			{
				strFilter = columnName + " IN (" + value + ")";
				break;
			}
			case NUMBER_RANGE:
			{
				// We rely on the fact that value is of type "3 and 10"
				// Will mean: 3<=x<=10
				strFilter = columnName + " BETWEEN " + value;
				break;
			}
		}
		
		return strFilter;
	}
}
