package org.xandercat.swing.zenput.converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

/**
 * Converter for converting between a single String of values separated by some 
 * separator characters (such as commas or semi-colons) and a List of values.
 * 
 * @author Scott Arnold
 */
public class CSVConverter implements InputConverter<String, List<String>> {

	private static final String REG_EX_RESERVED = "[\\^$.|?*+()";
	
	private String separatorChars = ",;";
	
	public String getSeparatorChars() {
		return separatorChars;
	}

	public void setSeparatorChars(String separatorChars) {
		if (separatorChars == null || separatorChars.length() == 0) {
			throw new IllegalArgumentException("separatorChars must contain at least one character.");
		}
		this.separatorChars = separatorChars;
	}

	private String getRegExForChar(char c) {
		if (REG_EX_RESERVED.indexOf(c) >= 0) {
			return "\\" + c;
		} else {
			return String.valueOf(c);
		}
	}
	
	public List<String> convertInput(String inputValue, String fieldName) throws ValidationException {
		if (inputValue == null) {
			return new ArrayList<String>();
		}
		char primaryChar = separatorChars.charAt(0);
		for (int i=1; i<separatorChars.length(); i++) {
			inputValue = inputValue.replaceAll(getRegExForChar(separatorChars.charAt(i)), String.valueOf(primaryChar));
		}
		return Arrays.asList(inputValue.split(getRegExForChar(primaryChar)));
	}

	public String convertSource(List<String> sourceValue) throws ZenputException {
		StringBuilder sb = new StringBuilder();
		for (String s : sourceValue) {
			if (sb.length() > 0) {
				sb.append(separatorChars.charAt(0));
			}
			sb.append(s);
		}
		return sb.toString();
	}

}
