package controller.filter;

public abstract class AbsInsertFilter {

	protected FilterOptionEnum option;
	private String fieldName;
	private String fieldValue;

	/**
	 * @return the fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * @param fieldName
	 *            the fieldName to set
	 */
	protected void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * @return the fieldValue
	 */
	public String getFieldValue() {
		return fieldValue;
	}

	/**
	 * @param fieldValue
	 *            the fieldValue to set
	 */
	protected void setFieldValue(String fieldValue) {
		this.fieldValue = fieldValue;
	}

	/**
	 * 
	 */
	public AbsInsertFilter(String fieldName, String fieldValue) {
		setFieldName(fieldName);
		setFieldValue(fieldValue);
	}
}
