package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.NumericCondition;
import org.xandercat.swing.zenput.condition.Operator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ValidateDependencyNumeric {
	String dependencyOn();
	Class<?> conditionClass() default NumericCondition.class; 
	Operator operator();
	Class<?> valueType();
	String stringValue();
}
