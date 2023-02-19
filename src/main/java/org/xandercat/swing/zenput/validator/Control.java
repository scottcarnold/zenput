package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Field-level control that can be used to determine whether or not a field should be validated.
 * 
 * @author Scott Arnold
 *
 * @param <T> value type for the field
 */
public interface Control<T> {
	
	/**
	 * Returns the class type of the objects to be validated.
	 * 
	 * @return		class type of the objects to be validated
	 */
	public Class<T> getValueType();
	
	/**
	 * Returns whether or not the given field should be validated. 
	 * 
	 * @param fieldName	name of field being validated
	 * 
	 * @return			whether or not the given value should be validated
	 * 
	 * @throws ValidationException if result cannot be determined
	 */
	public boolean shouldValidate(String fieldName) throws ValidationException;
}
