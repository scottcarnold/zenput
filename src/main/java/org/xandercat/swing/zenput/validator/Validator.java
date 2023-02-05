package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validates a value.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of field to be validated
 */
public interface Validator<T> {

	/**
	 * Returns the class type of the objects to be validated.
	 * 
	 * @return		class type of the objects to be validated
	 */
	public Class<T> getValueType();
	
	/**
	 * Returns whether or not the given value should be validated.  Under a typical
	 * usage pattern, this will return true whenever the value is considered not empty.
	 * 
	 * This method is allowed to throw a ValidationException; however, under most 
	 * circumstances, ValidationExceptions should only be thrown by the validate(...) method.
	 * 
	 * @param fieldName	name of field being validated
	 * @param value		value to test
	 * 
	 * @return			whether or not the given value should be validated
	 * 
	 * @throws ValidationException
	 */
	public boolean shouldValidate(String fieldName, T value) throws ValidationException;
	
	/**
	 * Validates the given value, throwing a ValidationException is the value is 
	 * invalid. 
	 * 
	 * @param fieldName	name of field being validated
	 * @param value		value to test
	 * 
	 * @throws ValidationException
	 */
	public void validate(String fieldName, T value) throws ValidationException;

}
