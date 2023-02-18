package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.validator.DateValidator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateDate {

	Class<?> validatorClass() default DateValidator.class;
	String pattern() default "MM/dd/yyyy";
	String before();
	String after();
	boolean beforeInclusive() default false;
	boolean afterInclusive() default true;
}
