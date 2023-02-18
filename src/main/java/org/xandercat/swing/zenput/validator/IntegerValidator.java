package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.annotation.ValidateInteger;
import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator for Integers.
 * 
 * @author Scott Arnold
 */
public class IntegerValidator extends AbstractValidator<Integer> {
	
	private int minValue = Integer.MIN_VALUE;
	private int maxValue = Integer.MAX_VALUE;
	
	public static IntegerValidator newValidator(ValidateInteger annotation) {
		return new IntegerValidator(annotation.min(), annotation.max());
	}
	
	public IntegerValidator() {
		super();
	}

	public IntegerValidator(Integer minValue, Integer maxValue) {
		this();
		if (minValue != null) {
			this.minValue = minValue;
		}
		if (maxValue != null) {
			this.maxValue = maxValue;
		}
	}
	
	@Override
	public Class<Integer> getValueType() {
		return Integer.class;
	}

	@Override
	public void validate(String fieldName, Integer value) throws ValidationException {
		if (value.intValue() > this.maxValue) {
			throw new ValidationException(fieldName, "Value cannot be greater than " + this.maxValue);
		}
		if (value.intValue() < this.minValue) {
			throw new ValidationException(fieldName, "Value cannot be less than " + this.minValue);
		}
	}
}
