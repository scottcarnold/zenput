package org.xandercat.swing.zenput.converter;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

public class DoubleConverter implements InputConverter<String, Double> {
	
	public Double convertInput(String inputValue, String fieldName) throws ValidationException {
		if (inputValue == null || inputValue.trim().length() == 0) {
			return null;
		}
		try {
			return Double.parseDouble(inputValue);
		} catch (Exception e) {
			throw new ValidationException(fieldName, "converter.double", e);
		}
	}

	public String convertSource(Double sourceValue) throws ZenputException {
		if (sourceValue == null) {
			return null;
		} else {
			return sourceValue.toString();
		}
	}
}
