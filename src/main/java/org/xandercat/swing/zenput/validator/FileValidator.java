package org.xandercat.swing.zenput.validator;

import java.io.File;

import org.xandercat.swing.zenput.annotation.ValidateFile;
import org.xandercat.swing.zenput.error.ValidationException;

/**
 * Validator for validating File values.
 * 
 * @author Scott Arnold
 */
public class FileValidator extends AbstractValidator<File> {

	public static final Mode FILES_ONLY = Mode.FILES_ONLY;
	public static final Mode DIRECTORIES_ONLY = Mode.DIRECTORIES_ONLY;
	public static final Mode FILES_AND_DIRECTORIES = Mode.FILES_AND_DIRECTORIES;
	
	public enum Mode {
		FILES_ONLY, DIRECTORIES_ONLY, FILES_AND_DIRECTORIES;
	}
	
	private boolean existsRequired;
	private Mode mode = FILES_AND_DIRECTORIES;
	
	public static FileValidator newValidator(ValidateFile annotation) {
		FileValidator fileValidator = new FileValidator(annotation.mode().getEquivalentFileValidatorMode(), annotation.exists());
		return fileValidator;
	}
	
	public FileValidator() {
	}
	
	public FileValidator(Mode mode, boolean existsRequired) {
		this.mode = mode;
		this.existsRequired = existsRequired;
	}

	@Override
	public Class<File> getValueType() {
		return File.class;
	}

	@Override
	public void validate(String fieldName, File value) throws ValidationException {
		if (value.exists()) {
			switch (mode) {
			case FILES_ONLY:
				if (value.isDirectory()) {
					throw new ValidationException(fieldName, "validator.file.filesOnly");
				}
				break;
			case DIRECTORIES_ONLY:
				if (!value.isDirectory()) {
					throw new ValidationException(fieldName, "validator.file.directoriesOnly");
				}
				break;
			}
		} else if (existsRequired) {
			throw new ValidationException(fieldName, "validator.file.existsRequired");
		}
	}
}
