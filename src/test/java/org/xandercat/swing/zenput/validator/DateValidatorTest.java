package org.xandercat.swing.zenput.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;

public class DateValidatorTest {

	private DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
	
	@Test
	public void testAfter() throws Exception {
		DateValidator validator = new DateValidator("MM/dd/yyyy");
		validator.setAfter("03/10/2023", false);
		try {
			validator.validate("field", dateFormat.parse("03/11/2023"));
		} catch (ValidationException ve) {
			fail("Validation error should not have been thrown.");
		}
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", dateFormat.parse("03/10/2023"));
		});
	}

	@Test
	public void testOnOrAfter() throws Exception {
		DateValidator validator = new DateValidator("MM/dd/yyyy");
		validator.setAfter("03/10/2023", true);
		try {
			validator.validate("field", dateFormat.parse("03/11/2023"));
			validator.validate("field", dateFormat.parse("03/10/2023"));
		} catch (ValidationException ve) {
			fail("Validation error should not have been thrown.");
		}
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", dateFormat.parse("03/09/2023"));
		});
	}
	
	@Test
	public void testBefore() throws Exception {
		DateValidator validator = new DateValidator("MM/dd/yyyy");
		validator.setBefore("03/10/2023", false);
		try {
			validator.validate("field", dateFormat.parse("03/09/2023"));
		} catch (ValidationException ve) {
			fail("Validation error should not have been thrown.");
		}
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", dateFormat.parse("03/10/2023"));
		});
	}
	
	@Test
	public void testOnOrBefore() throws Exception {
		DateValidator validator = new DateValidator("MM/dd/yyyy");
		validator.setBefore("03/10/2023", true);
		try {
			validator.validate("field", dateFormat.parse("03/09/2023"));
			validator.validate("field", dateFormat.parse("03/10/2023"));
		} catch (ValidationException ve) {
			fail("Validation error should not have been thrown.");
		}
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", dateFormat.parse("03/11/2023"));
		});
	}
}
