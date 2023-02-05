package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.validator.FileValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateFile {
	
	public enum Mode {
		FILES_AND_DIRECTORIES(FileValidator.Mode.FILES_AND_DIRECTORIES), 
		DIRECTORIES_ONLY(FileValidator.Mode.DIRECTORIES_ONLY), 
		FILES_ONLY(FileValidator.Mode.FILES_ONLY);
		
		private FileValidator.Mode equivalentFileValidatorMode;
		
		private Mode(FileValidator.Mode equivalentFileValidatorMode) {
			this.equivalentFileValidatorMode = equivalentFileValidatorMode;
		}
		public FileValidator.Mode getEquivalentFileValidatorMode() {
			return equivalentFileValidatorMode;
		}
	}
	Class<?> validatorClass() default FileValidator.class;
	Mode mode() default Mode.FILES_AND_DIRECTORIES;
	boolean exists() default false;
}
