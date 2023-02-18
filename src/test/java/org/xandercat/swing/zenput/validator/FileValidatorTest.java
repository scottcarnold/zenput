package org.xandercat.swing.zenput.validator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;

import org.junit.jupiter.api.Test;
import org.xandercat.swing.zenput.error.ValidationException;

public class FileValidatorTest {

	@Test
	public void testFileValidation() throws Exception {
		final FileValidator dirValidator = new FileValidator(FileValidator.DIRECTORIES_ONLY, true);
		final File nonexistentFile = new File("doesNotExist");
		assertThrows(ValidationException.class, () -> {
			dirValidator.validate("field", nonexistentFile);
		});
		File file = File.createTempFile("abc", "def");
		file.deleteOnExit();
		assertThrows(ValidationException.class, () -> {
			dirValidator.validate("field", file);
		});
		final FileValidator validator = new FileValidator(FileValidator.FILES_AND_DIRECTORIES, true);
		try {
			validator.validate("field", file);
		} catch (ValidationException ve) {
			fail("Validation exception should not have occurred.  Validation error message: " + ve.getMessage());
		}
	}
}
