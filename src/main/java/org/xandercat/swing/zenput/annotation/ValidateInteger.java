package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.validator.IntegerValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateInteger {
	Class<?> validatorClass() default IntegerValidator.class;
	int min() default Integer.MIN_VALUE;
	int max() default Integer.MAX_VALUE;
}
