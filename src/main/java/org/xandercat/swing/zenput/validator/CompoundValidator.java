package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator that combines the validation of multiple validators into a single validator.
 * 
 * @author Scott Arnold
 *
 * @param <T>	type of value to validate
 */
public class CompoundValidator<T> implements Validator<T> {

	private final Validator<? super T>[] validators;
	private final Class<T> valueType;
	
	public CompoundValidator(Validator<? super T>... validators) {
		this.validators = validators;
		Class<? super T> valueType = null;
		for (Validator<? super T> validator : validators) {
			if (valueType == null || validator.getValueType().isAssignableFrom(valueType)) {
				valueType = validator.getValueType();
			}
		}
		this.valueType = (Class<T>) valueType;
	}
	
	public CompoundValidator(Class<T> valueType, Validator<? super T>... validators) {
		this.validators = validators;
		this.valueType = valueType;
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
		for (Validator<? super T> validator : validators) {
			if (validator.shouldValidate(fieldName, value)) {
				validator.validate(fieldName, value);
			}
		}
	}
}
