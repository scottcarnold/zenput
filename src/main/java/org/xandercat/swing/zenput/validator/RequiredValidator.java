package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.annotation.ValidateRequired;
import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator to require that a field contains a value.  A value is considered to be
 * valid if it is non-null, and in the specific case of a String, that the string 
 * has a trimmed length greater than zero.
 * 
 * @author Scott Arnold
 */
public class RequiredValidator extends AbstractValidator<Object> {

	public static RequiredValidator newValidator(ValidateRequired annotation) {
		return new RequiredValidator();
	}
	
	public RequiredValidator() {
		super();
	}
	
	@Override
	public Class<Object> getValueType() {
		return Object.class;
	}

	@Override
	public boolean shouldValidate(String fieldName, Object value) throws ValidationException {
		return true; // override AbstractValidator implementation
	}

	@Override
	public void validate(String fieldName, Object value) throws ValidationException {
		boolean empty = value == null;
		if (!empty && value instanceof String) {
			empty = ((String) value).trim().length() == 0;
		}
		if (empty) {
			throw new ValidationException(fieldName, "validator.required");
		}
	}

}
