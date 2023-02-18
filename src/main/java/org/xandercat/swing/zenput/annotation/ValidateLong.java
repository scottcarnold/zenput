package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.validator.LongValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateLong {
	Class<?> validatorClass() default LongValidator.class;
	long min() default Long.MIN_VALUE;
	long max() default Long.MAX_VALUE;
}
