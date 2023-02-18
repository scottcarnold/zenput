package org.xandercat.swing.zenput.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;

public class StringValidatorTest {

	@Test
	public void testRegularExpressionStringValidation() {
		StringValidator validator = new StringValidator("^[a-z]{4}$");
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", "123");
		});
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", "abc");
		});
		try {
			validator.validate("field", "abcd");
		} catch (ValidationException ve) {
			fail("Should not have failed validation.  Validation message is " + ve.getMessage());
		}
	}
	
	@Test
	public void testRegularExpressionWithName() {
		StringValidator validator = new StringValidator("^[a-z]{4}$", "MyField");
		ValidationException ve = assertThrows(ValidationException.class, () -> {
			validator.validate("field", "123");
		});
		assertTrue(ve.getMessage().contains("MyField"));
	}
}
