package org.xandercat.swing.zenput.validator;

import org.xandercat.swing.zenput.annotation.ValidateFloat;
import org.xandercat.swing.zenput.error.ValidationException;

public class FloatValidator extends AbstractValidator<Float> {
	
	private float minValue = Float.MIN_VALUE;
	private float maxValue = Float.MAX_VALUE;
	
	public static FloatValidator newValidator(ValidateFloat annotation) {
		return new FloatValidator(annotation.min(), annotation.max());
	}
	
	public FloatValidator() {
		super();
	}

	public FloatValidator(Float minValue, Float maxValue) {
		this();
		if (minValue != null) {
			this.minValue = minValue;
		}
		if (maxValue != null) {
			this.maxValue = maxValue;
		}
	}
	
	@Override
	public Class<Float> getValueType() {
		return Float.class;
	}

	@Override
	public void validate(String fieldName, Float value) throws ValidationException {
		if (value.floatValue() > this.maxValue) {
			throw new ValidationException(fieldName, "Value cannot be greater than " + this.maxValue);
		}
		if (value.floatValue() < this.minValue) {
			throw new ValidationException(fieldName, "Value cannot be less than " + this.minValue);
		}
	}
}
