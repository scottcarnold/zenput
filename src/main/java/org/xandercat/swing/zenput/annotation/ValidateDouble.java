package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.validator.DoubleValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateDouble {
	Class<?> validatorClass() default DoubleValidator.class;
	double min() default Double.MIN_VALUE;
	double max() default Double.MAX_VALUE;
}
