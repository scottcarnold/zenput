package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator for values that are required but need a custom test to determine
 * if a value exists.  This validator can be used in place of a RequiredValidator 
 * when simply testing for a null value is not sufficient.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type to be validated
 */
public class RequiredNotEmptyValidator<T> extends AbstractValidator<T> {
	
	private EmptyTest<T> emptyTest;
	private Class<T> valueType;
	
	public RequiredNotEmptyValidator(Class<T> valueType, EmptyTest<T> emptyTest) {
		super();
		this.valueType = valueType;
		this.emptyTest = emptyTest;
	}
	
	@Override
	public Class<T> getValueType() {
		return valueType;
	}

	@Override
	public boolean shouldValidate(String fieldName, T value) throws ValidationException {
		return true;
	}

	@Override
	public void validate(String fieldName, T value) throws ValidationException {
		if (value == null || emptyTest.isEmpty(value)) {
			throw new ValidationException(fieldName, "Value is required.");
		}
	}
}
