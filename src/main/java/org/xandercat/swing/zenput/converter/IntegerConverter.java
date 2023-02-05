package org.xandercat.swing.zenput.converter;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * Converter for Integer values.
 * 
 * @author Scott Arnold
 */
public class IntegerConverter implements InputConverter<String, Integer> {

	public Integer convertInput(String inputValue, String fieldName) throws ValidationException {
		if (inputValue == null || inputValue.trim().length() == 0) {
			return null;
		}
		try {
			return Integer.parseInt(inputValue);
		} catch (Exception e) {
			throw new ValidationException(fieldName, "Value must be a whole number.", e);
		}
	}

	public String convertSource(Integer sourceValue) throws ZenputException {
		if (sourceValue == null) {
			return null;
		} else {
			return sourceValue.toString();
		}
	}
}
