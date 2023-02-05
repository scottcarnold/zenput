package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator for long values.
 * 
 * @author Scott Arnold
 */
public class LongValidator extends AbstractValidator<Long> {
	
	private long minValue = Long.MIN_VALUE;
	private long maxValue = Long.MAX_VALUE;
	
	public LongValidator() {
		super();
	}
	
	public LongValidator(long minValue, long maxValue) {
		this();
		this.minValue = minValue;
		this.maxValue = maxValue;
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
