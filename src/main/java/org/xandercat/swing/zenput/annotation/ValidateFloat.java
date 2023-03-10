package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.validator.FloatValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateFloat {
	Class<?> validatorClass() default FloatValidator.class;
	float min() default Float.MIN_VALUE;
	float max() default Float.MAX_VALUE;
}
