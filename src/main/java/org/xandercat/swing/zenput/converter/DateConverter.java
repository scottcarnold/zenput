package org.xandercat.swing.zenput.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import org.xandercat.swing.zenput.error.ValidationException;
import org.xandercat.swing.zenput.error.ZenputException;

public class DateConverter implements InputConverter<String, Date> {

	private DateFormat dateFormat;
	private String pattern;
	
	public DateConverter(String pattern) {
		this.pattern = pattern;
		this.dateFormat = new SimpleDateFormat(pattern);
	}
	
	@Override
	public Date convertInput(String inputValue, String fieldName) throws ValidationException {
		try {
			return dateFormat.parse(inputValue);
		} catch (ParseException e) { 
			throw new ValidationException(fieldName, "converter.date", Collections.singletonMap("pattern", pattern));
		}
	}

	@Override
	public String convertSource(Date sourceValue) throws ZenputException {
		return dateFormat.format(sourceValue);
	}

}
