package org.xandercat.swing.zenput.validator;

import java.util.Collections;

import org.xandercat.swing.zenput.annotation.ValidateDouble;
import org.xandercat.swing.zenput.error.ValidationException;

public class DoubleValidator extends AbstractValidator<Double> {
	
	private double minValue = Double.MIN_VALUE;
	private double maxValue = Double.MAX_VALUE;
	
	public static DoubleValidator newValidator(ValidateDouble annotation) {
		return new DoubleValidator(annotation.min(), annotation.max());
	}
	
	public DoubleValidator() {
		super();
	}

	public DoubleValidator(Double minValue, Double maxValue) {
		this();
		if (minValue != null) {
			this.minValue = minValue;
		}
		if (maxValue != null) {
			this.maxValue = maxValue;
		}
	}
	
	@Override
	public Class<Double> getValueType() {
		return Double.class;
	}

	@Override
	public void validate(String fieldName, Double value) throws ValidationException {
		if (value.doubleValue() > this.maxValue) {
			throw new ValidationException(fieldName, "validator.number.max", Collections.singletonMap("max", this.maxValue));
		}
		if (value.doubleValue() < this.minValue) {
			throw new ValidationException(fieldName, "validator.number.min", Collections.singletonMap("min", this.minValue));
		}
	}
}
