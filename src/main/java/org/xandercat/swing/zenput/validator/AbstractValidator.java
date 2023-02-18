package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * AbstractValidator provides default behavior for the shouldValidate(...) method by 
 * returning true whenever the value to be validated is not null, or in the 
 * case of a string, the value is non-blank.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of value to be validated
 */
public abstract class AbstractValidator<T> implements Validator<T> {

	public AbstractValidator() {
	}
	
	@Override
	public boolean shouldValidate(String fieldName, T value) throws ValidationException {
		return value != null && (!(value instanceof String) || ((String) value).trim().length() > 0);
	}
}
