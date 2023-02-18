package org.xandercat.swing.zenput.validator;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;

public class CompoundValidatorTest {

	@Test
	public void testCompoundValidation() {
		LongValidator longValidator1 = new LongValidator(Long.valueOf(1), null);
		LongValidator longValidator2 = new LongValidator(null, Long.valueOf(10));
		CompoundValidator<Long> validator = new CompoundValidator<Long>(longValidator1, longValidator2);
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", Long.valueOf(-5));
		});
		assertThrows(ValidationException.class, () -> {
			validator.validate("field", Long.valueOf(20));
		});		
	}
}
