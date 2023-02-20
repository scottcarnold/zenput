package org.xandercat.swing.zenput.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Properties;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;

public class ValidationErrorUtilTest {

	@Test
	public void testLoadDefaultProperties() {
		Properties properties = ValidationErrorUtil.getDefaultMessageProperties();
		assertNotNull(properties);
	}
	
	@Test
	public void test() {
		Properties properties = ValidationErrorUtil.getDefaultMessageProperties();
		assertNotNull(properties);
		ValidationException ve = new ValidationException("field", "validator.number.min", Collections.singletonMap("min", Integer.valueOf(5)));
		String message = ValidationErrorUtil.getMessage(properties, ve);
		assertEquals("Value cannot be less than 5", message);
	}
}
