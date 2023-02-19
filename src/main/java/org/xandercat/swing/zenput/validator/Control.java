package org.xandercat.swing.zenput.validator;

/**
 * Field-level control that can be used to determine whether or not a field should be validated.
 * 
 * @author Scott Arnold
 *
 * @param <T> value type for the field
 */
public interface Control<T> {
	
	/**
	 * Returns whether or not the given field should be validated. 
	 * 
	 * @param fieldName	name of field being validated
	 * @param value		value of the field
	 * 
	 * @return			whether or not the given value should be validated
	 */
	public boolean shouldValidate(String fieldName, T value);
}
