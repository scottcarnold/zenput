package org.xandercat.swing.zenput.converter;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

public class BooleanConverter implements InputConverter<String, Boolean> {

	@Override
	public Boolean convertInput(String inputValue, String fieldName) throws ValidationException {
		return Boolean.parseBoolean(inputValue);
	}

	@Override
	public String convertSource(Boolean sourceValue) throws ZenputException {
		return (sourceValue == null)? null : sourceValue.toString();
	}
}
