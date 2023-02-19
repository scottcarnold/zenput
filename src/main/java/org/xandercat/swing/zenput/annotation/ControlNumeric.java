package org.xandercat.swing.zenput.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.xandercat.swing.zenput.condition.NumericCondition;
import org.xandercat.swing.zenput.condition.Operator;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ControlNumeric {
	String dependencyOn();
	Class<?> conditionClass() default NumericCondition.class; 
	Operator operator();
	CompareTo compareTo() default CompareTo.FIXED_VALUE;
	Class<?> valueType() default Object.class;
	String stringValue() default "";
}
