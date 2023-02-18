package org.xandercat.swing.zenput.converter;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

public class LongConverter implements InputConverter<String, Long> {
	
	public Long convertInput(String inputValue, String fieldName) throws ValidationException {
		if (inputValue == null || inputValue.trim().length() == 0) {
			return null;
		}
		try {
			return Long.parseLong(inputValue);
		} catch (Exception e) {
			throw new ValidationException(fieldName, "Value must be a whole number.", e);
		}
	}

	public String convertSource(Long sourceValue) throws ZenputException {
		if (sourceValue == null) {
			return null;
		} else {
			return sourceValue.toString();
		}
	}
}
