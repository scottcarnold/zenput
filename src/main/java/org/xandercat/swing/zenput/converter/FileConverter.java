package org.xandercat.swing.zenput.converter;

import java.io.File;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * Converter for converting between a String absolute path input and a File source.
 * 
 * @author Scott Arnold
 */
public class FileConverter implements InputConverter<String, File> {

	public File convertInput(String inputValue, String fieldName) throws ValidationException {
		if (inputValue == null || inputValue.trim().length() == 0) {
			return null;
		}
		try {
			return new File(inputValue);
		} catch (Exception e) {
			throw new ValidationException(fieldName, "converter.file", e);
		}
	}

	public String convertSource(File sourceValue) throws ZenputException {
		return (sourceValue == null)? "" : sourceValue.getAbsolutePath();
	}
}
