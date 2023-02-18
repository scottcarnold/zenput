package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.annotation.ValidateLong;
import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator for long values.
 * 
 * @author Scott Arnold
 */
public class LongValidator extends AbstractValidator<Long> {
	
	private long minValue = Long.MIN_VALUE;
	private long maxValue = Long.MAX_VALUE;
	
	public static LongValidator newValidator(ValidateLong annotation) {
		return new LongValidator(annotation.min(), annotation.max());
	}
	
	public LongValidator() {
		super();
	}
	
	public LongValidator(Long minValue, Long maxValue) {
		this();
		if (minValue != null) {
			this.minValue = minValue;
		}
		if (maxValue != null) {
			this.maxValue = maxValue;
		}
	}
	
	@Override
	public Class<Long> getValueType() {
		return Long.class;
	}

	@Override
	public void validate(String fieldName, Long value) throws ValidationException {
		if (value.longValue() > this.maxValue) {
			throw new ValidationException(fieldName, "Value cannot be greater than " + this.maxValue);
		}
		if (value.longValue() < this.minValue) {
			throw new ValidationException(fieldName, "Value cannot be less than " + this.minValue);
		}
	}
}
