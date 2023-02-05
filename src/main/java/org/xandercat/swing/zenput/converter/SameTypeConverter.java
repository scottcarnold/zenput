package org.xandercat.swing.zenput.converter;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * Converter for inputs where the input and source are of the same type.
 * 
 * @author Scott Arnold
 *
 * @param <T>	source/input type
 */
public class SameTypeConverter<T> implements InputConverter<T, T> {

	public T convertInput(T inputValue, String fieldName) throws ValidationException {
		return inputValue;
	}

	public T convertSource(T sourceValue) throws ZenputException {
		return sourceValue;
	}	
}
