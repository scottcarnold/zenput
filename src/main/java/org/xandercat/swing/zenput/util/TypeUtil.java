package org.xandercat.swing.zenput.util;

import java.text.ParseException;

import org.xandercat.swing.zenput.converter.ConverterFactory;
import org.xandercat.swing.zenput.converter.InputConverter;
import org.xandercat.swing.zenput.error.ValidationException;

public class TypeUtil {

	public static <T> T parse(Class<T> c, String s) throws ParseException {
		@SuppressWarnings("unchecked")
		InputConverter<String, T> converter = (InputConverter<String, T>) ConverterFactory.getConverterForType(c);
		if (converter == null) {
			throw new ParseException("Unable to find converter for type " + c.getName(), 0);
		}
		try {
			return converter.convertInput(s, "N/A");
		} catch (ValidationException e) {
			throw new ParseException("Unable to parse value \"" + s + "\" to type " + c.getName(), 0);
		}
	}
}
